/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.client.extension.internal.upgrade.v3_5_3;

import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.client.extension.constants.ClientExtensionEntryConstants;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.UnicodePropertiesBuilder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Gabriel Prates
 */
public class ClientExtensionEntryPanelCategoryKeyUpgradeProcess
	extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		String selectSQL = StringBundler.concat(
			"select clientExtensionEntryId, typeSettings from ",
			"ClientExtensionEntry where type_ = '",
			ClientExtensionEntryConstants.TYPE_CUSTOM_ELEMENT, "'");
		String updateSQL =
			"update ClientExtensionEntry set typeSettings = ? where " +
				"clientExtensionEntryId = ?";

		try (Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(selectSQL);

			PreparedStatement preparedStatement =
				AutoBatchPreparedStatementUtil.autoBatch(
					connection, updateSQL)) {

			while (resultSet.next()) {
				UnicodeProperties unicodeProperties =
					UnicodePropertiesBuilder.create(
						true
					).fastLoad(
						resultSet.getString("typeSettings")
					).build();

				if (!ArrayUtil.contains(
						_REMOVED_PANEL_CATEGORY_KEYS,
						unicodeProperties.getProperty("panelCategoryKey"))) {

					continue;
				}

				unicodeProperties.setProperty(
					"panelCategoryKey",
					PanelCategoryKeys.
						APPLICATIONS_MENU_APPLICATIONS_DEVELOPER_INTEGRATION);

				preparedStatement.setString(1, unicodeProperties.toString());
				preparedStatement.setLong(
					2, resultSet.getLong("clientExtensionEntryId"));

				preparedStatement.addBatch();
			}

			preparedStatement.executeBatch();
		}
	}

	private static final String[] _REMOVED_PANEL_CATEGORY_KEYS = {
		"applications_menu.applications.batch_planner",
		"applications_menu.applications.commerce",
		"applications_menu.applications.communication",
		"applications_menu.applications.content",
		"applications_menu.applications.custom.apps",
		"applications_menu.applications.design",
		"applications_menu.applications.personalization",
		"applications_menu.applications.publications",
		"control_panel.search_experiences", "control_panel.search_tuning"
	};

}