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

import java.util.function.Consumer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.HasValue;


/**
 * The default simple implementation of {@link AbstractEditableLabel}.
 *
 * @param <V> value type which is handled through this component
 * @param <C> Vaadin-{@link Component} to edit the value
 *
 * @author AB
 * @author JR
 */
public class EditableLabel<C extends Component & HasSize & HasStyle & HasValue<?, V>, V>
	extends AbstractEditableLabel<EditableLabel<C, V>, C, V>
{
	public EditableLabel(final C editor)
	{
		this(editor, null);
	}
	
	public EditableLabel(final C editor, final Consumer<EditableLabel<C, V>> additionalInitActions)
	{
		super(editor, additionalInitActions);
	}
}
