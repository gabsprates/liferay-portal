/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import getPermissionsPageConfig from '../../../../src/main/resources/META-INF/resources/js/main_view/permissions/getPermissionsPageConfig';

const CONTENT_ACTIONS = [{key: 'VIEW', label: 'View Content'}];

const FILE_ACTIONS = [{key: 'DOWNLOAD', label: 'Download'}];

const FOLDER_ACTIONS = [{key: 'ADD_ENTRY', label: 'Add Entry'}];

function _getSectionActions(
	sections: ReturnType<typeof getPermissionsPageConfig>['sections']
) {
	return Object.fromEntries(
		sections.map((section) => [section.key, section.actions])
	);
}

describe('getPermissionsPageConfig', () => {
	it('gives each folder section the actions of its type', () => {
		const {sections} = getPermissionsPageConfig({
			actions: {
				L_CONTENTS: CONTENT_ACTIONS,
				L_FILES: FILE_ACTIONS,
				OBJECT_ENTRY_FOLDERS: FOLDER_ACTIONS,
			},
			className: 'com.liferay.object.model.ObjectEntryFolder',
			classPK: 1,
			defaultsSections: ['subfolders', 'contents'],
		});

		expect(_getSectionActions(sections)).toEqual({
			'contents': CONTENT_ACTIONS,
			'own-permissions': FOLDER_ACTIONS,
			'subfolders': FOLDER_ACTIONS,
		});
	});
});
