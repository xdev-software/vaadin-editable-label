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

import java.util.function.Consumer;

import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import software.xdev.vaadin.editable_label.AbstractEditableLabel;


/**
 * Offers a simple label which can be edited as a {@link NumberField}.
 *
 * @author JR
 * @author AB
 */
public class EditableLabelNumberField
	extends AbstractEditableLabel<EditableLabelNumberField, NumberField, Double>
{
	public EditableLabelNumberField()
	{
		this(new NumberField());
	}
	
	public EditableLabelNumberField(final NumberField editor)
	{
		this(editor, null);
	}
	
	public EditableLabelNumberField(final Consumer<EditableLabelNumberField> additionalInitActions)
	{
		this(new NumberField(), additionalInitActions);
	}
	
	public EditableLabelNumberField(
		final NumberField editor,
		final Consumer<EditableLabelNumberField> additionalInitActions)
	{
		super(editor, additionalInitActions);
	}
	
	@Override
	protected void initUI()
	{
		super.initUI();
		this.getEditor().setAutoselect(true);
		this.getEditor().addThemeVariants(TextFieldVariant.LUMO_SMALL);
	}
}
