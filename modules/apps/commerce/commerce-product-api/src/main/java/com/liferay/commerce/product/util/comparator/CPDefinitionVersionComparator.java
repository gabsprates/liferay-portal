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

package com.liferay.commerce.product.util.comparator;

import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * @author Andrea Sbarra
 */
public class CPDefinitionVersionComparator
	extends OrderByComparator<CPDefinition> {

	public static final String ORDER_BY_ASC = "version ASC";

	public static final String ORDER_BY_DESC = "version DESC";

	public static final String[] ORDER_BY_FIELDS = {"version"};

	public CPDefinitionVersionComparator() {
		this(false);
	}

	public CPDefinitionVersionComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(CPDefinition cpDefinition1, CPDefinition cpDefinition2) {
		int version1 = cpDefinition1.getVersion();
		int version2 = cpDefinition2.getVersion();

		int value = Integer.compare(version1, version2);

		if (_ascending) {
			return value;
		}

		return Math.negateExact(value);
	}

	@Override
	public String getOrderBy() {
		if (_ascending) {
			return ORDER_BY_ASC;
		}

		return ORDER_BY_DESC;
	}

	@Override
	public String[] getOrderByFields() {
		return ORDER_BY_FIELDS;
	}

	@Override
	public boolean isAscending() {
		return _ascending;
	}

	private final boolean _ascending;

}