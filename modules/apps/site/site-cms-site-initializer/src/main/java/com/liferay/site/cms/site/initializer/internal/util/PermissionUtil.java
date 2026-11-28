/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.cms.site.initializer.internal.util;

import com.liferay.depot.constants.DepotRolesConstants;
import com.liferay.depot.util.DepotRoleUtil;
import com.liferay.object.constants.ObjectEntryFolderConstants;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntryFolder;
import com.liferay.object.service.ObjectDefinitionLocalServiceUtil;
import com.liferay.petra.function.transform.TransformUtil;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Adolfo Pérez
 */
public class PermissionUtil {

	public static Map<String, Object>[] getActions(
		String className, HttpServletRequest httpServletRequest) {

		List<String> guestUnsupportedActions =
			ResourceActionsUtil.getResourceGuestUnsupportedActions(
				null, className);

		return TransformUtil.transformToArray(
			ResourceActionsUtil.getResourceActions(className),
			resourceAction -> HashMapBuilder.<String, Object>put(
				"guestUnsupported",
				guestUnsupportedActions.contains(resourceAction)
			).put(
				"key", resourceAction
			).put(
				"label",
				ResourceActionsUtil.getAction(
					httpServletRequest, resourceAction)
			).build(),
			Map.class);
	}

	public static Map<String, Map<String, Object>[]> getActionsMap(
		HttpServletRequest httpServletRequest, ThemeDisplay themeDisplay) {

		return HashMapBuilder.<String, Map<String, Object>[]>put(
			ObjectEntryFolderConstants.EXTERNAL_REFERENCE_CODE_CONTENTS,
			() -> {
				ObjectDefinition objectDefinition =
					ObjectDefinitionLocalServiceUtil.
						getObjectDefinitionByExternalReferenceCode(
							"L_CMS_BASIC_WEB_CONTENT",
							themeDisplay.getCompanyId());

				return getActions(
					objectDefinition.getClassName(), httpServletRequest);
			}
		).put(
			ObjectEntryFolderConstants.EXTERNAL_REFERENCE_CODE_FILES,
			() -> {
				ObjectDefinition objectDefinition =
					ObjectDefinitionLocalServiceUtil.
						getObjectDefinitionByExternalReferenceCode(
							"L_CMS_BASIC_DOCUMENT",
							themeDisplay.getCompanyId());

				return getActions(
					objectDefinition.getClassName(), httpServletRequest);
			}
		).put(
			"OBJECT_ENTRY_FOLDERS",
			() -> getActions(
				ObjectEntryFolder.class.getName(), httpServletRequest)
		).build();
	}

	public static Map<String, Object> getDefaultPermissionAdditionalProps(
		boolean allowPropagate, HttpServletRequest httpServletRequest,
		ThemeDisplay themeDisplay) {

		return HashMapBuilder.<String, Object>put(
			"actions", () -> getActionsMap(httpServletRequest, themeDisplay)
		).put(
			"allowPropagate", allowPropagate
		).put(
			"roles",
			() -> TransformUtil.transformToArray(
				RoleLocalServiceUtil.getGroupRolesAndTeamRoles(
					themeDisplay.getCompanyId(), null,
					Arrays.asList(
						RoleConstants.ADMINISTRATOR,
						DepotRolesConstants.ASSET_LIBRARY_OWNER),
					null, null,
					new int[] {
						RoleConstants.TYPE_REGULAR, RoleConstants.TYPE_DEPOT
					},
					DepotRolesConstants.SUBTYPE_SPACE, 0, 0, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS),
				role -> HashMapBuilder.put(
					"key", role.getName()
				).put(
					"name", role.getTitle(themeDisplay.getLocale())
				).put(
					"type", String.valueOf(role.getType())
				).build(),
				Map.class)
		).build();
	}

	public static Map<String, Object> getDefaultPermissionAdditionalProps(
		HttpServletRequest httpServletRequest, ThemeDisplay themeDisplay) {

		return getDefaultPermissionAdditionalProps(
			false, httpServletRequest, themeDisplay);
	}

	public static Map<String, Object>[] getRoles(
		String className, long groupId, ThemeDisplay themeDisplay) {

		List<String> resourceActions = ResourceActionsUtil.getResourceActions(
			className);

		Map<Long, Set<String>> companyActionIds =
			ResourcePermissionLocalServiceUtil.
				getAvailableResourcePermissionActionIds(
					themeDisplay.getCompanyId(), className,
					ResourceConstants.SCOPE_COMPANY,
					String.valueOf(themeDisplay.getCompanyId()),
					resourceActions);
		Map<Long, Set<String>> groupActionIds =
			ResourcePermissionLocalServiceUtil.
				getAvailableResourcePermissionActionIds(
					themeDisplay.getCompanyId(), className,
					ResourceConstants.SCOPE_GROUP, String.valueOf(groupId),
					resourceActions);

		String subtype = null;

		if (FeatureFlagManagerUtil.isEnabled(
				themeDisplay.getCompanyId(), "LPD-96750")) {

			subtype = DepotRoleUtil.getSubtype(groupId);
		}

		return TransformUtil.transformToArray(
			RoleLocalServiceUtil.getGroupRolesAndTeamRoles(
				themeDisplay.getCompanyId(), null,
				Arrays.asList(
					RoleConstants.ADMINISTRATOR,
					DepotRolesConstants.ASSET_LIBRARY_OWNER,
					DepotRolesConstants.DESIGN_LIBRARY_OWNER),
				null, null,
				new int[] {
					RoleConstants.TYPE_DEPOT, RoleConstants.TYPE_REGULAR
				},
				subtype, 0, groupId, QueryUtil.ALL_POS, QueryUtil.ALL_POS),
			role -> {
				Set<String> actionIds = new HashSet<>(
					companyActionIds.getOrDefault(
						role.getRoleId(), Collections.emptySet()));

				actionIds.addAll(
					groupActionIds.getOrDefault(
						role.getRoleId(), Collections.emptySet()));

				return HashMapBuilder.<String, Object>put(
					"actions", ArrayUtil.toStringArray(actionIds)
				).put(
					"key", role.getName()
				).put(
					"name", role.getTitle(themeDisplay.getLocale())
				).put(
					"type", String.valueOf(role.getType())
				).build();
			},
			Map.class);
	}

}