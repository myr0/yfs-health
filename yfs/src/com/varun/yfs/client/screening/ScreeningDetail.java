package com.varun.yfs.client.screening;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.HeaderGroupConfig;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.varun.yfs.client.index.IndexPage;
import com.varun.yfs.client.screening.rpc.ScreeningDetailService;
import com.varun.yfs.client.screening.rpc.ScreeningDetailServiceAsync;
import com.varun.yfs.dto.CityDTO;
import com.varun.yfs.dto.CountryDTO;
import com.varun.yfs.dto.LocalityDTO;
import com.varun.yfs.dto.PatientDetailDTO;
import com.varun.yfs.dto.ProcessTypeDTO;
import com.varun.yfs.dto.ScreeningDetailDTO;
import com.varun.yfs.dto.StateDTO;
import com.varun.yfs.dto.TownDTO;
import com.varun.yfs.dto.TypeOfLocationDTO;
import com.varun.yfs.dto.VillageDTO;

public class ScreeningDetail extends LayoutContainer
{
	private String headerText = "Default";
	private ScreeningDetailServiceAsync detailServiceAsync = GWT.create(ScreeningDetailService.class);

	protected ContentPanel mainContainerPanel = new ContentPanel();
	private final ComboBox<ModelData> country = new ComboBox<ModelData>();
	private final ComboBox<ModelData> state = new ComboBox<ModelData>();
	private final ComboBox<ModelData> city = new ComboBox<ModelData>();
	private final ComboBox<ModelData> town = new ComboBox<ModelData>();
	private final ComboBox<ModelData> village = new ComboBox<ModelData>();
	private final ComboBox<ModelData> chapterName = new ComboBox<ModelData>();
	private final ComboBox<ModelData> locality = new ComboBox<ModelData>();
	private final ComboBox<ModelData> processType = new ComboBox<ModelData>();
	private final ComboBox<ModelData> typeOfLocation = new ComboBox<ModelData>();
	private final CheckBoxListView<ModelData> volunteers = new CheckBoxListView<ModelData>();
	private final CheckBoxListView<ModelData> doctors = new CheckBoxListView<ModelData>();
	private final TextArea address = new TextArea();
	private final TextArea contactInformation = new TextArea();
	private final DateField screeningDate = new DateField();

	private final List<ColumnConfig> configs = getColumnConfigs();
	private final ColumnModel cm = getColumnModel(configs);
	private final ListStore<ModelData> store = new ListStore<ModelData>();
	private final EditorGrid<ModelData> editorGrid = new EditorGrid<ModelData>(store, cm);

	public ScreeningDetail()
	{
		setSize("700", "600");
	}

	@Override
	protected void onRender(Element parent, int index)
	{
		super.onRender(parent, index);

		setLayout(new FitLayout());

		mainContainerPanel.setHeading(headerText);

		LayoutContainer cpMain = new LayoutContainer();
		cpMain.setLayout(new TableLayout(3));

		LayoutContainer cpPart1 = new LayoutContainer();
		cpPart1.setLayout(new FormLayout());
		TableData td_cpPart1 = new TableData();
		td_cpPart1.setPadding(5);
		cpMain.add(cpPart1, td_cpPart1);

		country.setEditable(false);
		cpPart1.add(country, new FormData("90%"));
		country.setSize("150", "22");
		country.setFieldLabel("Country");
		country.setDisplayField("countryName");
		country.setStore(new ListStore<ModelData>());

		state.setEditable(false);
		cpPart1.add(state, new FormData("90%"));
		state.setSize("150", "22");
		state.setFieldLabel("State");
		state.setDisplayField("stateName");
		state.setStore(new ListStore<ModelData>());

		cpPart1.add(city, new FormData("90%"));
		city.setSize("150", "22");
		city.setFieldLabel("City");
		city.setValueField("id");
		city.setDisplayField("cityName");
		city.setStore(new ListStore<ModelData>());

		town.setFieldLabel("Town");
		cpPart1.add(town, new FormData("90%"));
		town.setSize("150", "22");
		town.setDisplayField("townName");
		town.setStore(new ListStore<ModelData>());

		village.setFieldLabel("Village");
		cpPart1.add(village, new FormData("90%"));
		village.setSize("150", "22");
		village.setDisplayField("villageName");
		village.setStore(new ListStore<ModelData>());

		chapterName.setFieldLabel("Chapter Name");
		cpPart1.add(chapterName, new FormData("90%"));
		chapterName.setSize("150", "22");
		chapterName.setDisplayField("name");
		chapterName.setStore(new ListStore<ModelData>());

		mainContainerPanel.add(cpMain);
		cpPart1.setSize("33%", "280px");

		LayoutContainer cpPart2 = new LayoutContainer();
		cpPart2.setLayout(new FormLayout());
		cpPart2.setSize("33%", "280px");
		cpPart2.add(locality, new FormData("100%"));
		locality.setFieldLabel("Locality");
		locality.setDisplayField("localityName");
		locality.setStore(new ListStore<ModelData>());
		locality.setWidth("150");

		cpPart2.add(screeningDate, new FormData("90%"));
		screeningDate.setFieldLabel("Date");

		cpPart2.add(processType, new FormData("90%"));
		processType.setFieldLabel("Process Type");
		processType.setDisplayField("name");
		processType.setStore(new ListStore<ModelData>());

		cpPart2.add(typeOfLocation, new FormData("90%"));
		typeOfLocation.setFieldLabel("Type of Location");
		typeOfLocation.setDisplayField("name");
		typeOfLocation.setStore(new ListStore<ModelData>());

		cpPart2.add(address, new FormData("100% -240"));
		address.setFieldLabel("Address");
		address.setWidth("150");

		cpPart2.add(contactInformation, new FormData("90% -235"));
		contactInformation.setFieldLabel("Contact Information");
		contactInformation.setWidth("150");

		TableData td_cpPart2 = new TableData();
		td_cpPart2.setPadding(5);
		cpMain.add(cpPart2, td_cpPart2);
		cpMain.setHeight("35%");

		LayoutContainer cpPart3 = new LayoutContainer();
		TableData td_cpPart3 = new TableData();
		td_cpPart3.setPadding(5);

		final ContentPanel cPanelDoctors = new ContentPanel();
		cPanelDoctors.setScrollMode(Scroll.AUTOY);
		cPanelDoctors.setHeading("Select Doctors");
		cPanelDoctors.setSize("150", "90");
		cPanelDoctors.add(doctors);
		cPanelDoctors.setBodyBorder(false);
		cPanelDoctors.setFrame(false);
		cPanelDoctors.setBorders(false);
		cpPart3.add(cPanelDoctors);
		doctors.setStore(new ListStore<ModelData>());
		doctors.setDisplayProperty("name");
		doctors.setStore(new ListStore<ModelData>());
//		doctors.setSize("150", "70");

		final ContentPanel cPanelVolunteers = new ContentPanel();
		cPanelVolunteers.setScrollMode(Scroll.AUTOY);
		cPanelVolunteers.setHeading("Select Volunteers");
		cPanelVolunteers.setSize("150", "90");
		cPanelVolunteers.add(volunteers);
		cpPart3.add(cPanelVolunteers);
		volunteers.setStore(new ListStore<ModelData>());
		volunteers.setDisplayProperty("name");
		volunteers.setStore(new ListStore<ModelData>());
//		volunteers.setSize("150", "70");

		cpMain.add(cpPart3, td_cpPart3);
		cpPart3.setSize("33%", "280");

		getColumnConfigs();

		editorGrid.setColumnLines(true);
		editorGrid.setLoadMask(true);
		editorGrid.setHeight("310px");

		final ContentPanel gridHolderPanel = new ContentPanel();
		gridHolderPanel.setHeading("Patient Details");
		gridHolderPanel.setHeaderVisible(true);

		ToolBar toolBar = new ToolBar();
		Button add = new Button("Add Patient");
		add.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				PatientDetailDTO plant = new PatientDetailDTO();
				editorGrid.stopEditing(false);
				store.insert(plant, 0);
				editorGrid.startEditing(0, 0);
			}
		});
		toolBar.add(add);

		Button remove = new Button("Remove Patient");
		add.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				editorGrid.stopEditing();
				ModelData selectedItem = editorGrid.getSelectionModel().getSelectedItem();
				if (selectedItem != null)
				{
					selectedItem.set("deleted", "Y");
					List<ModelData> lstModels = editorGrid.getStore().getModels();
					editorGrid.getStore().remove(selectedItem);
					editorGrid.mask("Removing Entry...");
				}
			}
		});
		toolBar.add(remove);

		gridHolderPanel.setTopComponent(toolBar);

		mainContainerPanel.setButtonAlign(HorizontalAlignment.CENTER);
		mainContainerPanel.addButton(new Button("Reset", new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
			}
		}));

		mainContainerPanel.addButton(new Button("Save", new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				IndexPage.maskCenterComponent("Saving...");
				ScreeningDetailDTO modelData = new ScreeningDetailDTO();
				modelData.setCountry((CountryDTO) country.getSelection().get(0));
				modelData.setState((StateDTO) state.getSelection().get(0));
				modelData.setCity((CityDTO) city.getSelection().get(0));
				modelData.setTown((TownDTO) town.getSelection().get(0));
				modelData.setVillage((VillageDTO) village.getSelection().get(0));
				modelData.setLocality((LocalityDTO) locality.getSelection().get(0));

				modelData.setProcessType((ProcessTypeDTO) processType.getSelection().get(0));
				modelData.setTypeOfLocation((TypeOfLocationDTO) typeOfLocation.getSelection().get(0));
				modelData.setScreeningDate(String.valueOf(screeningDate.getValue().getTime()));
				// modelData.setSetVolunteers(volunteers.getChecked());
				// modelData.setSetDoctors(doctors.getChecked());
				savePage(modelData);
			}
		}));

		gridHolderPanel.setLayout(new FitLayout());
		gridHolderPanel.add(editorGrid);
		gridHolderPanel.setHeight("380");

		mainContainerPanel.add(gridHolderPanel, new FitData(5));
		add(mainContainerPanel);
		mainContainerPanel.setSize("700px", "600px");

	}

	private ColumnModel getColumnModel(List<ColumnConfig> configs)
	{
		ColumnModel cm = new ColumnModel(configs);
		cm.addHeaderGroup(0, 8, new HeaderGroupConfig("Paediatric", 1, 5));
		cm.addHeaderGroup(0, 13, new HeaderGroupConfig("Dental", 1, 5));
		cm.addHeaderGroup(0, 18, new HeaderGroupConfig("Eye", 1, 5));
		cm.addHeaderGroup(0, 23, new HeaderGroupConfig("Skin", 1, 5));
		return cm;
	}

	private List<ColumnConfig> getColumnConfigs()
	{
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig nameColumn = new ColumnConfig("name", "Name", 150);
		configs.add(nameColumn);
		TextField<String> text = new TextField<String>();
		text.setAllowBlank(false);
		nameColumn.setEditor(new CellEditor(text));

		ColumnConfig ageColumn = new ColumnConfig("age", "Age", 50);
		configs.add(ageColumn);

		ColumnConfig sexColumn = new ColumnConfig("sex", "Sex", 50);
		configs.add(sexColumn);

		ColumnConfig classColumn = new ColumnConfig("class", "Class", 100);
		configs.add(classColumn);

		ColumnConfig heightColumn = new ColumnConfig("height", "Height(cm)", 100);
		configs.add(heightColumn);

		ColumnConfig weightColumn = new ColumnConfig("weight", "Weight(kg)", 100);
		configs.add(weightColumn);

		ColumnConfig addressColumn = new ColumnConfig("address", "Address", 100);
		configs.add(addressColumn);

		ColumnConfig contactNoColumn = new ColumnConfig("contactno", "Contact No.", 100);
		configs.add(contactNoColumn);

		ColumnConfig findingsPColumn = new ColumnConfig("findingsP", "Findings", 100);
		configs.add(findingsPColumn);

		ColumnConfig treatmentPColumn = new ColumnConfig("treatmentP", "Treatment Adviced", 100);
		configs.add(treatmentPColumn);

		ColumnConfig referralPColumn = new ColumnConfig("referralP", "Referral", 100);
		configs.add(referralPColumn);

		ColumnConfig emergencyPColumn = new ColumnConfig("emergencyP", "Emergency", 100);
		configs.add(emergencyPColumn);

		ColumnConfig medicinesPColumn = new ColumnConfig("medicinesP", "Medicines", 100);
		configs.add(medicinesPColumn);

		ColumnConfig findingsDColumn = new ColumnConfig("findingsD", "Findings", 100);
		configs.add(findingsDColumn);

		ColumnConfig treatmentD = new ColumnConfig("treatmentD", "Treatment Adviced", 100);
		configs.add(treatmentD);

		ColumnConfig referralD = new ColumnConfig("referralD", "Referral", 100);
		configs.add(referralD);

		ColumnConfig emergencyD = new ColumnConfig("emergencyD", "Emergency", 100);
		configs.add(emergencyD);

		ColumnConfig medicinesD = new ColumnConfig("medicinesD", "Medicines", 100);
		configs.add(medicinesD);

		ColumnConfig findingsE = new ColumnConfig("findingsE", "Findings", 100);
		configs.add(findingsE);

		ColumnConfig treatmentE = new ColumnConfig("treatmentE", "Treatment Adviced", 100);
		configs.add(treatmentE);

		ColumnConfig referralE = new ColumnConfig("referralE", "Referral", 100);
		configs.add(referralE);

		ColumnConfig emergencyE = new ColumnConfig("emergencyE", "Emergency", 100);
		configs.add(emergencyE);

		ColumnConfig medicinesE = new ColumnConfig("medicinesE", "Medicines", 100);
		configs.add(medicinesE);

		ColumnConfig findingsS = new ColumnConfig("findingsS", "Findings", 100);
		configs.add(findingsS);

		ColumnConfig treatmentS = new ColumnConfig("treatmentS", "Treatment Adviced", 100);
		configs.add(treatmentS);

		ColumnConfig referralS = new ColumnConfig("referralS", "Referral", 100);
		configs.add(referralS);

		ColumnConfig emergencyS = new ColumnConfig("emergencyS", "Emergency", 100);
		configs.add(emergencyS);

		ColumnConfig medicinesS = new ColumnConfig("medicinesS", "Medicines", 100);
		configs.add(medicinesS);

		for (ColumnConfig cc : configs)
		{
			cc.setEditor(new CellEditor(new TextField<String>()));
		}

		return configs;
	}

	public void initialize(String title)
	{
		mainContainerPanel.setHeading(title);

		detailServiceAsync.getModel("", new AsyncCallback<ModelData>()
		{

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(ModelData modelData)
			{
				editorGrid.reconfigure(new ListStore<ModelData>(), cm);
				country.getStore().add((List<ModelData>) modelData.get("lstCountry"));
				state.getStore().add((List<ModelData>) modelData.get("lstState"));
				city.getStore().add((List<ModelData>) modelData.get("lstCity"));
				town.getStore().add((List<ModelData>) modelData.get("lstTown"));
				village.getStore().add((List<ModelData>) modelData.get("lstVillage"));
				locality.getStore().add((List<ModelData>) modelData.get("lstLocality"));

				processType.getStore().add((List<ModelData>) modelData.get("lstProcessType"));
				typeOfLocation.getStore().add((List<ModelData>) modelData.get("lstTypeOfLocation"));
				volunteers.getStore().add((List<ModelData>) modelData.get("lstVolunteers"));
				doctors.getStore().add((List<ModelData>) modelData.get("lstDoctors"));
				IndexPage.unmaskCenterComponent();
			}

			@Override
			public void onFailure(Throwable caught)
			{
			}
		});

	}

	private void savePage(ScreeningDetailDTO model)
	{
		detailServiceAsync.saveModel("", model, new AsyncCallback<String>()
		{
			final Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>()
			{
				public void handleEvent(MessageBoxEvent ce)
				{
				}
			};

			@Override
			public void onFailure(Throwable caught)
			{
				IndexPage.unmaskCenterComponent();
				MessageBox.alert("Alert", "Error encountered while saving", l);
			}

			@Override
			public void onSuccess(String result)
			{
				IndexPage.unmaskCenterComponent();
			}
		});
	}
}
