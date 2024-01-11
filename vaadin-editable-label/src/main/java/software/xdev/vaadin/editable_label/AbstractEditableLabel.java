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
package software.xdev.vaadin.editable_label;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Focusable;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.shared.Registration;


/**
 * Describes a label which is editable.
 * <p/>
 * The default implementation is {@link EditableLabel}
 *
 * @param <S> own extending class (self).
 * @param <V> value type which is handled through this component
 * @param <C> Vaadin-{@link Component} to edit the value
 *
 * @author AB
 * @author JR
 */
@CssImport(value = EditableLabelStyles.LOCATION)
public abstract class AbstractEditableLabel<
	S extends AbstractEditableLabel<S, C, V>,
	C extends Component & HasSize & HasStyle & HasValue<?, V>,
	V>
	extends AbstractCompositeField<Div, S, V> // Using div because shadow root causes otherwise styling issues
	implements
	HasStyle,
	HasSize
{
	/*
	 * UI-Components
	 */
	protected final Button btnEdit = new Button(VaadinIcon.PENCIL.create());
	protected final Button btnSave = new Button(VaadinIcon.CHECK.create());
	protected final Button btnClose = new Button(VaadinIcon.CLOSE.create());
	protected final Label label = new Label();
	
	protected final C editor;
	
	/*
	 * Suppliers / Configuration
	 */
	protected ItemLabelGenerator<V> nativeLabelGenerator;
	protected String emptyLabelValue = "";
	
	protected AbstractEditableLabel(final C editor, final Consumer<S> additionalInitActions)
	{
		super(editor.getEmptyValue());
		
		this.editor = editor;
		
		this.initUI();
		this.registerListeners();
		
		if(additionalInitActions != null)
		{
			additionalInitActions.accept(this.self());
		}
		
		// initial UI state
		this.disableEditMode();
		this.withLabelGenerator(Object::toString);
	}
	
	protected void initUI()
	{
		this.addClassName(EditableLabelStyles.CONTAINER);
		
		this.label.addClassName(EditableLabelStyles.LABEL);
		
		this.btnEdit.addClassName(EditableLabelStyles.EDIT_BUTTON);
		
		this.btnSave.addClickShortcut(Key.ENTER);
		
		this.btnClose.addClickShortcut(Key.ESCAPE);
		
		Stream.of(this.btnEdit, this.btnSave, this.btnClose)
			.forEach(btn -> {
				btn.addClassName(EditableLabelStyles.BUTTON);
				btn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
			});
		
		this.getEditor().addClassName(EditableLabelStyles.EDITOR);
		this.getEditor().setWidthFull();
		
		this.getContent().add(this.label, this.editor, this.btnEdit, this.btnSave, this.btnClose);
	}
	
	// region Listeners
	
	protected void registerListeners()
	{
		this.btnEdit.addClickListener(this::onEdit);
		this.btnSave.addClickListener(this::onSave);
		this.btnClose.addClickListener(this::onClose);
	}
	
	protected void onEdit(final ClickEvent<Button> ev)
	{
		this.getEditor().setValue(this.getValue());
		
		this.enableEditMode(ev.isFromClient());
	}
	
	protected void onSave(final ClickEvent<Button> ev)
	{
		this.updateValue(this.getEditor().getValue(), ev.isFromClient());
		
		this.disableEditMode(ev.isFromClient());
	}
	
	protected void onClose(final ClickEvent<Button> ev)
	{
		this.disableEditMode(ev.isFromClient());
	}
	
	
	// endregion
	
	// region Value Management
	
	/**
	 * @see #setValue(Object)
	 */
	public S withValue(final V value)
	{
		this.setValue(value);
		return this.self();
	}
	
	/**
	 * Updates the underlying values (if the newValues doesn't equals the oldValue)
	 *
	 * @implNote
	 *           This is a "workaround" for
	 *           <a href="https://github.com/vaadin/flow/issues/11392">vaadin/flow#11392</a><br/>
	 *           The following behaviors may be unexpected:
	 *           <ul>
	 *           <li>The {@link ValueChangeEvent} is fired before the UI is updated</li>
	 *           <li>No internal data management like in {@link AbstractFieldSupport}</li>
	 *           </ul>
	 */
	protected void updateValue(final V newValue, final boolean isFromClient)
	{
		final V oldValue = this.getValue();
		this.setModelValue(newValue, isFromClient);
		
		if(!this.valueEquals(oldValue, newValue))
		{
			this.setPresentationValue(newValue);
		}
	}
	
	@Override
	protected void setPresentationValue(final V newPresentationValue)
	{
		this.updateLabelText(newPresentationValue);
	}
	
	/**
	 * Updates the label text based on the value to display.
	 * <p/>
	 * This value is rendered using the {@link #nativeLabelGenerator}.
	 * <p/>
	 * If the value is null or blank/empty the {@link #btnEdit} will always be shown
	 * and not just when hovering the label.
	 *
	 * @param value The value to display
	 */
	protected void updateLabelText(final V value)
	{
		final String labelText = this.nativeLabelGenerator.apply(value);
		
		// The edit button would not be displayed if nothing is visible
		this.btnEdit.setClassName(
			EditableLabelStyles.EDIT_BUTTON_ALWAYS_VISIBLE,
			labelText == null || labelText.isBlank());
		
		this.label.setText(labelText);
	}
	
	// endregion
	
	// region EditMode
	/**
	 * @see #setEditMode(boolean)
	 */
	public void enableEditMode()
	{
		this.enableEditMode(false);
	}
	
	protected void enableEditMode(final boolean isFromClient)
	{
		this.setEditMode(true, isFromClient);
	}
	
	/**
	 * @see #setEditMode(boolean)
	 */
	public void disableEditMode()
	{
		this.disableEditMode(false);
	}
	
	protected void disableEditMode(final boolean isFromClient)
	{
		this.setEditMode(false, isFromClient);
	}
	
	/**
	 * Sets the editMode:
	 * <ul>
	 *     <li>true  - Enables edit mode - displays the editor</li>
	 *     <li>false - Disables edit mode - displays the label</li>
	 * </ul>
	 * @param enabled <code>true</code> when in editMode otherwise <code>false</code>
	 */
	public void setEditMode(final boolean enabled)
	{
		this.setEditMode(enabled, false);
	}
	
	protected void setEditMode(final boolean enabled, final boolean isFromClient)
	{
		if(this.isEditMode() == enabled || this.isReadOnly() && enabled)
		{
			return;
		}
		
		this.getEditor().setVisible(enabled);
		this.label.setVisible(!enabled);
		this.btnEdit.setVisible(!enabled);
		this.btnSave.setVisible(enabled);
		this.btnClose.setVisible(enabled);
		
		if(enabled && this.getEditor() instanceof Focusable<?>)
		{
			((Focusable<?>)this.getEditor()).focus();
		}
		
		this.fireEvent(new EditModeChangedEvent<>(enabled, this.self(), isFromClient));
	}
	
	public boolean isEditMode()
	{
		return this.getEditor().isVisible();
	}
	
	@SuppressWarnings("unchecked")
	public Registration addEditModeChangedListener(final ComponentEventListener<EditModeChangedEvent<S, C, V>> listener)
	{
		return this.addListener(EditModeChangedEvent.class, (ComponentEventListener)listener);
	}
	
	// endregion
	
	// region withIcons
	
	/**
	 * Changes the icon of the edit button. Default is {@link VaadinIcon#PENCIL}
	 *
	 * @param editIcon to show on the edit button
	 * @return self
	 */
	public S withEditIcon(final Component editIcon)
	{
		this.btnEdit.setIcon(editIcon);
		return this.self();
	}
	
	/**
	 * Changes the icon of the save button. Default is {@link VaadinIcon#CHECK}
	 *
	 * @param saveIcon to show on the save button
	 * @return self
	 */
	public S withSaveIcon(final Component saveIcon)
	{
		this.btnSave.setIcon(saveIcon);
		return this.self();
	}
	
	/**
	 * Changes the icon of the close button. Default is {@link VaadinIcon#CLOSE}
	 *
	 * @param closeIcon to show on the close button
	 * @return self
	 */
	public S withCloseIcon(final Component closeIcon)
	{
		this.btnClose.setIcon(closeIcon);
		return this.self();
	}
	
	// endregion
	
	// region LabelGenerator
	
	/**
	 * Sets the label generator used for displaying the label.
	 * <p/>
	 * <b>It's recommended to use {@link #withLabelGenerator(ItemLabelGenerator, Supplier)}</b> or its variants
	 * because they included null/empty-value checks.
	 */
	public S withNativeLabelGenerator(final ItemLabelGenerator<V> labelGenerator)
	{
		this.nativeLabelGenerator = Objects.requireNonNull(labelGenerator);
		this.updateLabelText(this.getValue());
		return this.self();
	}
	
	/**
	 * Sets the label generator used for displaying the label.
	 * <p/>
	 * If the value is null or empty {@code emptyValue} is used.
	 * @see #withNativeLabelGenerator(ItemLabelGenerator)
	 */
	public S withLabelGenerator(final ItemLabelGenerator<V> notEmptyLabelGenerator, final Supplier<String> emptyValue)
	{
		return this.withNativeLabelGenerator(
			v -> v == null || this.valueEquals(v, this.getEmptyValue())
				? emptyValue.get()
				: notEmptyLabelGenerator.apply(v));
	}
	
	/**
	 * @see #withLabelGenerator(ItemLabelGenerator, Supplier)
	 */
	public S withLabelGenerator(final ItemLabelGenerator<V> notEmptyLabelGenerator, final String emptyValue)
	{
		Objects.requireNonNull(emptyValue);
		return this.withLabelGenerator(notEmptyLabelGenerator, () -> emptyValue);
	}
	
	/**
	 * @see #withLabelGenerator(ItemLabelGenerator, Supplier)
	 */
	public S withLabelGenerator(final ItemLabelGenerator<V> notEmptyLabelGenerator)
	{
		return this.withLabelGenerator(notEmptyLabelGenerator, this.getEmptyLabelValue());
	}
	
	public String getEmptyLabelValue()
	{
		return this.emptyLabelValue;
	}
	
	/**
	 * Set's the default value when the value to display is null or empty.
	 * @see #withLabelGenerator(ItemLabelGenerator, Supplier)
	 */
	public S withEmptyLabelValue(final String emptyLabelValue)
	{
		this.emptyLabelValue = Objects.requireNonNull(emptyLabelValue);
		return this.self();
	}
	
	//endregion
	
	/**
	 * @return the component used to edit the value
	 */
	public C getEditor()
	{
		return this.editor;
	}
	
	@Override
	public void setReadOnly(final boolean readOnly)
	{
		super.setReadOnly(readOnly);
		
		this.btnEdit.setEnabled(!readOnly);
		if(readOnly && this.isEditMode())
		{
			this.disableEditMode();
		}
	}
	
	@Override
	public void setRequiredIndicatorVisible(final boolean requiredIndicatorVisible)
	{
		super.setRequiredIndicatorVisible(requiredIndicatorVisible);
		this.getEditor().setRequiredIndicatorVisible(requiredIndicatorVisible);
	}
	
	@SuppressWarnings("unchecked")
	protected S self()
	{
		return (S)this;
	}
	
	protected static class EditModeChangedEvent
		<S extends AbstractEditableLabel<S, C, V>, C extends Component & HasSize & HasStyle & HasValue<?, V>, V>
		extends ComponentEvent<S>
	{
		protected final boolean editModeEnabled;
		
		public EditModeChangedEvent(final boolean editModeEnabled, final S source, final boolean fromClient)
		{
			super(source, fromClient);
			this.editModeEnabled = editModeEnabled;
		}
		
		public boolean isEditModeEnabled()
		{
			return this.editModeEnabled;
		}
	}
}
