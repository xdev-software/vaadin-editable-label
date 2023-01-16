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
package software.xdev.vaadin.editable_label.ui;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;


/**
 * Lets the user see a label which is editable. The value and editing-component is defined by the implementing class.
 *
 * @param <S> own extending class (self).
 * @param <V> value type which is handled through this component
 * @param <C> Vaadin-{@link Component} to edit the value
 * @author JohannesRabauer
 */
@CssImport(EditableLabelStyles.LOCATION)
public abstract class AbstractEditableLabel
	<S extends AbstractEditableLabel<S, V, C>, V, C extends Component & HasStyle>
	extends HorizontalLayout
	implements HasValue<AbstractField.ComponentValueChangeEvent<S, V>, V>
{
	private final ButtonForEditableLabel btnEdit = new ButtonForEditableLabel();
	private final ButtonForEditableLabel btnSave = new ButtonForEditableLabel();
	private final ButtonForEditableLabel btnClose = new ButtonForEditableLabel();
	private final Label label = new Label();
	protected final String emptyValue;
	private boolean readOnly = false;
	private final C editor;
	
	/**
	 * @param editor component to edit the value
	 */
	protected AbstractEditableLabel(final C editor)
	{
		this(editor, " - ");
	}
	
	/**
	 * @param editor     component to edit the value
	 * @param emptyValue which is displayed in the label if no value is set. Should not be empty or null, because then
	 *                   the width of the label is zero and therefor hovering the label component is not possible
	 *                   anymore. Hence, no button is shown.
	 */
	protected AbstractEditableLabel(final C editor, final String emptyValue)
	{
		super();
		this.editor = editor;
		this.emptyValue = emptyValue;
		this.initUI();
		this.registerListeners();
		
		this
			.withEditIcon(VaadinIcon.PENCIL.create())
			.withSaveIcon(VaadinIcon.CHECK.create())
			.withCloseIcon(VaadinIcon.CLOSE.create());
	}
	
	/**
	 * @return the text shown through the label
	 */
	public String getLabelText()
	{
		return this.label.getText();
	}
	
	/**
	 * Sets the displayed value. Does <b>not</b> change the actual value (like {@link #getValue()} contained in the
	 * EditableLabel-Component.
	 *
	 * @param value to display
	 */
	public void setLabelText(final String value)
	{
		this.label.setText(value);
	}
	
	/**
	 * @return the component to edit the value
	 */
	public C getEditor()
	{
		return this.editor;
	}
	
	/**
	 * The event is thrown when the edited value is saved, for instance if the Save-Button is clicked or
	 * {@link #setValue(Object)} is called.
	 *
	 * @param listener the listener to add, not <code>null</code>
	 * @return a handle that can be used for removing the listener
	 */
	@Override
	public Registration addValueChangeListener(
		final ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<S, V>> listener)
	{
		final ComponentEventListener componentListener = (event) -> {
			listener.valueChanged((AbstractField.ComponentValueChangeEvent)event);
		};
		return ComponentUtil.addListener(this, AbstractField.ComponentValueChangeEvent.class, componentListener);
	}
	
	/**
	 * Hides and shows all the elements needed to <b>edit</b> the value.
	 */
	public void enableEditMode()
	{
		this.getEditor().setVisible(true);
		this.label.setVisible(false);
		this.btnEdit.setVisible(false);
		this.btnSave.setVisible(true);
		this.btnClose.setVisible(true);
	}
	
	/**
	 * Hides and shows all the elements needed to <b>show</b> the value.
	 */
	public void disableEditMode()
	{
		this.getEditor().setVisible(false);
		this.label.setVisible(true);
		this.btnEdit.setVisible(true);
		this.btnSave.setVisible(false);
		this.btnClose.setVisible(false);
	}
	
	protected void fireChangedEvent(final V oldValue)
	{
		ComponentUtil.fireEvent(
			this,
			new AbstractField.ComponentValueChangeEvent<>(this, this, oldValue, true)
		);
	}
	
	/**
	 * Changes the icon of the edit button. Default is {@link com.vaadin.flow.component.icon.VaadinIcon#PENCIL}
	 *
	 * @param editIcon to show on the edit button
	 * @return self
	 */
	public S withEditIcon(final Component editIcon)
	{
		this.btnEdit.setIcon(editIcon);
		return (S)this;
	}
	
	/**
	 * Changes the icon of the save button. Default is {@link com.vaadin.flow.component.icon.VaadinIcon#CHECK}
	 *
	 * @param saveIcon to show on the save button
	 * @return self
	 */
	public S withSaveIcon(final Component saveIcon)
	{
		this.btnSave.setIcon(saveIcon);
		return (S)this;
	}
	
	/**
	 * Changes the icon of the close button. Default is {@link com.vaadin.flow.component.icon.VaadinIcon#CLOSE}
	 *
	 * @param closeIcon to show on the close button
	 * @return self
	 */
	public S withCloseIcon(final Component closeIcon)
	{
		this.btnClose.setIcon(closeIcon);
		return (S)this;
	}
	
	protected void initUI()
	{
		this.addClassName(EditableLabelStyles.CONTAINER);
		this.setSpacing(false);
		
		this.label.setText(this.emptyValue);
		this.label.addClassName(EditableLabelStyles.LABEL);
		
		this.btnSave.addClickShortcut(Key.ENTER);
		
		this.btnClose.addClickShortcut(Key.ESCAPE);
		
		this.getEditor().setVisible(false);
		this.getEditor().addClassName(EditableLabelStyles.EDITOR);
		
		this.add(this.label, this.editor, this.btnEdit, this.btnSave, this.btnClose);
		
	}
	
	protected void registerListeners()
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
		
		this.getElement().addEventListener("mouseout", c -> this.btnEdit.setVisible(false));
		
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
	 */
	protected abstract void btnEdit_onClick(final ClickEvent<Button> event);
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #btnSave}.
	 */
	protected abstract void btnSave_onClick(final ClickEvent<Button> event);
	
	/**
	 * Event handler delegate method for the {@link Button} {@link #btnClose}.
	 */
	protected abstract void btnClose_onClick(final ClickEvent<Button> event);
}
