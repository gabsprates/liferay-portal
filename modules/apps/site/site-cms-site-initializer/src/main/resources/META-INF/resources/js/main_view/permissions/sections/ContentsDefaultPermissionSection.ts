/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {
	Action,
	ActionsMap,
	DefaultAssetTypes,
} from '../../default_permission/DefaultPermissionTypes';
import {PermissionsSection} from '../PermissionsPageConfig';

export default class ContentsDefaultPermissionSection
	implements PermissionsSection
{
	readonly actions: Action[];
	readonly key = 'contents';
	readonly label = Liferay.Language.get('contents');

	constructor(actions: ActionsMap) {
		this.actions = actions[DefaultAssetTypes.L_CONTENTS] || [];
	}
}
