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

package com.liferay.commerce.warehouse.web.internal.frontend.taglib.form.navigator;

import com.liferay.commerce.warehouse.web.internal.constants.CommerceInventoryWarehouseFormNavigatorConstants;
import com.liferay.frontend.taglib.form.navigator.FormNavigatorCategory;
import com.liferay.portal.kernel.language.Language;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Di Giorgi
 */
@Component(
	enabled = false, property = "form.navigator.category.order:Integer=10",
	service = FormNavigatorCategory.class
)
public class CommerceInventoryWarehouseGeneralFormNavigatorCategory
	implements FormNavigatorCategory {

	@Override
	public String getFormNavigatorId() {
		return CommerceInventoryWarehouseFormNavigatorConstants.
			FORM_NAVIGATOR_ID_COMMERCE_WAREHOUSE;
	}

	@Override
	public String getKey() {
		return CommerceInventoryWarehouseFormNavigatorConstants.
			CATEGORY_KEY_COMMERCE_WAREHOUSE_GENERAL;
	}

	@Override
	public String getLabel(Locale locale) {
		return _language.get(locale, "general");
	}

	@Reference
	private Language _language;

}