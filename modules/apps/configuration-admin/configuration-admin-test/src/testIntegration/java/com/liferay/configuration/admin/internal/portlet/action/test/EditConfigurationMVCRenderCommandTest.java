/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.configuration.admin.internal.portlet.action.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.configuration.admin.constants.ConfigurationAdminPortletKeys;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.portal.kernel.portlet.PortletConfigFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.service.PortletLocalService;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletActionRequest;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletActionResponse;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletRenderRequest;
import com.liferay.portal.kernel.test.portlet.MockLiferayPortletRenderResponse;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;

import jakarta.portlet.PortletException;

import java.util.Collections;

import org.junit.After;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * A configuration whose object class definition declares
 * <code>groupId</code> as an ordinary attribute must still be found at system
 * scope. See LPP-65300.
 *
 * @author Gabriel Prates
 */
@RunWith(Arquillian.class)
public class EditConfigurationMVCRenderCommandTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE);

	@After
	public void tearDown() throws Exception {
		if (_configuration != null) {
			_configuration.delete();

			_configuration = null;
		}
	}

	@Test
	public void testDeleteFindsConfigurationDeclaringGroupIdAttribute()
		throws Exception {

		_addConfiguration();

		MockLiferayPortletActionRequest mockLiferayPortletActionRequest =
			new MockLiferayPortletActionRequest();

		mockLiferayPortletActionRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay());
		mockLiferayPortletActionRequest.setParameter("pid", _PID);

		_mvcActionCommand.processAction(
			mockLiferayPortletActionRequest,
			new MockLiferayPortletActionResponse());

		Assert.assertNull(
			_configurationAdmin.listConfigurations(
				"(service.pid=" + _PID + ")"));

		_configuration = null;
	}

	@Test
	public void testRenderFindsConfigurationDeclaringGroupIdAttribute()
		throws Exception {

		_addConfiguration();

		Object configurationModel = _renderConfigurationModel(_PID);

		Assert.assertNotNull(
			ReflectionTestUtil.invoke(
				configurationModel, "getConfiguration", new Class<?>[0]));

		Assert.assertTrue(
			(Boolean)ReflectionTestUtil.invoke(
				configurationModel, "hasScopeConfiguration",
				new Class<?>[] {ExtendedObjectClassDefinition.Scope.class},
				ExtendedObjectClassDefinition.Scope.SYSTEM));
	}

	@Test
	public void testRenderFindsPlainConfiguration() throws Exception {
		_addPlainConfiguration();

		Object configurationModel = _renderConfigurationModel(_PLAIN_PID);

		Assert.assertTrue(
			(Boolean)ReflectionTestUtil.invoke(
				configurationModel, "hasScopeConfiguration",
				new Class<?>[] {ExtendedObjectClassDefinition.Scope.class},
				ExtendedObjectClassDefinition.Scope.SYSTEM));
	}

	@Test
	public void testSaveLogsNoMissingCompanyIdError() throws Exception {
		try (LogCapture logCapture = LoggerTestUtil.configureLog4JLogger(
				"com.liferay.configuration.admin.web.internal.configuration." +
					"persistence.listener." +
						"ConfigurationImportGlobalConfigurationModelListener",
				LoggerTestUtil.ERROR)) {

			_addConfiguration();

			Assert.assertEquals(
				Collections.emptyList(), logCapture.getLogEntries());
		}
	}

	private void _addConfiguration() throws Exception {
		_configuration = _configurationAdmin.getConfiguration(_PID, "?");

		_configuration.update(
			HashMapDictionaryBuilder.<String, Object>put(
				"groupId", 20119L
			).put(
				"queueName", _QUEUE_NAME
			).build());
	}

	private void _addPlainConfiguration() throws Exception {
		_configuration = _configurationAdmin.getConfiguration(_PLAIN_PID, "?");

		_configuration.update(
			HashMapDictionaryBuilder.<String, Object>put(
				"queueName", _QUEUE_NAME
			).build());
	}

	private MockLiferayPortletRenderRequest
			_getMockLiferayPortletRenderRequest()
		throws Exception {

		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			new MockLiferayPortletRenderRequest();

		mockLiferayPortletRenderRequest.setAttribute(
			JavaConstants.JAKARTA_PORTLET_CONFIG,
			PortletConfigFactoryUtil.create(
				_portletLocalService.getPortletById(
					ConfigurationAdminPortletKeys.SYSTEM_SETTINGS),
				null));
		mockLiferayPortletRenderRequest.setAttribute(
			WebKeys.THEME_DISPLAY, _getThemeDisplay());

		return mockLiferayPortletRenderRequest;
	}

	private ThemeDisplay _getThemeDisplay() throws Exception {
		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(
			_companyLocalService.getCompany(TestPropsValues.getCompanyId()));
		themeDisplay.setLocale(LocaleUtil.US);

		return themeDisplay;
	}

	private Object _renderConfigurationModel(String pid) throws Exception {
		MockLiferayPortletRenderRequest mockLiferayPortletRenderRequest =
			_getMockLiferayPortletRenderRequest();

		mockLiferayPortletRenderRequest.setParameter("factoryPid", pid);

		try {
			_mvcRenderCommand.render(
				mockLiferayPortletRenderRequest,
				new MockLiferayPortletRenderResponse());
		}
		catch (PortletException portletException) {

			// The DDM form renderer needs a real HTTP request, which the mock
			// render request cannot provide. The configuration model is bound
			// before the form is rendered, so the assertions still hold. A
			// failure earlier than that leaves the attribute unset, which the
			// callers report.

		}

		return mockLiferayPortletRenderRequest.getAttribute(
			"CONFIGURATION_MODEL");
	}

	private static final String _PID =
		"com.liferay.configuration.admin.test.configuration." +
			"DeclaredScopePropertyKeysConfiguration";

	private static final String _PLAIN_PID =
		"com.liferay.configuration.admin.test.configuration." +
			"PlainSystemConfiguration";

	private static final String _QUEUE_NAME = "LPP-65300";

	@Inject
	private CompanyLocalService _companyLocalService;

	private Configuration _configuration;

	@Inject
	private ConfigurationAdmin _configurationAdmin;

	@Inject(
		filter = "mvc.command.name=/configuration_admin/delete_configuration",
		type = MVCActionCommand.class
	)
	private MVCActionCommand _mvcActionCommand;

	@Inject(
		filter = "mvc.command.name=/configuration_admin/edit_configuration",
		type = MVCRenderCommand.class
	)
	private MVCRenderCommand _mvcRenderCommand;

	@Inject
	private PortletLocalService _portletLocalService;

}