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

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.BlurNotifier.BlurEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;


/**
 * Offers a simple Vaadin label which can be edited as a {@link NumberField}.
 *
 * @author JohannesRabauer
 */
public class EditableLabelNumberField
	extends AbstractEditableLabel<Object, EditableLabelNumberField, Double, NumberField>
{
	private Double value;
	
	public EditableLabelNumberField()
	{
		super(new NumberField());
	}
	
	@Override
	public void setValue(final Double value)
	{
		if(value == null)
		{
			this.setLabelText(this.emptyValue);
		}
		else
		{
			this.value = value;
			this.setLabelText(value.toString());
			this.getEditor().setValue(value);
		}
	}
	
	@Override
	public Double getValue()
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
		return this.getEditor().isRequiredIndicatorVisible();
	}
	
	/**
	 * Event handler delegate method for the {@link NumberField} {@link #getEditor()}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 */
	private void numberField_onBlur(final BlurEvent<NumberField> event)
	{
		if(this.getLabelText().contentEquals(this.getEditor().getValue().toString()))
		{
			this.disableEditMode();
		}
	}
	
	/**
	 * Event handler delegate method for the {@link NumberField} {@link #getEditor()}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ValueChangeListener#valueChanged(ValueChangeEvent)
	 */
	private void numberField_valueChanged(final ComponentValueChangeEvent<NumberField, Double> event)
	{
		this.value = this.getEditor().getValue();
	}
	
	@Override
	protected void btnEdit_onClick(final ClickEvent<Button> event)
	{
		this.getEditor().setValue(this.value);
		this.getEditor().focus();
		this.enableEditMode();
	}
	
	@Override
	protected void btnSave_onClick(final ClickEvent<Button> event)
	{
		this.setLabelText(this.getEditor().getValue().toString());
		
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
		this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		this.getEditor().setAutoselect(true);
		this.getEditor().addThemeVariants(TextFieldVariant.LUMO_SMALL);
		
		this.getEditor().addBlurListener(this::numberField_onBlur);
		this.getEditor().addValueChangeListener(this::numberField_valueChanged);
	}
}
