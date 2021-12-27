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

package com.liferay.segments.internal.model.listener;

import com.liferay.portal.kernel.exception.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.segments.SegmentsEntryRetriever;
import com.liferay.segments.criteria.Criteria;
import com.liferay.segments.model.SegmentsEntry;
import com.liferay.segments.service.SegmentsEntryLocalService;
import com.liferay.segments.service.SegmentsEntryRelLocalService;

import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Laszlo Pap
 */
@Component(immediate = true, service = ModelListener.class)
public class UserGroupRoleModelListener
	extends BaseModelListener<UserGroupRole> {

	@Override
	public void onBeforeRemove(UserGroupRole userGroupRole)
		throws ModelListenerException {

		try {
			_deleteUserGroupRoleSegmentsData(userGroupRole);
		}
		catch (Exception exception) {
			throw new ModelListenerException(exception);
		}
	}

	private void _deleteUserGroupRoleSegmentsData(UserGroupRole userGroupRole)
		throws PortalException {

		long[] userSegmentsEntryIds =
			_segmentsEntryRetriever.getSegmentsEntryIds(
				userGroupRole.getGroupId(), userGroupRole.getUserId(), null);

		for (long userSegmentsEntryId : userSegmentsEntryIds) {
			SegmentsEntry userSegmentsEntry =
				_segmentsEntryLocalService.fetchSegmentsEntry(
					userSegmentsEntryId);

			if (userSegmentsEntry != null) {
				Criteria criteria = userSegmentsEntry.getCriteriaObj();

				Map<String, String> filterStrings = criteria.getFilterStrings();

				for (Map.Entry<String, String> entry :
						filterStrings.entrySet()) {

					long deletedUserGroupRoleRoleID = userGroupRole.getRoleId();
					String filterString = entry.getValue();

					if (filterString.contains("userGroupRoleIds") &&
						filterString.contains(
							String.valueOf(deletedUserGroupRoleRoleID))) {

						long classNameId =
							_classNameLocalService.getClassNameId(User.class);

						if (_segmentsEntryRelLocalService.hasSegmentsEntryRel(
								userSegmentsEntryId, classNameId,
								userGroupRole.getUserId())) {

							_segmentsEntryRelLocalService.
								deleteSegmentsEntryRel(
									userSegmentsEntryId, classNameId,
									userGroupRole.getUserId());
						}
					}
				}
			}
		}
	}

	@Reference
	private ClassNameLocalService _classNameLocalService;

	@Reference
	private SegmentsEntryLocalService _segmentsEntryLocalService;

	@Reference
	private SegmentsEntryRelLocalService _segmentsEntryRelLocalService;

	@Reference
	private SegmentsEntryRetriever _segmentsEntryRetriever;

}