/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {expect, mergeTests} from '@playwright/test';

import {apiHelpersTest} from '../../../fixtures/apiHelpersTest';
import {isolatedSiteTest} from '../../../fixtures/isolatedSiteTest';
import {loginTest} from '../../../fixtures/loginTest';
import {pageViewModePagesTest} from '../../../fixtures/pageViewModePagesTest';
import getRandomString from '../../../utils/getRandomString';

const test = mergeTests(
	apiHelpersTest,
	isolatedSiteTest,
	loginTest(),
	pageViewModePagesTest
);

test('Portlet can be dragged and dropped', async ({
	apiHelpers,
	page,
	site,
	widgetPagePage,
}) => {
	await test.step('Add portlet to the first column in the page layout', async () => {
		const layout = await apiHelpers.jsonWebServicesLayout.addLayout({
			groupId: site.id,
			options: {type: 'portlet'},
			title: getRandomString(),
		});

		await page.goto(`/web${site.friendlyUrlPath}${layout.friendlyURL}`);

		await widgetPagePage.addPortlet('Clay Sample');

		const portlet = page.getByRole('heading', {name: 'Clay Sample'});

		expect(
			page
				.getByRole('main')
				.locator('.portlet-column')
				.filter({has: portlet})
		).toHaveId('column-1');
	});

	await test.step('Drag and drop portlet to another column in page', async () => {
		await widgetPagePage.dragPortlet({
			portletName: 'Clay Sample',
			target: page
				.getByRole('main')
				.locator('.portlet-column .portlet-dropzone.empty'),
			topperSelector: '.portlet .portlet-topper',
		});

		const portlet = page.getByRole('heading', {name: 'Clay Sample'});

		expect(
			page
				.getByRole('main')
				.locator('.portlet-column')
				.filter({has: portlet})
		).toHaveId('column-2');
	});
});

test('Portlet can be removed', async ({
	apiHelpers,
	page,
	site,
	widgetPagePage,
}) => {
	await test.step('Add portlet to the page layout', async () => {
		const layout = await apiHelpers.jsonWebServicesLayout.addLayout({
			groupId: site.id,
			options: {type: 'portlet'},
			title: getRandomString(),
		});

		await page.goto(`/web${site.friendlyUrlPath}${layout.friendlyURL}`);

		await widgetPagePage.addPortlet('Clay Sample');

		const portlet = page.getByRole('heading', {name: 'Clay Sample'});

		expect(portlet).toBeVisible();
	});

	await test.step('Delete portlet from the page', async () => {
		const portlet = page.getByRole('heading', {name: 'Clay Sample'});

		await widgetPagePage.deletePortlet('Clay Sample');

		expect(portlet).not.toBeVisible();
	});
});
