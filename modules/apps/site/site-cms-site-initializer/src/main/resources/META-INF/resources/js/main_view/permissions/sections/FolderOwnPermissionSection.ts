/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import FolderService from '../../../common/services/FolderService';
import {
	Action,
	ActionsMap,
	DefaultAssetTypes,
	RoleSelectedActions,
} from '../../default_permission/DefaultPermissionTypes';
import {OWN_PERMISSIONS, PermissionsSection} from '../PermissionsPageConfig';

export default class FolderOwnPermissionSection implements PermissionsSection {
	readonly actions: Action[];
	readonly description = Liferay.Language.get('applies-to-this-folder-only');
	readonly key = OWN_PERMISSIONS;
	readonly label = Liferay.Language.get('this-folder');

	private readonly objectEntryFolderId: number;

	constructor(actions: ActionsMap, objectEntryFolderId: number) {
		this.actions = actions[DefaultAssetTypes.OBJECT_ENTRY_FOLDERS] || [];
		this.objectEntryFolderId = objectEntryFolderId;
	}

	async loadValues(): Promise<RoleSelectedActions> {
		const {data, error} = await FolderService.getFolderPermissions(
			this.objectEntryFolderId
		);

		if (!data) {
			throw new Error(error);
		}

		const roleSelectedActions: RoleSelectedActions = {};

		for (const {actionIds, roleName} of data.items) {
			roleSelectedActions[roleName] = actionIds;
		}

		return roleSelectedActions;
	}
}
