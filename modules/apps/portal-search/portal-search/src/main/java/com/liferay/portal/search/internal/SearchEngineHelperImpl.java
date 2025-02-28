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

package com.liferay.portal.search.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.IndexerRegistryUtil;
import com.liferay.portal.kernel.search.SearchEngine;
import com.liferay.portal.kernel.search.SearchEngineHelper;
import com.liferay.portal.kernel.util.ClassUtil;
import com.liferay.portal.search.configuration.SearchEngineHelperConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Michael C. Han
 */
@Component(
	configurationPid = "com.liferay.portal.search.configuration.SearchEngineHelperConfiguration",
	immediate = true, service = SearchEngineHelper.class
)
public class SearchEngineHelperImpl implements SearchEngineHelper {

	@Override
	public Collection<Long> getCompanyIds() {
		return _companyIds.keySet();
	}

	@Override
	public String getDefaultSearchEngineId() {
		if (_defaultSearchEngineId == null) {
			return SYSTEM_ENGINE_ID;
		}

		return _defaultSearchEngineId;
	}

	@Override
	public String[] getEntryClassNames() {
		Set<String> assetEntryClassNames = new HashSet<>();

		for (Indexer<?> indexer : IndexerRegistryUtil.getIndexers()) {
			for (String className : indexer.getSearchClassNames()) {
				if (!_excludedEntryClassNames.contains(className)) {
					assetEntryClassNames.add(className);
				}
			}
		}

		return assetEntryClassNames.toArray(new String[0]);
	}

	@Override
	public Collection<Long> getIndexedCompanyIds() {
		Collection<Long> companyIds = new ArrayList<>();

		for (SearchEngine searchEngine : _searchEngines.values()) {
			companyIds.addAll(searchEngine.getIndexedCompanyIds());
		}

		return companyIds;
	}

	@Override
	public SearchEngine getSearchEngine(String searchEngineId) {
		return _searchEngines.get(searchEngineId);
	}

	@Override
	public String getSearchEngineId(Collection<Document> documents) {
		if (!documents.isEmpty()) {
			Iterator<Document> iterator = documents.iterator();

			Document document = iterator.next();

			return getSearchEngineId(document);
		}

		return getDefaultSearchEngineId();
	}

	@Override
	public String getSearchEngineId(Document document) {
		String entryClassName = document.get("entryClassName");

		Indexer<?> indexer = IndexerRegistryUtil.getIndexer(entryClassName);

		String searchEngineId = indexer.getSearchEngineId();

		if (_log.isDebugEnabled()) {
			_log.debug(
				StringBundler.concat(
					"Search engine ID ", searchEngineId, " is associated with ",
					ClassUtil.getClassName(indexer)));
		}

		return searchEngineId;
	}

	@Override
	public Set<String> getSearchEngineIds() {
		return _searchEngines.keySet();
	}

	@Override
	public Collection<SearchEngine> getSearchEngines() {
		return Collections.unmodifiableCollection(_searchEngines.values());
	}

	@Override
	public SearchEngine getSearchEngineSilent(String searchEngineId) {
		return _searchEngines.get(searchEngineId);
	}

	@Override
	public String getSearchReaderDestinationName(String searchEngineId) {
		return StringBundler.concat(
			DestinationNames.SEARCH_READER, StringPool.SLASH, searchEngineId);
	}

	@Override
	public String getSearchWriterDestinationName(String searchEngineId) {
		return StringBundler.concat(
			DestinationNames.SEARCH_WRITER, StringPool.SLASH, searchEngineId);
	}

	@Override
	public synchronized void initialize(long companyId) {
		if (_companyIds.containsKey(companyId)) {
			return;
		}

		_companyIds.put(companyId, companyId);

		for (SearchEngine searchEngine : _searchEngines.values()) {
			searchEngine.initialize(companyId);
		}
	}

	@Override
	public void removeCompany(long companyId) {
		removeCompany(companyId, false);
	}

	@Override
	public synchronized void removeCompany(long companyId, boolean force) {
		if (!force && !_companyIds.containsKey(companyId)) {
			return;
		}

		for (SearchEngine searchEngine : _searchEngines.values()) {
			searchEngine.removeCompany(companyId);
		}

		_companyIds.remove(companyId);
	}

	@Override
	public SearchEngine removeSearchEngine(String searchEngineId) {
		return _searchEngines.remove(searchEngineId);
	}

	@Override
	public void setDefaultSearchEngineId(String defaultSearchEngineId) {
		_defaultSearchEngineId = defaultSearchEngineId;
	}

	@Override
	public void setSearchEngine(
		String searchEngineId, SearchEngine searchEngine) {

		_searchEngines.put(searchEngineId, searchEngine);

		for (Long companyId : _companyIds.keySet()) {
			searchEngine.initialize(companyId);
		}
	}

	@Activate
	@Modified
	protected synchronized void activate(
		BundleContext bundleContext, Map<String, Object> properties) {

		SearchEngineHelperConfiguration searchEngineHelperConfiguration =
			ConfigurableUtil.createConfigurable(
				SearchEngineHelperConfiguration.class, properties);

		_excludedEntryClassNames.clear();

		Collections.addAll(
			_excludedEntryClassNames,
			searchEngineHelperConfiguration.excludedEntryClassNames());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SearchEngineHelperImpl.class);

	private final Map<Long, Long> _companyIds = new ConcurrentHashMap<>();
	private String _defaultSearchEngineId;
	private final Set<String> _excludedEntryClassNames = new HashSet<>();
	private final Map<String, SearchEngine> _searchEngines =
		new ConcurrentHashMap<>();

}