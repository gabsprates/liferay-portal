/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {
	Action,
	ActionsMap,
} from '../../default_permission/DefaultPermissionTypes';
import {OWN_PERMISSIONS, PermissionsSection} from '../PermissionsPageConfig';

export default class SpaceOwnPermissionSection implements PermissionsSection {
	readonly actions: Action[];
	readonly key = OWN_PERMISSIONS;
	readonly label = Liferay.Language.get('this-space');

	constructor(actions: ActionsMap) {
		this.actions = actions.SPACE || [];
	}
}
