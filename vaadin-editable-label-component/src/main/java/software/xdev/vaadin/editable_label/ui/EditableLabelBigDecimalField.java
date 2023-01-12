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

import java.beans.Beans;
import java.math.BigDecimal;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.BlurNotifier.BlurEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.ShortcutRegistration;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.shared.Registration;

/**
 * Offers a simple Vaadin label which can be edited as a {@link BigDecimalField}.
 *
 * @author JohannesRabauer
 */
public class EditableLabelBigDecimalField extends HorizontalLayout
	implements HasValue<ComponentValueChangeEvent<EditableLabelBigDecimalField, BigDecimal>, BigDecimal>
{
	
	private BigDecimal value;
	private boolean readOnly = false;
	private boolean currency = false;
	private String currencySign = "€";
	private Button btnEdit, btnSave, btnClose;
	private BigDecimalField textField;
	private Label label;
	
	/**
	 *
	 */
	public EditableLabelBigDecimalField()
	{
		super();
		this.initUI();
		
		if(!Beans.isDesignTime())
		{
			this.getElement().addEventListener("mouseover", c ->
			{
				if(this.textField.isVisible())
				{
					this.btnEdit.setVisible(false);
				}
				
				if(this.label.isVisible())
				{
					this.btnEdit.setVisible(!this.readOnly);
				}
			});
			
			this.getElement().addEventListener("mouseout", c ->
			{
				this.btnEdit.setVisible(false);
			});
			
		}
	}
	
	@Override
	public void setValue(final BigDecimal value)
	{
		if(value == null)
		{
			this.label.setText("k.A.");
		}
		else
		{
			this.value = value;
			if(this.currency)
			{
				this.label.setText(value.toEngineeringString() + " " + this.currencySign);
			}
			else
			{
				this.label.setText(value.toEngineeringString());
			}
			
			this.textField.setValue(value);
		}
	}
	
	@Override
	public BigDecimal getValue()
	{
		return this.value;
	}
	
	@Override
	public Registration addValueChangeListener(
		final ValueChangeListener<? super ComponentValueChangeEvent<EditableLabelBigDecimalField, BigDecimal>> listener)
	{
		return this.textField.addValueChangeListener(
			(ValueChangeListener<? super ComponentValueChangeEvent<BigDecimalField, BigDecimal>>)listener);
	}
	
	@Override
	public void setReadOnly(final boolean readOnly)
	{
		this.readOnly = readOnly;
		
	}
	
	@Override
	public boolean isReadOnly()
	{
		return this.readOnly;
	}
	
	public void hasCurrency(final boolean isCurrency, final String currencySign)
	{
		this.currency = isCurrency;
		this.currencySign = currencySign;
	}
	
	@Override
	public void setRequiredIndicatorVisible(final boolean requiredIndicatorVisible)
	{
		this.textField.setRequiredIndicatorVisible(requiredIndicatorVisible);
	}
	
	@Override
	public boolean isRequiredIndicatorVisible()
	{
		return this.textField.isRequiredIndicatorVisible();
	}
	
	/**
	 * Event handler delegate method for the {@link BigDecimalField} {@link #textField}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 */
	private void textField_onBlur(final BlurEvent<BigDecimalField> event)
	{
		if(this.label.getText().contentEquals(this.textField.getValue().toPlainString()))
		{
			this.textField.setVisible(false);
			this.label.setVisible(true);
			this.btnSave.setVisible(false);
			this.btnClose.setVisible(false);
		}
	}
	
	/**
	 * Event handler delegate method for the {@link BigDecimalField} {@link #textField}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ValueChangeListener#valueChanged(ValueChangeEvent)
	 */
	private void textField_valueChanged(final ComponentValueChangeEvent<BigDecimalField, BigDecimal> event)
	{
		this.value = this.textField.getValue();
	}
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #btnEdit}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 */
	private void btnEdit_onClick(final ClickEvent<Button> event)
	{
		this.textField.setValue(this.value);
		this.textField.setVisible(true);
		this.textField.focus();
		
		this.label.setVisible(false);
		this.btnEdit.setVisible(false);
		this.btnSave.setVisible(true);
		this.btnClose.setVisible(true);
	}
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #btnSave}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 */
	private void btnSave_onClick(final ClickEvent<Button> event)
	{
		this.label.setText(this.textField.getValue().toPlainString());
		// EditableLabelsUtil.getNextParent(this, HasGlobalSave.class).save();
		System.out.println("Store item");
		
		this.textField.setVisible(false);
		this.label.setVisible(true);
		this.btnSave.setVisible(false);
		this.btnClose.setVisible(false);
	}
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #btnClose}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 */
	private void btnClose_onClick(final ClickEvent<Button> event)
	{
		this.textField.setVisible(false);
		
		this.label.setVisible(true);
		
		this.btnSave.setVisible(false);
		
		this.btnClose.setVisible(false);
	}
	
	/* WARNING: Do NOT edit!<br>The content of this method is always regenerated by the UI designer. */
	// <generated-code name="initUI">
	private void initUI()
	{
		this.label = new Label();
		this.textField = new BigDecimalField();
		this.btnEdit = new Button();
		this.btnSave = new Button();
		this.btnClose = new Button();
		
		this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		this.label.setText("k.A.");
		this.textField.setAutoselect(true);
		this.textField.setVisible(false);
		this.textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
		this.btnEdit.setVisible(false);
		this.btnEdit.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
		this.btnEdit.getStyle().set("margin", "0px");
		this.btnEdit.getStyle().set("padding", "0px");
		this.btnEdit.getStyle().set("font-size", "12px");
		this.btnEdit.setIcon(VaadinIcon.PENCIL.create());
		this.btnSave.setVisible(false);
		this.btnSave.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
		this.btnSave.getStyle().set("margin", "0px");
		this.btnSave.getStyle().set("padding", "0px");
		this.btnSave.getStyle().set("font-size", "12px");
		final ShortcutRegistration btnSaveShortcut = this.btnSave.addClickShortcut(Key.ENTER);
		btnSaveShortcut.setBrowserDefaultAllowed(true);
		btnSaveShortcut.setEventPropagationAllowed(false);
		this.btnSave.setIcon(VaadinIcon.DISC.create());
		this.btnClose.setVisible(false);
		this.btnClose.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
		this.btnClose.getStyle().set("margin", "0px");
		this.btnClose.getStyle().set("padding", "0px");
		this.btnClose.getStyle().set("font-size", "12px");
		final ShortcutRegistration btnCloseShortcut = this.btnClose.addClickShortcut(Key.ESCAPE);
		btnCloseShortcut.setBrowserDefaultAllowed(true);
		btnCloseShortcut.setEventPropagationAllowed(false);
		this.btnClose.setIcon(VaadinIcon.CLOSE.create());
		
		this.label.setSizeUndefined();
		this.textField.setWidthFull();
		this.textField.setHeight(null);
		this.btnEdit.setWidth("15px");
		this.btnEdit.setHeight("15px");
		this.btnSave.setWidth("15px");
		this.btnSave.setHeight("15px");
		this.btnClose.setWidth("15px");
		this.btnClose.setHeight("15px");
		this.add(this.label, this.textField, this.btnEdit, this.btnSave, this.btnClose);
		this.setWidthFull();
		this.setHeight(null);
		
		this.textField.addBlurListener(this::textField_onBlur);
		this.textField.addValueChangeListener(this::textField_valueChanged);
		this.btnEdit.addClickListener(this::btnEdit_onClick);
		this.btnSave.addClickListener(this::btnSave_onClick);
		this.btnClose.addClickListener(this::btnClose_onClick);
	} // </generated-code>
	
	// <generated-code name="variables">
	
	// </generated-code>
	
}
