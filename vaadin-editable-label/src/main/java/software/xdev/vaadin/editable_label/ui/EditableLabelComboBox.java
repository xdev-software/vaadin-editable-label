/*
 * Copyright © 2023 XDEV Software (https://xdev.software/en)
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
package software.xdev.vaadin.editable_label.ui;



import java.util.Collection;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.provider.DataProvider;


/**
 * Offers a simple Vaadin label which can be edited as a {@link DatePicker}.
 *
 * @author JohannesRabauer
 */
public class EditableLabelComboBox<T>
	extends AbstractEditableLabel<Object, EditableLabelComboBox<T>, T, ComboBox<T>>
{
	private T value;
	
	public EditableLabelComboBox()
	{
		super(new ComboBox<>());
	}
	
	public EditableLabelComboBox(final T selectedValue, final Collection<T> selectableItems)
	{
		this();
		this.setItems(selectableItems);
		this.setValue(selectedValue);
	}
	
	public EditableLabelComboBox(final T selectedValue, final T... selectableItems)
	{
		this();
		this.setItems(selectableItems);
		this.setValue(selectedValue);
	}
	
	public EditableLabelComboBox(final T selectedValue, final String emptyValue, final Collection<T> selectableItems)
	{
		super(new ComboBox<>(), emptyValue);
		this.setItems(selectableItems);
		this.setValue(selectedValue);
	}
	
	public EditableLabelComboBox(final T selectedValue, final String emptyValue, final T... selectableItems)
	{
		super(new ComboBox<>(), emptyValue);
		this.setItems(selectableItems);
		this.setValue(selectedValue);
	}
	
	@Override
	public void setValue(final T value)
	{
		final T oldValue = this.value;
		this.value = value;
		
		if(value == null)
		{
			this.setLabelText(this.emptyValue);
		}
		else
		{
			this.setLabelText(this.getEditor().getItemLabelGenerator().apply(this.value));
			if(this.getLabelText().isBlank())
			{
				this.setLabelText(this.emptyValue);
			}
		}
		this.fireChangedEvent(oldValue);
	}
	
	@Override
	public T getValue()
	{
		return this.value;
	}
	
	@Override
	public void setRequiredIndicatorVisible(final boolean requiredIndicatorVisible)
	{
		this.getEditor().setRequiredIndicatorVisible(requiredIndicatorVisible);
	}
	
	@Override
	public boolean isRequiredIndicatorVisible()
	{
		return false;
	}
	
	public void setItemLabelGenerator(
		final ItemLabelGenerator<T> itemLabelGenerator)
	{
		this.getEditor().setItemLabelGenerator(itemLabelGenerator);
	}

	@Override
	protected void btnEdit_onClick(final ClickEvent<Button> event)
	{
		this.getEditor().setValue(this.value);
		this.getEditor().focus();
		this.getEditor().setOpened(true);
		this.enableEditMode();
	}

	@Override
	protected void btnSave_onClick(final ClickEvent<Button> event)
	{
		this.setValue(this.value);
		this.disableEditMode();
	}
	
	@Override
	protected void btnClose_onClick(final ClickEvent<Button> event)
	{
		this.disableEditMode();
	}
	
	public void setDataProvider(final DataProvider<T, String> dataProvider)
	{
		this.getEditor().setItems(dataProvider);
	}
	
	public void setItems(final Collection<T> items)
	{
		this.getEditor().setItems(items);
	}
	
	public void setItems(final T... items)
	{
		this.getEditor().setItems(items);
	}
}
