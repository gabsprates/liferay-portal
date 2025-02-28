/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.security.ldap.internal;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.log.LogUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.ldap.LDAPSettings;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.security.ldap.authenticator.configuration.LDAPAuthConfiguration;
import com.liferay.portal.security.ldap.configuration.ConfigurationProvider;
import com.liferay.portal.security.ldap.configuration.LDAPServerConfiguration;
import com.liferay.portal.security.ldap.configuration.SystemLDAPConfiguration;
import com.liferay.portal.security.ldap.exportimport.configuration.LDAPExportConfiguration;
import com.liferay.portal.security.ldap.exportimport.configuration.LDAPImportConfiguration;
import com.liferay.portal.security.ldap.validator.LDAPFilterValidator;

import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Edward Han
 * @author Michael C. Han
 * @author Brian Wing Shun Chan
 */
@Component(immediate = true, service = LDAPSettings.class)
public class DefaultLDAPSettings implements LDAPSettings {

	@Override
	public Properties getContactExpandoMappings(
			long ldapServerId, long companyId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getConfiguration(
				companyId, ldapServerId);

		Properties contactExpandoMappings = _getProperties(
			ldapServerConfiguration.contactCustomMappings());

		LogUtil.debug(_log, contactExpandoMappings);

		return contactExpandoMappings;
	}

	@Override
	public Properties getContactMappings(long ldapServerId, long companyId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getConfiguration(
				companyId, ldapServerId);

		Properties contactMappings = _getProperties(
			ldapServerConfiguration.contactMappings());

		LogUtil.debug(_log, contactMappings);

		return contactMappings;
	}

	@Override
	public String[] getErrorPasswordHistoryKeywords(long companyId) {
		SystemLDAPConfiguration systemLDAPConfiguration =
			_systemLDAPConfigurationProvider.getConfiguration(companyId);

		return systemLDAPConfiguration.errorPasswordHistoryKeywords();
	}

	@Override
	public Properties getGroupMappings(long ldapServerId, long companyId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getConfiguration(
				companyId, ldapServerId);

		Properties groupMappings = _getProperties(
			ldapServerConfiguration.groupMappings());

		LogUtil.debug(_log, groupMappings);

		return groupMappings;
	}

	@Override
	public long getPreferredLDAPServerId(long companyId, String screenName) {
		User user = _userLocalService.fetchUserByScreenName(
			companyId, screenName);

		if (user == null) {
			return -1;
		}

		return user.getLdapServerId();
	}

	@Override
	public String getPropertyPostfix(long ldapServerId) {
		return StringPool.PERIOD + ldapServerId;
	}

	@Override
	public Properties getUserExpandoMappings(long ldapServerId, long companyId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getConfiguration(
				companyId, ldapServerId);

		Properties contactExpandoMappings = _getProperties(
			ldapServerConfiguration.userCustomMappings());

		LogUtil.debug(_log, contactExpandoMappings);

		return contactExpandoMappings;
	}

	@Override
	public Properties getUserMappings(long ldapServerId, long companyId)
		throws Exception {

		LDAPServerConfiguration ldapServerConfiguration =
			_ldapServerConfigurationProvider.getConfiguration(
				companyId, ldapServerId);

		Properties userMappings = _getProperties(
			ldapServerConfiguration.userMappings());

		LogUtil.debug(_log, userMappings);

		return userMappings;
	}

	@Override
	public boolean isExportEnabled(long companyId) {
		LDAPImportConfiguration ldapImportConfiguration =
			_ldapImportConfigurationProvider.getConfiguration(companyId);

		boolean defaultImportUserPasswordAutogenerated =
			ldapImportConfiguration.importUserPasswordAutogenerated();

		if (ldapImportConfiguration.importEnabled() &&
			defaultImportUserPasswordAutogenerated) {

			return false;
		}

		LDAPExportConfiguration ldapExportConfiguration =
			_ldapExportConfigurationProvider.getConfiguration(companyId);

		return ldapExportConfiguration.exportEnabled();
	}

	@Override
	public boolean isExportGroupEnabled(long companyId) {
		LDAPExportConfiguration ldapExportConfiguration =
			_ldapExportConfigurationProvider.getConfiguration(companyId);

		return ldapExportConfiguration.exportGroupEnabled();
	}

	@Override
	public boolean isImportEnabled(long companyId) {
		LDAPImportConfiguration ldapImportConfiguration =
			_ldapImportConfigurationProvider.getConfiguration(companyId);

		return ldapImportConfiguration.importEnabled();
	}

	@Override
	public boolean isImportOnStartup(long companyId) {
		LDAPImportConfiguration ldapImportConfiguration =
			_ldapImportConfigurationProvider.getConfiguration(companyId);

		return ldapImportConfiguration.importOnStartup();
	}

	@Override
	public boolean isPasswordPolicyEnabled(long companyId) {
		LDAPAuthConfiguration ldapAuthConfiguration =
			_ldapAuthConfigurationProvider.getConfiguration(companyId);

		return ldapAuthConfiguration.passwordPolicyEnabled();
	}

	private Properties _getProperties(String[] keyValuePairs) {
		Properties properties = new Properties();

		for (String keyValuePair : keyValuePairs) {
			String[] keyValue = StringUtil.split(keyValuePair, CharPool.EQUAL);

			if (ArrayUtil.isEmpty(keyValue)) {
				continue;
			}

			String value = StringPool.BLANK;

			if (keyValue.length == 2) {
				value = keyValue[1];
			}

			properties.put(keyValue[0], value);
		}

		return properties;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DefaultLDAPSettings.class);

	@Reference(
		target = "(factoryPid=com.liferay.portal.security.ldap.authenticator.configuration.LDAPAuthConfiguration)"
	)
	private ConfigurationProvider<LDAPAuthConfiguration>
		_ldapAuthConfigurationProvider;

	@Reference(
		target = "(factoryPid=com.liferay.portal.security.ldap.exportimport.configuration.LDAPExportConfiguration)"
	)
	private ConfigurationProvider<LDAPExportConfiguration>
		_ldapExportConfigurationProvider;

	@Reference
	private LDAPFilterValidator _ldapFilterValidator;

	@Reference(
		target = "(factoryPid=com.liferay.portal.security.ldap.exportimport.configuration.LDAPImportConfiguration)"
	)
	private ConfigurationProvider<LDAPImportConfiguration>
		_ldapImportConfigurationProvider;

	@Reference(
		target = "(factoryPid=com.liferay.portal.security.ldap.configuration.LDAPServerConfiguration)"
	)
	private ConfigurationProvider<LDAPServerConfiguration>
		_ldapServerConfigurationProvider;

	@Reference(
		target = "(factoryPid=com.liferay.portal.security.ldap.configuration.SystemLDAPConfiguration)"
	)
	private ConfigurationProvider<SystemLDAPConfiguration>
		_systemLDAPConfigurationProvider;

	@Reference
	private UserLocalService _userLocalService;

}