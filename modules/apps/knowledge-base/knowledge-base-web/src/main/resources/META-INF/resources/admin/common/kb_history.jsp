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

<%@ include file="/admin/common/init.jsp" %>

<%
KBArticle kbArticle = (KBArticle)request.getAttribute(KBWebKeys.KNOWLEDGE_BASE_KB_ARTICLE);

int status = (Integer)request.getAttribute(KBWebKeys.KNOWLEDGE_BASE_STATUS);

int sourceVersion = ParamUtil.getInteger(request, "sourceVersion", kbArticle.getVersion() - 1);
int targetVersion = ParamUtil.getInteger(request, "targetVersion", kbArticle.getVersion());

String orderByCol = ParamUtil.getString(request, "orderByCol", "version");
String orderByType = ParamUtil.getString(request, "orderByType", "desc");

boolean portletTitleBasedNavigation = GetterUtil.getBoolean(portletConfig.getInitParameter("portlet-title-based-navigation"));

if (portletTitleBasedNavigation) {
	portletDisplay.setShowBackIcon(true);
	portletDisplay.setURLBack(redirect);

	renderResponse.setTitle(kbArticle.getTitle());
}
%>

<c:if test="<%= !portletTitleBasedNavigation %>">
	<liferay-ui:header
		backURL="<%= redirect %>"
		localizeTitle="<%= false %>"
		title="<%= kbArticle.getTitle() %>"
	/>
</c:if>

<aui:fieldset cssClass='<%= portletTitleBasedNavigation ? "container-fluid container-fluid-max-xl panel" : StringPool.BLANK %>' markupView="lexicon">

	<%
	RowChecker rowChecker = new RowChecker(renderResponse);

	rowChecker.setAllRowIds(null);

	int selStatus = KBArticlePermission.contains(permissionChecker, kbArticle, KBActionKeys.UPDATE) ? WorkflowConstants.STATUS_ANY : status;
	%>

	<liferay-portlet:renderURL varImpl="iteratorURL">
		<portlet:param name="mvcPath" value="/admin/common/kb_history.jsp" />
		<portlet:param name="resourcePrimKey" value="<%= String.valueOf(kbArticle.getResourcePrimKey()) %>" />
		<portlet:param name="status" value="<%= String.valueOf(status) %>" />
	</liferay-portlet:renderURL>

	<liferay-ui:search-container
		emptyResultsMessage="no-articles-were-found"
		iteratorURL="<%= iteratorURL %>"
		orderByCol="<%= orderByCol %>"
		orderByComparator="<%= KBUtil.getKBArticleOrderByComparator(orderByCol, orderByType) %>"
		orderByType="<%= orderByType %>"
		rowChecker="<%= rowChecker %>"
		total="<%= KBArticleServiceUtil.getKBArticleVersionsCount(scopeGroupId, kbArticle.getResourcePrimKey(), selStatus) %>"
	>
		<liferay-ui:search-container-results
			results="<%= KBArticleServiceUtil.getKBArticleVersions(scopeGroupId, kbArticle.getResourcePrimKey(), selStatus, searchContainer.getStart(), searchContainer.getEnd(), searchContainer.getOrderByComparator()) %>"
		/>

		<liferay-ui:search-container-row
			className="com.liferay.knowledge.base.model.KBArticle"
			escapedModel="<%= true %>"
			keyProperty="version"
			modelVar="curKBArticle"
		>
			<liferay-portlet:renderURL var="rowURL">
				<portlet:param name="mvcPath" value="/admin/common/kb_history.jsp" />
				<portlet:param name="resourcePrimKey" value="<%= String.valueOf(curKBArticle.getResourcePrimKey()) %>" />
				<portlet:param name="status" value="<%= String.valueOf(status) %>" />
				<portlet:param name="sourceVersion" value="<%= String.valueOf(curKBArticle.getVersion()) %>" />
				<portlet:param name="targetVersion" value="<%= String.valueOf(curKBArticle.getVersion()) %>" />
			</liferay-portlet:renderURL>

			<liferay-ui:search-container-column-text
				cssClass="kb-column-no-wrap"
				href="<%= rowURL %>"
				name="version"
				orderable="<%= true %>"
			>
				<%= curKBArticle.getVersion() %>

				<c:choose>
					<c:when test="<%= (curKBArticle.getVersion() == sourceVersion) && (curKBArticle.getVersion() == targetVersion) %>">
						(<liferay-ui:message key="selected" />)
					</c:when>
					<c:when test="<%= curKBArticle.getVersion() == sourceVersion %>">
						(<liferay-ui:message key="source" />)
					</c:when>
					<c:when test="<%= curKBArticle.getVersion() == targetVersion %>">
						(<liferay-ui:message key="target" />)
					</c:when>
				</c:choose>
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-text
				href="<%= rowURL %>"
				name="author"
				orderable="<%= true %>"
				orderableProperty="user-name"
				property="userName"
			/>

			<liferay-ui:search-container-column-date
				cssClass="kb-column-no-wrap"
				href="<%= rowURL %>"
				name="date"
				orderable="<%= true %>"
				orderableProperty="modified-date"
				value="<%= curKBArticle.getModifiedDate() %>"
			/>

			<c:if test="<%= (status == WorkflowConstants.STATUS_ANY) || KBArticlePermission.contains(permissionChecker, kbArticle, KBActionKeys.UPDATE) %>">
				<liferay-ui:search-container-column-text
					cssClass="kb-column-no-wrap"
					href="<%= rowURL %>"
					name="status"
					orderable="<%= true %>"
					value='<%= curKBArticle.getStatus() + " (" + LanguageUtil.get(request, WorkflowConstants.getStatusLabel(curKBArticle.getStatus())) + ")" %>'
				/>
			</c:if>

			<liferay-ui:search-container-column-text
				cssClass="kb-column-no-wrap"
				href="<%= rowURL %>"
				name="views"
				orderable="<%= true %>"
				orderableProperty="view-count"
				property="viewCount"
			/>

			<c:if test="<%= KBArticlePermission.contains(permissionChecker, kbArticle, KBActionKeys.UPDATE) %>">
				<liferay-ui:search-container-column-text
					align="right"
				>
					<liferay-portlet:actionURL name="/knowledge_base/update_kb_article" varImpl="revertURL">
						<portlet:param name="mvcPath" value="/admin/common/kb_history.jsp" />
						<portlet:param name="redirect" value="<%= redirect %>" />
						<portlet:param name="resourcePrimKey" value="<%= String.valueOf(kbArticle.getResourcePrimKey()) %>" />
						<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.REVERT %>" />
						<portlet:param name="version" value="<%= String.valueOf(curKBArticle.getVersion()) %>" />
						<portlet:param name="workflowAction" value="<%= String.valueOf(WorkflowConstants.ACTION_PUBLISH) %>" />
					</liferay-portlet:actionURL>

					<%
					revertURL.setParameter("section", AdminUtil.unescapeSections(curKBArticle.getSections()));
					%>

					<liferay-ui:icon
						icon="undo"
						label="<%= true %>"
						markupView="lexicon"
						message="revert"
						url="<%= revertURL.toString() %>"
					/>
				</liferay-ui:search-container-column-text>
			</c:if>
		</liferay-ui:search-container-row>

		<aui:button-row>
			<aui:button name="compare" type="submit" value="compare-versions" />
		</aui:button-row>

		<liferay-ui:search-iterator
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</aui:fieldset>

<aui:script require="frontend-js-web/liferay/delegate/delegate.es as delegateModule">
	var compareVersionsButton = document.getElementById(
		'<portlet:namespace />compare'
	);

	if (compareVersionsButton) {
		compareVersionsButton.addEventListener('click', (event) => {
			var rowIds = document.querySelectorAll(
				'input[name="<portlet:namespace />rowIds"]:checked'
			);

			if (rowIds.length === 2) {
				<portlet:renderURL var="compareVersionURL">
					<portlet:param name="mvcPath" value="/admin/common/compare_versions.jsp" />
					<portlet:param name="<%= Constants.CMD %>" value="compareVersions" />
					<portlet:param name="backURL" value="<%= currentURL %>" />
					<portlet:param name="redirect" value="<%= redirect %>" />
					<portlet:param name="resourcePrimKey" value="<%= String.valueOf(kbArticle.getResourcePrimKey()) %>" />
				</portlet:renderURL>

				var uri = '<%= HtmlUtil.escapeJS(compareVersionURL) %>';

				uri = Liferay.Util.addParams(
					'<portlet:namespace />sourceVersion=' + rowIds[1].value,
					uri
				);
				uri = Liferay.Util.addParams(
					'<portlet:namespace />targetVersion=' + rowIds[0].value,
					uri
				);

				location.href = uri;
			}
		});
	}

	function <portlet:namespace />initRowsChecked() {
		Array.from(
			document.querySelectorAll('input[name=<portlet:namespace />rowIds]')
		).forEach((item, index, collection) => {
			if (index >= 2) {
				item.checked = false;
			}
		});
	}

	function <portlet:namespace />updateRowsChecked(element) {
		var rowsChecked = Array.from(
			document.querySelectorAll(
				'input[name=<portlet:namespace />rowIds]:checked'
			)
		);

		if (rowsChecked.length > 2) {
			var index = 2;

			if (rowsChecked[2] === element) {
				index = 1;
			}

			rowsChecked[index].checked = false;
		}
	}

	<portlet:namespace />initRowsChecked();

	var delegate = delegateModule.default;

	delegate(
		document.body,
		'click',
		'input[name=<portlet:namespace />rowIds]',
		(event) => {
			<portlet:namespace />updateRowsChecked(event.delegateTarget);
		}
	);
</aui:script>