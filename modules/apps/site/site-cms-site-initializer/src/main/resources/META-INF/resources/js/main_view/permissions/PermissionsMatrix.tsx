/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayLoadingIndicator from '@clayui/loading-indicator';
import {openToast} from 'frontend-js-components-web';
import React, {useEffect, useState} from 'react';

import DefaultPermissionForm from '../default_permission/DefaultPermissionForm';
import {
	Role,
	RoleSelectedActions,
} from '../default_permission/DefaultPermissionTypes';
import {PermissionsSection} from './PermissionsPageConfig';
import toRoleSelectedActions from './toRoleSelectedActions';

interface PermissionsMatrixProps {
	onChange: (sectionKey: string, values: RoleSelectedActions) => void;
	roles: Role[];
	section: PermissionsSection;
	values?: RoleSelectedActions;
}

export default function PermissionsMatrix({
	onChange,
	roles,
	section,
	values,
}: PermissionsMatrixProps) {
	const [loadFailed, setLoadFailed] = useState(false);

	useEffect(() => {
		if (loadFailed || values || !section.loadValues) {
			return;
		}

		section
			.loadValues()
			.then((loadedValues) => onChange(section.key, loadedValues))
			.catch((error: Error) => {
				openToast({message: error.message, type: 'danger'});

				setLoadFailed(true);
			});
	}, [loadFailed, onChange, section, values]);

	if (loadFailed || !section.loadValues) {
		return null;
	}

	if (!values) {
		return <ClayLoadingIndicator />;
	}

	return (
		<DefaultPermissionForm
			actions={section.actions}
			onChange={(checkedRoleActions) =>
				onChange(section.key, toRoleSelectedActions(checkedRoleActions))
			}
			roles={roles}
			values={values}
		/>
	);
}
