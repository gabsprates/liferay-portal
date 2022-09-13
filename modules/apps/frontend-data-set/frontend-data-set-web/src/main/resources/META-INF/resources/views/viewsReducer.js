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

import getViewComponent from './getViewComponent';

export const VIEWS_ACTION_TYPES = {
	ADD_OR_UPDATE_CUSTOM_VIEW: 'ADD_OR_UPDATE_CUSTOM_VIEW',
	UPDATE_ACTIVE_VIEW: 'UPDATE_ACTIVE_VIEW',
	UPDATE_FILTERS: 'UPDATE_FILTERS',
	UPDATE_PAGINATION_DELTA: 'UPDATE_PAGINATION_DELTA',
	UPDATE_SORTING: 'UPDATE_SORTING',
	UPDATE_VIEW_COMPONENT: 'UPDATE_VIEW_COMPONENT',
	UPDATE_VISIBLE_FIELD_NAMES: 'UPDATE_VISIBLE_FIELD_NAMES',
};

export function viewsReducer(state, {type, value}) {
	const {activeView, customViews, views} = state;

	if (type === VIEWS_ACTION_TYPES.ADD_OR_UPDATE_CUSTOM_VIEW) {
		const {id, viewState} = value;

		return {
			...state,
			activeCustomViewId: id,
			customViews: {
				...customViews,
				[id]: viewState,
			},
			viewUpdated: false,
		};
	}
	else if (type === VIEWS_ACTION_TYPES.UPDATE_ACTIVE_VIEW) {
		const activeView = views.find(({name}) => name === value);

		if (activeView) {
			activeView.component = getViewComponent(value);
		}

		return {
			...state,
			activeView,
			viewUpdated: true,
		};
	}
	else if (type === VIEWS_ACTION_TYPES.UPDATE_FILTERS) {
		return {
			...state,
			filters: value,
			viewUpdated: true,
		};
	}
	else if (type === VIEWS_ACTION_TYPES.UPDATE_PAGINATION_DELTA) {
		return {
			...state,
			paginationDelta: value,
			viewUpdated: true,
		};
	}
	else if (type === VIEWS_ACTION_TYPES.UPDATE_SORTING) {
		return {
			...state,
			sorting: value,
			viewUpdated: true,
		};
	}
	else if (type === VIEWS_ACTION_TYPES.UPDATE_VIEW_COMPONENT) {
		const {component, name} = value;

		return {
			...state,
			activeView:
				name === activeView?.name
					? {
							...activeView,
							component,
					  }
					: activeView,
			views: views.map((view) =>
				view.name === name
					? {
							...view,
							component,
					  }
					: view
			),
		};
	}
	else if (type === VIEWS_ACTION_TYPES.UPDATE_VISIBLE_FIELD_NAMES) {
		return {
			...state,
			viewUpdated: true,
			visibleFieldNames: value,
		};
	}

	return state;
}
