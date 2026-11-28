/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.cms.site.initializer.internal.fragment.renderer;

import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.service.DepotEntryLocalService;
import com.liferay.fragment.renderer.FragmentRenderer;
import com.liferay.fragment.renderer.FragmentRendererContext;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectEntryFolderConstants;
import com.liferay.object.entry.folder.util.ObjectEntryFolderUtil;
import com.liferay.object.model.ObjectDefinition;
import com.liferay.object.model.ObjectEntry;
import com.liferay.object.model.ObjectEntryFolder;
import com.liferay.object.rest.filter.factory.FilterFactory;
import com.liferay.object.service.ObjectEntryFolderLocalService;
import com.liferay.object.service.ObjectEntryLocalService;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionRegistryUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.site.cms.site.initializer.internal.util.PermissionUtil;
import com.liferay.site.cms.site.initializer.util.CMSDefaultPermissionUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Patrick Yeo
 */
@Component(service = FragmentRenderer.class)
public class PermissionsComponentSectionFragmentRenderer
	extends BaseComponentSectionFragmentRenderer {

	@Override
	public String getCollectionKey() {
		return "sections";
	}

	@Override
	public void render(
			FragmentRendererContext fragmentRendererContext,
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse)
		throws IOException {

		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		if (!FeatureFlagManagerUtil.isEnabled(
				themeDisplay.getCompanyId(), "LPD-114063")) {

			return;
		}

		super.render(
			fragmentRendererContext, httpServletRequest, httpServletResponse);
	}

	@Override
	protected String getComponentName() {
		return "Permissions";
	}

	@Override
	protected String getLabelKey() {
		return "permissions";
	}

	@Override
	protected Map<String, Object> getProps(
			FragmentRendererContext fragmentRendererContext,
			HttpServletRequest httpServletRequest)
		throws Exception {

		String className = ParamUtil.getString(httpServletRequest, "className");

		ModelResourcePermission<?> modelResourcePermission =
			ModelResourcePermissionRegistryUtil.getModelResourcePermission(
				className);

		if (modelResourcePermission == null) {
			throw new PrincipalException();
		}

		long classPK = ParamUtil.getLong(httpServletRequest, "classPK");
		ThemeDisplay themeDisplay =
			(ThemeDisplay)httpServletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		modelResourcePermission.check(
			themeDisplay.getPermissionChecker(), classPK,
			ActionKeys.PERMISSIONS);

		return HashMapBuilder.<String, Object>put(
			"actions",
			_getActions(className, classPK, httpServletRequest, themeDisplay)
		).put(
			"backURL", _getBackURL(httpServletRequest, themeDisplay)
		).put(
			"className", className
		).put(
			"classPK", classPK
		).put(
			"defaultPermissions", _getDefaultPermissions(className, classPK)
		).put(
			"defaultsSections", _getDefaultsSections(className, classPK)
		).put(
			"roles", _getRoles(className, classPK, themeDisplay)
		).build();
	}

	private Map<String, Map<String, Object>[]> _getActions(
			String className, long classPK,
			HttpServletRequest httpServletRequest, ThemeDisplay themeDisplay)
		throws Exception {

		if (Objects.equals(className, DepotEntry.class.getName())) {
			Map<String, Map<String, Object>[]> actions =
				PermissionUtil.getActionsMap(httpServletRequest, themeDisplay);

			actions.put(
				"SPACE",
				PermissionUtil.getActions(className, httpServletRequest));

			return actions;
		}

		if (Objects.equals(className, ObjectEntryFolder.class.getName())) {
			return PermissionUtil.getActionsMap(
				httpServletRequest, themeDisplay);
		}

		if (!className.startsWith(ObjectDefinition.class.getName())) {
			return Collections.emptyMap();
		}

		ObjectEntry objectEntry = _objectEntryLocalService.getObjectEntry(
			classPK);

		ObjectEntryFolder rootObjectEntryFolder =
			_objectEntryFolderLocalService.fetchObjectEntryFolder(
				ObjectEntryFolderUtil.getRootObjectEntryFolderId(
					objectEntry.getObjectEntryFolderId()));

		if (rootObjectEntryFolder == null) {
			return Collections.emptyMap();
		}

		return HashMapBuilder.<String, Map<String, Object>[]>put(
			rootObjectEntryFolder.getExternalReferenceCode(),
			PermissionUtil.getActions(className, httpServletRequest)
		).build();
	}

	private String _getBackURL(
		HttpServletRequest httpServletRequest, ThemeDisplay themeDisplay) {

		String backURL = PortalUtil.escapeRedirect(
			ParamUtil.getString(httpServletRequest, "backURL"));

		if (Validator.isNotNull(backURL)) {
			return backURL;
		}

		return themeDisplay.getPathFriendlyURLPublic() +
			GroupConstants.CMS_FRIENDLY_URL;
	}

	private Map<String, Object> _getDefaultPermissions(
			String className, long classPK)
		throws Exception {

		JSONObject jsonObject = null;

		if (Objects.equals(className, DepotEntry.class.getName())) {
			DepotEntry depotEntry = _depotEntryLocalService.getDepotEntry(
				classPK);

			Group group = depotEntry.getGroup();

			jsonObject = CMSDefaultPermissionUtil.getJSONObject(
				group.getCompanyId(), group.getCreatorUserId(),
				group.getExternalReferenceCode(), className, _filterFactory);
		}
		else if (Objects.equals(className, ObjectEntryFolder.class.getName())) {
			ObjectEntryFolder objectEntryFolder =
				_objectEntryFolderLocalService.getObjectEntryFolder(classPK);

			jsonObject = CMSDefaultPermissionUtil.getJSONObject(
				objectEntryFolder.getCompanyId(), objectEntryFolder.getUserId(),
				objectEntryFolder.getExternalReferenceCode(), className,
				_filterFactory);
		}
		else if (className.startsWith(ObjectDefinition.class.getName())) {
			jsonObject = _getObjectEntryDefaultPermissionsJSONObject(classPK);
		}

		if (jsonObject == null) {
			return Collections.emptyMap();
		}

		return jsonObject.toMap();
	}

	private List<String> _getDefaultsSections(String className, long classPK) {
		if (!Objects.equals(className, ObjectEntryFolder.class.getName())) {
			return Collections.emptyList();
		}

		ObjectEntryFolder rootObjectEntryFolder =
			_objectEntryFolderLocalService.fetchObjectEntryFolder(
				ObjectEntryFolderUtil.getRootObjectEntryFolderId(classPK));

		if (rootObjectEntryFolder == null) {
			return ListUtil.fromArray("subfolders");
		}

		if (Objects.equals(
				rootObjectEntryFolder.getExternalReferenceCode(),
				ObjectEntryFolderConstants.EXTERNAL_REFERENCE_CODE_CONTENTS)) {

			return ListUtil.fromArray("subfolders", "contents");
		}

		if (Objects.equals(
				rootObjectEntryFolder.getExternalReferenceCode(),
				ObjectEntryFolderConstants.EXTERNAL_REFERENCE_CODE_FILES)) {

			return ListUtil.fromArray("subfolders", "files");
		}

		return ListUtil.fromArray("subfolders");
	}

	private JSONObject _getObjectEntryDefaultPermissionsJSONObject(
			long objectEntryId)
		throws Exception {

		ObjectEntry objectEntry = _objectEntryLocalService.getObjectEntry(
			objectEntryId);

		JSONObject jsonObject =
			CMSDefaultPermissionUtil.getCMSDefaultPermissionJSONObject(
				objectEntry, _filterFactory);

		if (jsonObject == null) {
			return null;
		}

		ObjectEntryFolder rootObjectEntryFolder =
			_objectEntryFolderLocalService.fetchObjectEntryFolder(
				ObjectEntryFolderUtil.getRootObjectEntryFolderId(
					objectEntry.getObjectEntryFolderId()));

		if (rootObjectEntryFolder == null) {
			return null;
		}

		return jsonObject.getJSONObject(
			rootObjectEntryFolder.getExternalReferenceCode());
	}

	private Map<String, Object>[] _getRoles(
			String className, long classPK, ThemeDisplay themeDisplay)
		throws Exception {

		if (!Objects.equals(className, ObjectEntryFolder.class.getName())) {
			return new Map[0];
		}

		ObjectEntryFolder objectEntryFolder =
			_objectEntryFolderLocalService.getObjectEntryFolder(classPK);

		return PermissionUtil.getRoles(
			className, objectEntryFolder.getGroupId(), themeDisplay);
	}

	@Reference
	private DepotEntryLocalService _depotEntryLocalService;

	@Reference(
		target = "(filter.factory.key=" + ObjectDefinitionConstants.STORAGE_TYPE_DEFAULT + ")"
	)
	private FilterFactory<Predicate> _filterFactory;

	@Reference
	private ObjectEntryFolderLocalService _objectEntryFolderLocalService;

	@Reference
	private ObjectEntryLocalService _objectEntryLocalService;

}