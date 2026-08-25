/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.configuration.admin.web.internal.util;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.portal.kernel.util.SetUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

/**
 * @author Gabriel Prates
 */
public class ScopePropertyKeysUtil {

	/**
	 * Returns the scope property keys the object class definition declares as
	 * ordinary attributes. A key in that set holds the module's own data, so
	 * the scope lookups must not read it as a scope marker.
	 */
	public static Set<String> getDeclaredScopePropertyKeys(
		ObjectClassDefinition objectClassDefinition) {

		AttributeDefinition[] attributeDefinitions =
			objectClassDefinition.getAttributeDefinitions(
				ObjectClassDefinition.ALL);

		if (attributeDefinitions == null) {
			return Collections.emptySet();
		}

		Set<String> declaredScopePropertyKeys = new HashSet<>();

		for (AttributeDefinition attributeDefinition : attributeDefinitions) {
			String id = attributeDefinition.getID();

			if (_scopePropertyKeys.contains(id)) {
				declaredScopePropertyKeys.add(id);
			}
		}

		return declaredScopePropertyKeys;
	}

	private static final Set<String> _scopePropertyKeys = SetUtil.fromArray(
		ExtendedObjectClassDefinition.Scope.COMPANY.getPortablePropertyKey(),
		ExtendedObjectClassDefinition.Scope.COMPANY.getPropertyKey(),
		ExtendedObjectClassDefinition.Scope.GROUP.getPortablePropertyKey(),
		ExtendedObjectClassDefinition.Scope.GROUP.getPropertyKey(),
		ExtendedObjectClassDefinition.Scope.PORTLET_INSTANCE.
			getPortablePropertyKey(),
		ExtendedObjectClassDefinition.Scope.PORTLET_INSTANCE.getPropertyKey(),
		"siteExternalReferenceCode");

}