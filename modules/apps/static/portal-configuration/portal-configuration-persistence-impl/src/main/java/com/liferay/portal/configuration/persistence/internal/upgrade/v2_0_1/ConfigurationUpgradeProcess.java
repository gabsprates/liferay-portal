/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.configuration.persistence.internal.upgrade.v2_0_1;

import com.liferay.petra.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.portal.configuration.metatype.definitions.ExtendedMetaTypeInformation;
import com.liferay.portal.configuration.metatype.definitions.ExtendedMetaTypeService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import org.apache.felix.cm.file.ConfigurationHandler;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

/**
 * @author Thiago Buarque
 */
public class ConfigurationUpgradeProcess extends UpgradeProcess {

	public ConfigurationUpgradeProcess(
		ConfigurationAdmin configurationAdmin,
		GroupLocalService groupLocalService) {

		_configurationAdmin = configurationAdmin;
		_groupLocalService = groupLocalService;
	}

	@Override
	protected void doUpgrade() throws Exception {
		if (!hasTable("Configuration_")) {
			return;
		}

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"select configurationId, dictionary from Configuration_ " +
					"where dictionary like '%groupId=%'");

			ResultSet resultSet = preparedStatement.executeQuery()) {

			Set<String> declaringPids = _getDeclaringPids();

			while (resultSet.next()) {
				String configurationId = resultSet.getString("configurationId");

				if (declaringPids.contains(_getRawPid(configurationId))) {
					if (_log.isInfoEnabled()) {
						_log.info(
							StringBundler.concat(
								"Skipping configuration \"", configurationId,
								"\" because its object class definition ",
								"declares \"groupId\" as an ordinary ",
								"attribute"));
					}

					continue;
				}

				Dictionary<String, Object> dictionary = _toDictionary(
					resultSet.getString("dictionary"));

				Long companyId = _getCompanyId(configurationId, dictionary);

				if (companyId == null) {
					continue;
				}

				dictionary.put(
					ExtendedObjectClassDefinition.Scope.COMPANY.
						getPropertyKey(),
					companyId);

				Configuration configuration =
					_configurationAdmin.getConfiguration(configurationId, "?");

				configuration.update(dictionary);
			}
		}
	}

	private void _collectDeclaringPids(
		Set<String> declaringPids,
		ExtendedMetaTypeInformation extendedMetaTypeInformation,
		String[] pids) {

		for (String pid : pids) {
			if (_declaresGroupId(extendedMetaTypeInformation, pid)) {
				declaringPids.add(pid);
			}
		}
	}

	private boolean _declaresGroupId(
		ExtendedMetaTypeInformation extendedMetaTypeInformation, String pid) {

		ObjectClassDefinition objectClassDefinition =
			extendedMetaTypeInformation.getObjectClassDefinition(pid, null);

		if (objectClassDefinition == null) {
			return false;
		}

		AttributeDefinition[] attributeDefinitions =
			objectClassDefinition.getAttributeDefinitions(
				ObjectClassDefinition.ALL);

		if (attributeDefinitions == null) {
			return false;
		}

		String groupIdPropertyKey =
			ExtendedObjectClassDefinition.Scope.GROUP.getPropertyKey();

		for (AttributeDefinition attributeDefinition : attributeDefinitions) {
			if (groupIdPropertyKey.equals(attributeDefinition.getID())) {
				return true;
			}
		}

		return false;
	}

	private Long _getCompanyId(
		String configurationId, Dictionary<String, Object> dictionary) {

		long groupId = GetterUtil.getLong(
			dictionary.get(
				ExtendedObjectClassDefinition.Scope.GROUP.getPropertyKey()));

		Group group = _groupLocalService.fetchGroup(groupId);

		if (group == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					StringBundler.concat(
						"Skipping configuration \"", configurationId,
						"\" because group \"", groupId,
						"\" does not exist for company \"",
						CompanyThreadLocal.getCompanyId(), "\""));
			}

			return null;
		}

		return group.getCompanyId();
	}

	private Set<String> _getDeclaringPids() {
		Bundle bundle = FrameworkUtil.getBundle(
			ConfigurationUpgradeProcess.class);

		BundleContext bundleContext = bundle.getBundleContext();

		ServiceReference<ExtendedMetaTypeService> serviceReference =
			bundleContext.getServiceReference(ExtendedMetaTypeService.class);

		if (serviceReference == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to read metatype, a configuration that declares " +
						"\"groupId\" as an ordinary attribute will be " +
							"treated as group scoped");
			}

			return Collections.emptySet();
		}

		try {
			ExtendedMetaTypeService extendedMetaTypeService =
				bundleContext.getService(serviceReference);

			Set<String> declaringPids = new HashSet<>();

			for (Bundle curBundle : bundleContext.getBundles()) {
				ExtendedMetaTypeInformation extendedMetaTypeInformation =
					extendedMetaTypeService.getMetaTypeInformation(curBundle);

				if (extendedMetaTypeInformation == null) {
					continue;
				}

				_collectDeclaringPids(
					declaringPids, extendedMetaTypeInformation,
					extendedMetaTypeInformation.getFactoryPids());
				_collectDeclaringPids(
					declaringPids, extendedMetaTypeInformation,
					extendedMetaTypeInformation.getPids());
			}

			return declaringPids;
		}
		finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	private String _getRawPid(String configurationId) {
		int index = configurationId.indexOf(CharPool.TILDE);

		if (index == -1) {
			return configurationId;
		}

		return configurationId.substring(0, index);
	}

	private Dictionary<String, Object> _toDictionary(String dictionaryString)
		throws IOException {

		UnsyncByteArrayInputStream unsyncByteArrayInputStream =
			new UnsyncByteArrayInputStream(
				dictionaryString.getBytes(StandardCharsets.UTF_8));

		return ConfigurationHandler.read(unsyncByteArrayInputStream);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ConfigurationUpgradeProcess.class);

	private final ConfigurationAdmin _configurationAdmin;
	private final GroupLocalService _groupLocalService;

}