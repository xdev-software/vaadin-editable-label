package software.xdev.vaadin.editable_label;

import static java.util.Map.entry;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextAreaVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import software.xdev.vaadin.editable_label.predefined.EditableLabelBigDecimalField;
import software.xdev.vaadin.editable_label.predefined.EditableLabelComboBox;
import software.xdev.vaadin.editable_label.predefined.EditableLabelDatePicker;
import software.xdev.vaadin.editable_label.predefined.EditableLabelNumberField;
import software.xdev.vaadin.editable_label.predefined.EditableLabelTextArea;
import software.xdev.vaadin.editable_label.predefined.EditableLabelTextField;


@PageTitle("Editable Label Examples")
@Route("")
public class HomeView extends Composite<VerticalLayout>
{
	private static final Logger LOG = LoggerFactory.getLogger(HomeView.class);
	private final TextArea valueChangeEventTa = new TextArea();
	
	public HomeView()
	{
		this.initUI();
	}
	
	private void initUI()
	{
		this.valueChangeEventTa.setReadOnly(true);
		this.valueChangeEventTa.addThemeVariants(TextAreaVariant.LUMO_SMALL);
		this.valueChangeEventTa.setWidthFull();
		
		this.getContent().setSpacing(false);
		this.getContent().add(
			new H4("Predefined components"),
			this.getPredefinedComponents(),
			new H4("Custom Component"),
			this.getCustomComponent(),
			new H4("Event"),
			this.valueChangeEventTa);
	}
	
	private FormLayout getPredefinedComponents()
	{
		final FormLayout formLayout = new FormLayout();
		formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep(
			"0",
			2,
			FormLayout.ResponsiveStep.LabelsPosition.TOP));
		
		// Types are required here otherwise a compile error occurs (see https://github.com/vaadin/flow/issues/7920)
		Map.<String, AbstractEditableLabel<?, ?, ?>>ofEntries(
				entry(
					"TextField",
					new EditableLabelTextField()
						.withValue("Some text")),
				entry(
					"TextField with empty value",
					new EditableLabelTextField()),
				entry(
					"Textarea",
					new EditableLabelTextArea()
						.withValue(
							"Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut "
								+ "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation "
								+ "ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure "
								+ "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.")),
				entry(
					"NumberField",
					new EditableLabelNumberField()
						.withValue(123.45)),
				entry(
					"BigDecimalField",
					new EditableLabelBigDecimalField()
						.withValue(BigDecimal.ONE)),
				entry(
					"BigDecimalField with currency",
					new EditableLabelBigDecimalField()
						.withValue(BigDecimal.TEN)
						.withNumberFormat(NumberFormat.getCurrencyInstance())),
				entry(
					"ComboBox",
					new EditableLabelComboBox<Vehicle>()
						.withItems(Vehicle.values())
						.withValue(Vehicle.PLANE)
						.withLabelGenerator(Vehicle::getEmoji)),
				entry(
					"ComboBox with empty value 🚲",
					new EditableLabelComboBox<Vehicle>(el -> el.getEditor().setClearButtonVisible(true))
						.withItems(Vehicle.values())
						.withLabelGenerator(Vehicle::getEmoji, "🚲")),
				entry(
					"DatePicker",
					new EditableLabelDatePicker()
				),
				entry(
					"DatePicker with I18N",
					new EditableLabelDatePicker(getDatepickerWithI18N())
						.withValue(LocalDate.of(2000, 1, 1))
						.withTryUseI18NFormat()
				)
			).entrySet()
			.stream()
			.sorted(Map.Entry.comparingByKey())
			.forEach(e -> {
				this.registerValueChangeEvent(e.getKey(), e.getValue());
				formLayout.addFormItem(e.getValue(), e.getKey());
			});
		
		return formLayout;
	}
	
	private HorizontalLayout getCustomComponent()
	{
		final String defaultValue = "example@example.com";
		final EditableLabel<EmailField, String> emailLabel = new EditableLabel<>(new EmailField())
			.withValue(defaultValue);
		
		this.registerValueChangeEvent("Custom-Component", emailLabel);
		
		final Button btnRestoreDefault = new Button("Restore default", ev -> emailLabel.setValue(defaultValue));
		
		final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		final Button btnRestoreDefaultIn5s =
			new Button(
				"Restore default in 5s",
				ev -> scheduledExecutorService.schedule(
					() -> ev.getSource().getUI().ifPresent(ui -> {
						try
						{
							ui.access(() ->
							{
								emailLabel.setValue(defaultValue);
								ev.getSource().setEnabled(true);
							});
						}
						catch(final Exception ex)
						{
							// Ignore
						}
					}),
					5,
					TimeUnit.SECONDS));
		btnRestoreDefaultIn5s.setDisableOnClick(true);
		
		final HorizontalLayout hlButtons = new HorizontalLayout();
		hlButtons.add(btnRestoreDefault, btnRestoreDefaultIn5s);
		
		final HorizontalLayout hl = new HorizontalLayout();
		hl.add(emailLabel, hlButtons);
		hl.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
		hl.setWidthFull();
		return hl;
	}
	
	private void registerValueChangeEvent(final String source, final AbstractEditableLabel<?, ?, ?> ael)
	{
		ael.addValueChangeListener(ev -> {
			final String header = "Source '" + source + "' - ValueChangeEvent";
			final String text = "value: " + ev.getValue() + "\n"
				+ "oldValue: " + ev.getOldValue() + "\n"
				+ "isFromClient: " + ev.isFromClient();
			LOG.info("{}\n{}", header, text);
			
			this.valueChangeEventTa.setLabel(header);
			this.valueChangeEventTa.setValue(text);
		});
	}
	
	private static DatePicker getDatepickerWithI18N()
	{
		final DatePicker datePicker = new DatePicker();
		datePicker.setI18n(new DatePicker.DatePickerI18n()
			.setDateFormat("yyyy⏰MM⏰dd"));
		return datePicker;
	}
	
	enum Vehicle
	{
		CAR("🚗"),
		TRAIN("🚂"),
		PLANE("✈");
		
		private final String emoji;
		
		Vehicle(final String emoji)
		{
			this.emoji = emoji;
		}
		
		public String getEmoji()
		{
			return this.emoji;
		}
	}
}
