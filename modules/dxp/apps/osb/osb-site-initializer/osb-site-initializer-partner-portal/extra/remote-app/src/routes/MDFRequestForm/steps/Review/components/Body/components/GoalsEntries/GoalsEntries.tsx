/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import MDFRequest from '../../../../../../../../common/interfaces/mdfRequest';
import Table from '../../../Table';

interface IProps {
	values: MDFRequest;
}

const GoalsEntries = ({values}: IProps) => {
	return (
		<div>
			<Table
				items={[
					{
						title: 'Company Name',
						value: values.r_accountToMDFRequests_accountEntryId,
					},
					{
						title: 'Region',
						value: values.country.name,
					},
				]}
				title="Partner Summary"
			/>

			<Table
				items={[
					{
						title:
							'Provide a name and short description of the overall campaign',
						value: values.overallCampaign,
					},
					{
						title: 'Liferay business/sales goals',
						value: values.liferayBusinessSalesGoals.join(', '),
					},
				]}
				title="Activity Summary"
			/>

			<Table
				items={[
					{
						title: 'Target Market(s)',
						value: values.targetMarkets.join(', '),
					},
					{
						title: 'Additional Options',
						value: values.additionalOption.name
							? values.additionalOption.name
							: 'N/A',
					},
					{
						title: 'Target Audience/Role',
						value: values.targetAudienceRoles.join(', '),
					},
				]}
				title="Target Market"
			/>
		</div>
	);
};
export default GoalsEntries;
