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

import {
	cog,
	community,
	display,
	drop,
	filter,
	listUl,
	merge,
	orderFormCog,
	page,
	pencil,
	plus,
	plusUser,
	polls,
	print,
	spanner,
	trash,
} from './spritemap';

type TestrayIconsProps = {
	className?: string;
	fill?: string;
	size?: number;
	symbol?: string;
};

const TestrayIcons: React.FC<TestrayIconsProps> = ({
	className,
	fill,
	size = 20,
	symbol,
}) => {
	const icon: any = {
		cog,
		community,
		display,
		drop,
		filter,
		'list-ul': listUl,
		merge,
		'order-form-cog': orderFormCog,
		page,
		pencil,
		plus,
		'plus-user': plusUser,
		polls,
		print,
		spanner,
		trash,
	};

	return (
		<svg
			className={className}
			fill={fill}
			height={size}
			viewBox="0 0 30 30"
			width={size}
		>
			{icon[symbol]}
		</svg>
	);
};

export default TestrayIcons;
