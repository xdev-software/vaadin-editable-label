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
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.BigDecimalField;


/**
 * Offers a simple Vaadin label which can be edited as a {@link BigDecimalField}.
 *
 * @author JohannesRabauer
 */
public class EditableLabelBigDecimalField
	extends AbstractEditableLabel<Object, EditableLabelBigDecimalField, BigDecimal, BigDecimalField>
{
	
	private BigDecimal value;
	private NumberFormat numberFormatter;
	
	public EditableLabelBigDecimalField()
	{
		super(new BigDecimalField());
	}
	
	public EditableLabelBigDecimalField(final NumberFormat numberFormatter)
	{
		this();
		Objects.requireNonNull(this.numberFormatter = numberFormatter);
	}
	
	public EditableLabelBigDecimalField(final BigDecimal value, final NumberFormat numberFormatter)
	{
		this(numberFormatter);
		this.setValue(value);
	}
	
	public EditableLabelBigDecimalField(final BigDecimal value)
	{
		this();
		this.setValue(value);
	}
	
	public EditableLabelBigDecimalField(final Double value, final NumberFormat numberFormatter)
	{
		this(numberFormatter);
		this.setValue(new BigDecimal(value));
	}
	
	public EditableLabelBigDecimalField(final Double value)
	{
		this();
		this.setValue(new BigDecimal(value));
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
			if(this.numberFormatter != null)
			{
				this.setLabelText(this.numberFormatter.format(value));
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
	
	public void setCurrency(final NumberFormat numberFormatter)
	{
		this.numberFormatter = numberFormatter;
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
	protected void initUI()
	{
		super.initUI();
		this.getEditor().setAutoselect(true);
		this.getEditor().addBlurListener(this::textField_onBlur);
	}
}
