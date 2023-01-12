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
import java.util.Collection;

import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.ShortcutRegistration;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.HasFilterableDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.shared.Registration;


public class EditableLabelComboBox<T> extends HorizontalLayout
	implements HasValue<ComponentValueChangeEvent<EditableLabelComboBox<T>, T>, T>,
	HasFilterableDataProvider<T, String>
{
	private SerializableConsumer<T> onClick = null;
	private boolean clickable;
	
	private T value;
	private final ComboBox<T> comboBox = new ComboBox<>();
	private boolean readOnly = false;
	
	public EditableLabelComboBox()
	{
		super();
		this.initUI();
		
		if(!Beans.isDesignTime())
		{
			this.comboBox.setVisible(false);
			this.div.setVisible(false);
			// TODO
			// this.comboBox.setItemLabelGenerator(ItemLabelGeneratorFactory.NonNull(CaptionUtils::resolveCaption));
			this.comboBox.setWidthFull();
			this.comboBox.getElement().setAttribute("theme", "small");
			this.comboBox.addValueChangeListener(l ->
			{
				this.value = l.getValue();
			});
			
			this.div.add(this.comboBox);
			
			this.getElement().addEventListener("mouseover", c ->
			{
				if(this.comboBox.isVisible())
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
	public void setValue(final T value)
	{
		if(value == null)
		{
			this.label.setText("k.A.");
		}
		else
		{
			this.value = value;
			this.label.setText(this.comboBox.getItemLabelGenerator().apply(this.value));
			if(this.label.getText().isBlank())
			{
				this.label.setText("k.A.");
			}
		}
	}
	
	@Override
	public T getValue()
	{
		return this.value;
	}
	
	public void setClickable(final boolean clickable, final SerializableConsumer<T> onClick)
	{
		this.onClick = onClick;
		this.clickable = clickable;
		
		if(this.clickable)
		{
			this.label.setVisible(false);
			this.anchor.setVisible(true);
			
			this.anchor.setText(this.comboBox.getItemLabelGenerator().apply(this.value));
			this.onClick.accept(this.value);
		}
		else
		{
			this.label.setVisible(true);
			this.anchor.setVisible(false);
		}
		
	}
	
	public Anchor getAnchor()
	{
		return this.anchor;
	}
	
	@Override
	public Registration addValueChangeListener(
		final ValueChangeListener<? super ComponentValueChangeEvent<EditableLabelComboBox<T>, T>> listener)
	{
		return this.comboBox.addValueChangeListener(
			(ValueChangeListener<? super ComponentValueChangeEvent<ComboBox<T>, T>>)listener);
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
	
	@Override
	public void setRequiredIndicatorVisible(final boolean requiredIndicatorVisible)
	{
		this.comboBox.setRequiredIndicatorVisible(requiredIndicatorVisible);
	}
	
	@Override
	public boolean isRequiredIndicatorVisible()
	{
		return false;
	}
	
	public void setItemLabelGenerator(
		final ItemLabelGenerator<T> itemLabelGenerator)
	{
		this.comboBox.setItemLabelGenerator(itemLabelGenerator);
	}
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #btnEdit}.
	 *
	 * @eventHandlerDelegate Do NOT delete, used by UI designer!
	 * @see ComponentEventListener#onComponentEvent(ComponentEvent)
	 */
	private void btnEdit_onClick(final ClickEvent<Button> event)
	{
		this.comboBox.setValue(this.value);
		
		this.comboBox.setVisible(true);
		this.comboBox.focus();
		this.div.setVisible(true);
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
		this.label.setText(this.comboBox.getItemLabelGenerator().apply(this.value));
		if(this.label.getText().isBlank())
		{
			this.label.setText("k.A.");
		}
		// UIUtils.getNextParent(this, HasGlobalSave.class).save();
		System.out.println("Store item");
		
		this.comboBox.setVisible(false);
		this.div.setVisible(false);
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
		this.comboBox.setVisible(false);
		this.div.setVisible(false);
		this.label.setVisible(true);
		this.btnSave.setVisible(false);
		this.btnClose.setVisible(false);
	}
	
	/* WARNING: Do NOT edit!<br>The content of this method is always regenerated by the UI designer. */
	// <generated-code name="initUI">
	private void initUI()
	{
		this.label = new Label();
		this.anchor = new Anchor();
		this.div = new Div();
		this.btnEdit = new Button();
		this.btnSave = new Button();
		this.btnClose = new Button();
		
		this.setDefaultVerticalComponentAlignment(Alignment.CENTER);
		this.label.setText("k.A.");
		this.anchor.setText("k.A.");
		this.anchor.setVisible(false);
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
		this.anchor.setSizeUndefined();
		this.div.setWidthFull();
		this.div.setHeight(null);
		this.btnEdit.setWidth("15px");
		this.btnEdit.setHeight("15px");
		this.btnSave.setWidth("15px");
		this.btnSave.setHeight("15px");
		this.btnClose.setWidth("15px");
		this.btnClose.setHeight("15px");
		this.add(this.label, this.anchor, this.div, this.btnEdit, this.btnSave, this.btnClose);
		this.setWidthFull();
		this.setHeight(null);
		
		this.btnEdit.addClickListener(this::btnEdit_onClick);
		this.btnSave.addClickListener(this::btnSave_onClick);
		this.btnClose.addClickListener(this::btnClose_onClick);
	} // </generated-code>
	
	// <generated-code name="variables">
	private Button btnEdit, btnSave, btnClose;
	private Anchor anchor;
	private Label label;
	private Div div;
	// </generated-code>
	
	@Override
	public void setItems(final Collection<T> items)
	{
		this.comboBox.setItems(items);
	}
	
	@Override
	public <C> void setDataProvider(
		final DataProvider<T, C> dataProvider,
		final SerializableFunction<String, C> filterConverter)
	{
		this.comboBox.setDataProvider(dataProvider, filterConverter);
	}
	
}
