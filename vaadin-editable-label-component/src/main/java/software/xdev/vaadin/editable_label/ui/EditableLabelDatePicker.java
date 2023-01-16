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



import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
	public final static String DEFAULT_DATE_TIME_FORMAT_PATTERN = "dd.MM.yyyy";
	private final DateTimeFormatter dateTimeFormatter;
	private final String dateTimeFormatPattern;
	private LocalDate localDate;
	
	public EditableLabelDatePicker()
	{
		this(DEFAULT_DATE_TIME_FORMAT_PATTERN);
	}
	
	/**
	 * @param dateTimeFormatter used to format the selected date
	 */
	public EditableLabelDatePicker(final String dateTimeFormatPattern)
	{
		super(new DatePicker());
		this.dateTimeFormatPattern = dateTimeFormatPattern;
		this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatPattern);
	}
	
	/**
	 * @param value that is first displayed in the label
	 */
	public EditableLabelDatePicker(final LocalDate value)
	{
		this();
		this.setValue(value);
	}
	
	/**
	 * @param value             that is first displayed in the label
	 * @param emptyValue        that is displayed if no value is defined (at any time, now or in the future)
	 * @param dateTimeFormatter used to format the selected date
	 */
	public EditableLabelDatePicker(
		final LocalDate value,
		final String emptyLabel,
		final String dateTimeFormatPattern)
	{
		super(new DatePicker(), emptyLabel);
		this.dateTimeFormatPattern = dateTimeFormatPattern;
		this.dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormatPattern);
		this.setValue(value);
	}
	
	/**
	 * @param value             that is first displayed in the label
	 * @param dateTimeFormatter used to format the selected date
	 */
	public EditableLabelDatePicker(final LocalDate value, final String dateTimeFormatPattern)
	{
		this(dateTimeFormatPattern);
		this.setValue(value);
	}
	
	@Override
	public void setValue(final LocalDate value)
	{
		final LocalDate oldValue = this.localDate;
		this.localDate = value;
		this.getEditor().setValue(value);
		
		if(value == null)
		{
			this.setLabelText(this.emptyValue);
		}
		else
		{
			final DatePicker.DatePickerI18n datePickerI18n = new DatePicker.DatePickerI18n();
			datePickerI18n.setDateFormat(this.dateTimeFormatPattern);
			this.getEditor().setI18n(datePickerI18n);
			this.setLabelText(this.dateTimeFormatter.format(this.getEditor().getValue()));
		}
		this.fireChangedEvent(oldValue);
	}
	
	@Override
	public LocalDate getValue()
	{
		return this.localDate;
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
		this.setValue(this.getEditor().getValue());
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
		this.setAlignItems(Alignment.START);
		this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		
		this.getEditor().setSizeUndefined();
		
		this.setHeight("44px");
	}
	
}
