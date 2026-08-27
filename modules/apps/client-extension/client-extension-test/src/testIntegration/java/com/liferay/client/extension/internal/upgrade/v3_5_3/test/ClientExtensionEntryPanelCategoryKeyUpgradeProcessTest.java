/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.client.extension.internal.upgrade.v3_5_3.test;

import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.client.extension.constants.ClientExtensionEntryConstants;
import com.liferay.client.extension.model.ClientExtensionEntry;
import com.liferay.client.extension.service.ClientExtensionEntryLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.util.UpgradeProcessUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;
import com.liferay.portal.kernel.version.Version;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.portal.upgrade.test.util.UpgradeTestUtil;

import java.util.Collections;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Gabriel Prates
 */
@RunWith(Arquillian.class)
public class ClientExtensionEntryPanelCategoryKeyUpgradeProcessTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testUpgrade() throws Exception {
		_blankClientExtensionEntry = _addClientExtensionEntry(StringPool.BLANK);
		_mergedClientExtensionEntry = _addClientExtensionEntry(
			"control_panel.search_tuning");
		_removedClientExtensionEntry = _addClientExtensionEntry(
			"applications_menu.applications.custom.apps");
		_supportedClientExtensionEntry = _addClientExtensionEntry(
			PanelCategoryKeys.CONTROL_PANEL_OBJECT);

		for (UpgradeProcess upgradeProcess :
				UpgradeTestUtil.getUpgradeSteps(
					_upgradeStepRegistrator, new Version(3, 5, 3))) {

			upgradeProcess.upgrade();
		}

		EntityCacheUtil.clearCache();

		Assert.assertEquals(
			StringPool.BLANK,
			_getTypeSetting(_blankClientExtensionEntry, "panelCategoryKey"));
		Assert.assertEquals(
			PanelCategoryKeys.
				APPLICATIONS_MENU_APPLICATIONS_DEVELOPER_INTEGRATION,
			_getTypeSetting(_mergedClientExtensionEntry, "panelCategoryKey"));
		Assert.assertEquals(
			PanelCategoryKeys.
				APPLICATIONS_MENU_APPLICATIONS_DEVELOPER_INTEGRATION,
			_getTypeSetting(_removedClientExtensionEntry, "panelCategoryKey"));
		Assert.assertEquals(
			PanelCategoryKeys.CONTROL_PANEL_OBJECT,
			_getTypeSetting(
				_supportedClientExtensionEntry, "panelCategoryKey"));

		Assert.assertEquals(
			_HTML_ELEMENT_NAME,
			_getTypeSetting(_removedClientExtensionEntry, "htmlElementName"));
		Assert.assertEquals(
			"100",
			_getTypeSetting(_removedClientExtensionEntry, "panelAppOrder"));
	}

	private ClientExtensionEntry _addClientExtensionEntry(
			String panelCategoryKey)
		throws Exception {

		return _clientExtensionEntryLocalService.addClientExtensionEntry(
			RandomTestUtil.randomString(), TestPropsValues.getUserId(),
			StringPool.BLANK,
			Collections.singletonMap(
				LocaleUtil.fromLanguageId(
					UpgradeProcessUtil.getDefaultLanguageId(
						TestPropsValues.getCompanyId())),
				RandomTestUtil.randomString()),
			StringPool.BLANK, StringPool.BLANK,
			ClientExtensionEntryConstants.TYPE_CUSTOM_ELEMENT,
			UnicodePropertiesBuilder.create(
				true
			).put(
				"htmlElementName", _HTML_ELEMENT_NAME
			).put(
				"panelAppOrder", 100
			).put(
				"panelCategoryKey", panelCategoryKey
			).put(
				"urls", "http://" + RandomTestUtil.randomString() + ".com"
			).buildString());
	}

	private String _getTypeSetting(
		ClientExtensionEntry clientExtensionEntry, String key) {

		ClientExtensionEntry persistedClientExtensionEntry =
			_clientExtensionEntryLocalService.fetchClientExtensionEntry(
				clientExtensionEntry.getClientExtensionEntryId());

		UnicodeProperties unicodeProperties = UnicodePropertiesBuilder.create(
			true
		).fastLoad(
			persistedClientExtensionEntry.getTypeSettings()
		).build();

		return GetterUtil.getString(unicodeProperties.getProperty(key));
	}

	private static final String _HTML_ELEMENT_NAME = "valid-html-element-name";

	@DeleteAfterTestRun
	private ClientExtensionEntry _blankClientExtensionEntry;

	@Inject
	private ClientExtensionEntryLocalService _clientExtensionEntryLocalService;

	@DeleteAfterTestRun
	private ClientExtensionEntry _mergedClientExtensionEntry;

	@DeleteAfterTestRun
	private ClientExtensionEntry _removedClientExtensionEntry;

	@DeleteAfterTestRun
	private ClientExtensionEntry _supportedClientExtensionEntry;

	@Inject(
		filter = "component.name=com.liferay.client.extension.internal.upgrade.registry.ClientExtensionUpgradeStepRegistrator"
	)
	private UpgradeStepRegistrator _upgradeStepRegistrator;

}