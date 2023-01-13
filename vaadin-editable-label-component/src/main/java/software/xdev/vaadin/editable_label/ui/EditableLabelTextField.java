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

import com.vaadin.flow.component.BlurNotifier.BlurEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;


/**
 * Offers a simple Vaadin label which can be edited as a {@link TextField}.
 *
 * @author JohannesRabauer
 */
public class EditableLabelTextField<C extends Object>
	extends AbstractEditableLabel<C, EditableLabelTextField, String, TextField>
{
	
	public EditableLabelTextField()
	{
		super(new TextField());
	}
	
	@Override
	public void setValue(final String value)
	{
		if(value == null || value.isBlank())
		{
			// "Why don't we set the empty value as value?" -
			// Because then the label is not visible and therefor
			// nobody can over the label with the mouse to show
			// the edit-button.
			this.setLabelText(this.emptyValue);
		}
		else
		{
			this.setLabelText(value);
			this.getEditor().setValue(value);
		}
	}
	
	@Override
	public String getValue()
	{
		return this.getLabelText();
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
	
	@Override
	protected void btnEdit_onClick(final ClickEvent<Button> event)
	{
		this.getEditor().setValue(this.getLabelText());
		this.getEditor().setVisible(true);
		this.getEditor().focus();
		
		this.enableEditMode();
	}
	
	/**
	 * Event handler delegate method for the {@link TextField} {@link #textField}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 */
	private void textField_onBlur(final BlurEvent<TextField> event)
	{
		if(this.getLabelText().contentEquals(this.getEditor().getValue()))
		{
			this.disableEditMode();
		}
	}
	
	@Override
	protected void btnSave_onClick(final ClickEvent<Button> event)
	{
		final String oldValue = this.getLabelText();
		this.setLabelText(this.getEditor().getValue());
		
		this.fireChangedEvent(oldValue);
		
		if(this.getEditor().getValue().isBlank())
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
		this.getEditor().setAutoselect(true);
		this.getEditor().setValue(this.emptyValue);
		this.getEditor().addThemeVariants(TextFieldVariant.LUMO_SMALL);
		this.getEditor().addBlurListener(this::textField_onBlur);
		this.getEditor().setSizeUndefined();
	}
}
