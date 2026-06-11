/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

/**
 * A generic, API-agnostic REST client. Given an endpoint map `E` keyed by
 * `"METHOD /path"` strings, it produces a fully typed client whose methods are
 * scoped to the paths valid for each HTTP verb, with options and response
 * inferred from the matching entry.
 *
 * Any module that generates an `Endpoints` map in the shape described by
 * `EndpointSpec` can build its client with a single call:
 *
 * 	export const request = createRESTClient<Endpoints>(BASE_PATH);
 *
 * @author Gabriel Prates
 */

export type Method = 'DELETE' | 'GET' | 'PATCH' | 'POST' | 'PUT';

/**
 * The shape every entry in an endpoint map must follow. All members are
 * optional so an entry declares only what it needs (a bodyless GET has no
 * `body`, a collection-less call has no `params`, and so on).
 */
export interface EndpointSpec {
	body?: unknown;
	params?: Record<string, number | string>;
	query?: Record<string, unknown>;
	response?: unknown;
}

// Distribute over the endpoint keys to keep only the paths for one method.

type StripMethod<K, M extends string> = K extends `${M} ${infer P}` ? P : never;

type PathFor<E, M extends Method> = StripMethod<keyof E & string, M>;

// The endpoint entry for a given method + path.

type Entry<E, M extends Method, P extends string> = E[`${M} ${P}` & keyof E];

// Derive the call options from an entry, including only the keys it declares.

type OptionsFor<E> = (E extends {params: infer P} ? {params: P} : {}) &
	(E extends {query: infer Q} ? {query?: Q} : {}) &
	(E extends {body: infer B} ? {body: B} : {});

type ResponseFor<E> = E extends {response: infer R} ? R : void;

// Make `options` optional when every member is optional (e.g. query-only GETs).

type OptionsArg<E> =
	{} extends OptionsFor<E>
		? [options?: OptionsFor<E>]
		: [options: OptionsFor<E>];

type MethodFn<E, M extends Method> = <P extends PathFor<E, M>>(
	path: P,
	...args: OptionsArg<Entry<E, M, P>>
) => Promise<ResponseFor<Entry<E, M, P>>>;

export interface RequestClient<E> {
	delete: MethodFn<E, 'DELETE'>;
	get: MethodFn<E, 'GET'>;
	patch: MethodFn<E, 'PATCH'>;
	post: MethodFn<E, 'POST'>;
	put: MethodFn<E, 'PUT'>;
}

interface RequestOptions {
	body?: unknown;
	headers?: {[name: string]: string};
	params?: {[name: string]: number | string};
	query?: {[name: string]: unknown};
}

// Substitute `{name}` placeholders in a path template with param values.

function resolvePath(
	template: string,
	params: RequestOptions['params']
): string {
	if (!params) {
		return template;
	}

	return Object.entries(params).reduce(
		(path, [name, value]) =>
			path.replace(`{${name}}`, encodeURIComponent(String(value))),
		template
	);
}

// Build a `?a=1&b=2` string, expanding array values into repeated keys.

function buildQueryString(query: RequestOptions['query']): string {
	if (!query) {
		return '';
	}

	const searchParams = new URLSearchParams();

	for (const [name, value] of Object.entries(query)) {
		if (value === null || value === undefined) {
			continue;
		}

		for (const item of Array.isArray(value) ? value : [value]) {
			searchParams.append(name, String(item));
		}
	}

	const queryString = searchParams.toString();

	return queryString ? `?${queryString}` : '';
}

// Decode the body by content type; a 204 (No Content) yields `undefined`.

function parseResponse(response: Response): Promise<unknown> {
	if (response.status === 204) {
		return Promise.resolve(undefined);
	}

	const contentType = response.headers.get('content-type') || '';

	return contentType.includes('application/json')
		? response.json()
		: response.text();
}

async function send<R>(
	basePath: string,
	method: Method,
	pathTemplate: string,
	options: RequestOptions = {}
): Promise<R> {
	const hasBody = options.body !== null && options.body !== undefined;

	const response = await fetch(
		basePath +
			resolvePath(pathTemplate, options.params) +
			buildQueryString(options.query),
		{
			body: hasBody ? JSON.stringify(options.body) : undefined,
			headers: {
				Accept: 'application/json',
				...(hasBody ? {'Content-Type': 'application/json'} : {}),
				...options.headers,
			},
			method,
		}
	);

	if (!response.ok) {
		throw new Error(
			`HTTP ${response.status} ${response.statusText} calling ` +
				`${method} ${pathTemplate}: ${await response.text()}`
		);
	}

	// The runtime cannot know the response type; it is supplied by the caller
	// through the typed `RequestClient` surface, so narrow `unknown` to `R` here.

	return parseResponse(response) as Promise<R>;
}

/**
 * Builds a typed client bound to `basePath`. `E` is the endpoint map; the
 * `{[K in keyof E]: EndpointSpec}` constraint accepts any concrete map whose
 * entries follow `EndpointSpec` without requiring an index signature.
 */
export function createRESTClient<E extends {[K in keyof E]: EndpointSpec}>(
	basePath: string
): RequestClient<E> {
	return {
		delete: (path, ...args) => send(basePath, 'DELETE', path, args[0]),
		get: (path, ...args) => send(basePath, 'GET', path, args[0]),
		patch: (path, ...args) => send(basePath, 'PATCH', path, args[0]),
		post: (path, ...args) => send(basePath, 'POST', path, args[0]),
		put: (path, ...args) => send(basePath, 'PUT', path, args[0]),
	};
}
