/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {useResource} from '@clayui/data-provider';
import {
	ApiHelper,
	type AssetLibrary,
	type FileData,
	MultipleFileUploader,
} from '@liferay/site-cms-site-initializer';
import {fetch, getFileAsBase64} from 'frontend-js-web';
import React, {useEffect, useMemo, useState} from 'react';

import {HeadlessPage} from './types';

export interface IMultipleFilesUploaderWrapperProps {

	/**
	 * List of files that will represent the initial state of files to upload.
	 */
	files: FileData[];

	/**
	 * Callback for when cancel upload.
	 */
	onCloseUploadView: () => void;
}

function MultipleFileUploaderWrapper({
	files,
	onCloseUploadView,
}: IMultipleFilesUploaderWrapperProps) {
	const {resource: assetLibraries = []}: {resource: AssetLibrary[]} =
		useResource({
			fetch: async (link) => {
				const result = await fetch(link);

				const contentType = result.headers.get('Content-Type') || '';

				if (!contentType.includes('application/json')) {
					console.warn(
						'The ItemSelector expects an application/json response from apiURL provided.'
					);

					return;
				}

				const json = await result.json();

				if (!Array.isArray(json.items)) {
					console.warn(
						'The ItemSelector expects the response from apiURL to include an array of items.'
					);

					return json;
				}

				const {items} = json as HeadlessPage<AssetLibrary>;

				return {items};
			},
			link: `${location.origin}/o/headless-asset-library/v1.0/asset-libraries?filter=type eq 'Space'`,
		});

	const uploadRequest = async ({
		fileData,
		groupId,
	}: {
		fileData: FileData;
		groupId: string;
	}) => {
		const fileBase64 = await getFileAsBase64(fileData.file);

		return await ApiHelper.post(
			`/o/cms/basic-documents/scopes/${groupId}`,
			{
				file: {
					fileBase64,
					name: fileData.name,
				},
				objectEntryFolderExternalReferenceCode: 'L_FILES',
				title: fileData.name,
			}
		);
	};

	console.log(assetLibraries);

	return (
		<MultipleFileUploader
			assetLibraries={assetLibraries}
			filesToUpload={files}
			onModalClose={onCloseUploadView}
			onUploadComplete={(params) => {
				console.log(`DONE!\n${JSON.stringify(params, null, 2)}`);

				// setViewType('fds');

				onCloseUploadView();
			}}
			uploadRequest={uploadRequest}
		/>
	);
}

export default MultipleFileUploaderWrapper;
