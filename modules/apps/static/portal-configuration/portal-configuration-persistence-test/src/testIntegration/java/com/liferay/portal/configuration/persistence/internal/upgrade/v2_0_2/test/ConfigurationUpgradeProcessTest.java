/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.configuration.persistence.internal.upgrade.v2_0_2.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.upgrade.UpgradeStep;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;

import java.util.Dictionary;
import java.util.Objects;

import org.apache.felix.cm.PersistenceManager;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Gabriel Prates
 */
@RunWith(Arquillian.class)
public class ConfigurationUpgradeProcessTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		_upgradeStepRegistrator.register(
			new UpgradeStepRegistrator.Registry() {

				@Override
				public void register(
					String fromSchemaVersionString,
					String toSchemaVersionString, UpgradeStep... upgradeSteps) {

					for (UpgradeStep upgradeStep : upgradeSteps) {
						Class<?> clazz = upgradeStep.getClass();

						if (Objects.equals(clazz.getName(), _CLASS_NAME)) {
							_configurationUpgradeProcess =
								(UpgradeProcess)upgradeStep;
						}
					}
				}

			});
	}

	@Test
	public void testDoUpgradeRemovesInjectedCompanyId() throws Exception {
		_persistenceManager.store(
			_PID,
			HashMapDictionaryBuilder.<String, Object>put(
				"companyId", CompanyThreadLocal.getCompanyId()
			).put(
				"groupId", 20119L
			).put(
				"queueName", "LPP-65300"
			).put(
				"service.pid", _PID
			).build());

		try {
			_configurationUpgradeProcess.upgrade();

			Dictionary<?, ?> dictionary = _persistenceManager.load(_PID);

			Assert.assertNull(
				dictionary.get(
					ExtendedObjectClassDefinition.Scope.COMPANY.
						getPropertyKey()));

			Assert.assertEquals(Long.valueOf(20119), dictionary.get("groupId"));
			Assert.assertEquals("LPP-65300", dictionary.get("queueName"));
		}
		finally {
			_persistenceManager.delete(_PID);
		}
	}

	private static final String _CLASS_NAME =
		"com.liferay.portal.configuration.persistence.internal.upgrade." +
			"v2_0_2.ConfigurationUpgradeProcess";

	private static final String _PID =
		"com.liferay.portal.configuration.persistence.test.configuration." +
			"DeclaredScopePropertyKeysConfiguration";

	private static UpgradeProcess _configurationUpgradeProcess;

	@Inject(
		filter = "(&(component.name=com.liferay.portal.configuration.persistence.internal.upgrade.registry.ConfigurationPersistenceUpgradeStepRegistrator))"
	)
	private static UpgradeStepRegistrator _upgradeStepRegistrator;

	@Inject
	private PersistenceManager _persistenceManager;

}