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

package com.liferay.portal.workflow.kaleo.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.change.tracking.CTColumnResolutionType;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.change.tracking.helper.CTPersistenceHelper;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.workflow.kaleo.exception.NoSuchTimerInstanceTokenException;
import com.liferay.portal.workflow.kaleo.model.KaleoTimerInstanceToken;
import com.liferay.portal.workflow.kaleo.model.KaleoTimerInstanceTokenTable;
import com.liferay.portal.workflow.kaleo.model.impl.KaleoTimerInstanceTokenImpl;
import com.liferay.portal.workflow.kaleo.model.impl.KaleoTimerInstanceTokenModelImpl;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTimerInstanceTokenPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoTimerInstanceTokenUtil;
import com.liferay.portal.workflow.kaleo.service.persistence.impl.constants.KaleoPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the kaleo timer instance token service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(
	service = {KaleoTimerInstanceTokenPersistence.class, BasePersistence.class}
)
public class KaleoTimerInstanceTokenPersistenceImpl
	extends BasePersistenceImpl<KaleoTimerInstanceToken>
	implements KaleoTimerInstanceTokenPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>KaleoTimerInstanceTokenUtil</code> to access the kaleo timer instance token persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		KaleoTimerInstanceTokenImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByKaleoInstanceId;
	private FinderPath _finderPathWithoutPaginationFindByKaleoInstanceId;
	private FinderPath _finderPathCountByKaleoInstanceId;

	/**
	 * Returns all the kaleo timer instance tokens where kaleoInstanceId = &#63;.
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @return the matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKaleoInstanceId(
		long kaleoInstanceId) {

		return findByKaleoInstanceId(
			kaleoInstanceId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the kaleo timer instance tokens where kaleoInstanceId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @return the range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKaleoInstanceId(
		long kaleoInstanceId, int start, int end) {

		return findByKaleoInstanceId(kaleoInstanceId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens where kaleoInstanceId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKaleoInstanceId(
		long kaleoInstanceId, int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		return findByKaleoInstanceId(
			kaleoInstanceId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens where kaleoInstanceId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKaleoInstanceId(
		long kaleoInstanceId, int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoTimerInstanceToken.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindByKaleoInstanceId;
				finderArgs = new Object[] {kaleoInstanceId};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByKaleoInstanceId;
			finderArgs = new Object[] {
				kaleoInstanceId, start, end, orderByComparator
			};
		}

		List<KaleoTimerInstanceToken> list = null;

		if (useFinderCache && productionMode) {
			list = (List<KaleoTimerInstanceToken>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (KaleoTimerInstanceToken kaleoTimerInstanceToken : list) {
					if (kaleoInstanceId !=
							kaleoTimerInstanceToken.getKaleoInstanceId()) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KALEOINSTANCEID_KALEOINSTANCEID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(KaleoTimerInstanceTokenModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceId);

				list = (List<KaleoTimerInstanceToken>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first kaleo timer instance token in the ordered set where kaleoInstanceId = &#63;.
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByKaleoInstanceId_First(
			long kaleoInstanceId,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken =
			fetchByKaleoInstanceId_First(kaleoInstanceId, orderByComparator);

		if (kaleoTimerInstanceToken != null) {
			return kaleoTimerInstanceToken;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("kaleoInstanceId=");
		sb.append(kaleoInstanceId);

		sb.append("}");

		throw new NoSuchTimerInstanceTokenException(sb.toString());
	}

	/**
	 * Returns the first kaleo timer instance token in the ordered set where kaleoInstanceId = &#63;.
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKaleoInstanceId_First(
		long kaleoInstanceId,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		List<KaleoTimerInstanceToken> list = findByKaleoInstanceId(
			kaleoInstanceId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last kaleo timer instance token in the ordered set where kaleoInstanceId = &#63;.
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByKaleoInstanceId_Last(
			long kaleoInstanceId,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken =
			fetchByKaleoInstanceId_Last(kaleoInstanceId, orderByComparator);

		if (kaleoTimerInstanceToken != null) {
			return kaleoTimerInstanceToken;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("kaleoInstanceId=");
		sb.append(kaleoInstanceId);

		sb.append("}");

		throw new NoSuchTimerInstanceTokenException(sb.toString());
	}

	/**
	 * Returns the last kaleo timer instance token in the ordered set where kaleoInstanceId = &#63;.
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKaleoInstanceId_Last(
		long kaleoInstanceId,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		int count = countByKaleoInstanceId(kaleoInstanceId);

		if (count == 0) {
			return null;
		}

		List<KaleoTimerInstanceToken> list = findByKaleoInstanceId(
			kaleoInstanceId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the kaleo timer instance tokens before and after the current kaleo timer instance token in the ordered set where kaleoInstanceId = &#63;.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key of the current kaleo timer instance token
	 * @param kaleoInstanceId the kaleo instance ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken[] findByKaleoInstanceId_PrevAndNext(
			long kaleoTimerInstanceTokenId, long kaleoInstanceId,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = findByPrimaryKey(
			kaleoTimerInstanceTokenId);

		Session session = null;

		try {
			session = openSession();

			KaleoTimerInstanceToken[] array =
				new KaleoTimerInstanceTokenImpl[3];

			array[0] = getByKaleoInstanceId_PrevAndNext(
				session, kaleoTimerInstanceToken, kaleoInstanceId,
				orderByComparator, true);

			array[1] = kaleoTimerInstanceToken;

			array[2] = getByKaleoInstanceId_PrevAndNext(
				session, kaleoTimerInstanceToken, kaleoInstanceId,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected KaleoTimerInstanceToken getByKaleoInstanceId_PrevAndNext(
		Session session, KaleoTimerInstanceToken kaleoTimerInstanceToken,
		long kaleoInstanceId,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE);

		sb.append(_FINDER_COLUMN_KALEOINSTANCEID_KALEOINSTANCEID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(KaleoTimerInstanceTokenModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(kaleoInstanceId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						kaleoTimerInstanceToken)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<KaleoTimerInstanceToken> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the kaleo timer instance tokens where kaleoInstanceId = &#63; from the database.
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 */
	@Override
	public void removeByKaleoInstanceId(long kaleoInstanceId) {
		for (KaleoTimerInstanceToken kaleoTimerInstanceToken :
				findByKaleoInstanceId(
					kaleoInstanceId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
					null)) {

			remove(kaleoTimerInstanceToken);
		}
	}

	/**
	 * Returns the number of kaleo timer instance tokens where kaleoInstanceId = &#63;.
	 *
	 * @param kaleoInstanceId the kaleo instance ID
	 * @return the number of matching kaleo timer instance tokens
	 */
	@Override
	public int countByKaleoInstanceId(long kaleoInstanceId) {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoTimerInstanceToken.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByKaleoInstanceId;

			finderArgs = new Object[] {kaleoInstanceId};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KALEOINSTANCEID_KALEOINSTANCEID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceId);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_KALEOINSTANCEID_KALEOINSTANCEID_2 =
			"kaleoTimerInstanceToken.kaleoInstanceId = ?";

	private FinderPath _finderPathFetchByKITI_KTI;
	private FinderPath _finderPathCountByKITI_KTI;

	/**
	 * Returns the kaleo timer instance token where kaleoInstanceTokenId = &#63; and kaleoTimerId = &#63; or throws a <code>NoSuchTimerInstanceTokenException</code> if it could not be found.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param kaleoTimerId the kaleo timer ID
	 * @return the matching kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByKITI_KTI(
			long kaleoInstanceTokenId, long kaleoTimerId)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = fetchByKITI_KTI(
			kaleoInstanceTokenId, kaleoTimerId);

		if (kaleoTimerInstanceToken == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("kaleoInstanceTokenId=");
			sb.append(kaleoInstanceTokenId);

			sb.append(", kaleoTimerId=");
			sb.append(kaleoTimerId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchTimerInstanceTokenException(sb.toString());
		}

		return kaleoTimerInstanceToken;
	}

	/**
	 * Returns the kaleo timer instance token where kaleoInstanceTokenId = &#63; and kaleoTimerId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param kaleoTimerId the kaleo timer ID
	 * @return the matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKITI_KTI(
		long kaleoInstanceTokenId, long kaleoTimerId) {

		return fetchByKITI_KTI(kaleoInstanceTokenId, kaleoTimerId, true);
	}

	/**
	 * Returns the kaleo timer instance token where kaleoInstanceTokenId = &#63; and kaleoTimerId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param kaleoTimerId the kaleo timer ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKITI_KTI(
		long kaleoInstanceTokenId, long kaleoTimerId, boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoTimerInstanceToken.class);

		Object[] finderArgs = null;

		if (useFinderCache && productionMode) {
			finderArgs = new Object[] {kaleoInstanceTokenId, kaleoTimerId};
		}

		Object result = null;

		if (useFinderCache && productionMode) {
			result = finderCache.getResult(
				_finderPathFetchByKITI_KTI, finderArgs);
		}

		if (result instanceof KaleoTimerInstanceToken) {
			KaleoTimerInstanceToken kaleoTimerInstanceToken =
				(KaleoTimerInstanceToken)result;

			if ((kaleoInstanceTokenId !=
					kaleoTimerInstanceToken.getKaleoInstanceTokenId()) ||
				(kaleoTimerId != kaleoTimerInstanceToken.getKaleoTimerId())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KITI_KTI_KALEOINSTANCETOKENID_2);

			sb.append(_FINDER_COLUMN_KITI_KTI_KALEOTIMERID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceTokenId);

				queryPos.add(kaleoTimerId);

				List<KaleoTimerInstanceToken> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache && productionMode) {
						finderCache.putResult(
							_finderPathFetchByKITI_KTI, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!productionMode || !useFinderCache) {
								finderArgs = new Object[] {
									kaleoInstanceTokenId, kaleoTimerId
								};
							}

							_log.warn(
								"KaleoTimerInstanceTokenPersistenceImpl.fetchByKITI_KTI(long, long, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					KaleoTimerInstanceToken kaleoTimerInstanceToken = list.get(
						0);

					result = kaleoTimerInstanceToken;

					cacheResult(kaleoTimerInstanceToken);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (KaleoTimerInstanceToken)result;
		}
	}

	/**
	 * Removes the kaleo timer instance token where kaleoInstanceTokenId = &#63; and kaleoTimerId = &#63; from the database.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param kaleoTimerId the kaleo timer ID
	 * @return the kaleo timer instance token that was removed
	 */
	@Override
	public KaleoTimerInstanceToken removeByKITI_KTI(
			long kaleoInstanceTokenId, long kaleoTimerId)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = findByKITI_KTI(
			kaleoInstanceTokenId, kaleoTimerId);

		return remove(kaleoTimerInstanceToken);
	}

	/**
	 * Returns the number of kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and kaleoTimerId = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param kaleoTimerId the kaleo timer ID
	 * @return the number of matching kaleo timer instance tokens
	 */
	@Override
	public int countByKITI_KTI(long kaleoInstanceTokenId, long kaleoTimerId) {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoTimerInstanceToken.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByKITI_KTI;

			finderArgs = new Object[] {kaleoInstanceTokenId, kaleoTimerId};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KITI_KTI_KALEOINSTANCETOKENID_2);

			sb.append(_FINDER_COLUMN_KITI_KTI_KALEOTIMERID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceTokenId);

				queryPos.add(kaleoTimerId);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_KITI_KTI_KALEOINSTANCETOKENID_2 =
		"kaleoTimerInstanceToken.kaleoInstanceTokenId = ? AND ";

	private static final String _FINDER_COLUMN_KITI_KTI_KALEOTIMERID_2 =
		"kaleoTimerInstanceToken.kaleoTimerId = ?";

	private FinderPath _finderPathWithPaginationFindByKITI_C;
	private FinderPath _finderPathWithoutPaginationFindByKITI_C;
	private FinderPath _finderPathCountByKITI_C;

	/**
	 * Returns all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @return the matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_C(
		long kaleoInstanceTokenId, boolean completed) {

		return findByKITI_C(
			kaleoInstanceTokenId, completed, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @return the range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_C(
		long kaleoInstanceTokenId, boolean completed, int start, int end) {

		return findByKITI_C(kaleoInstanceTokenId, completed, start, end, null);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_C(
		long kaleoInstanceTokenId, boolean completed, int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		return findByKITI_C(
			kaleoInstanceTokenId, completed, start, end, orderByComparator,
			true);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_C(
		long kaleoInstanceTokenId, boolean completed, int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoTimerInstanceToken.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindByKITI_C;
				finderArgs = new Object[] {kaleoInstanceTokenId, completed};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByKITI_C;
			finderArgs = new Object[] {
				kaleoInstanceTokenId, completed, start, end, orderByComparator
			};
		}

		List<KaleoTimerInstanceToken> list = null;

		if (useFinderCache && productionMode) {
			list = (List<KaleoTimerInstanceToken>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (KaleoTimerInstanceToken kaleoTimerInstanceToken : list) {
					if ((kaleoInstanceTokenId !=
							kaleoTimerInstanceToken.
								getKaleoInstanceTokenId()) ||
						(completed != kaleoTimerInstanceToken.isCompleted())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					4 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(4);
			}

			sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KITI_C_KALEOINSTANCETOKENID_2);

			sb.append(_FINDER_COLUMN_KITI_C_COMPLETED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(KaleoTimerInstanceTokenModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceTokenId);

				queryPos.add(completed);

				list = (List<KaleoTimerInstanceToken>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByKITI_C_First(
			long kaleoInstanceTokenId, boolean completed,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = fetchByKITI_C_First(
			kaleoInstanceTokenId, completed, orderByComparator);

		if (kaleoTimerInstanceToken != null) {
			return kaleoTimerInstanceToken;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("kaleoInstanceTokenId=");
		sb.append(kaleoInstanceTokenId);

		sb.append(", completed=");
		sb.append(completed);

		sb.append("}");

		throw new NoSuchTimerInstanceTokenException(sb.toString());
	}

	/**
	 * Returns the first kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKITI_C_First(
		long kaleoInstanceTokenId, boolean completed,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		List<KaleoTimerInstanceToken> list = findByKITI_C(
			kaleoInstanceTokenId, completed, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByKITI_C_Last(
			long kaleoInstanceTokenId, boolean completed,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = fetchByKITI_C_Last(
			kaleoInstanceTokenId, completed, orderByComparator);

		if (kaleoTimerInstanceToken != null) {
			return kaleoTimerInstanceToken;
		}

		StringBundler sb = new StringBundler(6);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("kaleoInstanceTokenId=");
		sb.append(kaleoInstanceTokenId);

		sb.append(", completed=");
		sb.append(completed);

		sb.append("}");

		throw new NoSuchTimerInstanceTokenException(sb.toString());
	}

	/**
	 * Returns the last kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKITI_C_Last(
		long kaleoInstanceTokenId, boolean completed,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		int count = countByKITI_C(kaleoInstanceTokenId, completed);

		if (count == 0) {
			return null;
		}

		List<KaleoTimerInstanceToken> list = findByKITI_C(
			kaleoInstanceTokenId, completed, count - 1, count,
			orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the kaleo timer instance tokens before and after the current kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key of the current kaleo timer instance token
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken[] findByKITI_C_PrevAndNext(
			long kaleoTimerInstanceTokenId, long kaleoInstanceTokenId,
			boolean completed,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = findByPrimaryKey(
			kaleoTimerInstanceTokenId);

		Session session = null;

		try {
			session = openSession();

			KaleoTimerInstanceToken[] array =
				new KaleoTimerInstanceTokenImpl[3];

			array[0] = getByKITI_C_PrevAndNext(
				session, kaleoTimerInstanceToken, kaleoInstanceTokenId,
				completed, orderByComparator, true);

			array[1] = kaleoTimerInstanceToken;

			array[2] = getByKITI_C_PrevAndNext(
				session, kaleoTimerInstanceToken, kaleoInstanceTokenId,
				completed, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected KaleoTimerInstanceToken getByKITI_C_PrevAndNext(
		Session session, KaleoTimerInstanceToken kaleoTimerInstanceToken,
		long kaleoInstanceTokenId, boolean completed,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				5 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(4);
		}

		sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE);

		sb.append(_FINDER_COLUMN_KITI_C_KALEOINSTANCETOKENID_2);

		sb.append(_FINDER_COLUMN_KITI_C_COMPLETED_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(KaleoTimerInstanceTokenModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(kaleoInstanceTokenId);

		queryPos.add(completed);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						kaleoTimerInstanceToken)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<KaleoTimerInstanceToken> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and completed = &#63; from the database.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 */
	@Override
	public void removeByKITI_C(long kaleoInstanceTokenId, boolean completed) {
		for (KaleoTimerInstanceToken kaleoTimerInstanceToken :
				findByKITI_C(
					kaleoInstanceTokenId, completed, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(kaleoTimerInstanceToken);
		}
	}

	/**
	 * Returns the number of kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param completed the completed
	 * @return the number of matching kaleo timer instance tokens
	 */
	@Override
	public int countByKITI_C(long kaleoInstanceTokenId, boolean completed) {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoTimerInstanceToken.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByKITI_C;

			finderArgs = new Object[] {kaleoInstanceTokenId, completed};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_COUNT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KITI_C_KALEOINSTANCETOKENID_2);

			sb.append(_FINDER_COLUMN_KITI_C_COMPLETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceTokenId);

				queryPos.add(completed);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_KITI_C_KALEOINSTANCETOKENID_2 =
		"kaleoTimerInstanceToken.kaleoInstanceTokenId = ? AND ";

	private static final String _FINDER_COLUMN_KITI_C_COMPLETED_2 =
		"kaleoTimerInstanceToken.completed = ?";

	private FinderPath _finderPathWithPaginationFindByKITI_B_C;
	private FinderPath _finderPathWithoutPaginationFindByKITI_B_C;
	private FinderPath _finderPathCountByKITI_B_C;

	/**
	 * Returns all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @return the matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_B_C(
		long kaleoInstanceTokenId, boolean blocking, boolean completed) {

		return findByKITI_B_C(
			kaleoInstanceTokenId, blocking, completed, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @return the range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_B_C(
		long kaleoInstanceTokenId, boolean blocking, boolean completed,
		int start, int end) {

		return findByKITI_B_C(
			kaleoInstanceTokenId, blocking, completed, start, end, null);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_B_C(
		long kaleoInstanceTokenId, boolean blocking, boolean completed,
		int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		return findByKITI_B_C(
			kaleoInstanceTokenId, blocking, completed, start, end,
			orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findByKITI_B_C(
		long kaleoInstanceTokenId, boolean blocking, boolean completed,
		int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoTimerInstanceToken.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindByKITI_B_C;
				finderArgs = new Object[] {
					kaleoInstanceTokenId, blocking, completed
				};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByKITI_B_C;
			finderArgs = new Object[] {
				kaleoInstanceTokenId, blocking, completed, start, end,
				orderByComparator
			};
		}

		List<KaleoTimerInstanceToken> list = null;

		if (useFinderCache && productionMode) {
			list = (List<KaleoTimerInstanceToken>)finderCache.getResult(
				finderPath, finderArgs);

			if ((list != null) && !list.isEmpty()) {
				for (KaleoTimerInstanceToken kaleoTimerInstanceToken : list) {
					if ((kaleoInstanceTokenId !=
							kaleoTimerInstanceToken.
								getKaleoInstanceTokenId()) ||
						(blocking != kaleoTimerInstanceToken.isBlocking()) ||
						(completed != kaleoTimerInstanceToken.isCompleted())) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					5 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(5);
			}

			sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KITI_B_C_KALEOINSTANCETOKENID_2);

			sb.append(_FINDER_COLUMN_KITI_B_C_BLOCKING_2);

			sb.append(_FINDER_COLUMN_KITI_B_C_COMPLETED_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(KaleoTimerInstanceTokenModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceTokenId);

				queryPos.add(blocking);

				queryPos.add(completed);

				list = (List<KaleoTimerInstanceToken>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByKITI_B_C_First(
			long kaleoInstanceTokenId, boolean blocking, boolean completed,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = fetchByKITI_B_C_First(
			kaleoInstanceTokenId, blocking, completed, orderByComparator);

		if (kaleoTimerInstanceToken != null) {
			return kaleoTimerInstanceToken;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("kaleoInstanceTokenId=");
		sb.append(kaleoInstanceTokenId);

		sb.append(", blocking=");
		sb.append(blocking);

		sb.append(", completed=");
		sb.append(completed);

		sb.append("}");

		throw new NoSuchTimerInstanceTokenException(sb.toString());
	}

	/**
	 * Returns the first kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKITI_B_C_First(
		long kaleoInstanceTokenId, boolean blocking, boolean completed,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		List<KaleoTimerInstanceToken> list = findByKITI_B_C(
			kaleoInstanceTokenId, blocking, completed, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByKITI_B_C_Last(
			long kaleoInstanceTokenId, boolean blocking, boolean completed,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = fetchByKITI_B_C_Last(
			kaleoInstanceTokenId, blocking, completed, orderByComparator);

		if (kaleoTimerInstanceToken != null) {
			return kaleoTimerInstanceToken;
		}

		StringBundler sb = new StringBundler(8);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("kaleoInstanceTokenId=");
		sb.append(kaleoInstanceTokenId);

		sb.append(", blocking=");
		sb.append(blocking);

		sb.append(", completed=");
		sb.append(completed);

		sb.append("}");

		throw new NoSuchTimerInstanceTokenException(sb.toString());
	}

	/**
	 * Returns the last kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo timer instance token, or <code>null</code> if a matching kaleo timer instance token could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByKITI_B_C_Last(
		long kaleoInstanceTokenId, boolean blocking, boolean completed,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		int count = countByKITI_B_C(kaleoInstanceTokenId, blocking, completed);

		if (count == 0) {
			return null;
		}

		List<KaleoTimerInstanceToken> list = findByKITI_B_C(
			kaleoInstanceTokenId, blocking, completed, count - 1, count,
			orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the kaleo timer instance tokens before and after the current kaleo timer instance token in the ordered set where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key of the current kaleo timer instance token
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken[] findByKITI_B_C_PrevAndNext(
			long kaleoTimerInstanceTokenId, long kaleoInstanceTokenId,
			boolean blocking, boolean completed,
			OrderByComparator<KaleoTimerInstanceToken> orderByComparator)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = findByPrimaryKey(
			kaleoTimerInstanceTokenId);

		Session session = null;

		try {
			session = openSession();

			KaleoTimerInstanceToken[] array =
				new KaleoTimerInstanceTokenImpl[3];

			array[0] = getByKITI_B_C_PrevAndNext(
				session, kaleoTimerInstanceToken, kaleoInstanceTokenId,
				blocking, completed, orderByComparator, true);

			array[1] = kaleoTimerInstanceToken;

			array[2] = getByKITI_B_C_PrevAndNext(
				session, kaleoTimerInstanceToken, kaleoInstanceTokenId,
				blocking, completed, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected KaleoTimerInstanceToken getByKITI_B_C_PrevAndNext(
		Session session, KaleoTimerInstanceToken kaleoTimerInstanceToken,
		long kaleoInstanceTokenId, boolean blocking, boolean completed,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator,
		boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				6 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(5);
		}

		sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE);

		sb.append(_FINDER_COLUMN_KITI_B_C_KALEOINSTANCETOKENID_2);

		sb.append(_FINDER_COLUMN_KITI_B_C_BLOCKING_2);

		sb.append(_FINDER_COLUMN_KITI_B_C_COMPLETED_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(KaleoTimerInstanceTokenModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(kaleoInstanceTokenId);

		queryPos.add(blocking);

		queryPos.add(completed);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						kaleoTimerInstanceToken)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<KaleoTimerInstanceToken> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63; from the database.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 */
	@Override
	public void removeByKITI_B_C(
		long kaleoInstanceTokenId, boolean blocking, boolean completed) {

		for (KaleoTimerInstanceToken kaleoTimerInstanceToken :
				findByKITI_B_C(
					kaleoInstanceTokenId, blocking, completed,
					QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(kaleoTimerInstanceToken);
		}
	}

	/**
	 * Returns the number of kaleo timer instance tokens where kaleoInstanceTokenId = &#63; and blocking = &#63; and completed = &#63;.
	 *
	 * @param kaleoInstanceTokenId the kaleo instance token ID
	 * @param blocking the blocking
	 * @param completed the completed
	 * @return the number of matching kaleo timer instance tokens
	 */
	@Override
	public int countByKITI_B_C(
		long kaleoInstanceTokenId, boolean blocking, boolean completed) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoTimerInstanceToken.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByKITI_B_C;

			finderArgs = new Object[] {
				kaleoInstanceTokenId, blocking, completed
			};

			count = (Long)finderCache.getResult(finderPath, finderArgs);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_COUNT_KALEOTIMERINSTANCETOKEN_WHERE);

			sb.append(_FINDER_COLUMN_KITI_B_C_KALEOINSTANCETOKENID_2);

			sb.append(_FINDER_COLUMN_KITI_B_C_BLOCKING_2);

			sb.append(_FINDER_COLUMN_KITI_B_C_COMPLETED_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoInstanceTokenId);

				queryPos.add(blocking);

				queryPos.add(completed);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_KITI_B_C_KALEOINSTANCETOKENID_2 =
		"kaleoTimerInstanceToken.kaleoInstanceTokenId = ? AND ";

	private static final String _FINDER_COLUMN_KITI_B_C_BLOCKING_2 =
		"kaleoTimerInstanceToken.blocking = ? AND ";

	private static final String _FINDER_COLUMN_KITI_B_C_COMPLETED_2 =
		"kaleoTimerInstanceToken.completed = ?";

	public KaleoTimerInstanceTokenPersistenceImpl() {
		setModelClass(KaleoTimerInstanceToken.class);

		setModelImplClass(KaleoTimerInstanceTokenImpl.class);
		setModelPKClass(long.class);

		setTable(KaleoTimerInstanceTokenTable.INSTANCE);
	}

	/**
	 * Caches the kaleo timer instance token in the entity cache if it is enabled.
	 *
	 * @param kaleoTimerInstanceToken the kaleo timer instance token
	 */
	@Override
	public void cacheResult(KaleoTimerInstanceToken kaleoTimerInstanceToken) {
		if (kaleoTimerInstanceToken.getCtCollectionId() != 0) {
			return;
		}

		entityCache.putResult(
			KaleoTimerInstanceTokenImpl.class,
			kaleoTimerInstanceToken.getPrimaryKey(), kaleoTimerInstanceToken);

		finderCache.putResult(
			_finderPathFetchByKITI_KTI,
			new Object[] {
				kaleoTimerInstanceToken.getKaleoInstanceTokenId(),
				kaleoTimerInstanceToken.getKaleoTimerId()
			},
			kaleoTimerInstanceToken);
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the kaleo timer instance tokens in the entity cache if it is enabled.
	 *
	 * @param kaleoTimerInstanceTokens the kaleo timer instance tokens
	 */
	@Override
	public void cacheResult(
		List<KaleoTimerInstanceToken> kaleoTimerInstanceTokens) {

		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (kaleoTimerInstanceTokens.size() >
				 _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (KaleoTimerInstanceToken kaleoTimerInstanceToken :
				kaleoTimerInstanceTokens) {

			if (kaleoTimerInstanceToken.getCtCollectionId() != 0) {
				continue;
			}

			if (entityCache.getResult(
					KaleoTimerInstanceTokenImpl.class,
					kaleoTimerInstanceToken.getPrimaryKey()) == null) {

				cacheResult(kaleoTimerInstanceToken);
			}
		}
	}

	/**
	 * Clears the cache for all kaleo timer instance tokens.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(KaleoTimerInstanceTokenImpl.class);

		finderCache.clearCache(KaleoTimerInstanceTokenImpl.class);
	}

	/**
	 * Clears the cache for the kaleo timer instance token.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(KaleoTimerInstanceToken kaleoTimerInstanceToken) {
		entityCache.removeResult(
			KaleoTimerInstanceTokenImpl.class, kaleoTimerInstanceToken);
	}

	@Override
	public void clearCache(
		List<KaleoTimerInstanceToken> kaleoTimerInstanceTokens) {

		for (KaleoTimerInstanceToken kaleoTimerInstanceToken :
				kaleoTimerInstanceTokens) {

			entityCache.removeResult(
				KaleoTimerInstanceTokenImpl.class, kaleoTimerInstanceToken);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(KaleoTimerInstanceTokenImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(
				KaleoTimerInstanceTokenImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		KaleoTimerInstanceTokenModelImpl kaleoTimerInstanceTokenModelImpl) {

		Object[] args = new Object[] {
			kaleoTimerInstanceTokenModelImpl.getKaleoInstanceTokenId(),
			kaleoTimerInstanceTokenModelImpl.getKaleoTimerId()
		};

		finderCache.putResult(
			_finderPathCountByKITI_KTI, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByKITI_KTI, args, kaleoTimerInstanceTokenModelImpl);
	}

	/**
	 * Creates a new kaleo timer instance token with the primary key. Does not add the kaleo timer instance token to the database.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key for the new kaleo timer instance token
	 * @return the new kaleo timer instance token
	 */
	@Override
	public KaleoTimerInstanceToken create(long kaleoTimerInstanceTokenId) {
		KaleoTimerInstanceToken kaleoTimerInstanceToken =
			new KaleoTimerInstanceTokenImpl();

		kaleoTimerInstanceToken.setNew(true);
		kaleoTimerInstanceToken.setPrimaryKey(kaleoTimerInstanceTokenId);

		kaleoTimerInstanceToken.setCompanyId(CompanyThreadLocal.getCompanyId());

		return kaleoTimerInstanceToken;
	}

	/**
	 * Removes the kaleo timer instance token with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key of the kaleo timer instance token
	 * @return the kaleo timer instance token that was removed
	 * @throws NoSuchTimerInstanceTokenException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken remove(long kaleoTimerInstanceTokenId)
		throws NoSuchTimerInstanceTokenException {

		return remove((Serializable)kaleoTimerInstanceTokenId);
	}

	/**
	 * Removes the kaleo timer instance token with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the kaleo timer instance token
	 * @return the kaleo timer instance token that was removed
	 * @throws NoSuchTimerInstanceTokenException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken remove(Serializable primaryKey)
		throws NoSuchTimerInstanceTokenException {

		Session session = null;

		try {
			session = openSession();

			KaleoTimerInstanceToken kaleoTimerInstanceToken =
				(KaleoTimerInstanceToken)session.get(
					KaleoTimerInstanceTokenImpl.class, primaryKey);

			if (kaleoTimerInstanceToken == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchTimerInstanceTokenException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(kaleoTimerInstanceToken);
		}
		catch (NoSuchTimerInstanceTokenException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected KaleoTimerInstanceToken removeImpl(
		KaleoTimerInstanceToken kaleoTimerInstanceToken) {

		Session session = null;

		try {
			session = openSession();

			if (!session.contains(kaleoTimerInstanceToken)) {
				kaleoTimerInstanceToken = (KaleoTimerInstanceToken)session.get(
					KaleoTimerInstanceTokenImpl.class,
					kaleoTimerInstanceToken.getPrimaryKeyObj());
			}

			if ((kaleoTimerInstanceToken != null) &&
				ctPersistenceHelper.isRemove(kaleoTimerInstanceToken)) {

				session.delete(kaleoTimerInstanceToken);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (kaleoTimerInstanceToken != null) {
			clearCache(kaleoTimerInstanceToken);
		}

		return kaleoTimerInstanceToken;
	}

	@Override
	public KaleoTimerInstanceToken updateImpl(
		KaleoTimerInstanceToken kaleoTimerInstanceToken) {

		boolean isNew = kaleoTimerInstanceToken.isNew();

		if (!(kaleoTimerInstanceToken instanceof
				KaleoTimerInstanceTokenModelImpl)) {

			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(kaleoTimerInstanceToken.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					kaleoTimerInstanceToken);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in kaleoTimerInstanceToken proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom KaleoTimerInstanceToken implementation " +
					kaleoTimerInstanceToken.getClass());
		}

		KaleoTimerInstanceTokenModelImpl kaleoTimerInstanceTokenModelImpl =
			(KaleoTimerInstanceTokenModelImpl)kaleoTimerInstanceToken;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (kaleoTimerInstanceToken.getCreateDate() == null)) {
			if (serviceContext == null) {
				kaleoTimerInstanceToken.setCreateDate(date);
			}
			else {
				kaleoTimerInstanceToken.setCreateDate(
					serviceContext.getCreateDate(date));
			}
		}

		if (!kaleoTimerInstanceTokenModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				kaleoTimerInstanceToken.setModifiedDate(date);
			}
			else {
				kaleoTimerInstanceToken.setModifiedDate(
					serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (ctPersistenceHelper.isInsert(kaleoTimerInstanceToken)) {
				if (!isNew) {
					session.evict(
						KaleoTimerInstanceTokenImpl.class,
						kaleoTimerInstanceToken.getPrimaryKeyObj());
				}

				session.save(kaleoTimerInstanceToken);
			}
			else {
				kaleoTimerInstanceToken =
					(KaleoTimerInstanceToken)session.merge(
						kaleoTimerInstanceToken);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (kaleoTimerInstanceToken.getCtCollectionId() != 0) {
			if (isNew) {
				kaleoTimerInstanceToken.setNew(false);
			}

			kaleoTimerInstanceToken.resetOriginalValues();

			return kaleoTimerInstanceToken;
		}

		entityCache.putResult(
			KaleoTimerInstanceTokenImpl.class, kaleoTimerInstanceTokenModelImpl,
			false, true);

		cacheUniqueFindersCache(kaleoTimerInstanceTokenModelImpl);

		if (isNew) {
			kaleoTimerInstanceToken.setNew(false);
		}

		kaleoTimerInstanceToken.resetOriginalValues();

		return kaleoTimerInstanceToken;
	}

	/**
	 * Returns the kaleo timer instance token with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the kaleo timer instance token
	 * @return the kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByPrimaryKey(Serializable primaryKey)
		throws NoSuchTimerInstanceTokenException {

		KaleoTimerInstanceToken kaleoTimerInstanceToken = fetchByPrimaryKey(
			primaryKey);

		if (kaleoTimerInstanceToken == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchTimerInstanceTokenException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return kaleoTimerInstanceToken;
	}

	/**
	 * Returns the kaleo timer instance token with the primary key or throws a <code>NoSuchTimerInstanceTokenException</code> if it could not be found.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key of the kaleo timer instance token
	 * @return the kaleo timer instance token
	 * @throws NoSuchTimerInstanceTokenException if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken findByPrimaryKey(
			long kaleoTimerInstanceTokenId)
		throws NoSuchTimerInstanceTokenException {

		return findByPrimaryKey((Serializable)kaleoTimerInstanceTokenId);
	}

	/**
	 * Returns the kaleo timer instance token with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the kaleo timer instance token
	 * @return the kaleo timer instance token, or <code>null</code> if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByPrimaryKey(Serializable primaryKey) {
		if (ctPersistenceHelper.isProductionMode(
				KaleoTimerInstanceToken.class, primaryKey)) {

			return super.fetchByPrimaryKey(primaryKey);
		}

		KaleoTimerInstanceToken kaleoTimerInstanceToken = null;

		Session session = null;

		try {
			session = openSession();

			kaleoTimerInstanceToken = (KaleoTimerInstanceToken)session.get(
				KaleoTimerInstanceTokenImpl.class, primaryKey);

			if (kaleoTimerInstanceToken != null) {
				cacheResult(kaleoTimerInstanceToken);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		return kaleoTimerInstanceToken;
	}

	/**
	 * Returns the kaleo timer instance token with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param kaleoTimerInstanceTokenId the primary key of the kaleo timer instance token
	 * @return the kaleo timer instance token, or <code>null</code> if a kaleo timer instance token with the primary key could not be found
	 */
	@Override
	public KaleoTimerInstanceToken fetchByPrimaryKey(
		long kaleoTimerInstanceTokenId) {

		return fetchByPrimaryKey((Serializable)kaleoTimerInstanceTokenId);
	}

	@Override
	public Map<Serializable, KaleoTimerInstanceToken> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		if (ctPersistenceHelper.isProductionMode(
				KaleoTimerInstanceToken.class)) {

			return super.fetchByPrimaryKeys(primaryKeys);
		}

		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, KaleoTimerInstanceToken> map =
			new HashMap<Serializable, KaleoTimerInstanceToken>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			KaleoTimerInstanceToken kaleoTimerInstanceToken = fetchByPrimaryKey(
				primaryKey);

			if (kaleoTimerInstanceToken != null) {
				map.put(primaryKey, kaleoTimerInstanceToken);
			}

			return map;
		}

		if ((databaseInMaxParameters > 0) &&
			(primaryKeys.size() > databaseInMaxParameters)) {

			Iterator<Serializable> iterator = primaryKeys.iterator();

			while (iterator.hasNext()) {
				Set<Serializable> page = new HashSet<>();

				for (int i = 0;
					 (i < databaseInMaxParameters) && iterator.hasNext(); i++) {

					page.add(iterator.next());
				}

				map.putAll(fetchByPrimaryKeys(page));
			}

			return map;
		}

		StringBundler sb = new StringBundler((primaryKeys.size() * 2) + 1);

		sb.append(getSelectSQL());
		sb.append(" WHERE ");
		sb.append(getPKDBName());
		sb.append(" IN (");

		for (Serializable primaryKey : primaryKeys) {
			sb.append((long)primaryKey);

			sb.append(",");
		}

		sb.setIndex(sb.index() - 1);

		sb.append(")");

		String sql = sb.toString();

		Session session = null;

		try {
			session = openSession();

			Query query = session.createQuery(sql);

			for (KaleoTimerInstanceToken kaleoTimerInstanceToken :
					(List<KaleoTimerInstanceToken>)query.list()) {

				map.put(
					kaleoTimerInstanceToken.getPrimaryKeyObj(),
					kaleoTimerInstanceToken);

				cacheResult(kaleoTimerInstanceToken);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the kaleo timer instance tokens.
	 *
	 * @return the kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the kaleo timer instance tokens.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @return the range of kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findAll(
		int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the kaleo timer instance tokens.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoTimerInstanceTokenModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of kaleo timer instance tokens
	 * @param end the upper bound of the range of kaleo timer instance tokens (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of kaleo timer instance tokens
	 */
	@Override
	public List<KaleoTimerInstanceToken> findAll(
		int start, int end,
		OrderByComparator<KaleoTimerInstanceToken> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoTimerInstanceToken.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<KaleoTimerInstanceToken> list = null;

		if (useFinderCache && productionMode) {
			list = (List<KaleoTimerInstanceToken>)finderCache.getResult(
				finderPath, finderArgs);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_KALEOTIMERINSTANCETOKEN);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_KALEOTIMERINSTANCETOKEN;

				sql = sql.concat(
					KaleoTimerInstanceTokenModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<KaleoTimerInstanceToken>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the kaleo timer instance tokens from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (KaleoTimerInstanceToken kaleoTimerInstanceToken : findAll()) {
			remove(kaleoTimerInstanceToken);
		}
	}

	/**
	 * Returns the number of kaleo timer instance tokens.
	 *
	 * @return the number of kaleo timer instance tokens
	 */
	@Override
	public int countAll() {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoTimerInstanceToken.class);

		Long count = null;

		if (productionMode) {
			count = (Long)finderCache.getResult(
				_finderPathCountAll, FINDER_ARGS_EMPTY);
		}

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(
					_SQL_COUNT_KALEOTIMERINSTANCETOKEN);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(
						_finderPathCountAll, FINDER_ARGS_EMPTY, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "kaleoTimerInstanceTokenId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_KALEOTIMERINSTANCETOKEN;
	}

	@Override
	public Set<String> getCTColumnNames(
		CTColumnResolutionType ctColumnResolutionType) {

		return _ctColumnNamesMap.getOrDefault(
			ctColumnResolutionType, Collections.emptySet());
	}

	@Override
	public List<String> getMappingTableNames() {
		return _mappingTableNames;
	}

	@Override
	public Map<String, Integer> getTableColumnsMap() {
		return KaleoTimerInstanceTokenModelImpl.TABLE_COLUMNS_MAP;
	}

	@Override
	public String getTableName() {
		return "KaleoTimerInstanceToken";
	}

	@Override
	public List<String[]> getUniqueIndexColumnNames() {
		return _uniqueIndexColumnNames;
	}

	private static final Map<CTColumnResolutionType, Set<String>>
		_ctColumnNamesMap = new EnumMap<CTColumnResolutionType, Set<String>>(
			CTColumnResolutionType.class);
	private static final List<String> _mappingTableNames =
		new ArrayList<String>();
	private static final List<String[]> _uniqueIndexColumnNames =
		new ArrayList<String[]>();

	static {
		Set<String> ctControlColumnNames = new HashSet<String>();
		Set<String> ctIgnoreColumnNames = new HashSet<String>();
		Set<String> ctStrictColumnNames = new HashSet<String>();

		ctControlColumnNames.add("mvccVersion");
		ctControlColumnNames.add("ctCollectionId");
		ctStrictColumnNames.add("groupId");
		ctStrictColumnNames.add("companyId");
		ctStrictColumnNames.add("userId");
		ctStrictColumnNames.add("userName");
		ctStrictColumnNames.add("createDate");
		ctIgnoreColumnNames.add("modifiedDate");
		ctStrictColumnNames.add("kaleoClassName");
		ctStrictColumnNames.add("kaleoClassPK");
		ctStrictColumnNames.add("kaleoDefinitionId");
		ctStrictColumnNames.add("kaleoDefinitionVersionId");
		ctStrictColumnNames.add("kaleoInstanceId");
		ctStrictColumnNames.add("kaleoInstanceTokenId");
		ctStrictColumnNames.add("kaleoTaskInstanceTokenId");
		ctStrictColumnNames.add("kaleoTimerId");
		ctStrictColumnNames.add("kaleoTimerName");
		ctStrictColumnNames.add("blocking");
		ctStrictColumnNames.add("completionUserId");
		ctStrictColumnNames.add("completed");
		ctStrictColumnNames.add("completionDate");
		ctStrictColumnNames.add("workflowContext");

		_ctColumnNamesMap.put(
			CTColumnResolutionType.CONTROL, ctControlColumnNames);
		_ctColumnNamesMap.put(
			CTColumnResolutionType.IGNORE, ctIgnoreColumnNames);
		_ctColumnNamesMap.put(
			CTColumnResolutionType.PK,
			Collections.singleton("kaleoTimerInstanceTokenId"));
		_ctColumnNamesMap.put(
			CTColumnResolutionType.STRICT, ctStrictColumnNames);
	}

	/**
	 * Initializes the kaleo timer instance token persistence.
	 */
	@Activate
	public void activate() {
		_valueObjectFinderCacheListThreshold = GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD));

		_finderPathWithPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathWithPaginationFindByKaleoInstanceId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByKaleoInstanceId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"kaleoInstanceId"}, true);

		_finderPathWithoutPaginationFindByKaleoInstanceId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByKaleoInstanceId",
			new String[] {Long.class.getName()},
			new String[] {"kaleoInstanceId"}, true);

		_finderPathCountByKaleoInstanceId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByKaleoInstanceId",
			new String[] {Long.class.getName()},
			new String[] {"kaleoInstanceId"}, false);

		_finderPathFetchByKITI_KTI = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByKITI_KTI",
			new String[] {Long.class.getName(), Long.class.getName()},
			new String[] {"kaleoInstanceTokenId", "kaleoTimerId"}, true);

		_finderPathCountByKITI_KTI = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByKITI_KTI",
			new String[] {Long.class.getName(), Long.class.getName()},
			new String[] {"kaleoInstanceTokenId", "kaleoTimerId"}, false);

		_finderPathWithPaginationFindByKITI_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByKITI_C",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Integer.class.getName(), Integer.class.getName(),
				OrderByComparator.class.getName()
			},
			new String[] {"kaleoInstanceTokenId", "completed"}, true);

		_finderPathWithoutPaginationFindByKITI_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByKITI_C",
			new String[] {Long.class.getName(), Boolean.class.getName()},
			new String[] {"kaleoInstanceTokenId", "completed"}, true);

		_finderPathCountByKITI_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByKITI_C",
			new String[] {Long.class.getName(), Boolean.class.getName()},
			new String[] {"kaleoInstanceTokenId", "completed"}, false);

		_finderPathWithPaginationFindByKITI_B_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByKITI_B_C",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Boolean.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"kaleoInstanceTokenId", "blocking", "completed"},
			true);

		_finderPathWithoutPaginationFindByKITI_B_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByKITI_B_C",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Boolean.class.getName()
			},
			new String[] {"kaleoInstanceTokenId", "blocking", "completed"},
			true);

		_finderPathCountByKITI_B_C = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByKITI_B_C",
			new String[] {
				Long.class.getName(), Boolean.class.getName(),
				Boolean.class.getName()
			},
			new String[] {"kaleoInstanceTokenId", "blocking", "completed"},
			false);

		_setKaleoTimerInstanceTokenUtilPersistence(this);
	}

	@Deactivate
	public void deactivate() {
		_setKaleoTimerInstanceTokenUtilPersistence(null);

		entityCache.removeCache(KaleoTimerInstanceTokenImpl.class.getName());
	}

	private void _setKaleoTimerInstanceTokenUtilPersistence(
		KaleoTimerInstanceTokenPersistence kaleoTimerInstanceTokenPersistence) {

		try {
			Field field = KaleoTimerInstanceTokenUtil.class.getDeclaredField(
				"_persistence");

			field.setAccessible(true);

			field.set(null, kaleoTimerInstanceTokenPersistence);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@Override
	@Reference(
		target = KaleoPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = KaleoPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = KaleoPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Reference
	protected CTPersistenceHelper ctPersistenceHelper;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_KALEOTIMERINSTANCETOKEN =
		"SELECT kaleoTimerInstanceToken FROM KaleoTimerInstanceToken kaleoTimerInstanceToken";

	private static final String _SQL_SELECT_KALEOTIMERINSTANCETOKEN_WHERE =
		"SELECT kaleoTimerInstanceToken FROM KaleoTimerInstanceToken kaleoTimerInstanceToken WHERE ";

	private static final String _SQL_COUNT_KALEOTIMERINSTANCETOKEN =
		"SELECT COUNT(kaleoTimerInstanceToken) FROM KaleoTimerInstanceToken kaleoTimerInstanceToken";

	private static final String _SQL_COUNT_KALEOTIMERINSTANCETOKEN_WHERE =
		"SELECT COUNT(kaleoTimerInstanceToken) FROM KaleoTimerInstanceToken kaleoTimerInstanceToken WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS =
		"kaleoTimerInstanceToken.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No KaleoTimerInstanceToken exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No KaleoTimerInstanceToken exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		KaleoTimerInstanceTokenPersistenceImpl.class);

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

}