/* eslint no-spaced-func: 0 */

import {Config} from 'metal-state';
import {EventHandler} from 'metal-events';
import {PagesVisitor} from './visitors.es';
import PortletBase from 'frontend-js-web/liferay/PortletBase.es';
import FormSupport from '../components/Form/FormSupport.es';

class StateSyncronizer extends PortletBase {
	static STATE = {
		descriptionEditor: Config.any(),
		layoutProvider: Config.any(),
		localizedDescription: Config.object().value({}),
		localizedName: Config.object().value({}),
		nameEditor: Config.any(),
		settingsDDMForm: Config.any(),
		translationManager: Config.any()
	};

	created() {
		const {descriptionEditor, nameEditor} = this;
		this._eventHandler = new EventHandler();

		this._eventHandler.add(
			descriptionEditor.on('change', this._handleDescriptionEditorChanged.bind(this)),
			nameEditor.on('change', this._handleNameEditorChanged.bind(this))
		);
	}

	disposeInternal() {
		this._eventHandler.removeAllListeners();
	}

	getState() {
		const {layoutProvider, localizedDescription, translationManager} = this;

		return {
			availableLanguageIds: translationManager.get('availableLocales'),
			defaultLanguageId: translationManager.get('defaultLocale'),
			description: localizedDescription,
			name: this._getLocalizedName(),
			pages: layoutProvider.state.pages,
			paginationMode: layoutProvider.state.paginationMode,
			rules: [],
			successPageSettings: {
				body: {},
				enabled: false,
				title: {}
			}
		};
	}

	isEmpty() {
		const {layoutProvider} = this;

		return FormSupport.emptyPages(layoutProvider.state.pages);
	}

	syncInputs() {
		const {settingsDDMForm} = this;
		const state = this.getState();
		const {
			description,
			name
		} = state;

		const publishedField = settingsDDMForm.getField('published');

		publishedField.set('value', this.published);

		this.one('#name').value = JSON.stringify(name);
		this.one('#description').value = JSON.stringify(description);
		this.one('#serializedFormBuilderContext').value = this._getSerializedFormBuilderContext();
		this.one('#serializedSettingsContext').value = JSON.stringify(settingsDDMForm.toJSON());
	}

	_getLocalizedName() {
		const {localizedName, translationManager} = this;
		const defaultLocale = translationManager.get('defaultLocale');

		if (!localizedName[defaultLocale].trim()) {
			localizedName[defaultLocale] = Liferay.Language.get('untitled-form');
		}

		return localizedName;
	}

	_getSerializedFormBuilderContext() {
		const state = this.getState();

		const visitor = new PagesVisitor(state.pages);

		return JSON.stringify(
			{
				...state,
				pages: visitor.mapPages(
					page => {
						return {
							...page,
							description: page.localizedDescription,
							title: page.localizedTitle
						};
					}
				)
			}
		);
	}

	_handleDescriptionEditorChanged(event) {
		const {descriptionEditor, localizedDescription, translationManager} = this;
		const editor = window[descriptionEditor.name];

		localizedDescription[translationManager.get('editingLocale')] = editor.getHTML();
	}

	_handleNameEditorChanged(event) {
		const {localizedName, nameEditor, translationManager} = this;
		const editor = window[nameEditor.name];

		localizedName[translationManager.get('editingLocale')] = editor.getHTML();
	}
}

export default StateSyncronizer;