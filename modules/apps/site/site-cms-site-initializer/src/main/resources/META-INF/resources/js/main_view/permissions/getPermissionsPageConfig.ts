/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {
	DEPOT_ENTRY_CLASS_NAME,
	OBJECT_ENTRY_FOLDER_CLASS_NAME,
} from '../../common/utils/constants';
import {
	ActionsMap,
	DefaultAssetTypes,
} from '../default_permission/DefaultPermissionTypes';
import {
	DefaultsSection,
	PermissionsPageConfig,
	PermissionsSection,
} from './PermissionsPageConfig';
import ContentOwnPermissionSection from './sections/ContentOwnPermissionSection';
import ContentsDefaultPermissionSection from './sections/ContentsDefaultPermissionSection';
import FileOwnPermissionSection from './sections/FileOwnPermissionSection';
import FilesDefaultPermissionSection from './sections/FilesDefaultPermissionSection';
import FolderOwnPermissionSection from './sections/FolderOwnPermissionSection';
import SpaceOwnPermissionSection from './sections/SpaceOwnPermissionSection';
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

	if (className === DEPOT_ENTRY_CLASS_NAME) {
		return new SpaceOwnPermissionSection(actions);
	}

	if (actions[DefaultAssetTypes.L_FILES]) {
		return new FileOwnPermissionSection(actions);
	}

	return new ContentOwnPermissionSection(actions);
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
