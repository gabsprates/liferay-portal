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

import {setSessionValue} from 'frontend-js-web';
import React, {useEffect} from 'react';

import ConnectToAC from './components/ConnectToAC.es';
import SegmentsExperimentsSidebar from './components/SegmentsExperimentsSidebar.es';
import SegmentsExperimentsContext from './context.es';
import APIService from './util/APIService.es';

import '../css/main.scss';

export default function ({context, props}) {
	const isAnalyticsSync = props.analyticsData?.isSynced;
	const {endpoints, imagesPath, page} = context;
	const {
		calculateSegmentsExperimentEstimatedDurationURL,
		createSegmentsExperimentURL,
		createSegmentsVariantURL,
		deleteSegmentsExperimentURL,
		deleteSegmentsVariantURL,
		editSegmentsExperimentStatusURL,
		editSegmentsExperimentURL,
		editSegmentsVariantLayoutURL,
		editSegmentsVariantURL,
		runSegmentsExperimentURL,
	} = endpoints;
	const segmentsExperimentPanelToggle = document.getElementById(
		`${context.namespace}segmentsExperimentPanelToggleId`
	);

	useEffect(() => {
		if (segmentsExperimentPanelToggle) {
			const sidenavInstance = Liferay.SideNavigation.instance(
				segmentsExperimentPanelToggle
			);

			sidenavInstance.on('open.lexicon.sidenav', () => {
				setSessionValue(
					'com.liferay.segments.experiment.web_panelState',
					'open'
				);
			});

			sidenavInstance.on('closed.lexicon.sidenav', () => {
				setSessionValue(
					'com.liferay.segments.experiment.web_panelState',
					'closed'
				);
			});

			Liferay.once('screenLoad', () => {
				Liferay.SideNavigation.destroy(segmentsExperimentPanelToggle);
			});
		}
	}, [segmentsExperimentPanelToggle]);

	return (
		<div id={`${context.namespace}-segments-experiment-root`}>
			{isAnalyticsSync ? (
				<SegmentsExperimentsContext.Provider
					value={{
						APIService: APIService({
							contentPageEditorNamespace:
								context.contentPageEditorNamespace,
							endpoints: {
								calculateSegmentsExperimentEstimatedDurationURL,
								createSegmentsExperimentURL,
								createSegmentsVariantURL,
								deleteSegmentsExperimentURL,
								deleteSegmentsVariantURL,
								editSegmentsExperimentStatusURL,
								editSegmentsExperimentURL,
								editSegmentsVariantURL,
								runSegmentsExperimentURL,
							},
							namespace: context.namespace,
						}),
						editVariantLayoutURL: editSegmentsVariantLayoutURL,
						imagesPath,
						page,
					}}
				>
					<div id={`${context.namespace}-segments-experiment-root`}>
						<SegmentsExperimentsSidebar
							initialExperimentHistory={
								props.historySegmentsExperiments
							}
							initialGoals={props.segmentsExperimentGoals}
							initialSegmentsExperiment={props.segmentsExperiment}
							initialSegmentsVariants={
								props.initialSegmentsVariants
							}
							initialSelectedSegmentsExperienceId={
								props.selectedSegmentsExperienceId
							}
							winnerSegmentsVariantId={
								props.winnerSegmentsVariantId
							}
						/>
					</div>
				</SegmentsExperimentsContext.Provider>
			) : (
				<ConnectToAC
					analyticsCloudTrialURL={props.analyticsData?.cloudTrialURL}
					analyticsURL={props.analyticsData?.url}
					hideAnalyticsReportsPanelURL={
						props.hideSegmentsExperimentPanelURL
					}
					isAnalyticsConnected={props.analyticsData?.isConnected}
					pathToAssets={props.pathToAssets}
				/>
			)}
		</div>
	);
}
