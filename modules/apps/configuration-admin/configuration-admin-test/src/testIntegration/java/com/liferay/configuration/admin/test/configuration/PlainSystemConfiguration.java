/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.configuration.admin.test.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 * Control for {@link DeclaredScopePropertyKeysConfiguration}. Declares no
 * attribute that collides with a scope property key, so it must keep behaving
 * exactly as it does today.
 *
 * @author Gabriel Prates
 */
@Meta.OCD(
	id = "com.liferay.configuration.admin.test.configuration.PlainSystemConfiguration"
)
public interface PlainSystemConfiguration {

	@Meta.AD(deflt = "", required = false)
	public String queueName();

}