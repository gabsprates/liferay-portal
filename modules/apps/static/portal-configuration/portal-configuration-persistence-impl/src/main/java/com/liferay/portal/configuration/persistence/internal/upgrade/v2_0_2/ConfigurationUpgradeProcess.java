/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.configuration.persistence.internal.upgrade.v2_0_2;

import com.liferay.petra.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.portal.configuration.metatype.definitions.ExtendedMetaTypeInformation;
import com.liferay.portal.configuration.metatype.definitions.ExtendedMetaTypeService;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.GetterUtil;

import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Map;
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
 * Undoes what the 2.0.1 upgrade did to a system scoped configuration that
 * declares <code>groupId</code> as an ordinary attribute. That upgrade read the
 * declared value as a scope marker and stamped a company onto the row, which
 * hides it from System Settings and makes deleting the group delete the
 * configuration.
 *
 * @author Gabriel Prates
 */
public class ConfigurationUpgradeProcess extends UpgradeProcess {

	public ConfigurationUpgradeProcess(ConfigurationAdmin configurationAdmin) {
		_configurationAdmin = configurationAdmin;
	}

	@Override
	protected void doUpgrade() throws Exception {
		if (!hasTable("Configuration_")) {
			return;
		}

		Set<String> repairablePids = _getRepairablePids();

		if (repairablePids.isEmpty()) {
			return;
		}

		try (PreparedStatement preparedStatement = connection.prepareStatement(
				"select configurationId, dictionary from Configuration_ " +
					"where dictionary like '%groupId=%' and dictionary like " +
						"'%companyId=%'");

			ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				String configurationId = resultSet.getString("configurationId");

				if (!repairablePids.contains(_getRawPid(configurationId))) {
					continue;
				}

				Dictionary<String, Object> dictionary = _toDictionary(
					resultSet.getString("dictionary"));

				String companyIdPropertyKey =
					ExtendedObjectClassDefinition.Scope.COMPANY.
						getPropertyKey();

				if (dictionary.remove(companyIdPropertyKey) == null) {
					continue;
				}

				if (_log.isInfoEnabled()) {
					_log.info(
						StringBundler.concat(
							"Removing \"companyId\" from configuration \"",
							configurationId, "\" because its object class ",
							"definition is system scoped and declares ",
							"\"groupId\" as an ordinary attribute"));
				}

				Configuration configuration =
					_configurationAdmin.getConfiguration(configurationId, "?");

				configuration.update(dictionary);
			}
		}
	}

	private void _collectRepairablePids(
		Set<String> repairablePids,
		ExtendedMetaTypeInformation extendedMetaTypeInformation,
		String[] pids) {

		for (String pid : pids) {
			if (_isRepairable(extendedMetaTypeInformation, pid)) {
				repairablePids.add(pid);
			}
		}
	}

	private String _getRawPid(String configurationId) {
		int index = configurationId.indexOf(CharPool.TILDE);

		if (index == -1) {
			return configurationId;
		}

		return configurationId.substring(0, index);
	}

	private Set<String> _getRepairablePids() {
		Bundle bundle = FrameworkUtil.getBundle(
			ConfigurationUpgradeProcess.class);

		BundleContext bundleContext = bundle.getBundleContext();

		ServiceReference<ExtendedMetaTypeService> serviceReference =
			bundleContext.getServiceReference(ExtendedMetaTypeService.class);

		if (serviceReference == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to read metatype, no configuration will be " +
						"repaired");
			}

			return Collections.emptySet();
		}

		try {
			ExtendedMetaTypeService extendedMetaTypeService =
				bundleContext.getService(serviceReference);

			Set<String> repairablePids = new HashSet<>();

			for (Bundle curBundle : bundleContext.getBundles()) {
				ExtendedMetaTypeInformation extendedMetaTypeInformation =
					extendedMetaTypeService.getMetaTypeInformation(curBundle);

				if (extendedMetaTypeInformation == null) {
					continue;
				}

				_collectRepairablePids(
					repairablePids, extendedMetaTypeInformation,
					extendedMetaTypeInformation.getFactoryPids());
				_collectRepairablePids(
					repairablePids, extendedMetaTypeInformation,
					extendedMetaTypeInformation.getPids());
			}

			return repairablePids;
		}
		finally {
			bundleContext.ungetService(serviceReference);
		}
	}

	private boolean _isRepairable(
		ExtendedMetaTypeInformation extendedMetaTypeInformation, String pid) {

		com.liferay.portal.configuration.metatype.definitions.
			ExtendedObjectClassDefinition extendedObjectClassDefinition =
				extendedMetaTypeInformation.getObjectClassDefinition(pid, null);

		if (extendedObjectClassDefinition == null) {
			return false;
		}

		Map<String, String> extensionAttributes =
			extendedObjectClassDefinition.getExtensionAttributes(
				ExtendedObjectClassDefinition.XML_NAMESPACE);

		ExtendedObjectClassDefinition.Scope scope =
			ExtendedObjectClassDefinition.Scope.SYSTEM;

		if (!scope.equals(
				GetterUtil.getString(
					extensionAttributes.get("scope"), scope.toString()))) {

			return false;
		}

		AttributeDefinition[] attributeDefinitions =
			extendedObjectClassDefinition.getAttributeDefinitions(
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

}