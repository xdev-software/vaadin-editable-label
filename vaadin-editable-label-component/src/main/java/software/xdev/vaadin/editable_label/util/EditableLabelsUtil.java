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
package software.xdev.vaadin.editable_label.util;

import java.util.Objects;
import java.util.function.Predicate;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.shared.Registration;


public class EditableLabelsUtil
{
	public static Component getNextParent(final Component c, final Class<Component> type)
	{
		Objects.requireNonNull(type);
		return EditableLabelsUtil.getNextParent(c, type::isInstance);
	}
	
	public static Component getNextParent(final Component c, final Predicate<Component> predicate)
	{
		for(Component parent = c; parent != null; parent = (Component)parent.getParent().orElse((Component)null))
		{
			if(predicate.test(parent))
			{
				return parent;
			}
		}
		
		return null;
	}
	
	public static <C extends Component, V> Registration addValueChangeListener(
		final Component component,
		final HasValue.ValueChangeListener<? super AbstractField.ComponentValueChangeEvent<C,
			V>> listener)
	{
		final ComponentEventListener componentListener = (event) -> {
			final AbstractField.ComponentValueChangeEvent<C, V> valueChangeEvent =
				(AbstractField.ComponentValueChangeEvent)event;
			listener.valueChanged(valueChangeEvent);
		};
		return ComponentUtil.addListener(component, AbstractField.ComponentValueChangeEvent.class, componentListener);
	}
	
}
