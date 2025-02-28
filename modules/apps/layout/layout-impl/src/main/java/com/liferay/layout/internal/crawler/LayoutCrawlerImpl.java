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

package com.liferay.layout.internal.crawler;

import com.liferay.layout.crawler.LayoutCrawler;
import com.liferay.layout.internal.configuration.LayoutCrawlerClientConfiguration;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.service.CompanyLocalService;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.CookieKeys;
import com.liferay.portal.kernel.util.Http;
import com.liferay.portal.kernel.util.HttpComponentsUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.Validator;

import java.net.InetAddress;

import java.util.Locale;
import java.util.Objects;

import javax.servlet.http.Cookie;

import org.apache.http.HttpStatus;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pavel Savinov
 */
@Component(immediate = true, service = LayoutCrawler.class)
public class LayoutCrawlerImpl implements LayoutCrawler {

	@Override
	public String getLayoutContent(Layout layout, Locale locale)
		throws Exception {

		LayoutCrawlerClientConfiguration layoutCrawlerClientConfiguration =
			_configurationProvider.getGroupConfiguration(
				LayoutCrawlerClientConfiguration.class, layout.getGroupId());

		String hostName = _getHostName(layoutCrawlerClientConfiguration);

		if (Validator.isNull(hostName)) {
			if (_log.isWarnEnabled()) {
				_log.warn("Unable to get host name");
			}

			return StringPool.BLANK;
		}

		Company company = _companyLocalService.getCompany(
			layout.getCompanyId());

		Http.Options options = new Http.Options();

		options.addHeader(HttpHeaders.USER_AGENT, _USER_AGENT);
		options.addHeader("Host", company.getVirtualHostname());

		Cookie cookie = new Cookie(
			CookieKeys.GUEST_LANGUAGE_ID, LocaleUtil.toLanguageId(locale));

		cookie.setDomain(hostName);

		options.setCookies(new Cookie[] {cookie});

		ThemeDisplay themeDisplay = _getThemeDisplay(
			company, hostName, layout, layoutCrawlerClientConfiguration,
			locale);

		options.setLocation(
			HttpComponentsUtil.addParameter(
				_portal.getLayoutFullURL(layout, themeDisplay), "p_l_mode",
				Constants.SEARCH));

		String response = _http.URLtoString(options);

		Http.Response httpResponse = options.getResponse();

		if (httpResponse.getResponseCode() == HttpStatus.SC_OK) {
			return response;
		}

		return StringPool.BLANK;
	}

	private String _getHostName(
		LayoutCrawlerClientConfiguration layoutCrawlerClientConfiguration) {

		if (layoutCrawlerClientConfiguration.enabled()) {
			return layoutCrawlerClientConfiguration.hostName();
		}

		InetAddress inetAddress = _portal.getPortalServerInetAddress(
			_isHttpsEnabled());

		if (inetAddress != null) {
			return inetAddress.getHostName();
		}

		return null;
	}

	private String _getI18nPath(Locale locale) {
		Locale defaultLocale = _language.getLocale(locale.getLanguage());

		if (LocaleUtil.equals(defaultLocale, locale)) {
			return StringPool.SLASH + defaultLocale.getLanguage();
		}

		return StringPool.SLASH + locale.toLanguageTag();
	}

	private ThemeDisplay _getThemeDisplay(
			Company company, String hostName, Layout layout,
			LayoutCrawlerClientConfiguration layoutCrawlerClientConfiguration,
			Locale locale)
		throws Exception {

		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(company);
		themeDisplay.setI18nLanguageId(locale.toString());
		themeDisplay.setI18nPath(_getI18nPath(locale));
		themeDisplay.setLanguageId(LocaleUtil.toLanguageId(locale));
		themeDisplay.setLayout(layout);
		themeDisplay.setLayoutSet(layout.getLayoutSet());
		themeDisplay.setLocale(locale);
		themeDisplay.setScopeGroupId(layout.getGroupId());

		if (layoutCrawlerClientConfiguration.enabled()) {
			themeDisplay.setSecure(layoutCrawlerClientConfiguration.secure());
			themeDisplay.setServerName(
				layoutCrawlerClientConfiguration.hostName());
			themeDisplay.setServerPort(layoutCrawlerClientConfiguration.port());
		}
		else {
			themeDisplay.setServerName(hostName);
			themeDisplay.setServerPort(
				_portal.getPortalServerPort(_isHttpsEnabled()));
		}

		themeDisplay.setSiteGroupId(layout.getGroupId());

		return themeDisplay;
	}

	private boolean _isHttpsEnabled() {
		if (Objects.equals(
				Http.HTTPS,
				PropsUtil.get(PropsKeys.PORTAL_INSTANCE_PROTOCOL)) ||
			Objects.equals(
				Http.HTTPS, PropsUtil.get(PropsKeys.WEB_SERVER_PROTOCOL))) {

			return true;
		}

		return false;
	}

	private static final String _USER_AGENT = "Liferay Page Crawler";

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutCrawlerImpl.class);

	@Reference
	private CompanyLocalService _companyLocalService;

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private Http _http;

	@Reference
	private Language _language;

	@Reference
	private Portal _portal;

}