/*
 * Copyright © 2023 XDEV Software (https://xdev.software)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package software.xdev.vaadin.editable_label.predefined;

import java.util.Collection;
import java.util.function.Consumer;

import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.dataview.ComboBoxDataView;
import com.vaadin.flow.component.combobox.dataview.ComboBoxLazyDataView;
import com.vaadin.flow.component.combobox.dataview.ComboBoxListDataView;
import com.vaadin.flow.data.provider.BackEndDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.HasDataView;
import com.vaadin.flow.data.provider.HasLazyDataView;
import com.vaadin.flow.data.provider.HasListDataView;
import com.vaadin.flow.data.provider.InMemoryDataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.SerializableFunction;

import software.xdev.vaadin.editable_label.AbstractEditableLabel;


/**
 * Offers a simple label which can be edited as a {@link ComboBox}.
 *
 * @author AB
 * @author JR
 */
public class EditableLabelComboBox<T>
	extends AbstractEditableLabel<EditableLabelComboBox<T>, ComboBox<T>, T>
	implements
	HasDataView<T, String, ComboBoxDataView<T>>,
	HasListDataView<T, ComboBoxListDataView<T>>,
	HasLazyDataView<T, String, ComboBoxLazyDataView<T>>
{
	public EditableLabelComboBox()
	{
		this(new ComboBox<>());
	}
	
	public EditableLabelComboBox(final ComboBox<T> editor)
	{
		this(editor, null);
	}
	
	public EditableLabelComboBox(final Consumer<EditableLabelComboBox<T>> additionalInitActions)
	{
		this(new ComboBox<>(), additionalInitActions);
	}
	
	public EditableLabelComboBox(
		final ComboBox<T> editor,
		final Consumer<EditableLabelComboBox<T>> additionalInitActions)
	{
		super(editor, additionalInitActions);
	}
	
	@Override
	protected void initUI()
	{
		super.initUI();
		// Open ComboBox when in edit mode
		this.addEditModeChangedListener(ev -> this.getEditor().setOpened(ev.isEditModeEnabled()));
	}
	
	@Override
	public EditableLabelComboBox<T> withNativeLabelGenerator(final ItemLabelGenerator<T> labelGenerator)
	{
		super.withNativeLabelGenerator(labelGenerator);
		this.getEditor().setItemLabelGenerator(labelGenerator);
		return this.self();
	}
	
	public EditableLabelComboBox<T> withItems(final T... items)
	{
		return this.withItems(DataProvider.ofItems(items));
	}
	
	public EditableLabelComboBox<T> withItems(final Collection<T> items)
	{
		return this.withItems(DataProvider.ofCollection(items));
	}
	
	public EditableLabelComboBox<T> withItems(final ListDataProvider<T> items)
	{
		this.setItems(items);
		return this.self();
	}
	
	@Override
	public ComboBoxListDataView<T> setItems(final ListDataProvider<T> dataProvider)
	{
		return this.getEditor().setItems(dataProvider);
	}
	
	@Override
	public ComboBoxListDataView<T> getListDataView()
	{
		return this.getEditor().getListDataView();
	}
	
	@Override
	public ComboBoxDataView<T> setItems(final DataProvider<T, String> dataProvider)
	{
		return this.getEditor().setItems(dataProvider);
	}
	
	/**
	 * @see ComboBox#setItems(InMemoryDataProvider, SerializableFunction)
	 * @deprecated See upstream
	 */
	@Deprecated(forRemoval = true)
	@Override
	public ComboBoxDataView<T> setItems(final InMemoryDataProvider<T> dataProvider)
	{
		return this.getEditor().setItems(dataProvider);
	}
	
	@Override
	public ComboBoxDataView<T> getGenericDataView()
	{
		return this.getEditor().getGenericDataView();
	}
	
	@Override
	public ComboBoxLazyDataView<T> setItems(final BackEndDataProvider<T, String> dataProvider)
	{
		return this.getEditor().setItems(dataProvider);
	}
	
	@Override
	public ComboBoxLazyDataView<T> getLazyDataView()
	{
		return this.getEditor().getLazyDataView();
	}
}
