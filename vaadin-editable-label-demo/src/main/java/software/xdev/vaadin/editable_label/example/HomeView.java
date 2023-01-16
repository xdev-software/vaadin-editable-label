package software.xdev.vaadin.editable_label.example;

import java.text.NumberFormat;
import java.time.LocalDate;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
		final HorizontalLayout hlText = new HorizontalLayout();
		hlText.add(this.createEditableLabelPresenter(
			"EditableLabelTextField",
			new EditableLabelTextField("I'm editable!")));
		hlText.add(this.createEditableLabelPresenter("EditableLabelTextArea", new EditableLabelTextArea("Me too!")));
		this.getContent().add(hlText);
		
		final HorizontalLayout hlComboBox = new HorizontalLayout();
		hlComboBox.add(this.createEditableLabelPresenter(
			"EditableLabelComboBox - Empty",
			new EditableLabelComboBox()));
		hlComboBox.add(this.createEditableLabelPresenter(
			"EditableLabelComboBox - Filled",
			new EditableLabelComboBox<>(DEMO_VALUES.DemoValue1, DEMO_VALUES.values())));
		this.getContent().add(hlComboBox);
		
		final HorizontalLayout hlDatePicker = new HorizontalLayout();
		hlDatePicker.add(this.createEditableLabelPresenter(
			"EditableLabelDatePicker - No Format",
			new EditableLabelDatePicker(LocalDate.now())));
		hlDatePicker.add(this.createEditableLabelPresenter(
			"EditableLabelDatePicker - Formatted",
			new EditableLabelDatePicker(LocalDate.now(), "MMM yy")));
		this.getContent().add(hlDatePicker);
		
		this.getContent().add(
			this.createEditableLabelPresenter("EditableLabelNumberField", new EditableLabelNumberField(123.0))
		);
		
		final HorizontalLayout hlBigDecimal = new HorizontalLayout();
		hlBigDecimal.add(this.createEditableLabelPresenter(
			"BigDecimalField - No Format",
			new EditableLabelBigDecimalField(12.0)));
		hlBigDecimal.add(this.createEditableLabelPresenter(
			"BigDecimalField - Currency",
			new EditableLabelBigDecimalField(
				12.3,
				NumberFormat.getCurrencyInstance())));
		this.getContent().add(hlBigDecimal);
		
		this.getContent().setHeightFull();
	}
	
	private enum DEMO_VALUES
	{
		DemoValue1,
		DemoValue2,
		DemoValue3
	}
	
	private Component createEditableLabelPresenter(final String name, final HasValue<?, ?> editableLabelComponent)
	{
		final VerticalLayout vl = new VerticalLayout();
		vl.add(new H4(name));
		vl.add((Component)editableLabelComponent);
		
		this.addValueChangeListener(name, editableLabelComponent);
		
		return vl;
	}
	
	private <T extends HasValue<?, ?>> T addValueChangeListener(
		final String name,
		final T editableLabelComponent)
	{
		editableLabelComponent.addValueChangeListener(l -> System.out.println(
			"Component " + name + " newValue:" + l.getValue() + " oldValue:" + l.getOldValue()));
		return editableLabelComponent;
	}
}
