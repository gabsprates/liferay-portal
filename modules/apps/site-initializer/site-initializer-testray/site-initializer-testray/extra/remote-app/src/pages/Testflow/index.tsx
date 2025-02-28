/* eslint-disable no-console */
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

import Avatar from '../../components/Avatar';
import Container from '../../components/Layout/Container';
import ListView from '../../components/ListView';
import ProgressBar from '../../components/ProgressBar';
import StatusBadge from '../../components/StatusBadge';
import {useHeader} from '../../hooks';
import i18n from '../../i18n';
import {testrayTaskImpl} from '../../services/rest';
import {SUBTASK_STATUS} from '../../util/constants';
import {getTimeFromNow} from '../../util/date';
import {routines} from '../../util/mock';
import TestflowModal from './TestflowModal';
import useTestflowActions from './useTestflowActions';

const TestFlow = () => {
	const {actions, modal} = useTestflowActions();

	useHeader({useIcon: 'merge'});

	return (
		<Container>
			<ListView
				managementToolbarProps={{
					addButton: () => modal.open(),
					title: i18n.translate('tasks'),
				}}
				resource={testrayTaskImpl.resource}
				tableProps={{
					actions,
					columns: [
						{
							clickable: true,
							key: 'dueStatus',
							render: (status: number) => (
								<StatusBadge
									type={
										(SUBTASK_STATUS as any)[status]?.color
									}
								>
									{(SUBTASK_STATUS as any)[status]?.label}
								</StatusBadge>
							),
							value: i18n.translate('status'),
						},
						{
							clickable: true,
							key: 'dateCreated',
							render: (_, task) =>
								task?.build?.dateCreated &&
								getTimeFromNow(task?.build?.dateCreated),
							value: i18n.translate('start-date'),
						},
						{
							clickable: true,
							key: 'name',
							size: 'sm',
							value: i18n.translate('task'),
						},
						{
							clickable: true,
							key: 'projectName',
							render: (_, task) => task?.build?.project?.name,
							value: i18n.translate('project-name'),
						},
						{
							clickable: true,
							key: 'routineName',
							render: (_, task) => task?.build?.routine?.name,
							value: i18n.translate('routine-name'),
						},
						{
							clickable: true,
							key: 'buildName',
							render: (_, task) => task?.build?.name,
							value: i18n.translate('build-name'),
						},
						{
							key: 'score',
							render: () => '59 / 2172 (3%)',
							value: i18n.translate('score'),
						},
						{
							key: 'progress',
							render: () => (
								<ProgressBar
									items={{
										incomplete: 100,
										passed: 10,
									}}
								/>
							),
							size: 'sm',
							value: i18n.translate('progress'),
						},
						{
							key: 'assigned',
							render: () => (
								<Avatar.Group
									assignedUsers={routines[0].assigned}
									groupSize={3}
								/>
							),
							value: i18n.translate('assigned'),
						},
					],
					navigateTo: (item) => `/testflow/${item.id}`,
				}}
				transformData={(response) =>
					testrayTaskImpl.transformDataFromList(response)
				}
			/>

			<TestflowModal modal={modal} />
		</Container>
	);
};

export default TestFlow;
