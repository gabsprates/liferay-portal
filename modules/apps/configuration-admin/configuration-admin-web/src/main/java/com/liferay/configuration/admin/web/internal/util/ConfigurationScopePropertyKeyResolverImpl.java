/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.configuration.admin.web.internal.util;

import com.liferay.configuration.admin.util.ConfigurationPidUtil;
import com.liferay.configuration.admin.util.ConfigurationScopePropertyKeyResolver;
import com.liferay.portal.configuration.metatype.definitions.ExtendedMetaTypeInformation;
import com.liferay.portal.configuration.metatype.definitions.ExtendedMetaTypeService;
import com.liferay.portal.configuration.metatype.definitions.ExtendedObjectClassDefinition;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * Reads bundle metatype only. It runs inside a Felix Configuration Admin write
 * when a configuration model listener asks for a PID, so it must never call
 * back into Configuration Admin.
 *
 * @author Gabriel Prates
 */
@Component(service = ConfigurationScopePropertyKeyResolver.class)
public class ConfigurationScopePropertyKeyResolverImpl
	implements BundleListener, ConfigurationScopePropertyKeyResolver {

	@Override
	public void bundleChanged(BundleEvent bundleEvent) {
		_declaredScopePropertyKeysMap = null;
	}

	@Override
	public Set<String> getDeclaredScopePropertyKeys(String pid) {
		Map<String, Set<String>> declaredScopePropertyKeysMap =
			getDeclaredScopePropertyKeysMap();

		if (declaredScopePropertyKeysMap.isEmpty()) {
			return Collections.emptySet();
		}

		Set<String> declaredScopePropertyKeys =
			declaredScopePropertyKeysMap.get(pid);

		if (declaredScopePropertyKeys == null) {
			declaredScopePropertyKeys = declaredScopePropertyKeysMap.get(
				ConfigurationPidUtil.getRawPid(pid));
		}

		if (declaredScopePropertyKeys == null) {
			return Collections.emptySet();
		}

		return declaredScopePropertyKeys;
	}

	@Override
	public Map<String, Set<String>> getDeclaredScopePropertyKeysMap() {
		Map<String, Set<String>> declaredScopePropertyKeysMap =
			_declaredScopePropertyKeysMap;

		if (declaredScopePropertyKeysMap != null) {
			return declaredScopePropertyKeysMap;
		}

		declaredScopePropertyKeysMap = new HashMap<>();

		for (Bundle bundle : _bundleContext.getBundles()) {
			if (bundle.getState() != Bundle.ACTIVE) {
				continue;
			}

			ExtendedMetaTypeInformation extendedMetaTypeInformation =
				_extendedMetaTypeService.getMetaTypeInformation(bundle);

			if (extendedMetaTypeInformation == null) {
				continue;
			}

			_collectDeclaredScopePropertyKeys(
				declaredScopePropertyKeysMap, extendedMetaTypeInformation,
				extendedMetaTypeInformation.getFactoryPids());
			_collectDeclaredScopePropertyKeys(
				declaredScopePropertyKeysMap, extendedMetaTypeInformation,
				extendedMetaTypeInformation.getPids());
		}

		_declaredScopePropertyKeysMap = declaredScopePropertyKeysMap;

		return declaredScopePropertyKeysMap;
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_bundleContext = bundleContext;

		_bundleContext.addBundleListener(this);
	}

	@Deactivate
	protected void deactivate() {
		_bundleContext.removeBundleListener(this);

		_declaredScopePropertyKeysMap = null;
	}

	private void _collectDeclaredScopePropertyKeys(
		Map<String, Set<String>> declaredScopePropertyKeysMap,
		ExtendedMetaTypeInformation extendedMetaTypeInformation,
		String[] pids) {

		for (String pid : pids) {
			ExtendedObjectClassDefinition extendedObjectClassDefinition =
				extendedMetaTypeInformation.getObjectClassDefinition(pid, null);

			if (extendedObjectClassDefinition == null) {
				continue;
			}

			Set<String> declaredScopePropertyKeys =
				ScopePropertyKeysUtil.getDeclaredScopePropertyKeys(
					extendedObjectClassDefinition);

			if (!declaredScopePropertyKeys.isEmpty()) {
				declaredScopePropertyKeysMap.put(
					pid, declaredScopePropertyKeys);
			}
		}
	}

	private BundleContext _bundleContext;
	private volatile Map<String, Set<String>> _declaredScopePropertyKeysMap;

	@Reference
	private ExtendedMetaTypeService _extendedMetaTypeService;

}