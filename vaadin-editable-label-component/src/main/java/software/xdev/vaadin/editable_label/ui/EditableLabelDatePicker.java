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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;


/**
 * Offers a simple Vaadin label which can be edited as a {@link DatePicker}.
 *
 * @author JohannesRabauer
 */
public class EditableLabelDatePicker
	extends AbstractEditableLabel<Object, EditableLabelDatePicker, LocalDate, DatePicker>
{
	private final DateTimeFormatter dateTimeFormatter;
	private LocalDate localDate;
	
	/**
	 *
	 */
	public EditableLabelDatePicker()
	{
		super(new DatePicker());
		this.dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		this.getEditor().getElement().setAttribute("theme", "small");
	}
	
	@Override
	public void setValue(final LocalDate value)
	{
		if(value == null)
		{
			this.setLabelText(this.emptyValue);
		}
		else
		{
			this.localDate = value;
			this.setLabelText(this.localDate.format(this.dateTimeFormatter));
			this.getEditor().setValue(value);
		}
	}
	
	@Override
	public LocalDate getValue()
	{
		return this.getEditor().getValue();
	}
	
	@Override
	public void setRequiredIndicatorVisible(final boolean requiredIndicatorVisible)
	{
		this.getEditor().setRequiredIndicatorVisible(requiredIndicatorVisible);
	}
	
	@Override
	public boolean isRequiredIndicatorVisible()
	{
		return this.getEditor().isRequiredIndicatorVisible();
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
		this.getEditor().setValue(this.localDate);
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
		final LocalDate oldValue = this.getOldValue();
		final LocalDate selectedDate = this.getEditor().getValue();
		if(selectedDate != null)
		{
			this.setLabelText(selectedDate.format(this.dateTimeFormatter));
			this.localDate = selectedDate;
		}
		
		this.fireChangedEvent(oldValue);
		
		this.disableEditMode();
	}
	
	private LocalDate getOldValue()
	{
		if(this.getLabelText() == null || this.getLabelText().isBlank())
		{
			return null;
		}
		else
		{
			try
			{
				this.dateTimeFormatter.parse(this.getLabelText());
				return LocalDate.parse(this.getLabelText(), this.dateTimeFormatter);
			}
			catch(final Exception e)
			{
				// There is no clean way to check if the String is a parsable Date.
				// So if there is an exception the String is declared as not-parsable.
				return null;
			}
		}
	}
	
	@Override
	protected void btnClose_onClick(final ClickEvent<Button> event)
	{
		this.disableEditMode();
	}
	
	private void datePicker_valueChanged(final ComponentValueChangeEvent<DatePicker, LocalDate> event)
	{
		this.localDate = this.getEditor().getValue();
	}
	
	@Override
	protected void initUI(
		final Component editIcon,
		final Component saveIcon,
		final Component abortIcon
	)
	{
		super.initUI(editIcon, saveIcon, abortIcon);
		this.setAlignItems(Alignment.START);
		this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		
		this.getEditor().setSizeUndefined();
		
		this.setHeight("44px");
		
		this.getEditor().addValueChangeListener(this::datePicker_valueChanged);
	}
	
}
