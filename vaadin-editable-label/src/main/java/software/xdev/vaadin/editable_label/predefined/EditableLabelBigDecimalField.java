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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.function.Consumer;

import com.vaadin.flow.component.textfield.BigDecimalField;

import software.xdev.vaadin.editable_label.AbstractEditableLabel;


/**
 * Offers a simple label which can be edited as a {@link BigDecimalField}.
 *
 * @author JR
 * @author AB
 */
public class EditableLabelBigDecimalField
	extends AbstractEditableLabel<EditableLabelBigDecimalField, BigDecimalField, BigDecimal>
{
	public EditableLabelBigDecimalField()
	{
		this(new BigDecimalField());
	}
	
	public EditableLabelBigDecimalField(final BigDecimalField editor)
	{
		this(editor, null);
	}
	
	public EditableLabelBigDecimalField(final Consumer<EditableLabelBigDecimalField> additionalInitActions)
	{
		this(new BigDecimalField(), additionalInitActions);
	}
	
	public EditableLabelBigDecimalField(
		final BigDecimalField editor,
		final Consumer<EditableLabelBigDecimalField> additionalInitActions)
	{
		super(editor, additionalInitActions);
	}
	
	@Override
	protected void initUI()
	{
		super.initUI();
		this.getEditor().setAutoselect(true);
	}
	
	public EditableLabelBigDecimalField withNumberFormat(final NumberFormat format)
	{
		return this.withLabelGenerator(format::format);
	}
	
}
