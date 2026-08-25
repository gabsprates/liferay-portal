/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.headless.admin.configuration.test.configuration;

import aQute.bnd.annotation.metatype.Meta;

/**
 * Declares <code>groupId</code> as an ordinary attribute, colliding with
 * Liferay's reserved scope property key. The configuration is system scoped,
 * so the value is business data and must not hide it from the system
 * configuration API.
 *
 * @author Gabriel Prates
 */
@Meta.OCD(
	id = "com.liferay.headless.admin.configuration.test.configuration.DeclaredScopePropertyKeysConfiguration"
)
public interface DeclaredScopePropertyKeysConfiguration {

	@Meta.AD(deflt = "0", required = false)
	public long groupId();

	@Meta.AD(deflt = "", required = false)
	public String queueName();

}