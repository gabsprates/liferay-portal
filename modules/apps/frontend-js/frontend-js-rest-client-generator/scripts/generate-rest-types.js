/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

// @ts-check

/**
 * Scaffolds and regenerates a `*-rest-client-js` package from a REST Builder
 * module's OpenAPI spec.
 *
 * It reads `rest-config.yaml` (for the base URI) and `rest-openapi.yaml` (for
 * the version, DTOs, and paths) from a `*-rest-impl` module, then:
 *
 *   - scaffolds the sibling `<name>-rest-client-js` package (package.json,
 *     node-scripts.config.js, build.gradle, src/index.ts, src/request.ts) if it
 *     does not exist yet, without clobbering files that are already present, and
 *   - (re)generates `src/types.ts` with the DTO interfaces plus the `Endpoints`
 *     map consumed by `createRESTClient`.
 *
 * Usage:
 *   node generate-rest-types.js <impl-module-dir> [client-package-dir]
 *
 * With one argument it targets the sibling `<name>-rest-client-js` package next
 * to the impl module; a second argument overrides that location.
 *
 * Example:
 *   node generate-rest-types.js \
 *     ../../headless/headless-asset-library/headless-asset-library-impl
 */

const fs = require('fs');
const path = require('path');
const yaml = require('js-yaml');

/**
 * An OpenAPI schema node, as it appears under `components.schemas`, in a
 * property, in `items`, or in a request/response body. Every field is optional
 * because a node carries only what its kind needs.
 *
 * @typedef {Object} SchemaNode
 * @property {string} [$ref]
 * @property {string} [type]
 * @property {string[]} [enum]
 * @property {SchemaNode} [items]
 * @property {Object<string, SchemaNode>} [properties]
 * @property {string[]} [required]
 * @property {SchemaNode|boolean} [additionalProperties]
 * @property {string} [description]
 * @property {boolean} [readOnly]
 * @property {boolean} [writeOnly]
 */

/**
 * A map from media type (e.g. `"application/json"`) to its schema wrapper.
 *
 * @typedef {Object<string, {schema?: SchemaNode}>} ContentMap
 */

/**
 * An OpenAPI operation parameter (`in: "path"` or `in: "query"`).
 *
 * @typedef {Object} Parameter
 * @property {string} name
 * @property {string} in
 * @property {string} [description]
 * @property {SchemaNode} [schema]
 */

/**
 * An OpenAPI operation (the value under a path + HTTP method).
 *
 * @typedef {Object} Operation
 * @property {string} [operationId]
 * @property {string} [description]
 * @property {Parameter[]} [parameters]
 * @property {{content?: ContentMap}} [requestBody]
 * @property {Object<string, {content?: ContentMap}>} [responses]
 */

/** @type {string[]} */
const METHODS = ['get', 'post', 'put', 'patch', 'delete'];

/**
 * Returns the trailing segment of a `$ref` (the schema name).
 *
 * @param {SchemaNode} node
 * @returns {string}
 */
function refName(node) {
	const segments = String(node['$ref']).split('/');

	return segments[segments.length - 1];
}

/**
 * Collapses all runs of whitespace to single spaces and trims the result.
 *
 * @param {string} text
 * @returns {string}
 */
function collapseWhitespace(text) {
	return String(text).replace(/\s+/g, ' ').trim();
}

/**
 * Builds a single-line JSDoc comment, or `null` when there is nothing to say.
 *
 * @param {string|null|undefined} description
 * @param {string|null|undefined} extra
 * @param {string} indent
 * @returns {string|null}
 */
function jsDoc(description, extra, indent) {
	const parts = [];

	if (description) {
		parts.push(collapseWhitespace(description));
	}

	if (extra) {
		parts.push(extra);
	}

	if (!parts.length) {
		return null;
	}

	return `${indent}/** ${parts.join(' — ')} */`;
}

/**
 * Maps an OpenAPI schema node to its TypeScript type expression.
 *
 * @param {SchemaNode} node
 * @param {string} indent
 * @returns {string}
 */
function tsType(node, indent) {
	if (!node || typeof node !== 'object') {
		return 'unknown';
	}

	if (node['$ref']) {
		return refName(node);
	}

	const type = node.type;

	if (node.enum && (type === 'string' || type === undefined)) {
		return node.enum.map((value) => `"${value}"`).join(' | ');
	}

	if (type === 'array') {
		const inner = tsType(node.items || {}, indent);

		return /[| ]/.test(inner) ? `(${inner})[]` : `${inner}[]`;
	}

	if (type === 'object' || (type === undefined && node.properties)) {
		if (node.properties) {
			return inlineObject(node, indent);
		}

		const additionalProperties = node.additionalProperties;

		if (additionalProperties && typeof additionalProperties === 'object') {
			return `Record<string, ${tsType(additionalProperties, indent)}>`;
		}

		return 'Record<string, unknown>';
	}

	if (type === 'string') {
		return 'string';
	}

	if (type === 'integer' || type === 'number') {
		return 'number';
	}

	if (type === 'boolean') {
		return 'boolean';
	}

	if (type === 'customField') {
		return 'CustomField';
	}

	if (type === 'permission') {
		return 'Permission';
	}

	return 'unknown';
}

/**
 * Renders an object schema as an inline TypeScript object type, one property
 * per line, sorted by name.
 *
 * @param {SchemaNode} node
 * @param {string} indent
 * @returns {string}
 */
function inlineObject(node, indent) {
	const properties = node.properties || {};
	const required = new Set(node.required || []);
	const innerIndent = indent + '\t';
	const lines = ['{'];

	for (const name of Object.keys(properties).sort()) {
		const property = properties[name];
		const optional = required.has(name) ? '' : '?';
		const markers = [];

		if (property.readOnly) {
			markers.push('read-only');
		}

		if (property.writeOnly) {
			markers.push('write-only');
		}

		const doc = jsDoc(
			property.description,
			markers.length ? markers.join(', ') : null,
			innerIndent
		);

		if (doc) {
			lines.push(doc);
		}

		const key = /^[A-Za-z_$][A-Za-z0-9_$]*$/.test(name) ? name : `"${name}"`;

		lines.push(`${innerIndent}${key}${optional}: ${tsType(property, innerIndent)};`);
	}

	lines.push(indent + '}');

	return lines.join('\n');
}

/**
 * Maps a path/query parameter to its TypeScript type.
 *
 * @param {Parameter} parameter
 * @returns {string}
 */
function paramType(parameter) {
	const type = (parameter.schema || {}).type;

	if (type === 'array') {
		return 'string[]';
	}

	if (type === 'integer' || type === 'number') {
		return 'number';
	}

	if (type === 'boolean') {
		return 'boolean';
	}

	return 'string';
}

/**
 * Resolves the TypeScript response type of an operation: an array response
 * becomes `Page<T>`, a `$ref` becomes the DTO, and no content becomes `void`.
 *
 * @param {Operation} operation
 * @returns {string}
 */
function responseType(operation) {
	const responses = operation.responses || {};

	for (const code of ['200', '201']) {
		const response = responses[code];

		if (!response) {
			continue;
		}

		const content = response.content;

		if (!content) {
			return 'void';
		}

		let schema = (content['application/json'] || {}).schema;

		if (!schema) {
			for (const [contentType, value] of Object.entries(content)) {
				if (contentType !== 'application/xml') {
					schema = value.schema;
					break;
				}
			}
		}

		schema = schema || {};

		if (schema['$ref']) {
			return refName(schema);
		}

		if (schema.type === 'array') {
			const items = schema.items || {};
			const inner = items['$ref'] ? refName(items) : tsType(items, '\t');

			return `Page<${inner}>`;
		}

		if (schema.type === 'boolean') {
			return 'boolean';
		}

		if (schema.type === 'object') {
			return tsType(schema, '\t');
		}
	}

	return 'void';
}

/**
 * Resolves the TypeScript request-body type of an operation, or `null` when the
 * operation takes no body.
 *
 * @param {Operation} operation
 * @returns {string|null}
 */
function bodyType(operation) {
	const requestBody = operation.requestBody;

	if (!requestBody) {
		return null;
	}

	const content = requestBody.content || {};
	const schema = (
		content['application/json'] ||
		content['multipart/form-data'] ||
		{}
	).schema;

	if (!schema) {
		return null;
	}

	if (schema['$ref']) {
		return refName(schema);
	}

	if (schema.type === 'array') {
		const items = schema.items || {};
		const inner = items['$ref'] ? refName(items) : tsType(items, '\t');

		return `${inner}[]`;
	}

	return tsType(schema, '\t');
}

/**
 * Writes a file only when it does not already exist, creating parent
 * directories as needed.
 *
 * @param {string} filePath
 * @param {string} contents
 * @returns {boolean} `true` when the file was written, `false` when it existed.
 */
function writeIfAbsent(filePath, contents) {
	if (fs.existsSync(filePath)) {
		return false;
	}

	fs.mkdirSync(path.dirname(filePath), {recursive: true});
	fs.writeFileSync(filePath, contents);

	return true;
}

const LICENSE_HEADER =
	'/**\n' +
	' * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com\n' +
	' * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06\n' +
	' */\n';

/**
 * Scaffolds the package boilerplate, skipping any file already present.
 *
 * @param {string} clientDir Absolute path to the `*-rest-client-js` package.
 * @param {string} packageName The npm name, e.g. `@liferay/foo-rest-client-js`.
 * @param {string} title Human-readable API title for the client JSDoc.
 * @returns {string[]} The relative paths of the files that were created.
 */
function scaffold(clientDir, packageName, title) {
	const created = [];

	if (
		writeIfAbsent(
			path.join(clientDir, 'package.json'),
			JSON.stringify(
				{
					dependencies: {'@liferay/frontend-js-rest-client-generator': '*'},
					devDependencies: {
						'@types/node': '^12',
						typescript: '^4.0 || ^5.0',
					},
					main: 'src/index.ts',
					name: packageName,
					private: true,
					type: 'commonjs',
					version: '1.0.0',
				},
				null,
				'\t'
			) + '\n'
		)
	) {
		created.push('package.json');
	}

	if (
		writeIfAbsent(
			path.join(clientDir, 'node-scripts.config.js'),
			LICENSE_HEADER + "\nmodule.exports = {\n\tmain: './src/index.ts',\n};\n"
		)
	) {
		created.push('node-scripts.config.js');
	}

	if (writeIfAbsent(path.join(clientDir, 'build.gradle'), '')) {
		created.push('build.gradle');
	}

	if (
		writeIfAbsent(
			path.join(clientDir, 'src', 'index.ts'),
			LICENSE_HEADER + "\nexport * from './request';\nexport * from './types';\n"
		)
	) {
		created.push('src/index.ts');
	}

	if (
		writeIfAbsent(
			path.join(clientDir, 'src', 'request.ts'),
			LICENSE_HEADER +
				"\nimport {createRESTClient} from '@liferay/frontend-js-rest-client-generator';\n\n" +
				"import {BASE_PATH} from './types';\n\n" +
				"import type {Endpoints} from './types';\n\n" +
				'/**\n' +
				` * Typed client for the ${title}. Each method is scoped to the paths valid\n` +
				' * for that HTTP verb, and the options and response are inferred from the\n' +
				' * matching `Endpoints` entry.\n' +
				' */\n' +
				'export const request = createRESTClient<Endpoints>(BASE_PATH);\n'
		)
	) {
		created.push('src/request.ts');
	}

	return created;
}

/**
 * Scaffolds (if needed) and regenerates the client package for the given impl
 * module.
 *
 * @param {string} implDir Absolute path to the `*-rest-impl` module.
 * @param {string} clientDir Absolute path to the target client package.
 * @returns {void}
 */
function generate(implDir, clientDir) {
	const config = yaml.load(
		fs.readFileSync(path.join(implDir, 'rest-config.yaml'), 'utf8')
	);
	const spec = yaml.load(
		fs.readFileSync(path.join(implDir, 'rest-openapi.yaml'), 'utf8')
	);

	const schemas = (spec.components && spec.components.schemas) || {};
	const paths = spec.paths || {};
	const version = (spec.info && spec.info.version) || 'v1.0';
	const title = (spec.info && spec.info.title) || 'REST';
	const basePath = `/o${config.application.baseURI}/${version}`;
	const implName = path.basename(implDir);

	const created = scaffold(
		clientDir,
		`@liferay/${path.basename(clientDir)}`,
		title
	);

	if (created.length) {
		process.stdout.write(`Scaffolded ${path.basename(clientDir)}: ${created.join(', ')}\n`);
	}

	const outputFile = path.join(clientDir, 'src', 'types.ts');

	const lines = [];

	lines.push('/**');
	lines.push(' * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com');
	lines.push(' * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06');
	lines.push(' */');
	lines.push('');
	lines.push('/**');
	lines.push(` * TypeScript definitions for the ${title} REST API (version ${version}).`);
	lines.push(' *');
	lines.push(` * @generated by the @liferay/frontend-js-rest-client-generator type generator from`);
	lines.push(` * ${implName}/rest-openapi.yaml. Do not edit by hand.`);
	lines.push(' */');
	lines.push('');
	lines.push(`export const BASE_PATH = "${basePath}";`);
	lines.push('');
	lines.push('// Envelope & Liferay extension types');
	lines.push('');
	lines.push('/** Paginated collection envelope returned for list (array) endpoints. */');
	lines.push('export interface Page<T> {');
	lines.push('\tactions?: Record<string, Record<string, string>>;');
	lines.push('\tfacets?: unknown[];');
	lines.push('\titems: T[];');
	lines.push('\tlastPage: number;');
	lines.push('\tpage: number;');
	lines.push('\tpageSize: number;');
	lines.push('\ttotalCount: number;');
	lines.push('}');
	lines.push('');
	lines.push('/** Liferay expando/custom field (not formally defined in the OpenAPI schema). */');
	lines.push('export interface CustomField {');
	lines.push('\tcustomValue?: {data?: unknown};');
	lines.push('\tdataType?: string;');
	lines.push('\tfieldType?: string;');
	lines.push('\tlabel?: string;');
	lines.push('\tname?: string;');
	lines.push('\tnestedFields?: Record<string, unknown>;');
	lines.push('}');
	lines.push('');
	lines.push('/** Liferay permission entry (not formally defined in the OpenAPI schema). */');
	lines.push('export interface Permission {');
	lines.push('\tactionIds?: string[];');
	lines.push('\troleName?: string;');
	lines.push('}');
	lines.push('');
	lines.push('// DTOs (components.schemas)');
	lines.push('');

	for (const name of Object.keys(schemas).sort()) {
		const schema = schemas[name];
		const properties = schema.properties || {};
		const required = new Set(schema.required || []);
		const doc = jsDoc(schema.description, null, '');

		if (doc) {
			lines.push(doc);
		}

		lines.push(`export interface ${name} {`);

		for (const propertyName of Object.keys(properties).sort()) {
			const property = properties[propertyName];
			const optional = required.has(propertyName) ? '' : '?';
			const markers = [];

			if (property.readOnly) {
				markers.push('read-only');
			}

			if (property.writeOnly) {
				markers.push('write-only');
			}

			const propertyDoc = jsDoc(
				property.description,
				markers.length ? markers.join(', ') : null,
				'\t'
			);

			if (propertyDoc) {
				lines.push(propertyDoc);
			}

			const key = /^[A-Za-z_$][A-Za-z0-9_$]*$/.test(propertyName)
				? propertyName
				: `"${propertyName}"`;

			lines.push(`\t${key}${optional}: ${tsType(property, '\t')};`);
		}

		lines.push('}');
		lines.push('');
	}

	lines.push('// Endpoints — keyed by `METHOD <path>` (prefix paths with BASE_PATH).');
	lines.push('');
	lines.push('export interface Endpoints {');

	for (const apiPath of Object.keys(paths).sort()) {
		const operations = paths[apiPath];

		for (const method of METHODS) {
			const operation = operations[method];

			if (!operation || typeof operation !== 'object') {
				continue;
			}

			const parameters = operation.parameters || [];
			const pathParameters = parameters
				.filter((parameter) => parameter.in === 'path')
				.sort((a, b) => a.name.localeCompare(b.name));
			const queryParameters = parameters
				.filter((parameter) => parameter.in === 'query')
				.sort((a, b) => a.name.localeCompare(b.name));

			const operationId = operation.operationId;
			const doc = jsDoc(
				operation.description,
				operationId ? `operationId: ${operationId}` : null,
				'\t'
			);

			if (doc) {
				lines.push(doc);
			}

			lines.push(`\t"${method.toUpperCase()} ${apiPath}": {`);

			if (pathParameters.length) {
				lines.push('\t\tparams: {');

				for (const parameter of pathParameters) {
					lines.push(`\t\t\t${parameter.name}: ${paramType(parameter)};`);
				}

				lines.push('\t\t};');
			}

			if (queryParameters.length) {
				lines.push('\t\tquery: {');

				for (const parameter of queryParameters) {
					const parameterDoc = jsDoc(parameter.description, null, '\t\t\t');

					if (parameterDoc) {
						lines.push(parameterDoc);
					}

					lines.push(`\t\t\t${parameter.name}?: ${paramType(parameter)};`);
				}

				lines.push('\t\t};');
			}

			const body = bodyType(operation);

			if (body) {
				lines.push(`\t\tbody: ${body};`);
			}

			lines.push(`\t\tresponse: ${responseType(operation)};`);
			lines.push('\t};');
		}
	}

	lines.push('}');
	lines.push('');
	lines.push('export type EndpointKey = keyof Endpoints;');
	lines.push('');

	fs.writeFileSync(outputFile, lines.join('\n'));

	const endpointCount = lines.filter((line) =>
		/^\t"(DELETE|GET|PATCH|POST|PUT) /.test(line)
	).length;

	process.stdout.write(
		`Generated ${outputFile}\n` +
			`  ${Object.keys(schemas).length} DTOs, ${endpointCount} endpoints, ` +
			`base path ${basePath}\n`
	);
}

/**
 * Parses the CLI arguments and runs the generator.
 *
 * @returns {void}
 */
function main() {
	const [implDirArg, clientDirArg] = process.argv.slice(2);

	if (!implDirArg) {
		process.stderr.write(
			'Usage: node generate-rest-types.js <impl-module-dir> [client-package-dir]\n'
		);
		process.exit(1);
	}

	const implDir = path.resolve(implDirArg);
	const base = path.basename(implDir).replace(/-impl$/, '');
	const clientDir = clientDirArg
		? path.resolve(clientDirArg)
		: path.join(path.dirname(implDir), `${base}-rest-client-js`);

	generate(implDir, clientDir);
}

main();
