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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

import com.vaadin.flow.component.datepicker.DatePicker;

import software.xdev.vaadin.editable_label.AbstractEditableLabel;


/**
 * Offers a simple label which can be edited as a {@link DatePicker}.
 *
 * @author JR
 * @author AB
 */
public class EditableLabelDatePicker
	extends AbstractEditableLabel<EditableLabelDatePicker, DatePicker, LocalDate>
{
	public EditableLabelDatePicker()
	{
		this(new DatePicker());
	}
	
	public EditableLabelDatePicker(final DatePicker editor)
	{
		this(editor, null);
	}
	
	public EditableLabelDatePicker(final Consumer<EditableLabelDatePicker> additionalInitActions)
	{
		this(new DatePicker(), additionalInitActions);
	}
	
	public EditableLabelDatePicker(
		final DatePicker editor,
		final Consumer<EditableLabelDatePicker> additionalInitActions)
	{
		super(editor, additionalInitActions);
	}
	
	public EditableLabelDatePicker withTryUseI18NFormat()
	{
		final DatePicker.DatePickerI18n i18n = this.editor.getI18n();
		if(i18n != null && !i18n.getDateFormats().isEmpty())
		{
			this.withLabelGenerator(DateTimeFormatter.ofPattern(i18n.getDateFormats().get(0))::format);
		}
		return this.self();
	}
}
