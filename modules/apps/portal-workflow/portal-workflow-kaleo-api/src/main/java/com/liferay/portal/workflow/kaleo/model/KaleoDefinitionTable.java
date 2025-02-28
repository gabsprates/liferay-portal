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

package com.liferay.portal.workflow.kaleo.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Clob;
import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;KaleoDefinition&quot; database table.
 *
 * @author Brian Wing Shun Chan
 * @see KaleoDefinition
 * @generated
 */
public class KaleoDefinitionTable extends BaseTable<KaleoDefinitionTable> {

	public static final KaleoDefinitionTable INSTANCE =
		new KaleoDefinitionTable();

	public final Column<KaleoDefinitionTable, Long> mvccVersion = createColumn(
		"mvccVersion", Long.class, Types.BIGINT, Column.FLAG_NULLITY);
	public final Column<KaleoDefinitionTable, Long> ctCollectionId =
		createColumn(
			"ctCollectionId", Long.class, Types.BIGINT, Column.FLAG_PRIMARY);
	public final Column<KaleoDefinitionTable, Long> kaleoDefinitionId =
		createColumn(
			"kaleoDefinitionId", Long.class, Types.BIGINT, Column.FLAG_PRIMARY);
	public final Column<KaleoDefinitionTable, Long> groupId = createColumn(
		"groupId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionTable, Long> companyId = createColumn(
		"companyId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionTable, Long> userId = createColumn(
		"userId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionTable, String> userName = createColumn(
		"userName", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionTable, Date> createDate = createColumn(
		"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionTable, Date> modifiedDate = createColumn(
		"modifiedDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionTable, String> name = createColumn(
		"name", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionTable, String> title = createColumn(
		"title", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionTable, String> description =
		createColumn(
			"description", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionTable, Clob> content = createColumn(
		"content", Clob.class, Types.CLOB, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionTable, String> scope = createColumn(
		"scope", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionTable, Integer> version = createColumn(
		"version", Integer.class, Types.INTEGER, Column.FLAG_DEFAULT);
	public final Column<KaleoDefinitionTable, Boolean> active = createColumn(
		"active_", Boolean.class, Types.BOOLEAN, Column.FLAG_DEFAULT);

	private KaleoDefinitionTable() {
		super("KaleoDefinition", KaleoDefinitionTable::new);
	}

}