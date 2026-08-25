/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.configuration.admin.web.internal.util;

import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.Collections;
import java.util.Set;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.Mockito;

import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

/**
 * @author Gabriel Prates
 */
public class ScopePropertyKeysUtilTest {

	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

	@Test
	public void testGetDeclaredScopePropertyKeys() {
		Assert.assertEquals(
			Collections.emptySet(),
			ScopePropertyKeysUtil.getDeclaredScopePropertyKeys(
				_toObjectClassDefinition("queueName", "userId")));

		Assert.assertEquals(
			Collections.singleton("groupId"),
			ScopePropertyKeysUtil.getDeclaredScopePropertyKeys(
				_toObjectClassDefinition("groupId", "queueName")));
	}

	@Test
	public void testGetDeclaredScopePropertyKeysWhenAttributesAreNull() {
		ObjectClassDefinition objectClassDefinition = Mockito.mock(
			ObjectClassDefinition.class);

		Mockito.when(
			objectClassDefinition.getAttributeDefinitions(
				ObjectClassDefinition.ALL)
		).thenReturn(
			null
		);

		Assert.assertEquals(
			Collections.emptySet(),
			ScopePropertyKeysUtil.getDeclaredScopePropertyKeys(
				objectClassDefinition));
	}

	@Test
	public void testGetDeclaredScopePropertyKeysWithPortableKey() {
		Set<String> declaredScopePropertyKeys =
			ScopePropertyKeysUtil.getDeclaredScopePropertyKeys(
				_toObjectClassDefinition(
					"companyWebId", "groupKey", "siteExternalReferenceCode"));

		Assert.assertTrue(declaredScopePropertyKeys.contains("companyWebId"));
		Assert.assertTrue(declaredScopePropertyKeys.contains("groupKey"));
		Assert.assertTrue(
			declaredScopePropertyKeys.contains("siteExternalReferenceCode"));
	}

	private ObjectClassDefinition _toObjectClassDefinition(String... ids) {
		AttributeDefinition[] attributeDefinitions =
			new AttributeDefinition[ids.length];

		for (int i = 0; i < ids.length; i++) {
			AttributeDefinition attributeDefinition = Mockito.mock(
				AttributeDefinition.class);

			Mockito.when(
				attributeDefinition.getID()
			).thenReturn(
				ids[i]
			);

			attributeDefinitions[i] = attributeDefinition;
		}

		ObjectClassDefinition objectClassDefinition = Mockito.mock(
			ObjectClassDefinition.class);

		Mockito.when(
			objectClassDefinition.getAttributeDefinitions(
				ObjectClassDefinition.ALL)
		).thenReturn(
			attributeDefinitions
		);

		return objectClassDefinition;
	}

}