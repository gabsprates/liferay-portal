/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {OBJECT_ENTRY_FOLDER_CLASS_NAME} from '../../common/utils/constants';
import {ActionsMap} from '../default_permission/DefaultPermissionTypes';
import {
	DefaultsSection,
	OWN_PERMISSIONS,
	PermissionsPageConfig,
	PermissionsSection,
} from './PermissionsPageConfig';
import ContentsDefaultPermissionSection from './sections/ContentsDefaultPermissionSection';
import FilesDefaultPermissionSection from './sections/FilesDefaultPermissionSection';
import FolderOwnPermissionSection from './sections/FolderOwnPermissionSection';
import SubfoldersDefaultPermissionSection from './sections/SubfoldersDefaultPermissionSection';

function _getDefaultsSection(
	actions: ActionsMap,
	defaultsSection: DefaultsSection
): PermissionsSection {
	if (defaultsSection === 'contents') {
		return new ContentsDefaultPermissionSection(actions);
	}

	if (defaultsSection === 'files') {
		return new FilesDefaultPermissionSection(actions);
	}

	return new SubfoldersDefaultPermissionSection(actions);
}

function _getOwnSection(
	actions: ActionsMap,
	className: string,
	classPK: number
): PermissionsSection {
	if (className === OBJECT_ENTRY_FOLDER_CLASS_NAME) {
		return new FolderOwnPermissionSection(actions, classPK);
	}

	return {
		actions: [],
		key: OWN_PERMISSIONS,
		label: Liferay.Language.get('permissions'),
	};
}

export default function getPermissionsPageConfig({
	actions,
	className,
	classPK,
	defaultsSections,
}: {
	actions: ActionsMap;
	className: string;
	classPK: number;
	defaultsSections: DefaultsSection[];
}): PermissionsPageConfig {
	return {
		sections: [
			_getOwnSection(actions, className, classPK),
			...defaultsSections.map((defaultsSection) =>
				_getDefaultsSection(actions, defaultsSection)
			),
		],
	};
}
