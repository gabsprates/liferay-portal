/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import '@testing-library/jest-dom';
import {render, screen, waitFor} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import {openToast} from 'frontend-js-components-web';
import {fetch, navigate} from 'frontend-js-web';
import React from 'react';

import Permissions from '../../../../src/main/resources/META-INF/resources/js/main_view/permissions/Permissions';

jest.mock('frontend-js-components-web', () => ({
	...jest.requireActual('frontend-js-components-web'),
	openToast: jest.fn(),
}));

const mockFetch = fetch as jest.Mock;

const ACTIONS = {
	L_CONTENTS: [{key: 'VIEW', label: 'View Content'}],
	OBJECT_ENTRY_FOLDERS: [
		{key: 'UPDATE', label: 'Update'},
		{key: 'VIEW', label: 'View'},
	],
};

const BACK_URL = '/cms/contents';

const FOLDER_CLASS_NAME = 'com.liferay.object.model.ObjectEntryFolder';

const ROLES = [
	{key: 'Space Member', name: 'Space Member', type: '5'},
	{actions: ['VIEW'], key: 'User', name: 'User', type: '1'},
];

function _getCheckbox(roleKey: string, actionKey: string) {
	return screen.getByTestId(`row-checkbox-${roleKey}_${actionKey}`);
}

function _mockFolderPermissions() {
	mockFetch.mockResolvedValueOnce({
		json: async () => ({
			items: [
				{actionIds: ['VIEW'], roleName: 'Space Member'},
				{actionIds: ['VIEW'], roleName: 'User'},
			],
		}),
		ok: true,
		status: 200,
	});
}

function _renderFolder(defaultsSections = ['subfolders', 'contents']) {
	return render(
		<Permissions
			actions={ACTIONS}
			backURL={BACK_URL}
			className={FOLDER_CLASS_NAME}
			classPK={1}
			defaultsSections={defaultsSections as any}
			roles={ROLES}
		/>
	);
}

describe('Permissions', () => {
	beforeEach(() => {
		jest.clearAllMocks();
	});

	it('shows the folder sections in the sidebar', async () => {
		_mockFolderPermissions();

		_renderFolder();

		expect(
			screen.getByRole('heading', {name: 'permissions'})
		).toBeInTheDocument();
		expect(
			screen.getByRole('heading', {name: 'this-folder'})
		).toBeInTheDocument();
		expect(screen.getByText('subfolders')).toBeInTheDocument();
		expect(screen.queryByText('files')).not.toBeInTheDocument();

		await screen.findByText('Space Member');

		await userEvent.click(screen.getByText('contents'));

		expect(
			screen.getByRole('heading', {name: 'contents'})
		).toBeInTheDocument();
	});

	it('shows the files section for a folder that holds files', async () => {
		_mockFolderPermissions();

		_renderFolder(['subfolders', 'files']);

		expect(screen.getByText('files')).toBeInTheDocument();
		expect(screen.queryByText('contents')).not.toBeInTheDocument();

		await screen.findByText('Space Member');
	});

	it('shows the folder roles and their current permissions', async () => {
		_mockFolderPermissions();

		_renderFolder();

		await screen.findByText('Space Member');

		expect(screen.getByText('User')).toBeInTheDocument();
		expect(screen.getByText('Update')).toBeInTheDocument();
		expect(screen.getByText('View')).toBeInTheDocument();

		expect(_getCheckbox('Space Member', 'VIEW')).toBeChecked();
		expect(_getCheckbox('Space Member', 'UPDATE')).not.toBeChecked();
	});

	it('shows inherited permissions as checked and read only', async () => {
		_mockFolderPermissions();

		_renderFolder();

		await screen.findByText('User');

		expect(_getCheckbox('User', 'VIEW')).toBeChecked();
		expect(_getCheckbox('User', 'VIEW')).toBeDisabled();
		expect(_getCheckbox('User', 'UPDATE')).toBeEnabled();
	});

	it('keeps changes across sections without saving them', async () => {
		_mockFolderPermissions();

		_renderFolder();

		await screen.findByText('Space Member');

		await userEvent.click(_getCheckbox('Space Member', 'UPDATE'));

		expect(_getCheckbox('Space Member', 'UPDATE')).toBeChecked();

		await userEvent.click(screen.getByText('subfolders'));
		await userEvent.click(screen.getByText('this-folder'));

		expect(_getCheckbox('Space Member', 'UPDATE')).toBeChecked();
		expect(mockFetch).toHaveBeenCalledTimes(1);
	});

	it('filters the roles', async () => {
		_mockFolderPermissions();

		_renderFolder();

		await screen.findByText('Space Member');

		await userEvent.type(screen.getByPlaceholderText('search-for'), 'spa');

		expect(screen.getByText('Space Member')).toBeInTheDocument();
		expect(screen.queryByText('User')).not.toBeInTheDocument();
	});

	it('shows an error when the permissions cannot be loaded', async () => {
		mockFetch.mockResolvedValueOnce({
			json: async () => ({title: 'Not allowed'}),
			ok: false,
			status: 403,
		});

		_renderFolder();

		await waitFor(() =>
			expect(openToast).toHaveBeenCalledWith({
				message: 'Not allowed',
				type: 'danger',
			})
		);

		expect(screen.queryByText('Space Member')).not.toBeInTheDocument();
	});

	it('describes the folder section', async () => {
		_mockFolderPermissions();

		_renderFolder();

		expect(
			screen.getByText('applies-to-this-folder-only')
		).toBeInTheDocument();

		await screen.findByText('Space Member');
	});

	it('does not show a sidebar for an item', () => {
		render(
			<Permissions
				actions={{}}
				backURL={BACK_URL}
				className="com.liferay.object.model.ObjectDefinition#Z7P5"
				classPK={1}
				defaultsSections={[]}
				roles={[]}
			/>
		);

		expect(
			screen.getByRole('heading', {level: 2, name: 'permissions'})
		).toBeInTheDocument();
		expect(screen.queryByText('this-folder')).not.toBeInTheDocument();
	});

	it('labels the first section for a space', () => {
		render(
			<Permissions
				actions={{}}
				backURL={BACK_URL}
				className="com.liferay.depot.model.DepotEntry"
				classPK={1}
				defaultsSections={['subfolders']}
				roles={[]}
			/>
		);

		expect(
			screen.getByRole('heading', {level: 2, name: 'this-space'})
		).toBeInTheDocument();
	});

	it('returns to the previous view on cancel', async () => {
		_mockFolderPermissions();

		_renderFolder();

		await screen.findByText('Space Member');

		await userEvent.click(screen.getByRole('button', {name: 'cancel'}));

		expect(navigate).toHaveBeenCalledWith(BACK_URL);
	});

	it('links the back button to the previous view', async () => {
		_mockFolderPermissions();

		_renderFolder();

		await screen.findByText('Space Member');

		expect(screen.getByRole('button', {name: 'back'})).toHaveAttribute(
			'href',
			BACK_URL
		);
	});
});
