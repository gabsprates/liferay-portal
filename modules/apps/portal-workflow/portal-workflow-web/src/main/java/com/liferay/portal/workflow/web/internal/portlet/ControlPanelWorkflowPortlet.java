/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.workflow.web.internal.portlet;

import com.liferay.portal.kernel.resource.bundle.ResourceBundleLoaderUtil;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.workflow.constants.WorkflowPortletKeys;
import com.liferay.portal.workflow.constants.WorkflowWebKeys;
import com.liferay.portal.workflow.web.internal.display.context.WorkflowNavigationDisplayContext;

import java.util.Arrays;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;

import org.osgi.service.component.annotations.Component;

/**
 * @author Adam Brandizzi
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.css-class-wrapper=portlet-workflow",
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.footer-portlet-javascript=/js/main.js",
		"com.liferay.portlet.friendly-url-mapping=workflow",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.icon=/icons/workflow.png",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.render-weight=50",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=Process Builder",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.template-path=/META-INF/resources/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + WorkflowPortletKeys.CONTROL_PANEL_WORKFLOW,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=administrator",
		"javax.portlet.version=3.0"
	},
	service = Portlet.class
)
public class ControlPanelWorkflowPortlet extends BaseWorkflowPortlet {

	@Override
	public List<String> getWorkflowPortletTabNames() {
		return Arrays.asList(
			WorkflowWebKeys.WORKFLOW_TAB_DEFINITION,
			WorkflowWebKeys.WORKFLOW_TAB_DEFINITION_LINK);
	}

	@Override
	protected void addRenderRequestAttributes(RenderRequest renderRequest) {
		super.addRenderRequestAttributes(renderRequest);

		WorkflowNavigationDisplayContext workflowNavigationDisplayContext =
			new WorkflowNavigationDisplayContext(
				renderRequest,
				ResourceBundleLoaderUtil.getPortalResourceBundleLoader());

		renderRequest.setAttribute(
			WorkflowWebKeys.WORKFLOW_NAVIGATION_DISPLAY_CONTEXT,
			workflowNavigationDisplayContext);
	}

	@Override
	protected void checkPermissions(PortletRequest portletRequest)
		throws Exception {

		super.checkPermissions(portletRequest);

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		if (!permissionChecker.isCompanyAdmin()) {
			throw new PrincipalException.MustBeCompanyAdmin(
				permissionChecker.getUserId());
		}
	}

}