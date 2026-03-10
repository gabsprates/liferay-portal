/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.design.library.web.internal.display.context;

import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.service.DepotEntryLocalServiceUtil;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;

import com.liferay.portal.kernel.util.WebKeys;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * @author Gabriel Prates
 */
public class DesignLibraryDashboardDisplayContext {

	public DesignLibraryDashboardDisplayContext(
		HttpServletRequest httpServletRequest) {

		_httpServletRequest = httpServletRequest;

		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

	}

	public String getAPIURL() {
		return "/o/search/v1.0/search?page=1&pageSize=20&emptySearch=true"
			+ "&filter=cmsRoot eq true and cmsSection eq 'files' and status in (0, 2, 3, 1, 7)"
			+ "&nestedFields=embedded,embeddedTaxonomyCategory,file.metadata,file.previewURL,file.thumbnailURL,numberOfObjectEntries,numberOfObjectEntryFolders,systemProperties.objectDefinitionBrief";
	}

	public Map<String, Object> getEmptyState() {
		return HashMapBuilder.<String, Object>put(
			"description",
			LanguageUtil.get(
				_httpServletRequest,
				"click-new-to-create-or-import-your-design-resource")
		).put(
			"image", "/states/resources_empty_state.svg"
		).put(
			"title",
			LanguageUtil.get(_httpServletRequest, "no-design-resources-yet")
		).build();
	}

	public Map<String, Object> getHeaderProps(long designLibraryEntryId) {
		DepotEntry designLibraryDepotEntry = DepotEntryLocalServiceUtil.fetchDepotEntry(designLibraryEntryId);

		return HashMapBuilder.<String, Object>put(
			"actionItems", () -> {
				JSONArray jsonArray = JSONUtil.putAll();

				jsonArray.put(
					JSONUtil.put(
						"href", "#settings"
					).put(
						"title",
						LanguageUtil.get(_httpServletRequest, "settings")
					).put(
						"symbolLeft", "cog"
					)
				).put(
					JSONUtil.put(
						"href", "#connected-sites"
					).put(
						"title",
						LanguageUtil.get(_httpServletRequest, "connected-sites")
					).put(
						"symbolLeft", "globe"
					)
				).put(
					JSONUtil.put(
						"href", "#manage-members"
					).put(
						"title",
						LanguageUtil.get(_httpServletRequest, "manage-members")
					).put(
						"symbolLeft", "users"
					)
				).put(
					JSONUtil.put(
						"href", "#import"
					).put(
						"title",
						LanguageUtil.get(_httpServletRequest, "import")
					).put(
						"symbolLeft", "import"
					)
				).put(
					JSONUtil.put(
						"href", "#export"
					).put(
						"title",
						LanguageUtil.get(_httpServletRequest, "export")
					).put(
						"symbolLeft", "export"
					)
				).put(
					JSONUtil.put(
						"href", "#delete"
					).put(
						"title",
						LanguageUtil.get(_httpServletRequest, "delete")
					).put(
						"symbolLeft", "trash"
					)
				);

				return jsonArray;
			}
		).put(
			"breadcrumbProps", () -> {
				HashMapBuilder.HashMapWrapper<String, Object> propsHashMap = HashMapBuilder.put(
					"redirect", HashMapBuilder.<String,Object>put(
						"active", false
					).put(
						"href", _themeDisplay.getPortletDisplay().getURLBack()
					).put(
						"label", LanguageUtil.get(_httpServletRequest, "design-libraries")
					).build()
				);

				try {
					Group group = designLibraryDepotEntry.getGroup();

					propsHashMap.put(
						"current", HashMapBuilder.<String,Object>put(
							"active", true
						).put(
							"href", null
						).put(
							"label", group.getName(_httpServletRequest.getLocale())
						).build()
					);

					return propsHashMap.build();
				}
				catch (Exception exception) {
					return propsHashMap.build();
				}
			}
		).build();
	}

	private final HttpServletRequest _httpServletRequest;

	private final ThemeDisplay _themeDisplay;

}