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

package com.liferay.apio.architect.test.util.pagination;

import com.liferay.apio.architect.impl.pagination.PaginationImpl;
import com.liferay.apio.architect.pagination.Pagination;

/**
 * A utility for creating Pagination instances.
 *
 * @author Julio Camarero
 * @review
 */
public class PaginationRequest {

	/**
	 * Creates a new {@link Pagination} instance.
	 *
	 * @param  itemsPerPage the number of items per page
	 * @param  pageNumber the page number
	 * @return the {@link Pagination} instance
	 * @review
	 */
	public static Pagination of(int itemsPerPage, int pageNumber) {
		return new PaginationImpl(itemsPerPage, pageNumber);
	}

}