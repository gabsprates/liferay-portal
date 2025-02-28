<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
CommercePriceListItemSelectorViewDisplayContext commercePriceListItemSelectorViewDisplayContext = (CommercePriceListItemSelectorViewDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

String itemSelectedEventName = commercePriceListItemSelectorViewDisplayContext.getItemSelectedEventName();
%>

<clay:management-toolbar
	managementToolbarDisplayContext="<%= new CommercePriceListItemSelectorViewManagementToolbarDisplayContext(request, liferayPortletRequest, liferayPortletResponse, commercePriceListItemSelectorViewDisplayContext.getSearchContainer()) %>"
/>

<div class="container-fluid container-fluid-max-xl" id="<portlet:namespace />commercePriceListSelectorWrapper">
	<liferay-ui:search-container
		id="commercePriceLists"
		searchContainer="<%= commercePriceListItemSelectorViewDisplayContext.getSearchContainer() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.commerce.price.list.model.CommercePriceList"
			cssClass="commerce-price-list-row"
			keyProperty="commercePriceListId"
			modelVar="commercePriceList"
		>
			<liferay-ui:search-container-column-text
				cssClass="table-cell-expand"
				property="name"
			/>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-expand"
				name="author"
				property="userName"
			/>

			<liferay-ui:search-container-column-status
				cssClass="table-cell-expand"
				name="status"
				status="<%= commercePriceList.getStatus() %>"
			/>

			<liferay-ui:search-container-column-date
				cssClass="table-cell-expand"
				name="create-date"
				property="createDate"
			/>

			<liferay-ui:search-container-column-date
				cssClass="table-cell-expand"
				name="display-date"
				property="displayDate"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="list"
			markupView="lexicon"
			searchContainer="<%= commercePriceListItemSelectorViewDisplayContext.getSearchContainer() %>"
		/>
	</liferay-ui:search-container>
</div>

<aui:script use="liferay-search-container">
	var commercePriceListSelectorWrapper = A.one(
		'#<portlet:namespace />commercePriceListSelectorWrapper'
	);

	var searchContainer = Liferay.SearchContainer.get(
		'<portlet:namespace />commercePriceLists'
	);

	searchContainer.on('rowToggled', (event) => {
		Liferay.Util.getOpener().Liferay.fire(
			'<%= HtmlUtil.escapeJS(itemSelectedEventName) %>',
			{
				data: Liferay.Util.getCheckedCheckboxes(
					commercePriceListSelectorWrapper,
					'<portlet:namespace />allRowIds'
				),
			}
		);
	});
</aui:script>