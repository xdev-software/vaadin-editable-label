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



import com.vaadin.flow.component.BlurNotifier.BlurEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;


/**
 * Offers a simple Vaadin label which can be edited as a {@link TextArea}.
 *
 * @author JohannesRabauer
 */
public class EditableLabelTextArea
	extends AbstractEditableLabel<Object, EditableLabelTextArea, String, TextArea>
{
	public EditableLabelTextArea()
	{
		super(new TextArea());
	}
	
	/**
	 * @param value that is at first displayed in the label
	 */
	public EditableLabelTextArea(final String value)
	{
		this();
		this.setValue(value);
	}
	
	/**
	 * @param value      that is at first displayed in the label
	 * @param emptyValue that is displayed if no value is defined (at any time, now or in the future)
	 */
	public EditableLabelTextArea(final String value, final String emptyValue)
	{
		super(new TextArea(), emptyValue);
		this.setValue(value);
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
	
	/**
	 * Event handler delegate method for the {@link TextArea} {@link #textField}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 */
	private void textArea_onBlur(final BlurEvent<TextArea> event)
	{
		if(this.getLabelText().contentEquals(this.getEditor().getValue()))
		{
			this.disableEditMode();
		}
	}
	
	@Override
	protected void btnEdit_onClick(final ClickEvent<Button> event)
	{
		this.getEditor().setValue(this.getLabelText());
		this.getEditor().focus();
		this.enableEditMode();
	}
	
	@Override
	protected void btnSave_onClick(final ClickEvent<Button> event)
	{
		this.setLabelText(this.getEditor().getValue());
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
		this.getEditor().addThemeVariants(TextAreaVariant.LUMO_SMALL);
		this.getEditor().setSizeUndefined();
		this.getEditor().addBlurListener(this::textArea_onBlur);
	}
}
