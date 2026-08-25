/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.configuration.admin.util;

import java.util.Map;
import java.util.Set;

/**
 * Resolves the scope property keys a configuration declares as ordinary
 * attributes. A key in that set holds the module's own data, so the scope
 * lookups must not read it as a scope marker.
 *
 * @author Gabriel Prates
 */
public interface ConfigurationScopePropertyKeyResolver {

	public Set<String> getDeclaredScopePropertyKeys(String pid);

	/**
	 * Returns only the PIDs that declare at least one scope property key. The
	 * map is empty on an installation where no module declares one, which is
	 * what keeps a broad configuration listing cheap.
	 */
	public Map<String, Set<String>> getDeclaredScopePropertyKeysMap();

}