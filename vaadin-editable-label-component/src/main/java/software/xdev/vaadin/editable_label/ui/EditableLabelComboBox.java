package software.xdev.vaadin.editable_label.ui;

/*-
 * #%L
 * Editable labels for Vaadin
 * %%
 * Copyright (C) 2023 XDEV Software
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Collection;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.binder.HasDataProvider;
import com.vaadin.flow.data.provider.DataProvider;


/**
 * Offers a simple Vaadin label which can be edited as a {@link DatePicker}.
 *
 * @author JohannesRabauer
 */
public class EditableLabelComboBox<T>
	extends AbstractEditableLabel<Object, EditableLabelComboBox, T, ComboBox<T>>
	implements HasDataProvider<T>
{
	private T value;
	
	public EditableLabelComboBox()
	{
		super(new ComboBox<>());
	}
	
	@Override
	public void setValue(final T value)
	{
		if(value == null)
		{
			this.setLabelText(this.emptyValue);
		}
		else
		{
			this.value = value;
			this.setLabelText(this.getEditor().getItemLabelGenerator().apply(this.value));
			if(this.getLabelText().isBlank())
			{
				this.setLabelText(this.emptyValue);
			}
		}
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
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #btnEdit}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 */
	@Override
	protected void btnEdit_onClick(final ClickEvent<Button> event)
	{
		this.getEditor().setValue(this.value);
		this.getEditor().focus();
		this.enableEditMode();
	}
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #btnSave}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 */
	@Override
	protected void btnSave_onClick(final ClickEvent<Button> event)
	{
		final T oldValue = this.value;
		this.value = this.getEditor().getValue();
		this.setLabelText(
			this.getEditor().getItemLabelGenerator().apply(this.value)
		);
		this.fireChangedEvent(oldValue);
		if(this.getLabelText().isBlank())
		{
			this.setLabelText(this.emptyValue);
		}
		
		this.disableEditMode();
	}
	
	@Override
	protected void btnClose_onClick(final ClickEvent<Button> event)
	{
		this.disableEditMode();
	}
	
	@Override
	protected void initUI(
		final Component editIcon,
		final Component saveIcon,
		final Component abortIcon
	)
	{
		super.initUI(editIcon, saveIcon, abortIcon);
		// this.getEditor().setItemLabelGenerator(EditableLabelComboBox.NonNull(CaptionUtils::resolveCaption));
		this.getEditor().setWidthFull();
		this.getEditor().getElement().setAttribute("theme", "small");
		this.getEditor().addValueChangeListener(l ->
		{
			this.value = l.getValue();
		});
	}
	
	@Override
	public void setDataProvider(final DataProvider<T, ?> dataProvider)
	{
		// this.getEditor().setItems(dataProvider);
	}
	
	@Override
	public void setItems(final Collection<T> items)
	{
		this.getEditor().setItems(items);
	}
	
	@Override
	public void setItems(final T... items)
	{
		this.getEditor().setItems(items);
	}
}
