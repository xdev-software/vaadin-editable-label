package software.xdev.vaadin.editable_label.util;

import java.util.Objects;
import java.util.function.Predicate;

import com.vaadin.flow.component.Component;


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
}
