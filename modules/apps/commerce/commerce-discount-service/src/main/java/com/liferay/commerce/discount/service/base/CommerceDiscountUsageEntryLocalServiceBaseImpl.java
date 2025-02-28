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

package com.liferay.commerce.discount.service.base;

import com.liferay.commerce.discount.model.CommerceDiscountUsageEntry;
import com.liferay.commerce.discount.service.CommerceDiscountUsageEntryLocalService;
import com.liferay.commerce.discount.service.CommerceDiscountUsageEntryLocalServiceUtil;
import com.liferay.commerce.discount.service.persistence.CommerceDiscountUsageEntryPersistence;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.Serializable;

import java.lang.reflect.Field;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the commerce discount usage entry local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.commerce.discount.service.impl.CommerceDiscountUsageEntryLocalServiceImpl}.
 * </p>
 *
 * @author Marco Leo
 * @see com.liferay.commerce.discount.service.impl.CommerceDiscountUsageEntryLocalServiceImpl
 * @generated
 */
public abstract class CommerceDiscountUsageEntryLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements AopService, CommerceDiscountUsageEntryLocalService,
			   IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>CommerceDiscountUsageEntryLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>CommerceDiscountUsageEntryLocalServiceUtil</code>.
	 */

	/**
	 * Adds the commerce discount usage entry to the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceDiscountUsageEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceDiscountUsageEntry the commerce discount usage entry
	 * @return the commerce discount usage entry that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceDiscountUsageEntry addCommerceDiscountUsageEntry(
		CommerceDiscountUsageEntry commerceDiscountUsageEntry) {

		commerceDiscountUsageEntry.setNew(true);

		return commerceDiscountUsageEntryPersistence.update(
			commerceDiscountUsageEntry);
	}

	/**
	 * Creates a new commerce discount usage entry with the primary key. Does not add the commerce discount usage entry to the database.
	 *
	 * @param commerceDiscountUsageEntryId the primary key for the new commerce discount usage entry
	 * @return the new commerce discount usage entry
	 */
	@Override
	@Transactional(enabled = false)
	public CommerceDiscountUsageEntry createCommerceDiscountUsageEntry(
		long commerceDiscountUsageEntryId) {

		return commerceDiscountUsageEntryPersistence.create(
			commerceDiscountUsageEntryId);
	}

	/**
	 * Deletes the commerce discount usage entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceDiscountUsageEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceDiscountUsageEntryId the primary key of the commerce discount usage entry
	 * @return the commerce discount usage entry that was removed
	 * @throws PortalException if a commerce discount usage entry with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CommerceDiscountUsageEntry deleteCommerceDiscountUsageEntry(
			long commerceDiscountUsageEntryId)
		throws PortalException {

		return commerceDiscountUsageEntryPersistence.remove(
			commerceDiscountUsageEntryId);
	}

	/**
	 * Deletes the commerce discount usage entry from the database. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceDiscountUsageEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceDiscountUsageEntry the commerce discount usage entry
	 * @return the commerce discount usage entry that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public CommerceDiscountUsageEntry deleteCommerceDiscountUsageEntry(
		CommerceDiscountUsageEntry commerceDiscountUsageEntry) {

		return commerceDiscountUsageEntryPersistence.remove(
			commerceDiscountUsageEntry);
	}

	@Override
	public <T> T dslQuery(DSLQuery dslQuery) {
		return commerceDiscountUsageEntryPersistence.dslQuery(dslQuery);
	}

	@Override
	public int dslQueryCount(DSLQuery dslQuery) {
		Long count = dslQuery(dslQuery);

		return count.intValue();
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			CommerceDiscountUsageEntry.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return commerceDiscountUsageEntryPersistence.findWithDynamicQuery(
			dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.discount.model.impl.CommerceDiscountUsageEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return commerceDiscountUsageEntryPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.discount.model.impl.CommerceDiscountUsageEntryModelImpl</code>.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return commerceDiscountUsageEntryPersistence.findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return commerceDiscountUsageEntryPersistence.countWithDynamicQuery(
			dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection) {

		return commerceDiscountUsageEntryPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public CommerceDiscountUsageEntry fetchCommerceDiscountUsageEntry(
		long commerceDiscountUsageEntryId) {

		return commerceDiscountUsageEntryPersistence.fetchByPrimaryKey(
			commerceDiscountUsageEntryId);
	}

	/**
	 * Returns the commerce discount usage entry with the primary key.
	 *
	 * @param commerceDiscountUsageEntryId the primary key of the commerce discount usage entry
	 * @return the commerce discount usage entry
	 * @throws PortalException if a commerce discount usage entry with the primary key could not be found
	 */
	@Override
	public CommerceDiscountUsageEntry getCommerceDiscountUsageEntry(
			long commerceDiscountUsageEntryId)
		throws PortalException {

		return commerceDiscountUsageEntryPersistence.findByPrimaryKey(
			commerceDiscountUsageEntryId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(
			commerceDiscountUsageEntryLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(CommerceDiscountUsageEntry.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"commerceDiscountUsageEntryId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			commerceDiscountUsageEntryLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(
			CommerceDiscountUsageEntry.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"commerceDiscountUsageEntryId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(
			commerceDiscountUsageEntryLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(CommerceDiscountUsageEntry.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"commerceDiscountUsageEntryId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel createPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return commerceDiscountUsageEntryPersistence.create(
			((Long)primaryKeyObj).longValue());
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		if (_log.isWarnEnabled()) {
			_log.warn(
				"Implement CommerceDiscountUsageEntryLocalServiceImpl#deleteCommerceDiscountUsageEntry(CommerceDiscountUsageEntry) to avoid orphaned data");
		}

		return commerceDiscountUsageEntryLocalService.
			deleteCommerceDiscountUsageEntry(
				(CommerceDiscountUsageEntry)persistedModel);
	}

	@Override
	public BasePersistence<CommerceDiscountUsageEntry> getBasePersistence() {
		return commerceDiscountUsageEntryPersistence;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return commerceDiscountUsageEntryPersistence.findByPrimaryKey(
			primaryKeyObj);
	}

	/**
	 * Returns a range of all the commerce discount usage entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>com.liferay.commerce.discount.model.impl.CommerceDiscountUsageEntryModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of commerce discount usage entries
	 * @param end the upper bound of the range of commerce discount usage entries (not inclusive)
	 * @return the range of commerce discount usage entries
	 */
	@Override
	public List<CommerceDiscountUsageEntry> getCommerceDiscountUsageEntries(
		int start, int end) {

		return commerceDiscountUsageEntryPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of commerce discount usage entries.
	 *
	 * @return the number of commerce discount usage entries
	 */
	@Override
	public int getCommerceDiscountUsageEntriesCount() {
		return commerceDiscountUsageEntryPersistence.countAll();
	}

	/**
	 * Updates the commerce discount usage entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * <p>
	 * <strong>Important:</strong> Inspect CommerceDiscountUsageEntryLocalServiceImpl for overloaded versions of the method. If provided, use these entry points to the API, as the implementation logic may require the additional parameters defined there.
	 * </p>
	 *
	 * @param commerceDiscountUsageEntry the commerce discount usage entry
	 * @return the commerce discount usage entry that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public CommerceDiscountUsageEntry updateCommerceDiscountUsageEntry(
		CommerceDiscountUsageEntry commerceDiscountUsageEntry) {

		return commerceDiscountUsageEntryPersistence.update(
			commerceDiscountUsageEntry);
	}

	@Deactivate
	protected void deactivate() {
		_setLocalServiceUtilService(null);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			CommerceDiscountUsageEntryLocalService.class,
			IdentifiableOSGiService.class, PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		commerceDiscountUsageEntryLocalService =
			(CommerceDiscountUsageEntryLocalService)aopProxy;

		_setLocalServiceUtilService(commerceDiscountUsageEntryLocalService);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return CommerceDiscountUsageEntryLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return CommerceDiscountUsageEntry.class;
	}

	protected String getModelClassName() {
		return CommerceDiscountUsageEntry.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource =
				commerceDiscountUsageEntryPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	private void _setLocalServiceUtilService(
		CommerceDiscountUsageEntryLocalService
			commerceDiscountUsageEntryLocalService) {

		try {
			Field field =
				CommerceDiscountUsageEntryLocalServiceUtil.class.
					getDeclaredField("_service");

			field.setAccessible(true);

			field.set(null, commerceDiscountUsageEntryLocalService);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	protected CommerceDiscountUsageEntryLocalService
		commerceDiscountUsageEntryLocalService;

	@Reference
	protected CommerceDiscountUsageEntryPersistence
		commerceDiscountUsageEntryPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	private static final Log _log = LogFactoryUtil.getLog(
		CommerceDiscountUsageEntryLocalServiceBaseImpl.class);

}