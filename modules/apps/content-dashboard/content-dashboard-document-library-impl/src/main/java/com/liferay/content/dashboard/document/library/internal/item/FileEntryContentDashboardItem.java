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

package com.liferay.content.dashboard.document.library.internal.item;

import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetTag;
import com.liferay.content.dashboard.item.ContentDashboardItemVersion;
import com.liferay.content.dashboard.item.VersionableContentDashboardItem;
import com.liferay.content.dashboard.item.action.ContentDashboardItemAction;
import com.liferay.content.dashboard.item.action.ContentDashboardItemActionProviderTracker;
import com.liferay.content.dashboard.item.action.exception.ContentDashboardItemActionException;
import com.liferay.content.dashboard.item.action.provider.ContentDashboardItemActionProvider;
import com.liferay.content.dashboard.item.type.ContentDashboardItemSubtype;
import com.liferay.document.library.util.DLURLHelper;
import com.liferay.info.field.InfoFieldValue;
import com.liferay.info.item.InfoItemClassDetails;
import com.liferay.info.item.InfoItemFieldValues;
import com.liferay.info.item.InfoItemReference;
import com.liferay.info.item.provider.InfoItemFieldValuesProvider;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alejandro Tardín
 */
public class FileEntryContentDashboardItem
	implements VersionableContentDashboardItem<FileEntry> {

	public FileEntryContentDashboardItem(
		List<AssetCategory> assetCategories, List<AssetTag> assetTags,
		ContentDashboardItemActionProviderTracker
			contentDashboardItemActionProviderTracker,
		ContentDashboardItemSubtype contentDashboardItemSubtype,
		DLURLHelper dlURLHelper, FileEntry fileEntry, Group group,
		InfoItemFieldValuesProvider<FileEntry> infoItemFieldValuesProvider,
		Language language, Portal portal) {

		if (ListUtil.isEmpty(assetCategories)) {
			_assetCategories = Collections.emptyList();
		}
		else {
			_assetCategories = Collections.unmodifiableList(assetCategories);
		}

		if (ListUtil.isEmpty(assetTags)) {
			_assetTags = Collections.emptyList();
		}
		else {
			_assetTags = Collections.unmodifiableList(assetTags);
		}

		_contentDashboardItemActionProviderTracker =
			contentDashboardItemActionProviderTracker;
		_contentDashboardItemSubtype = contentDashboardItemSubtype;
		_dlURLHelper = dlURLHelper;
		_fileEntry = fileEntry;
		_group = group;
		_infoItemFieldValuesProvider = infoItemFieldValuesProvider;
		_language = language;
		_portal = portal;
	}

	@Override
	public List<ContentDashboardItemVersion> getAllContentDashboardItemVersions(
		ThemeDisplay themeDisplay) {

		int status = WorkflowConstants.STATUS_APPROVED;

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		User user = themeDisplay.getUser();

		if ((user.getUserId() == _fileEntry.getUserId()) ||
			permissionChecker.isContentReviewer(
				user.getCompanyId(), themeDisplay.getScopeGroupId())) {

			status = WorkflowConstants.STATUS_ANY;
		}

		Stream<FileVersion> stream = _fileEntry.getFileVersions(
			status
		).stream();

		return stream.map(
			fileVersion -> new ContentDashboardItemVersion(
				fileVersion.getChangeLog(), fileVersion.getCreateDate(),
				_language.get(
					themeDisplay.getLocale(),
					WorkflowConstants.getStatusLabel(fileVersion.getStatus())),
				WorkflowConstants.getStatusStyle(fileVersion.getStatus()),
				fileVersion.getUserName(),
				String.valueOf(fileVersion.getVersion()))
		).collect(
			Collectors.toList()
		);
	}

	@Override
	public List<AssetCategory> getAssetCategories() {
		return _assetCategories;
	}

	@Override
	public List<AssetCategory> getAssetCategories(long assetVocabularyId) {
		Stream<AssetCategory> stream = _assetCategories.stream();

		return stream.filter(
			assetCategory ->
				assetCategory.getVocabularyId() == assetVocabularyId
		).collect(
			Collectors.toList()
		);
	}

	@Override
	public List<AssetTag> getAssetTags() {
		return _assetTags;
	}

	@Override
	public List<Locale> getAvailableLocales() {
		return Collections.emptyList();
	}

	@Override
	public List<ContentDashboardItemAction> getContentDashboardItemActions(
		HttpServletRequest httpServletRequest,
		ContentDashboardItemAction.Type... types) {

		List<ContentDashboardItemActionProvider>
			contentDashboardItemActionProviders =
				_contentDashboardItemActionProviderTracker.
					getContentDashboardItemActionProviders(
						FileEntry.class.getName(), types);

		Stream<ContentDashboardItemActionProvider> stream =
			contentDashboardItemActionProviders.stream();

		return stream.map(
			contentDashboardItemActionProvider -> {
				try {
					return Optional.ofNullable(
						contentDashboardItemActionProvider.
							getContentDashboardItemAction(
								_fileEntry, httpServletRequest));
				}
				catch (ContentDashboardItemActionException
							contentDashboardItemActionException) {

					_log.error(contentDashboardItemActionException);
				}

				return Optional.<ContentDashboardItemAction>empty();
			}
		).filter(
			Optional::isPresent
		).map(
			Optional::get
		).collect(
			Collectors.toList()
		);
	}

	@Override
	public ContentDashboardItemSubtype getContentDashboardItemSubtype() {
		return _contentDashboardItemSubtype;
	}

	@Override
	public Date getCreateDate() {
		return _fileEntry.getCreateDate();
	}

	@Override
	public ContentDashboardItemAction getDefaultContentDashboardItemAction(
		HttpServletRequest httpServletRequest) {

		long userId = _portal.getUserId(httpServletRequest);

		Locale locale = _portal.getLocale(httpServletRequest);

		ContentDashboardItemVersion contentDashboardItemVersion =
			_getLastContentDashboardItemVersion(locale);

		if ((getUserId() == userId) &&
			Objects.equals(
				contentDashboardItemVersion.getLabel(),
				_language.get(
					locale,
					WorkflowConstants.getStatusLabel(
						WorkflowConstants.STATUS_DRAFT)))) {

			Optional<ContentDashboardItemActionProvider>
				contentDashboardItemActionProviderOptional =
					_contentDashboardItemActionProviderTracker.
						getContentDashboardItemActionProviderOptional(
							FileEntry.class.getName(),
							ContentDashboardItemAction.Type.EDIT);

			return contentDashboardItemActionProviderOptional.map(
				contentDashboardItemActionProvider ->
					_toContentDashboardItemAction(
						contentDashboardItemActionProvider, httpServletRequest)
			).orElse(
				null
			);
		}

		Optional<ContentDashboardItemActionProvider>
			viewContentDashboardItemActionProviderOptional =
				_contentDashboardItemActionProviderTracker.
					getContentDashboardItemActionProviderOptional(
						FileEntry.class.getName(),
						ContentDashboardItemAction.Type.VIEW);

		return viewContentDashboardItemActionProviderOptional.map(
			contentDashboardItemActionProvider -> _toContentDashboardItemAction(
				contentDashboardItemActionProvider, httpServletRequest)
		).orElseGet(
			() -> {
				Optional<ContentDashboardItemActionProvider>
					editContentDashboardItemActionProviderOptional =
						_contentDashboardItemActionProviderTracker.
							getContentDashboardItemActionProviderOptional(
								FileEntry.class.getName(),
								ContentDashboardItemAction.Type.EDIT);

				return editContentDashboardItemActionProviderOptional.map(
					contentDashboardItemActionProvider ->
						_toContentDashboardItemAction(
							contentDashboardItemActionProvider,
							httpServletRequest)
				).orElse(
					null
				);
			}
		);
	}

	@Override
	public Locale getDefaultLocale() {
		try {
			return _portal.getSiteDefaultLocale(_fileEntry.getGroupId());
		}
		catch (PortalException portalException) {
			_log.error(portalException);

			return LocaleUtil.getDefault();
		}
	}

	@Override
	public String getDescription(Locale locale) {
		return _getStringValue("description");
	}

	@Override
	public InfoItemReference getInfoItemReference() {
		return new InfoItemReference(
			FileEntry.class.getName(), _fileEntry.getFileEntryId());
	}

	@Override
	public List<ContentDashboardItemVersion>
		getLatestContentDashboardItemVersions(Locale locale) {

		try {
			FileVersion latestFileVersion = _fileEntry.getLatestFileVersion();
			FileVersion latestTrustedFileVersion =
				_fileEntry.getLatestFileVersion(true);

			List<FileVersion> fileVersions = new ArrayList<>();

			fileVersions.add(latestTrustedFileVersion);

			if (!latestFileVersion.equals(latestTrustedFileVersion)) {
				fileVersions.add(latestFileVersion);
			}

			Stream<FileVersion> stream = fileVersions.stream();

			return stream.map(
				fileVersion -> _toVersionOptional(fileVersion, locale)
			).filter(
				Optional::isPresent
			).map(
				Optional::get
			).sorted(
				Comparator.comparing(ContentDashboardItemVersion::getVersion)
			).collect(
				Collectors.toList()
			);
		}
		catch (PortalException portalException) {
			_log.error(portalException);

			return Collections.emptyList();
		}
	}

	@Override
	public Date getModifiedDate() {
		return _fileEntry.getModifiedDate();
	}

	@Override
	public String getScopeName(Locale locale) {
		return Optional.ofNullable(
			_group
		).map(
			group -> {
				try {
					return Optional.ofNullable(
						group.getDescriptiveName(locale)
					).orElseGet(
						() -> group.getName(locale)
					);
				}
				catch (PortalException portalException) {
					_log.error(portalException);

					return group.getName(locale);
				}
			}
		).orElse(
			StringPool.BLANK
		);
	}

	@Override
	public Map<String, Object> getSpecificInformation(Locale locale) {
		return HashMapBuilder.<String, Object>put(
			"extension", _getExtension()
		).put(
			"file-name", _getFileName()
		).put(
			"latest-version-url", _getLatestVersionURL()
		).put(
			"size", _getSize(locale)
		).put(
			"web-dav-url", _getWebDAVURL()
		).build();
	}

	@Override
	public String getTitle(Locale locale) {
		return _fileEntry.getTitle();
	}

	@Override
	public String getTypeLabel(Locale locale) {
		InfoItemClassDetails infoItemClassDetails = new InfoItemClassDetails(
			FileEntry.class.getName());

		return infoItemClassDetails.getLabel(locale);
	}

	@Override
	public long getUserId() {
		return _fileEntry.getUserId();
	}

	@Override
	public String getUserName() {
		return _fileEntry.getUserName();
	}

	@Override
	public String getViewVersionsURL(HttpServletRequest httpServletRequest) {
		return null;
	}

	@Override
	public boolean isViewable(HttpServletRequest httpServletRequest) {
		if (ListUtil.isEmpty(
				_fileEntry.getFileVersions(
					WorkflowConstants.STATUS_APPROVED))) {

			return false;
		}

		Optional<ContentDashboardItemActionProvider>
			contentDashboardItemActionProviderOptional =
				_contentDashboardItemActionProviderTracker.
					getContentDashboardItemActionProviderOptional(
						FileEntry.class.getName(),
						ContentDashboardItemAction.Type.VIEW);

		return contentDashboardItemActionProviderOptional.map(
			contentDashboardItemActionProvider ->
				contentDashboardItemActionProvider.isShow(
					_fileEntry, httpServletRequest)
		).orElse(
			false
		);
	}

	private String _getExtension() {
		return FileUtil.getExtension(_getFileName());
	}

	private String _getFileName() {
		return _getStringValue("fileName");
	}

	private ContentDashboardItemVersion _getLastContentDashboardItemVersion(
		Locale locale) {

		List<ContentDashboardItemVersion> contentDashboardItemVersions =
			getLatestContentDashboardItemVersions(locale);

		return contentDashboardItemVersions.get(
			contentDashboardItemVersions.size() - 1);
	}

	private URL _getLatestVersionURL() {
		return Optional.ofNullable(
			ServiceContextThreadLocal.getServiceContext()
		).map(
			ServiceContext::getLiferayPortletRequest
		).map(
			portletRequest -> {
				List<ContentDashboardItemAction> contentDashboardItemActions =
					getContentDashboardItemActions(
						_portal.getHttpServletRequest(portletRequest),
						ContentDashboardItemAction.Type.PREVIEW);

				if (!contentDashboardItemActions.isEmpty()) {
					ContentDashboardItemAction contentDashboardItemAction =
						contentDashboardItemActions.get(0);

					try {
						return new URL(contentDashboardItemAction.getURL());
					}
					catch (MalformedURLException malformedURLException) {
						_log.error(malformedURLException);

						return null;
					}
				}

				return null;
			}
		).orElse(
			null
		);
	}

	private String _getSize(Locale locale) {
		return LanguageUtil.formatStorageSize(_fileEntry.getSize(), locale);
	}

	private String _getStringValue(String infoFieldName) {
		InfoItemFieldValues infoItemFieldValues =
			_infoItemFieldValuesProvider.getInfoItemFieldValues(_fileEntry);

		return Optional.ofNullable(
			infoItemFieldValues.getInfoFieldValue(infoFieldName)
		).map(
			InfoFieldValue::getValue
		).orElse(
			StringPool.BLANK
		).toString();
	}

	private URL _getWebDAVURL() {
		return Optional.ofNullable(
			ServiceContextThreadLocal.getServiceContext()
		).map(
			ServiceContext::getLiferayPortletRequest
		).map(
			portletRequest -> {
				try {
					return new URL(
						_dlURLHelper.getWebDavURL(
							(ThemeDisplay)portletRequest.getAttribute(
								WebKeys.THEME_DISPLAY),
							_fileEntry.getFolder(), _fileEntry));
				}
				catch (Exception exception) {
					_log.error(exception);

					return null;
				}
			}
		).orElse(
			null
		);
	}

	private ContentDashboardItemAction _toContentDashboardItemAction(
		ContentDashboardItemActionProvider contentDashboardItemActionProvider,
		HttpServletRequest httpServletRequest) {

		try {
			return contentDashboardItemActionProvider.
				getContentDashboardItemAction(_fileEntry, httpServletRequest);
		}
		catch (ContentDashboardItemActionException
					contentDashboardItemActionException) {

			_log.error(contentDashboardItemActionException);

			return null;
		}
	}

	private Optional<ContentDashboardItemVersion> _toVersionOptional(
		FileVersion fileVersion, Locale locale) {

		return Optional.ofNullable(
			fileVersion
		).map(
			curFileVersion -> new ContentDashboardItemVersion(
				curFileVersion.getChangeLog(), curFileVersion.getCreateDate(),
				_language.get(
					locale,
					WorkflowConstants.getStatusLabel(
						curFileVersion.getStatus())),
				WorkflowConstants.getStatusStyle(curFileVersion.getStatus()),
				curFileVersion.getUserName(), curFileVersion.getVersion())
		);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FileEntryContentDashboardItem.class);

	private final List<AssetCategory> _assetCategories;
	private final List<AssetTag> _assetTags;
	private final ContentDashboardItemActionProviderTracker
		_contentDashboardItemActionProviderTracker;
	private final ContentDashboardItemSubtype _contentDashboardItemSubtype;
	private final DLURLHelper _dlURLHelper;
	private final FileEntry _fileEntry;
	private final Group _group;
	private final InfoItemFieldValuesProvider<FileEntry>
		_infoItemFieldValuesProvider;
	private final Language _language;
	private final Portal _portal;

}