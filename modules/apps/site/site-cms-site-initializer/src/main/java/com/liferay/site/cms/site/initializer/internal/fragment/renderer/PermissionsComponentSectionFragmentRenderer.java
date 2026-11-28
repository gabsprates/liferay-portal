/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.cms.site.initializer.internal.fragment.renderer;

import com.liferay.fragment.renderer.FragmentRenderer;
import com.liferay.fragment.renderer.FragmentRendererContext;
import com.liferay.object.constants.ObjectDefinitionConstants;
import com.liferay.object.constants.ObjectEntryFolderConstants;
import com.liferay.object.entry.folder.util.ObjectEntryFolderUtil;
import com.liferay.object.model.ObjectEntryFolder;
import com.liferay.object.rest.filter.factory.FilterFactory;
import com.liferay.object.service.ObjectEntryFolderLocalService;
import com.liferay.petra.sql.dsl.expression.Predicate;
import com.liferay.portal.kernel.feature.flag.FeatureFlagManagerUtil;
import com.liferay.portal.kernel.json.JSONObject;
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
			"actions", _getActions(className, httpServletRequest, themeDisplay)
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
		String className, HttpServletRequest httpServletRequest,
		ThemeDisplay themeDisplay) {

		if (!Objects.equals(className, ObjectEntryFolder.class.getName())) {
			return Collections.emptyMap();
		}

		return PermissionUtil.getActionsMap(httpServletRequest, themeDisplay);
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

		if (!Objects.equals(className, ObjectEntryFolder.class.getName())) {
			return Collections.emptyMap();
		}

		ObjectEntryFolder objectEntryFolder =
			_objectEntryFolderLocalService.getObjectEntryFolder(classPK);

		JSONObject jsonObject = CMSDefaultPermissionUtil.getJSONObject(
			objectEntryFolder.getCompanyId(), objectEntryFolder.getUserId(),
			objectEntryFolder.getExternalReferenceCode(), className,
			_filterFactory);

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

	@Reference(
		target = "(filter.factory.key=" + ObjectDefinitionConstants.STORAGE_TYPE_DEFAULT + ")"
	)
	private FilterFactory<Predicate> _filterFactory;

	@Reference
	private ObjectEntryFolderLocalService _objectEntryFolderLocalService;

}