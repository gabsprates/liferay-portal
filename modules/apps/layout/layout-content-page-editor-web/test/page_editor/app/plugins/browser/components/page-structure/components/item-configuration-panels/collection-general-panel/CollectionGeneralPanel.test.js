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

import '@testing-library/jest-dom/extend-expect';
import {act, fireEvent, render, screen} from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import React, {useEffect} from 'react';

import {COLLECTION_FILTER_FRAGMENT_ENTRY_KEY} from '../../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/collectionFilterFragmentEntryKey';
import {FREEMARKER_FRAGMENT_ENTRY_PROCESSOR} from '../../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/freemarkerFragmentEntryProcessor';
import {LAYOUT_DATA_ITEM_TYPES} from '../../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/config/constants/layoutDataItemTypes';
import {StoreAPIContextProvider} from '../../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/contexts/StoreContext';
import updateItemConfig from '../../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/thunks/updateItemConfig';
import CollectionSelector from '../../../../../../../../../../src/main/resources/META-INF/resources/page_editor/common/components/CollectionSelector';
import {CollectionGeneralPanel} from '../../../../../../../../../../src/main/resources/META-INF/resources/page_editor/plugins/browser/components/page-structure/components/item-configuration-panels/collection-general-panel/CollectionGeneralPanel';

jest.mock(
	'../../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/config',
	() => ({
		config: {
			availableLanguages: {
				en_US: {
					default: false,
					displayName: 'English (United States)',
					languageIcon: 'en-us',
					languageId: 'en_US',
					w3cLanguageId: 'en-US',
				},
			},
			commonStyles: [],
			searchContainerPageMaxDelta: '50',
		},
	})
);

jest.mock(
	'../../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/services/CollectionService',
	() => ({
		getCollectionItemCount: jest.fn(() =>
			Promise.resolve({
				totalNumberOfItems: 32,
			})
		),
	})
);

jest.mock(
	'../../../../../../../../../../src/main/resources/META-INF/resources/page_editor/common/components/CollectionSelector',
	() => jest.fn(() => null)
);

jest.mock(
	'../../../../../../../../../../src/main/resources/META-INF/resources/page_editor/app/thunks/updateItemConfig',
	() => jest.fn()
);

const DEFAULT_ITEM_CONFIG = {
	collection: {
		classNameId: '22101',
		classPK: '40724',
		title: 'collection1',
	},
	displayAllItems: false,
	displayAllPages: false,
	gutters: false,
	listStyle: '',
	numberOfColumns: 1,
	numberOfItems: 5,
	numberOfItemsPerPage: 5,
	numberOfPages: 1,
	paginationType: 'numeric',
	verticalAlignment: 'start',
};

const renderComponent = ({
	itemConfig = {},
	fragmentEntryLinks = {},
	itemId = '0',
	layoutData = {},
	selectedViewportSize = 'desktop',
} = {}) => {
	return render(
		<StoreAPIContextProvider
			dispatch={() => {}}
			getState={() => ({
				fragmentEntryLinks,
				languageId: 'en_US',
				layoutData,
				permissions: {UPDATE: true},
				segmentsExperienceId: '0',
				selectedViewportSize,
			})}
		>
			<CollectionGeneralPanel
				item={{
					children: [],
					config: {...DEFAULT_ITEM_CONFIG, ...itemConfig},
					itemId,
					parentId: '',
					type: LAYOUT_DATA_ITEM_TYPES.collection,
				}}
			/>
		</StoreAPIContextProvider>
	);
};

describe('CollectionGeneralPanel', () => {
	it('allows changing the Gutter select', () => {
		renderComponent({
			itemConfig: {
				numberOfColumns: 2,
			},
		});

		const input = screen.getByLabelText('show-gutter');

		userEvent.click(input);

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: {gutters: true},
			itemId: '0',
		});
	});

	it('allows changing the Vertical Alignment select', () => {
		renderComponent();

		const input = screen.getByLabelText('vertical-alignment');

		userEvent.selectOptions(input, 'center');
		fireEvent.change(input);

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: {
				verticalAlignment: 'center',
			},
			itemId: '0',
		});
	});

	it('hides vertical alignment and layout selects when flex is selected', () => {
		renderComponent({itemConfig: {listStyle: 'flex-column'}});

		expect(
			screen.queryByLabelText('vertical-alignment')
		).not.toBeInTheDocument();
		expect(screen.queryByLabelText('layout')).not.toBeInTheDocument();
	});

	it('hides flex options when flex is not selected', () => {
		renderComponent();

		expect(screen.queryByLabelText('flex-wrap')).not.toBeInTheDocument();
		expect(screen.queryByLabelText('align-items')).not.toBeInTheDocument();
		expect(
			screen.queryByLabelText('justify-content')
		).not.toBeInTheDocument();
	});

	it('allows changing the Show Empty Collection Alert checkbox', () => {
		renderComponent();

		const input = screen.getByLabelText('show-empty-collection-alert');

		userEvent.click(input);

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: expect.objectContaining({
				emptyCollectionOptions: {
					displayMessage: false,
				},
			}),
			itemId: '0',
		});
	});

	it('allows changing the Empty Collection Alert input', () => {
		renderComponent();

		const input = screen.getByLabelText('empty-collection-alert');

		userEvent.type(input, 'Hello world!');

		act(() => {
			fireEvent.blur(input);
		});

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: expect.objectContaining({
				emptyCollectionOptions: {
					message: {
						['en_US']: 'Hello world!',
					},
				},
			}),
			itemId: '0',
		});
	});

	it('allows changing the Pagination select', () => {
		renderComponent();

		const input = screen.getByLabelText('pagination');

		userEvent.selectOptions(input, 'none');
		fireEvent.change(input);

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: {
				paginationType: 'none',
			},
			itemId: '0',
		});
	});

	it('allows changing the Display All Collection Items checkbox', async () => {
		renderComponent({itemConfig: {paginationType: 'none'}});

		const input = screen.getByLabelText('display-all-collection-items');

		await act(async () => {
			userEvent.click(input);
		});

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: expect.objectContaining({
				displayAllItems: true,
			}),
			itemId: '0',
		});
	});

	it('shows a message saying that enabling Display All Collection Items could affect performance', async () => {
		renderComponent({
			itemConfig: {
				displayAllItems: true,
				paginationType: 'none',
			},
		});

		expect(
			await screen.findByText(
				'this-setting-can-affect-page-performance-severely-if-the-number-of-collection-items-is-above-50.-we-strongly-recommend-using-pagination-instead'
			)
		).toBeInTheDocument();
	});

	it('allows changing the Display All Pages checkbox', () => {
		renderComponent();

		const input = screen.getByLabelText('display-all-pages');

		userEvent.click(input);

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: expect.objectContaining({
				displayAllPages: true,
			}),
			itemId: '0',
		});
	});

	describe('Number of Items Input', () => {
		it('allows changing input value', async () => {
			renderComponent({itemConfig: {paginationType: 'none'}});

			const input = screen.getByLabelText(
				'maximum-number-of-items-to-display'
			);

			userEvent.type(input, '3');

			await act(async () => {
				fireEvent.blur(input);
			});

			expect(updateItemConfig).toHaveBeenCalledWith({
				itemConfig: {
					numberOfItems: 3,
				},
				itemId: '0',
			});
		});

		it('shows a warning message when the number of items is bigger than the total items of the collection', async () => {
			renderComponent({
				itemConfig: {
					numberOfItems: 33,
					paginationType: 'none',
				},
			});

			expect(
				await screen.findByText(
					'the-current-number-of-items-in-this-collection-is-32'
				)
			).toBeInTheDocument();
		});

		it('shows a message saying that exceeding the default max value could affeect performance', async () => {
			renderComponent({itemConfig: {paginationType: 'none'}});

			expect(
				await screen.findByText(
					'setting-a-value-above-50-can-affect-page-performance-severely'
				)
			).toBeInTheDocument();
		});
	});

	describe('Number of Pages Input', () => {
		it('allows changing input value', () => {
			renderComponent();

			const input = screen.getByLabelText(
				'maximum-number-of-pages-to-display'
			);

			userEvent.type(input, '3');

			act(() => {
				fireEvent.blur(input);
			});

			expect(updateItemConfig).toHaveBeenCalledWith({
				itemConfig: {
					numberOfPages: 3,
				},
				itemId: '0',
			});
		});
	});

	describe('Number of Items per Page Input', () => {
		it('allows changing the input value', () => {
			renderComponent();

			const input = screen.getByLabelText(
				'maximum-number-of-items-per-page'
			);

			userEvent.type(input, '2');

			act(() => {
				fireEvent.blur(input);
			});

			expect(updateItemConfig).toHaveBeenCalledWith({
				itemConfig: {
					numberOfItemsPerPage: 2,
				},
				itemId: '0',
			});
		});

		it('shows a warning message when the number of items per page is bigger than searchContainerPageMaxDelta', async () => {
			renderComponent({
				itemConfig: {
					numberOfItemsPerPage: 53,
				},
			});

			expect(
				await screen.findByText(
					'you-can-only-display-a-maximum-of-50-items-per-page'
				)
			).toBeInTheDocument();
		});
	});

	describe('Collection Display with filtering', () => {
		let globalConfirm;

		beforeEach(() => {
			globalConfirm = window.confirm;
			window.confirm = jest.fn();
		});

		afterEach(() => {
			window.confirm = globalConfirm;
		});

		it('shows a confirmation when updating a collection linked to a filter', async () => {
			CollectionSelector.mockImplementation(
				({onPreventCollectionSelect}) => {
					useEffect(
						() => {
							onPreventCollectionSelect();
						},
						// eslint-disable-next-line react-hooks/exhaustive-deps
						[]
					);

					return <h1>Collection Selector</h1>;
				}
			);

			const {findByText} = renderComponent({
				fragmentEntryLinks: {
					'collection-filter-fragment-a': {
						editableValues: {
							[FREEMARKER_FRAGMENT_ENTRY_PROCESSOR]: {
								targetCollections: ['collection-display-a'],
							},
						},
						fragmentEntryKey: COLLECTION_FILTER_FRAGMENT_ENTRY_KEY,
					},
				},

				itemId: 'collection-display-a',
				layoutData: {
					items: {
						'collection-display-a': {
							itemId: 'collection-display-a',
							type: LAYOUT_DATA_ITEM_TYPES.collection,
						},
						'collection-filter-a': {
							config: {
								fragmentEntryLinkId:
									'collection-filter-fragment-a',
							},
							itemId: 'collection-filter-a',
							type: LAYOUT_DATA_ITEM_TYPES.fragment,
						},
					},
				},
			});

			await findByText('Collection Selector');

			expect(confirm).toHaveBeenCalledWith(
				'if-you-change-the-collection-you-unlink-the-collection-filter\n\ndo-you-want-to-continue'
			);
		});
	});

	it('allows changing Layout for a given viewport', () => {
		renderComponent({
			selectedViewportSize: 'tablet',
		});
		const input = screen.getByLabelText('layout');

		userEvent.type(input, '1');
		fireEvent.change(input);

		expect(updateItemConfig).toHaveBeenCalledWith({
			itemConfig: {
				tablet: {numberOfColumns: '1'},
			},
			itemId: '0',
		});
	});
});
