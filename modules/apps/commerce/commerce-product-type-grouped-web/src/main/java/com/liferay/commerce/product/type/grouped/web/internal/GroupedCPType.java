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

package com.liferay.commerce.product.type.grouped.web.internal;

import com.liferay.commerce.product.type.CPType;
import com.liferay.commerce.product.type.grouped.constants.GroupedCPTypeConstants;
import com.liferay.commerce.product.type.grouped.service.CPDefinitionGroupedEntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marco Leo
 * @author Andrea Di Giorgi
 */
@Component(
	enabled = false, immediate = true,
	property = {
		"commerce.product.type.display.order:Integer=10",
		"commerce.product.type.name=" + GroupedCPTypeConstants.NAME
	},
	service = CPType.class
)
public class GroupedCPType implements CPType {

	@Override
	public void deleteCPDefinition(long cpDefinitionId) throws PortalException {
		_cpDefinitionGroupedEntryLocalService.deleteCPDefinitionGroupedEntries(
			cpDefinitionId);
	}

	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return _language.get(resourceBundle, GroupedCPTypeConstants.NAME);
	}

	@Override
	public String getName() {
		return GroupedCPTypeConstants.NAME;
	}

	@Reference
	private CPDefinitionGroupedEntryLocalService
		_cpDefinitionGroupedEntryLocalService;

	@Reference
	private Language _language;

}