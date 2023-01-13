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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Objects;

import com.vaadin.flow.component.BlurNotifier.BlurEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextFieldVariant;


/**
 * Offers a simple Vaadin label which can be edited as a {@link BigDecimalField}.
 *
 * @author JohannesRabauer
 */
public class EditableLabelBigDecimalField
	extends AbstractEditableLabel<Object, EditableLabelBigDecimalField, BigDecimal, BigDecimalField>
{
	
	private BigDecimal value;
	private NumberFormat currencyFormatter;
	
	public EditableLabelBigDecimalField()
	{
		super(new BigDecimalField());
	}
	
	public EditableLabelBigDecimalField(final NumberFormat currencyFormatter)
	{
		this();
		Objects.requireNonNull(this.currencyFormatter = currencyFormatter);
	}
	
	@Override
	public void setValue(final BigDecimal value)
	{
		if(value == null)
		{
			this.setLabelText(this.emptyValue);
		}
		else
		{
			if(this.currencyFormatter != null)
			{
				this.setLabelText(this.currencyFormatter.format(value));
			}
			else
			{
				this.setLabelText(value.toPlainString());
			}
			
			this.getEditor().setValue(value);
		}
		this.value = value;
	}
	
	@Override
	public BigDecimal getValue()
	{
		return this.value;
	}
	
	public void setCurrency(final NumberFormat currencyFormatter)
	{
		this.currencyFormatter = currencyFormatter;
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
	 * Event handler delegate method for the {@link BigDecimalField} {@link #textField}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 */
	private void textField_onBlur(final BlurEvent<BigDecimalField> event)
	{
		if(this.getLabelText().contentEquals(this.getEditor().getValue().toPlainString()))
		{
			this.disableEditMode();
		}
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
		final BigDecimal oldValue = this.value;
		this.setValue(this.getEditor().getValue());
		
		this.fireChangedEvent(oldValue);
		
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
		
		this.getEditor().setAutoselect(true);
		this.getEditor().addThemeVariants(TextFieldVariant.LUMO_SMALL);
		
		this.getEditor().setWidthFull();
		this.getEditor().setHeight(null);
		
		this.getEditor().addBlurListener(this::textField_onBlur);
	}
}
