---

allowed-tools: [Artifact, Bash, Glob, Grep, Read, Write]
argument-hint: "<component-name>"
description: Map every place the repository builds a given UI component, across React, taglib, and markup-class usage, and publish the result as a browsable Artifact. Use when the user asks for a usage atlas, a component inventory, or wants to know where and how a Clay component is used before migrating or deprecating it.
name: ui-component-atlas

---

# UI Component Atlas

Produce a single self-contained HTML Artifact mapping every place `liferay-portal` builds one UI component, in the three ways it can be built: React consumers of the `@clayui/*` package, the `<clay:*>` JSP taglibs, and bare CSS classes written straight into markup.

The component is given as the argument, for example `/ui-component-atlas drop-down`. Resolve its Clay package under `modules/apps/frontend-js/frontend-js-clay-web/clay/clay-*` before starting.

## Discover The Vocabulary

Do this before counting anything. The three surfaces rarely share a spelling, and hardcoding a guess is what makes this exercise wrong on every component except `card`.

Derive each vocabulary from the source:

1. **React surface** — read `modules/apps/frontend-js/frontend-js-clay-web/clay/clay-${PACKAGE}/src` and list the real exported names: default export, named exports, and dotted subcomponents such as `ClayCard.Body`. The npm package name, the directory name, and the exported name often differ.

1. **Taglib surface** — grep the tag names in `modules/apps/frontend-taglib/frontend-taglib-clay/src/main/resources/META-INF/liferay-clay.tld` for the component word in *any* position. It may lead, trail, or stand alone: `<clay:alert>`, `<clay:image-card>`, `<clay:management-toolbar>`.

1. **CSS surface** — find the real class prefix by grepping selectors in `modules/apps/frontend-js/frontend-js-clay-web/clay/clay-css/src/scss`. The SCSS prefix frequently differs from the package name; `clay-drop-down` ships `.dropdown-*`. Settle on the prefix the stylesheets actually define, and state in the output which prefix was chosen.

Report the three resolved vocabularies before scanning, so the scope is auditable.

`drop-down` is the honest self-test: package `clay-drop-down`, export `ClayDropDown`, classes `dropdown-*`. A run that reports all three with the same spelling skipped this step.

## Scan

Treat the three as independent detection kinds. A file may land in more than one; that overlap is a finding, not a problem.

- **react** — an `import` or `require` of `@clayui/${PACKAGE}`, or a `<Clay…>` element from the resolved list, in `.js .jsx .ts .tsx`. Record dotted subcomponents per file.
- **taglib** — any resolved tag name in `.jsp .jspf .ftl .html`.
- **markup-class** — tokens matching the resolved prefix, bare and `prefix-*`, inside a `class`, `className`, or `cssClass` attribute, in `.jsp .jspf .ftl .html .jsx .tsx .vm .tpl .soy`.

Exclude `node_modules`, build output, and binary files. Record per file: path, module, owning app (the `modules/apps/<area>` or `workspaces/<workspace>` owner), layer (`modules/apps`, `modules/dxp/apps`, `workspaces`, `portal-web`), extension, kinds, and per-name counts.

Then classify every class found. It is **clay** when it appears as a selector anywhere in the clay-css SCSS source, and **custom** otherwise — an app-local invention that merely shares the prefix. This split is the sharpest signal in the atlas; get it right.

Collect dependency drift alongside: every module whose own `package.json` names `@clayui/${PACKAGE}`, with the declared range, grouped by range.

## Build The Page

One HTML file, all data inline in a `<script type="application/json">` payload, all rendering driven from that payload. No external data, no build step.

Sections, in order:

- **Masthead** — what was scanned, plus headline stats: files, total usages, per-kind counts.
- **Lanes** — one per detection kind: count, file count, a one-line characterization, and a representative signature.
- **Owner × approach matrix** — files per app per kind, fill depth computed within each column, the number always printed. Clicking a cell loads that slice into the explorer.
- **Vocabulary** — every distinct component, taglib, and class name found, as clickable chips that filter the explorer. Render clay-defined and app-local classes distinctly, the app-local ones dashed; that contrast is the point of the section.
- **Overlap** — the files mixing more than one approach, where migrating one approach would strand another.
- **Explorer** — every file, with search over path, module, and token; kind toggles; layer and app filters; sort; and expandable rows showing the exact names each file uses.
- **Dependency drift** — the declared ranges side by side.
- **Methodology footer** — the detection rules verbatim, the clay-vs-app-local rule, and the repo, branch, commit, and commit date scanned.

State plainly in that footer what is **not** covered: class names assembled at runtime (string concatenation, `classNames()` variables, Java-generated markup), `.scss` overrides, `node_modules`, and Java sources. Counts are lower bounds, not a census of rendered components.

## Constraints

- Every number on the page comes from the scan. When something cannot be determined, say so rather than estimating.
- Theme-aware in light and dark, responsive, with wide content scrolling inside its own container.
- Title the Artifact `<Component> Usage Atlas`.
- Publish it and hand back the URL. Invoking this skill is the request to publish.
