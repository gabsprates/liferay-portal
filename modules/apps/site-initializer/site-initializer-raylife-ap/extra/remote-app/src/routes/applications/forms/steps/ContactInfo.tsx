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

import ClayDatePicker from '@clayui/date-picker';
import ClayForm, {
	ClayInput,
	ClayRadio,
	ClayRadioGroup,
	ClaySelect,
} from '@clayui/form';
import ClayIcon from '@clayui/icon';
import classNames from 'classnames';
import {useContext, useState} from 'react';

import {
	ACTIONS,
	NewApplicationAutoContext,
} from '../../context/NewApplicationAutoContextProvider';

const ContactInfo = () => {
	const [state, dispatch] = useContext(NewApplicationAutoContext);

	const {form} = state?.steps?.contactInfo;

	const handleChangeField = (fieldName: string, fieldValue: string) => {
		const payload = {
			...form,
			[fieldName]: fieldValue,
		};

		dispatch({payload, type: ACTIONS.SET_CONTACT_INFO_FORM});
		dispatch({payload: true, type: ACTIONS.SET_HAS_FORM_CHANGE});
	};

	const stateOptions = [
		{
			label: '',
			value: '1',
		},
		{
			label: 'CHOOSE AN OPTION',
			value: '2',
		},
		{
			label: 'CA',
			value: '3',
		},
		{
			label: 'NV',
			value: '4',
		},
		{
			label: 'NY',
			value: '5',
		},
	];

	type hasRequiredErrorTypes = {
		city: boolean;
		dateOfBirth: boolean;
		email: boolean;
		firstName: boolean;
		lastName: boolean;
		phone: boolean;
		state: boolean;
		streetAddress: boolean;
		zipCode: boolean;
	};

	type HasValidationsTypes = {
		city: boolean;
		dateOfBirth: boolean;
		email: boolean;
		firstName: boolean;
		lastName: boolean;
		phone: boolean;
		state: boolean;
		streetAddress: boolean;
		zipCode: boolean;
	};

	const hasRequiredError: hasRequiredErrorTypes = {
		city: false,
		dateOfBirth: false,
		email: false,
		firstName: false,
		lastName: false,
		phone: false,
		state: false,
		streetAddress: false,
		zipCode: false,
	};

	const hasValidations: HasValidationsTypes = {
		city: false,
		dateOfBirth: false,
		email: false,
		firstName: false,
		lastName: false,
		phone: false,
		state: false,
		streetAddress: false,
		zipCode: false,
	};

	const [hasError, setHasError] = useState(hasRequiredError);

	const [hasValidation, setHasValidation] = useState(hasValidations);

	const ErrorMessage = ({text}: any) => (
		<div className="form-feedback-group">
			<div className="form-feedback-item">
				<span className="form-feedback-indicator">
					<ClayIcon symbol="info-circle"></ClayIcon>
				</span>

				{text ? text : 'This field is required'}
			</div>
		</div>
	);

	const isValidUSZip = (zipCode: string) => {
		const zipCodePattern = /(^\d{5}$)|(^\d{5}-\d{4}$)/;

		return zipCodePattern.test(zipCode);
	};

	const isValidEmail = (email: string) => {
		const emailPattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

		return emailPattern.test(email);
	};

	const isValidPhone = (elementValue: string) => {
		const phoneNumberPattern = /^\(?(\d{3})\)?[- ]?(\d{3})[- ]?(\d{4})$/;

		return phoneNumberPattern.test(elementValue);
	};

	return (
		<div className="mx-8">
			<div className="font-weight-bolder text-paragraph-sm text-uppercase">
				Contact Information
			</div>

			<hr></hr>

			<ClayForm>
				<div className="d-flex flex-row">
					<div
						className={classNames(
							'col filled form-condensed form-group',
							{
								'has-error': hasError.firstName,
							}
						)}
					>
						<ClayInput
							className="bg-neutral-0"
							id="firstName"
							name="firstName"
							onBlur={(event) => {
								const firstNameValue = event.target.value;
								setHasError({
									...hasError,
									firstName:
										firstNameValue === '' ? true : false,
								});
							}}
							onChange={(event) => {
								const firstNameValue = event.target.value;
								handleChangeField('firstName', firstNameValue);
								setHasError({
									...hasError,
									firstName:
										firstNameValue === '' ? true : false,
								});
							}}
							required
							type="text"
							value={form.firstName}
						/>

						<label htmlFor="firstName">
							First Name&nbsp;
							<span className="text-danger-darken-1">*</span>
						</label>

						{hasError.firstName && <ErrorMessage />}
					</div>

					<div
						className={classNames(
							'col filled form-condensed form-group',
							{
								'has-error': hasError.lastName,
							}
						)}
					>
						<ClayInput
							className="bg-neutral-0"
							id="lastName"
							name="lastName"
							onBlur={(event) => {
								const lastNameValue = event.target.value;
								setHasError({
									...hasError,
									lastName:
										lastNameValue === '' ? true : false,
								});
							}}
							onChange={(event) => {
								const lastNameValue = event.target.value;
								handleChangeField('lastName', lastNameValue);
								setHasError({
									...hasError,
									lastName:
										lastNameValue === '' ? true : false,
								});
							}}
							required
							type="text"
							value={form.lastName}
						/>

						<label htmlFor="lastName">
							Last Name&nbsp;
							<span className="text-danger-darken-1">*</span>
						</label>

						{hasError.lastName && <ErrorMessage />}
					</div>
				</div>

				<div className="d-flex flex-row">
					<div
						className={classNames(
							'col filled form-condensed form-group position-relative"',
							{
								'has-error': hasError.dateOfBirth,
							}
						)}
					>
						<ClayDatePicker
							dateFormat="MM/dd/yyyy"
							onBlur={(event) => {
								const dateValue = event.target.value;
								setHasError({
									...hasError,
									dateOfBirth:
										dateValue === '' ? true : false,
								});
							}}
							onExpandedChange={(event: any) => {
								const dateValue = event.target.value;
								handleChangeField(form.dateOfBirth, dateValue);

								setHasError({
									...hasError,
									dateOfBirth:
										dateValue === '' ? true : false,
								});
							}}
							placeholder="__/__/____"
							value={form.dateOfBirth}
							years={{
								end: 2026,
								start: 1990,
							}}
						/>

						<ClayIcon
							className="calendar-icon mr-3"
							symbol="calendar"
						/>

						<label htmlFor="dateOfBirth">
							Date of Birth&nbsp;
							<span className="text-danger-darken-1">*</span>
						</label>

						{hasError.dateOfBirth && <ErrorMessage />}
					</div>

					<div
						className={classNames(
							'col filled form-condensed form-group',
							{
								'has-error':
									hasError.phone || hasValidation.phone,
							}
						)}
					>
						<ClayInput
							className="bg-neutral-0"
							id="phone"
							name="phone"
							onBlur={(event) => {
								const phoneValue = event.target.value;
								setHasError({
									...hasError,
									phone: phoneValue === '' ? true : false,
								});
							}}
							onChange={(event) => {
								const phoneValue = event.target.value;
								handleChangeField('phone', phoneValue);
								setHasError({
									...hasError,
									phone: phoneValue === '' ? true : false,
								});
								setHasValidation({
									...hasValidation,
									phone:
										isValidPhone(phoneValue) ||
										phoneValue === ''
											? false
											: true,
								});
							}}
							placeholder="xxx-xxx-xxxx"
							required
							type="text"
							value={form.phone}
						/>

						<label htmlFor="phone">
							Phone&nbsp;
							<span className="text-danger-darken-1">*</span>
						</label>

						{hasError.phone && <ErrorMessage />}

						{hasValidation.phone && (
							<ErrorMessage text="Phone number is invalid" />
						)}
					</div>

					<div
						className={classNames(
							'col filled form-condensed form-group',
							{
								'has-error':
									hasError.email || hasValidation.email,
							}
						)}
					>
						<ClayInput
							className="bg-neutral-0"
							id="email"
							name="email"
							onBlur={(event) => {
								const emailValue = event.target.value;
								setHasError({
									...hasError,
									email: emailValue === '' ? true : false,
								});
							}}
							onChange={(event) => {
								const emailValue = event.target.value;
								handleChangeField('email', emailValue);
								setHasError({
									...hasError,
									email: emailValue === '' ? true : false,
								});
								setHasValidation({
									...hasValidation,
									email:
										isValidEmail(emailValue) ||
										emailValue === ''
											? false
											: true,
								});
							}}
							required
							type="text"
							value={form.email}
						/>

						<label htmlFor="email">
							Email&nbsp;
							<span className="text-danger-darken-1">*</span>
						</label>

						{hasError.email && <ErrorMessage />}

						{!!hasValidation.email && (
							<ErrorMessage text="Email is invalid" />
						)}
					</div>
				</div>
			</ClayForm>

			<div className="font-weight-bolder text-paragraph-sm text-uppercase">
				Address Information
			</div>

			<hr></hr>

			<ClayForm>
				<div className="d-flex flex-row">
					<div
						className={classNames(
							'col filled form-condensed form-group',
							{
								'has-error': hasError.streetAddress,
							}
						)}
					>
						<ClayInput
							className="bg-neutral-0"
							id="streetAddress"
							name="streetAddress"
							onBlur={(event) => {
								const streetAddressValue = event.target.value;
								setHasError({
									...hasError,
									streetAddress:
										streetAddressValue === ''
											? true
											: false,
								});
							}}
							onChange={(event) => {
								const streetAddressValue = event.target.value;
								handleChangeField(
									'streetAddress',
									streetAddressValue
								);
								setHasError({
									...hasError,
									streetAddress:
										streetAddressValue === ''
											? true
											: false,
								});
							}}
							placeholder="Enter a location"
							required
							type="text"
							value={form.streetAddress}
						/>

						<label htmlFor="streetAddress">
							Street Address&nbsp;
							<span className="text-danger-darken-1">*</span>
						</label>

						{hasError.streetAddress && <ErrorMessage />}
					</div>

					<div className="col filled form-condensed form-group">
						<ClayInput
							className="bg-neutral-0"
							id="apt"
							name="apt"
							onChange={(event) => {
								handleChangeField('apt', event.target.value);
							}}
							required
							type="text"
							value={form.apt}
						/>

						<label htmlFor="apt">Apt</label>
					</div>
				</div>

				<div className="d-flex flex-row">
					<div
						className={classNames(
							'col filled form-condensed form-group',
							{
								'has-error': hasError.city,
							}
						)}
					>
						<ClayInput
							className="bg-neutral-0"
							id="city"
							name="city"
							onBlur={(event) => {
								const cityValue = event.target.value;
								setHasError({
									...hasError,
									city: cityValue === '' ? true : false,
								});
							}}
							onChange={(event) => {
								const cityValue = event.target.value;
								handleChangeField('city', cityValue);
								setHasError({
									...hasError,
									city: cityValue === '' ? true : false,
								});
							}}
							required
							type="text"
							value={form.city}
						/>

						<label htmlFor="city">
							City&nbsp;
							<span className="text-danger-darken-1">*</span>
						</label>

						{hasError.city && <ErrorMessage />}
					</div>

					<div
						className={classNames(
							'col filled form-condensed form-group',
							{
								'has-error': hasError.state,
							}
						)}
					>
						<ClaySelect
							aria-label="Select Label"
							id="mySelectId"
							onBlur={(event) => {
								const stateValue = event.target.value;
								setHasError({
									...hasError,
									state:
										stateValue === '1' || stateValue === '2'
											? true
											: false,
								});
							}}
							onChange={(event) => {
								const stateValue = event.target.value;
								handleChangeField('state', stateValue);

								setHasError({
									...hasError,
									state:
										stateValue === '1' || stateValue === '2'
											? true
											: false,
								});
							}}
						>
							{stateOptions.map((item) => (
								<ClaySelect.Option
									key={item.value}
									label={item.label}
									value={item.label}
								/>
							))}
						</ClaySelect>

						<label htmlFor="state">
							State&nbsp;
							<span className="text-danger-darken-1">*</span>
						</label>

						{hasError.state && <ErrorMessage />}
					</div>

					<div
						className={classNames(
							'col filled form-condensed form-group',
							{
								'has-error':
									hasError.zipCode || hasValidation.zipCode,
							}
						)}
					>
						<ClayInput
							className="bg-neutral-0"
							id="zipCode"
							name="zipCode"
							onBlur={(event) => {
								const zipCodeValue = event.target.value;
								setHasError({
									...hasError,
									zipCode: zipCodeValue === '' ? true : false,
								});
							}}
							onChange={(event) => {
								const zipCodeValue = event.target.value;
								handleChangeField('zipCode', zipCodeValue);
								setHasError({
									...hasError,
									zipCode: zipCodeValue === '' ? true : false,
								});
								setHasValidation({
									...hasValidation,
									zipCode:
										isValidUSZip(zipCodeValue) ||
										zipCodeValue === ''
											? false
											: true,
								});
							}}
							required
							type="text"
							value={form.zipCode}
						/>

						<label htmlFor="zipCode">
							Zip Code&nbsp;
							<span className="text-danger-darken-1">*</span>
						</label>

						{hasError.zipCode && <ErrorMessage />}

						{!!hasValidation.zipCode && (
							<ErrorMessage text="Zip code is invalid" />
						)}
					</div>
				</div>
			</ClayForm>

			<div>
				<div className="mb-1 ml-3 text-neutral-9 text-paragraph-sm">
					Ownership&nbsp;
					<span className="text-danger-darken-1">*</span>
				</div>

				<ClayRadioGroup
					className="ml-3"
					inline
					onChange={(event: any) => {
						const ownershipValue = event.target.value;
						handleChangeField(form.ownership, ownershipValue);
					}}
				>
					<ClayRadio label="Rent" value="One" />

					<ClayRadio label="Own" value="Two" />
				</ClayRadioGroup>
			</div>
		</div>
	);
};

export default ContactInfo;
