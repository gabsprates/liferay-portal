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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/clay" prefix="clay" %><%@
taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %><%@
taglib uri="http://liferay.com/tld/util" prefix="liferay-util" %>

<%@ page import="com.liferay.frontend.taglib.clay.servlet.taglib.util.JSPNavigationItemList" %><%@
page import="com.liferay.oauth.client.admin.web.internal.display.context.OAuthClientASLocalMetadataManagementToolbarDisplayContext" %><%@
page import="com.liferay.oauth.client.admin.web.internal.display.context.OAuthClientEntriesManagementToolbarDisplayContext" %><%@
page import="com.liferay.oauth.client.persistence.constants.OAuthClientEntryConstants" %><%@
page import="com.liferay.oauth.client.persistence.exception.DuplicateOAuthClientASLocalMetadataException" %><%@
page import="com.liferay.oauth.client.persistence.exception.DuplicateOAuthClientEntryException" %><%@
page import="com.liferay.oauth.client.persistence.exception.OAuthClientASLocalMetadataJSONException" %><%@
page import="com.liferay.oauth.client.persistence.exception.OAuthClientASLocalMetadataLocalWellKnownURIException" %><%@
page import="com.liferay.oauth.client.persistence.exception.OAuthClientEntryAuthRequestParametersJSONException" %><%@
page import="com.liferay.oauth.client.persistence.exception.OAuthClientEntryAuthServerWellKnownURIException" %><%@
page import="com.liferay.oauth.client.persistence.exception.OAuthClientEntryInfoJSONException" %><%@
page import="com.liferay.oauth.client.persistence.exception.OAuthClientEntryOIDCUserInfoMapperJSONException" %><%@
page import="com.liferay.oauth.client.persistence.exception.OAuthClientEntryTokenRequestParametersJSONException" %><%@
page import="com.liferay.oauth.client.persistence.model.OAuthClientASLocalMetadata" %><%@
page import="com.liferay.oauth.client.persistence.model.OAuthClientEntry" %><%@
page import="com.liferay.oauth.client.persistence.service.OAuthClientASLocalMetadataLocalServiceUtil" %><%@
page import="com.liferay.oauth.client.persistence.service.OAuthClientASLocalMetadataServiceUtil" %><%@
page import="com.liferay.oauth.client.persistence.service.OAuthClientEntryLocalServiceUtil" %><%@
page import="com.liferay.oauth.client.persistence.service.OAuthClientEntryServiceUtil" %><%@
page import="com.liferay.petra.portlet.url.builder.PortletURLBuilder" %><%@
page import="com.liferay.petra.string.StringPool" %><%@
page import="com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker" %><%@
page import="com.liferay.portal.kernel.dao.search.ResultRow" %><%@
page import="com.liferay.portal.kernel.json.JSONUtil" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %>

<%@ page import="javax.portlet.PortletURL" %>

<liferay-frontend:defineObjects />

<liferay-theme:defineObjects />

<portlet:defineObjects />