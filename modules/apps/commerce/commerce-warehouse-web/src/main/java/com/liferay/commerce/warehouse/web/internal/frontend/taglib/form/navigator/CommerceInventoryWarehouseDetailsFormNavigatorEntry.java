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

import com.liferay.commerce.inventory.model.CommerceInventoryWarehouse;
import com.liferay.commerce.warehouse.web.internal.constants.CommerceInventoryWarehouseFormNavigatorConstants;
import com.liferay.frontend.taglib.form.navigator.BaseJSPFormNavigatorEntry;
import com.liferay.frontend.taglib.form.navigator.FormNavigatorEntry;
import com.liferay.portal.kernel.language.Language;

import java.util.Locale;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Andrea Di Giorgi
 */
@Component(
	enabled = false, property = "form.navigator.entry.order:Integer=50",
	service = FormNavigatorEntry.class
)
public class CommerceInventoryWarehouseDetailsFormNavigatorEntry
	extends BaseJSPFormNavigatorEntry<CommerceInventoryWarehouse> {

	@Override
	public String getCategoryKey() {
		return CommerceInventoryWarehouseFormNavigatorConstants.
			CATEGORY_KEY_COMMERCE_WAREHOUSE_GENERAL;
	}

	@Override
	public String getFormNavigatorId() {
		return CommerceInventoryWarehouseFormNavigatorConstants.
			FORM_NAVIGATOR_ID_COMMERCE_WAREHOUSE;
	}

	@Override
	public String getKey() {
		return "details";
	}

	@Override
	public String getLabel(Locale locale) {
		return _language.get(locale, getKey());
	}

	@Override
	public ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	protected String getJspPath() {
		return "/commerce_inventory_warehouse/details.jsp";
	}

	@Reference
	private Language _language;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.warehouse.web)"
	)
	private ServletContext _servletContext;

}