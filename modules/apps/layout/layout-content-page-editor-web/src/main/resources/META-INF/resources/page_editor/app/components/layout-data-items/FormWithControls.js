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

import classNames from 'classnames';
import React from 'react';

import {useSelector, useSelectorCallback} from '../../contexts/StoreContext';
import selectLanguageId from '../../selectors/selectLanguageId';
import {formIsMapped} from '../../utils/formIsMapped';
import {getEditableLocalizedValue} from '../../utils/getEditableLocalizedValue';
import isItemEmpty from '../../utils/isItemEmpty';
import ContainerWithControls from './ContainerWithControls';

const FormWithControls = React.forwardRef(({children, item, ...rest}, ref) => {
	const isMapped = formIsMapped(item);

	const isEmpty = useSelectorCallback(
		(state) =>
			isItemEmpty(item, state.layoutData, state.selectedViewportSize),
		[item]
	);

	const showMessagePreview = item.config?.showMessagePreview;

	return (
		<form
			className={classNames('page-editor__form', {
				'page-editor__form--success': showMessagePreview,
			})}
			onSubmit={(event) => event.preventDefault()}
			ref={ref}
		>
			<ContainerWithControls {...rest} item={item}>
				{showMessagePreview ? (
					<FormSuccessMessage item={item} />
				) : isEmpty || !isMapped ? (
					<FormEmptyState isMapped={isMapped} />
				) : (
					children
				)}
			</ContainerWithControls>
		</form>
	);
});

export default FormWithControls;

function FormEmptyState({isMapped}) {
	if (isMapped) {
		return (
			<div className="page-editor__no-fragments-state">
				<p className="m-0 page-editor__no-fragments-state__message">
					{Liferay.Language.get('place-fragments-here')}
				</p>
			</div>
		);
	}

	return (
		<div className="bg-lighter page-editor__no-fragments-state">
			<p className="page-editor__no-fragments-state__title">
				{Liferay.Language.get('map-your-form')}
			</p>

			<p className="page-editor__no-fragments-state__message">
				{Liferay.Language.get(
					'select-a-content-type-to-start-creating-the-form'
				)}
			</p>
		</div>
	);
}

function FormSuccessMessage({item}) {
	const languageId = useSelector(selectLanguageId);

	return (
		<div className="align-items-center d-flex justify-content-center p-5 page-editor__form__success-message">
			<span className="font-weight-semi-bold text-secondary">
				{getEditableLocalizedValue(
					item.config?.successMessage?.message,
					languageId,
					Liferay.Language.get(
						'thank-you.-your-information-was-successfully-received'
					)
				)}
			</span>
		</div>
	);
}
