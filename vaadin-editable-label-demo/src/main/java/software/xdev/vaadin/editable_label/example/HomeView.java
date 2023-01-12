package software.xdev.vaadin.editable_label.example;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import software.xdev.vaadin.editable_label.ui.EditableLabelBigDecimalField;
import software.xdev.vaadin.editable_label.ui.EditableLabelComboBox;
import software.xdev.vaadin.editable_label.ui.EditableLabelDatePicker;
import software.xdev.vaadin.editable_label.ui.EditableLabelNumberField;
import software.xdev.vaadin.editable_label.ui.EditableLabelTextArea;
import software.xdev.vaadin.editable_label.ui.EditableLabelTextField;


@PageTitle("Editable Label Examples")
@Route("")
public class HomeView extends Composite<VerticalLayout>
{
	public HomeView()
	{
		this.getContent().add(
			this.createEditableLabelPresenter("ComboBox", new EditableLabelComboBox())
		);
		this.getContent().add(
			this.createEditableLabelPresenter("DatePicker", new EditableLabelDatePicker())
		);
		this.getContent().add(
			this.createEditableLabelPresenter("NumberField", new EditableLabelNumberField())
		);
		this.getContent().add(
			this.createEditableLabelPresenter("TextArea", new EditableLabelTextArea())
		);
		
		this.getContent().add(
			this.createEditableLabelPresenter("TextField", new EditableLabelTextField())
		);
		this.getContent().add(
			this.createEditableLabelPresenter("BigDecimalField", new EditableLabelBigDecimalField())
		);
		
		this.getContent().setHeightFull();
	}
	
	private Component createEditableLabelPresenter(final String name, final HasValue<?, ?> editableLabelComponent)
	{
		final VerticalLayout vl = new VerticalLayout();
		vl.add(new Label(name));
		vl.add((Component)editableLabelComponent);
		
		editableLabelComponent.addValueChangeListener(l -> System.out.println(
			"Component " + name + " newValue:" + l.getValue() + " oldValue:" + l.getOldValue()));
		
		return vl;
	}
}
