/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import ClayButton from '@clayui/button';
import ClayLayout from '@clayui/layout';
import {ClayVerticalNav} from '@clayui/nav';
import {navigate} from 'frontend-js-web';
import React, {useCallback, useMemo, useState} from 'react';

import Toolbar from '../../common/components/Toolbar';
import {
	ActionsMap,
	Role,
	RoleSelectedActions,
} from '../default_permission/DefaultPermissionTypes';
import PermissionsMatrix from './PermissionsMatrix';
import {DefaultsSection, OWN_PERMISSIONS} from './PermissionsPageConfig';
import getPermissionsPageConfig from './getPermissionsPageConfig';

import './Permissions.scss';

interface PermissionsProps {
	actions: ActionsMap;
	backURL: string;
	className: string;
	classPK: number;
	defaultsSections: DefaultsSection[];
	roles: Role[];
}

export default function Permissions({
	actions,
	backURL,
	className,
	classPK,
	defaultsSections,
	roles,
}: PermissionsProps) {
	const [activeSectionKey, setActiveSectionKey] =
		useState<string>(OWN_PERMISSIONS);
	const [values, setValues] = useState<Record<string, RoleSelectedActions>>(
		{}
	);

	const {sections} = useMemo(
		() =>
			getPermissionsPageConfig({
				actions,
				className,
				classPK,
				defaultsSections,
			}),
		[actions, className, classPK, defaultsSections]
	);

	const activeSection =
		sections.find((section) => section.key === activeSectionKey) ||
		sections[0];

	const handleChange = useCallback(
		(sectionKey: string, sectionValues: RoleSelectedActions) =>
			setValues((previousValues) => ({
				...previousValues,
				[sectionKey]: sectionValues,
			})),
		[]
	);

	return (
		<>
			<Toolbar
				backURL={backURL}
				title={Liferay.Language.get('permissions')}
			>
				<Toolbar.Item>
					<ClayButton
						borderless
						displayType="secondary"
						onClick={() => navigate(backURL)}
						size="sm"
					>
						{Liferay.Language.get('cancel')}
					</ClayButton>
				</Toolbar.Item>
			</Toolbar>

			<ClayLayout.ContainerFluid size={false}>
				<ClayLayout.Row className="min-vh-100">
					{sections.length > 1 ? (
						<ClayLayout.Col
							className="cms-sidebar-nav sidebar-layout"
							md="auto"
							sm={12}
						>
							<div className="px-md-2 py-3 py-md-4">
								<ClayVerticalNav
									items={sections.map((section) => ({
										active: activeSection === section,
										label: section.label,
										onClick: () => {
											setActiveSectionKey(section.key);
										},
									}))}
								/>
							</div>
						</ClayLayout.Col>
					) : (
						<div className="min-vh-100 py-4" />
					)}

					<ClayLayout.Col className="col-md px-4 py-4" sm={12}>
						<ClayLayout.ContainerFluid size="xxl">
							<h2
								className={
									activeSection.description
										? 'mb-1 text-5'
										: 'text-5'
								}
							>
								{activeSection.label}
							</h2>

							{activeSection.description && (
								<p className="text-3 text-secondary">
									{activeSection.description}
								</p>
							)}

							<PermissionsMatrix
								key={activeSection.key}
								onChange={handleChange}
								roles={roles}
								section={activeSection}
								values={values[activeSection.key]}
							/>
						</ClayLayout.ContainerFluid>
					</ClayLayout.Col>

					<aside className="cms-permissions-aside">
						<p>{Liferay.Language.get('unsaved-changes')}</p>
					</aside>
				</ClayLayout.Row>
			</ClayLayout.ContainerFluid>
		</>
	);
}
