/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {
	Action,
	RoleSelectedActions,
} from '../default_permission/DefaultPermissionTypes';

export type DefaultsSection = 'contents' | 'files' | 'subfolders';

export const OWN_PERMISSIONS = 'own-permissions';

export interface PermissionsSection {
	actions: Action[];
	description?: string;
	key: string;
	label: string;
	loadValues?: () => Promise<RoleSelectedActions>;
}

export interface PermissionsPageConfig {
	sections: PermissionsSection[];
}
