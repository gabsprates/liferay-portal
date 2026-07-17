/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.application.list.display.context.logic.test;

import com.liferay.application.list.PanelCategory;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.application.list.display.context.logic.test.constants.ApplicationsMenuTestPortletKeys;
import com.liferay.application.list.util.PanelCategoryRegistryUtil;
import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Objects;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Gabriel Prates
 */
@RunWith(Arquillian.class)
public class ApplicationsPanelCategoryTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testShowForCompanyAdmin() throws Exception {
		Assert.assertTrue(
			_hasApplicationsCategory(
				PermissionCheckerFactoryUtil.create(
					TestPropsValues.getUser())));
	}

	@Test
	public void testShowForUserWithApplicationsMenuApp() throws Exception {
		_user = UserTestUtil.addUser();

		_role = RoleTestUtil.addRole(
			RandomTestUtil.randomString(), RoleConstants.TYPE_REGULAR,
			ApplicationsMenuTestPortletKeys.APPLICATIONS_MENU_TEST_PORTLET,
			ResourceConstants.SCOPE_COMPANY,
			String.valueOf(TestPropsValues.getCompanyId()),
			ActionKeys.ACCESS_IN_CONTROL_PANEL);

		UserLocalServiceUtil.addRoleUsers(
			_role.getRoleId(), new long[] {_user.getUserId()});

		Assert.assertTrue(
			_hasApplicationsCategory(
				PermissionCheckerFactoryUtil.create(_user)));
	}

	@Test
	public void testShowForUserWithoutApplicationsMenuApp() throws Exception {
		_user = UserTestUtil.addUser();

		Assert.assertFalse(
			_hasApplicationsCategory(
				PermissionCheckerFactoryUtil.create(_user)));
	}

	private boolean _hasApplicationsCategory(
			PermissionChecker permissionChecker)
		throws Exception {

		Group group = GroupLocalServiceUtil.getCompanyGroup(
			TestPropsValues.getCompanyId());

		for (PanelCategory panelCategory :
				PanelCategoryRegistryUtil.getChildPanelCategories(
					PanelCategoryKeys.APPLICATIONS_MENU, permissionChecker,
					group)) {

			if (Objects.equals(
					panelCategory.getKey(),
					PanelCategoryKeys.APPLICATIONS_MENU_APPLICATIONS)) {

				return true;
			}
		}

		return false;
	}

	@DeleteAfterTestRun
	private Role _role;

	@DeleteAfterTestRun
	private User _user;

}