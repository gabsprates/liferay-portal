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

package com.liferay.frontend.data.set.filter;

import java.util.Map;

/**
 * @author Marco Leo
 */
public interface FDSFilter {

	public default String getEntityFieldType() {
		return null;
	}

	public String getId();

	public String getLabel();

	public default Map<String, Object> getPreloadedData() {
		return null;
	}

	public String getType();

}