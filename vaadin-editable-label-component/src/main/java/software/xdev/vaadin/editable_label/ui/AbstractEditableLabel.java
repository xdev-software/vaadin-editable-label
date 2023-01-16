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



import java.beans.Beans;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.ShortcutRegistration;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;

import software.xdev.vaadin.editable_label.util.EditableLabelsUtil;


public abstract class AbstractEditableLabel
	<C, SELF extends AbstractEditableLabel<C, SELF, VALUE, EDIT>, VALUE, EDIT extends Component>
	extends HorizontalLayout
	implements HasValue<AbstractField.ComponentValueChangeEvent<SELF, VALUE>, VALUE>
{
	private final Button btnEdit = new Button();
	private final Button btnSave = new Button();
	private final Button btnClose = new Button();
	private final Label label = new Label();
	protected final String emptyValue;
	private boolean clickable;
	private boolean readOnly = false;
	private final EDIT editor;
	private SerializableConsumer<C> onClick = null;
	
	public AbstractEditableLabel(final EDIT editor)
	{
		this(editor, " - ");
		
	}
	
	public AbstractEditableLabel(final EDIT editor, final String emptyValue)
	{
		super();
		this.editor = editor;
		this.emptyValue = emptyValue;
		this.initUI(
		);
		
		this
			.withEditIcon(VaadinIcon.PENCIL.create())
			.withSaveIcon(VaadinIcon.CHECK.create())
			.withCloseIcon(VaadinIcon.CLOSE.create());
		
		if(!Beans.isDesignTime())
		{
			this.getElement().addEventListener("mouseover", c ->
			{
				if(this.editor.isVisible())
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
	
	public void setClickable(final boolean clickable, final SerializableConsumer<C> onClick)
	{
		this.onClick = onClick;
		this.clickable = clickable;
		
		if(this.clickable)
		{
			this.label.setVisible(false);
			this.onClick.accept((C)this.label.getText());
		}
		else
		{
			this.label.setVisible(true);
		}
	}
	
	public String getLabelText()
	{
		return this.label.getText();
	}
	
	public void setLabelText(final String value)
	{
		this.label.setText(value);
	}
	
	public EDIT getEditor()
	{
		return this.editor;
	}
	
	@Override
	public Registration addValueChangeListener(
		final ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<SELF, VALUE>> listener)
	{
		return EditableLabelsUtil.addValueChangeListener(this, listener);
	}
	
	public void enableEditMode()
	{
		this.getEditor().setVisible(true);
		if(!this.clickable)
		{
			this.label.setVisible(false);
		}
		this.btnEdit.setVisible(false);
		this.btnSave.setVisible(true);
		this.btnClose.setVisible(true);
	}
	
	public void disableEditMode()
	{
		this.getEditor().setVisible(false);
		if(!this.clickable)
		{
			this.label.setVisible(true);
		}
		this.btnEdit.setVisible(true);
		this.btnSave.setVisible(false);
		this.btnClose.setVisible(false);
	}
	
	public void fireChangedEvent(final VALUE oldValue)
	{
		ComponentUtil.fireEvent(
			this,
			new AbstractField.ComponentValueChangeEvent(this, this, oldValue, true)
		);
	}
	
	public SELF withEditIcon(final Component editIcon)
	{
		this.btnEdit.setIcon(editIcon);
		return (SELF)this;
	}
	
	public SELF withSaveIcon(final Component saveIcon)
	{
		this.btnSave.setIcon(saveIcon);
		return (SELF)this;
	}
	
	public SELF withCloseIcon(final Component closeIcon)
	{
		this.btnClose.setIcon(closeIcon);
		return (SELF)this;
	}
	
	protected void initUI()
	{
		this.label.setText(this.emptyValue);
		this.label.setSizeUndefined();
		
		this.btnEdit.setVisible(false);
		this.btnEdit.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
		this.btnEdit.getStyle().set("margin", "0px");
		this.btnEdit.getStyle().set("padding", "0px");
		this.btnEdit.getStyle().set("font-size", "12px");
		this.btnEdit.setWidth("15px");
		this.btnEdit.setHeight("var()");
		this.btnEdit.addClassName("xdev-editable-label-edit-button");
		
		this.btnSave.setVisible(false);
		this.btnSave.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
		this.btnSave.getStyle().set("margin", "0px");
		this.btnSave.getStyle().set("padding", "0px");
		this.btnSave.getStyle().set("font-size", "12px");
		final ShortcutRegistration btnSaveShortcut = this.btnSave.addClickShortcut(Key.ENTER);
		btnSaveShortcut.setBrowserDefaultAllowed(true);
		btnSaveShortcut.setEventPropagationAllowed(false);
		this.btnSave.setWidth("15px");
		this.btnSave.setHeight("15px");
		
		this.btnClose.setVisible(false);
		this.btnClose.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
		this.btnClose.getStyle().set("margin", "0px");
		this.btnClose.getStyle().set("padding", "0px");
		this.btnClose.getStyle().set("font-size", "12px");
		final ShortcutRegistration btnCloseShortcut = this.btnClose.addClickShortcut(Key.ESCAPE);
		btnCloseShortcut.setBrowserDefaultAllowed(true);
		btnCloseShortcut.setEventPropagationAllowed(false);
		this.btnClose.setWidth("15px");
		this.btnClose.setHeight("15px");
		
		this.getEditor().setVisible(false);
		
		this.add(this.label, this.editor, this.btnEdit, this.btnSave, this.btnClose);
		this.setWidthFull();
		this.setHeight(null);
		this.setSpacing(false);
		this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		this.getStyle().set("margin", "0px");
		this.getStyle().set("padding", "0px");
		
		this.btnEdit.addClickListener(this::btnEdit_onClick);
		this.btnSave.addClickListener(this::btnSave_onClick);
		this.btnClose.addClickListener(this::btnClose_onClick);
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
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #btnEdit}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 */
	protected abstract void btnEdit_onClick(final ClickEvent<Button> event);
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #btnSave}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 */
	protected abstract void btnSave_onClick(final ClickEvent<Button> event);
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #btnClose}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 */
	protected abstract void btnClose_onClick(final ClickEvent<Button> event);
}
