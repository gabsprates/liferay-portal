/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

import {createRESTClient} from '@liferay/frontend-js-rest-client-generator';

import {BASE_PATH} from './types';

import type {Endpoints} from './types';

/**
 * Typed client for the Headless Admin User. Each method is scoped to the paths valid
 * for that HTTP verb, and the options and response are inferred from the
 * matching `Endpoints` entry.
 */
export const request = createRESTClient<Endpoints>(BASE_PATH);
