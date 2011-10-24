package com.varun.yfs.client.screening;

import java.util.ArrayList;
import java.util.Date;
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
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
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
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.index.IndexPage;
import com.varun.yfs.client.screening.rpc.ScreeningDetailService;
import com.varun.yfs.client.screening.rpc.ScreeningDetailServiceAsync;
import com.varun.yfs.dto.ChapterNameDTO;
import com.varun.yfs.dto.CityDTO;
import com.varun.yfs.dto.CountryDTO;
import com.varun.yfs.dto.DoctorDTO;
import com.varun.yfs.dto.GenderDTO;
import com.varun.yfs.dto.LocalityDTO;
import com.varun.yfs.dto.PatientDetailDTO;
import com.varun.yfs.dto.ProcessTypeDTO;
import com.varun.yfs.dto.ReferralTypeDTO;
import com.varun.yfs.dto.ScreeningDetailDTO;
import com.varun.yfs.dto.StateDTO;
import com.varun.yfs.dto.TownDTO;
import com.varun.yfs.dto.TypeOfLocationDTO;
import com.varun.yfs.dto.VillageDTO;
import com.varun.yfs.dto.VolunteerDTO;
import com.varun.yfs.dto.YesNoDTO;

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
	private final CheckBoxListView<VolunteerDTO> volunteers = new CheckBoxListView<VolunteerDTO>();
	private final CheckBoxListView<DoctorDTO> doctors = new CheckBoxListView<DoctorDTO>();
	private final TextArea address = new TextArea();
	private final TextArea contactInformation = new TextArea();
	private final DateField screeningDate = new DateField();

	private ListStore<PatientDetailDTO> editorGridStore;
	private EditorGrid<PatientDetailDTO> editorGrid;
	private String scrId;
	
	public EditorGrid<PatientDetailDTO> getEditorGrid()
	{
		return editorGrid;
	}

	public void setEditorGrid(EditorGrid<PatientDetailDTO> editorGrid)
	{
		this.editorGrid = editorGrid;
	}

	public ScreeningDetail()
	{
	}

	protected final Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>()
	{
		public void handleEvent(MessageBoxEvent ce)
		{
		}
	};

	@Override
	protected void onRender(Element parent, int index)
	{
		super.onRender(parent, index);

		setLayout(new FitLayout());
		setLayoutData(new FitData(5));

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
		country.setForceSelection(true);
		country.setTriggerAction(TriggerAction.ALL);
		country.setStore(new ListStore<ModelData>());

		state.setEditable(false);
		cpPart1.add(state, new FormData("90%"));
		state.setSize("150", "22");
		state.setFieldLabel("State");
		state.setDisplayField("stateName");
		state.setTriggerAction(TriggerAction.ALL);
		state.setStore(new ListStore<ModelData>());

		cpPart1.add(city, new FormData("90%"));
		city.setSize("150", "22");
		city.setFieldLabel("City");
		city.setDisplayField("cityName");
		city.setTriggerAction(TriggerAction.ALL);
		city.setStore(new ListStore<ModelData>());

		town.setFieldLabel("Town");
		cpPart1.add(town, new FormData("90%"));
		town.setSize("150", "22");
		town.setDisplayField("townName");
		town.setTriggerAction(TriggerAction.ALL);
		town.setStore(new ListStore<ModelData>());

		village.setFieldLabel("Village");
		cpPart1.add(village, new FormData("90%"));
		village.setSize("150", "22");
		village.setDisplayField("villageName");
		village.setTriggerAction(TriggerAction.ALL);
		village.setStore(new ListStore<ModelData>());

		chapterName.setFieldLabel("Chapter Name");
		cpPart1.add(chapterName, new FormData("90%"));
		chapterName.setSize("150", "22");
		chapterName.setDisplayField("name");
		chapterName.setTriggerAction(TriggerAction.ALL);
		chapterName.setStore(new ListStore<ModelData>());

		mainContainerPanel.add(cpMain);
		cpPart1.setSize("33%", "280px");

		LayoutContainer cpPart2 = new LayoutContainer();
		cpPart2.setLayout(new FormLayout());
		cpPart2.setSize("33%", "280px");
		cpPart2.add(locality, new FormData("100%"));
		locality.setFieldLabel("Locality");
		locality.setDisplayField("localityName");
		locality.setTriggerAction(TriggerAction.ALL);
		locality.setStore(new ListStore<ModelData>());
		locality.setWidth("150");

		cpPart2.add(screeningDate, new FormData("90%"));
		screeningDate.setFieldLabel("Date");
		screeningDate.setAllowBlank(false);

		cpPart2.add(processType, new FormData("90%"));
		processType.setFieldLabel("Process Type");
		processType.setDisplayField("name");
		processType.setTriggerAction(TriggerAction.ALL);
		processType.setStore(new ListStore<ModelData>());

		cpPart2.add(typeOfLocation, new FormData("90%"));
		typeOfLocation.setFieldLabel("Type of Location");
		typeOfLocation.setDisplayField("name");
		typeOfLocation.setTriggerAction(TriggerAction.ALL);
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
		doctors.setStore(new ListStore<DoctorDTO>());
		doctors.setDisplayProperty("name");

		final ContentPanel cPanelVolunteers = new ContentPanel();
		cPanelVolunteers.setScrollMode(Scroll.AUTOY);
		cPanelVolunteers.setHeading("Select Volunteers");
		cPanelVolunteers.setSize("150", "90");
		cPanelVolunteers.add(volunteers);
		cPanelVolunteers.setBodyBorder(false);
		cPanelVolunteers.setFrame(false);
		cPanelVolunteers.setBorders(false);
		cpPart3.add(cPanelVolunteers);
		volunteers.setStore(new ListStore<VolunteerDTO>());
		volunteers.setDisplayProperty("name");

		cpMain.add(cpPart3, td_cpPart3);
		cpPart3.setSize("33%", "280");

		editorGridStore = new ListStore<PatientDetailDTO>();
		ColumnModel columnModel = getColumnModel();
		editorGrid = new EditorGrid<PatientDetailDTO>(editorGridStore, columnModel);
		// editorGrid.reconfigure(editorGridStore, columnModel);
		editorGrid.setBorders(true);
		editorGrid.setSelectionModel(new GridSelectionModel<PatientDetailDTO>());
		editorGrid.setLoadMask(true);
		editorGrid.setColumnLines(true);
		editorGrid.setLoadMask(true);
		editorGrid.setHeight("310px");
		editorGrid.setClicksToEdit(EditorGrid.ClicksToEdit.ONE);

		final ContentPanel gridHolderPanel = new ContentPanel();
		gridHolderPanel.setHeading("Patient Details");
		gridHolderPanel.setHeaderVisible(true);

		ToolBar toolBar = new ToolBar();
		Button add = new Button("Add");
		add.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				editorGrid.unmask();
				PatientDetailDTO patientDetail = new PatientDetailDTO();
				patientDetail.setDeleted("N");
				editorGrid.stopEditing();
				editorGridStore.insert(patientDetail, 0);
				editorGrid.startEditing(editorGridStore.indexOf(patientDetail), 0);
			}
		});
		toolBar.add(add);

		Button remove = new Button("Remove");
		remove.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				editorGrid.stopEditing();
				PatientDetailDTO selectedItem = editorGrid.getSelectionModel().getSelectedItem();
				if (selectedItem != null)
				{
					selectedItem.set("deleted", "Y");
					editorGrid.mask("Removing Entry...");
					ScreeningDetailDTO modelData = extractFormData();
					savePage(modelData);
					editorGrid.getStore().remove(selectedItem);
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
				clearStores();
				initialize(getTitle(), scrId);
			}
		}));

		mainContainerPanel.addButton(new Button("Save", new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				if (!validateFormEntry())
					return;

				ScreeningDetailDTO modelData = extractFormData();
				savePage(modelData);
			}

			private boolean validateFormEntry()
			{
				boolean validationState = true;
				if (country.getSelection().isEmpty())
				{
					Info.display("New Screening", "You need to select a country to proceed.");
					validationState = false;
				}
				if (state.getSelection().isEmpty())
				{
					Info.display("New Screening", "You need to select a state to proceed.");
					validationState = false;
				}
				if (city.getSelection().isEmpty())
				{
					Info.display("New Screening", "You need to select a city to proceed.");
					validationState = false;
				}
				if (town.getSelection().isEmpty())
				{
					Info.display("New Screening", "You need to select a town to proceed.");
					validationState = false;
				}
				if (village.getSelection().isEmpty())
				{
					Info.display("New Screening", "You need to select a village to proceed.");
					validationState = false;
				}
				if (chapterName.getSelection().isEmpty())
				{
					Info.display("New Screening", "You need to select a Chapter Name to proceed.");
					validationState = false;
				}
				if (locality.getSelection().isEmpty())
				{
					Info.display("New Screening", "You need to select a Locality to proceed.");
					validationState = false;
				}
				if (screeningDate.getValue() == null)
				{
					Info.display("New Screening", "You need to select a Screening-Date to proceed.");
					validationState = false;
				}
				if (processType.getSelection().isEmpty())
				{
					Info.display("New Screening", "You need to select a Process Type to proceed.");
					validationState = false;
				}
				if (typeOfLocation.getSelection().isEmpty())
				{
					Info.display("New Screening", "You need to select a Type Of Location to proceed.");
					validationState = false;
				}
				if (address != null && address.getValue().isEmpty())
				{
					Info.display("New Screening", "You need to enter the Address to proceed.");
					validationState = false;
				}
				if (contactInformation != null && contactInformation.getValue().isEmpty())
				{
					Info.display("New Screening", "You need to enter the Contact Information to proceed.");
					validationState = false;
				}
				return validationState;
			}
		}));

		gridHolderPanel.setLayout(new FitLayout());
		gridHolderPanel.add(editorGrid);
		gridHolderPanel.setHeight("380");

		mainContainerPanel.add(gridHolderPanel, new FitData(5));
		add(mainContainerPanel);
//		mainContainerPanel.setSize("700px", "600px");

	}

	private ScreeningDetailDTO extractFormData()
	{
		IndexPage.maskCenterComponent("Saving...");
		ScreeningDetailDTO modelData = new ScreeningDetailDTO();
		modelData.setCountry((CountryDTO) country.getSelection().get(0));
		modelData.setState((StateDTO) state.getSelection().get(0));
		modelData.setCity((CityDTO) city.getSelection().get(0));
		modelData.setTown((TownDTO) town.getSelection().get(0));
		modelData.setVillage((VillageDTO) village.getSelection().get(0));
		modelData.setLocality((LocalityDTO) locality.getSelection().get(0));

		modelData.setChapterName((ChapterNameDTO) chapterName.getSelection().get(0));
		modelData.setProcessType((ProcessTypeDTO) processType.getSelection().get(0));
		modelData.setTypeOfLocation((TypeOfLocationDTO) typeOfLocation.getSelection().get(0));
		modelData.setScreeningDate(String.valueOf(screeningDate.getValue().getTime()));
		modelData.setContactInformation(contactInformation.getValue());
		modelData.setAddress(address.getValue());
		modelData.setVolunteers(volunteers.getChecked());
		modelData.setDoctors(doctors.getChecked());

		editorGrid.stopEditing();
		editorGridStore.commitChanges();
		List<PatientDetailDTO> models = editorGridStore.getModels();
		modelData.setPatientDetails(models);
		return modelData;
	}

	private ColumnModel getColumnModel()
	{
		List<ColumnConfig> configs = getColumnConfigs();
		return new ColumnModel(configs);
	}

	private List<ColumnConfig> getColumnConfigs()
	{
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig nameColumn = new ColumnConfig("id", "Id", 20);
		configs.add(nameColumn);

		nameColumn = new ColumnConfig("name", "Name", 150);
		TextField<String> textField = new TextField<String>();
		textField.setAllowBlank(false);
		textField.setMinLength(2);
		textField.setMaxLength(255);
		nameColumn.setEditor(new CellEditor(textField));
		configs.add(nameColumn);

		ColumnConfig ageColumn = new ColumnConfig("age", "Age", 50);
		NumberField numField = new NumberField();
		numField.setAllowBlank(false);
		numField.setMinLength(1);
		numField.setMaxLength(3);
		numField.setPropertyEditorType(Integer.class);
		ageColumn.setEditor(new CellEditor(numField));
		configs.add(ageColumn);

		ColumnConfig sexColumn = new ColumnConfig("sex", "Sex", 50);
		ComboBox<GenderDTO> field = new ComboBox<GenderDTO>();
		field.setDisplayField("name");
		field.setValueField("name");
		field.setStore(GenderDTO.getValues());
		field.setTriggerAction(TriggerAction.ALL);
		field.setEditable(false);
		sexColumn.setEditor(new CellEditor(field));
		configs.add(sexColumn);

		ColumnConfig classColumn = new ColumnConfig("standard", "Standard", 100);
		textField = new TextField<String>();
		textField.setAllowBlank(false);
		textField.setMinLength(2);
		textField.setMaxLength(4);
		classColumn.setEditor(new CellEditor(textField));
		configs.add(classColumn);

		ColumnConfig heightColumn = new ColumnConfig("height", "Height(cm)", 100);
		numField = new NumberField();
		numField.setAllowBlank(false);
		numField.setMinLength(1);
		numField.setMaxLength(3);
		numField.setPropertyEditorType(Integer.class);
		heightColumn.setEditor(new CellEditor(numField));
		configs.add(heightColumn);

		ColumnConfig weightColumn = new ColumnConfig("weight", "Weight(kg)", 100);
		numField = new NumberField();
		numField.setAllowBlank(false);
		numField.setMinLength(1);
		numField.setMaxLength(3);
		numField.setPropertyEditorType(Integer.class);
		weightColumn.setEditor(new CellEditor(numField));
		configs.add(weightColumn);

		ColumnConfig addressColumn = new ColumnConfig("address", "Address", 100);
		textField = new TextField<String>();
		textField.setAllowBlank(false);
		textField.setMinLength(2);
		textField.setMaxLength(255);
		addressColumn.setEditor(new CellEditor(textField));
		configs.add(addressColumn);

		ColumnConfig contactNoColumn = new ColumnConfig("contactNo", "Contact No.", 100);
		numField = new NumberField();
		numField.setAllowBlank(false);
		numField.setMinLength(4);
		numField.setMaxLength(14);
		numField.setPropertyEditorType(Integer.class);
		contactNoColumn.setEditor(new CellEditor(numField));
		configs.add(contactNoColumn);

		ColumnConfig findingsPColumn = new ColumnConfig("findings", "Findings", 100);
		textField = new TextField<String>();
		textField.setAllowBlank(false);
		textField.setMinLength(2);
		textField.setMaxLength(255);
		addressColumn.setEditor(new CellEditor(textField));
		configs.add(findingsPColumn);

		ColumnConfig referral1Column = new ColumnConfig("referral1", "Referral 1", 100);
		configs.add(referral1Column);

		ColumnConfig medicines2Column = new ColumnConfig("referral2", "Referral 2", 100);
		configs.add(medicines2Column);

		ColumnConfig medicines3Column = new ColumnConfig("referral3", "Referral 3", 100);
		configs.add(medicines3Column);

		ColumnConfig emergency = new ColumnConfig("emergency", "Emergency", 100);
		ComboBox<YesNoDTO> yesNoDto = new ComboBox<YesNoDTO>();
		yesNoDto.setDisplayField("name");
		yesNoDto.setValueField("name");
		yesNoDto.setEditable(false);
		yesNoDto.setStore(YesNoDTO.getValues());
		yesNoDto.setTriggerAction(TriggerAction.ALL);
		emergency.setEditor(new CellEditor(yesNoDto));
		configs.add(emergency);

		ColumnConfig surgeryCase = new ColumnConfig("surgeryCase", "Surgery Case", 100);
		yesNoDto = new ComboBox<YesNoDTO>();
		yesNoDto.setDisplayField("name");
		yesNoDto.setValueField("name");
		yesNoDto.setEditable(false);
		yesNoDto.setStore(YesNoDTO.getValues());
		yesNoDto.setTriggerAction(TriggerAction.ALL);
		surgeryCase.setEditor(new CellEditor(yesNoDto));
		configs.add(surgeryCase);

		ColumnConfig caseClosed = new ColumnConfig("caseClosed", "Case Closed", 100);
		yesNoDto = new ComboBox<YesNoDTO>();
		yesNoDto.setDisplayField("name");
		yesNoDto.setValueField("name");
		yesNoDto.setEditable(false);
		yesNoDto.setStore(YesNoDTO.getValues());
		yesNoDto.setTriggerAction(TriggerAction.ALL);
		caseClosed.setEditor(new CellEditor(yesNoDto));
		configs.add(caseClosed);

		return configs;
	}

	public void initialize(String title, String scrId)
	{
		mainContainerPanel.setHeading(title);
		this.scrId = scrId;

		detailServiceAsync.getModel(scrId, new AsyncCallback<ModelData>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(ModelData modelData)
			{
				clearStores();

				country.getStore().add((List<ModelData>) modelData.get("lstCountry"));
				state.getStore().add((List<ModelData>) modelData.get("lstState"));
				city.getStore().add((List<ModelData>) modelData.get("lstCity"));
				town.getStore().add((List<ModelData>) modelData.get("lstTown"));
				village.getStore().add((List<ModelData>) modelData.get("lstVillage"));
				locality.getStore().add((List<ModelData>) modelData.get("lstLocality"));

				chapterName.getStore().add((List<ModelData>) modelData.get("lstChapterName"));
				processType.getStore().add((List<ModelData>) modelData.get("lstProcessType"));
				typeOfLocation.getStore().add((List<ModelData>) modelData.get("lstTypeOfLocation"));
				volunteers.getStore().add((List<VolunteerDTO>) modelData.get("lstVolunteers"));
				doctors.getStore().add((List<DoctorDTO>) modelData.get("lstDoctors"));

				List<ReferralTypeDTO> lst = (List<ReferralTypeDTO>) modelData.get("lstReferralTypes");

				ColumnConfig columnById = editorGrid.getColumnModel().getColumnById("referral1");
				ComboBox<ReferralTypeDTO> field = new ComboBox<ReferralTypeDTO>();
				field.setStore(new ListStore<ReferralTypeDTO>());
				field.setDisplayField("name");
				field.setValueField("name");
				field.setEditable(false);
				columnById.setEditor(new CellEditor(field));
				field.getStore().add(lst);

				columnById = editorGrid.getColumnModel().getColumnById("referral2");
				field = new ComboBox<ReferralTypeDTO>();
				field.setStore(new ListStore<ReferralTypeDTO>());
				field.setDisplayField("name");
				field.setValueField("name");
				field.setEditable(false);
				columnById.setEditor(new CellEditor(field));
				field.getStore().add(lst);

				columnById = editorGrid.getColumnModel().getColumnById("referral3");
				field = new ComboBox<ReferralTypeDTO>();
				field.setStore(new ListStore<ReferralTypeDTO>());
				field.setDisplayField("name");
				field.setValueField("name");
				field.setEditable(false);
				columnById.setEditor(new CellEditor(field));
				field.getStore().add(lst);

				ScreeningDetailDTO scrDto = modelData.get("data");
				if (scrDto != null)
				{
					address.setValue(scrDto.getAddress());
					chapterName.setValue(scrDto.getChapterName());
					city.setValue(scrDto.getCity());
					contactInformation.setValue(scrDto.getContactInformation());
					country.setValue(scrDto.getCountry());
					locality.setValue(scrDto.getLocality());
					processType.setValue(scrDto.getProcessType());
					screeningDate.setValue(new Date(Long.valueOf(scrDto.getScreeningDate())));
					state.setValue(scrDto.getState());
					town.setValue(scrDto.getTown());
					typeOfLocation.setValue(scrDto.getTypeOfLocation());
					village.setValue(scrDto.getVillage());

					for (DoctorDTO doctor : scrDto.getDoctors())
					{
						doctors.setChecked(doctor, true);
					}

					for (VolunteerDTO volunteer : scrDto.getVolunteers())
					{
						volunteers.setChecked(volunteer, true);
					}

					editorGridStore.removeAll();
					List<PatientDetailDTO> patientDetails = scrDto.getPatientDetails();
					editorGridStore.add(patientDetails);
				}
				IndexPage.unmaskCenterComponent();
			}

			@Override
			public void onFailure(Throwable caught)
			{
				MessageBox.alert("Alert", "Error encountered while loading", l);
			}
		});

	}

	private void savePage(ScreeningDetailDTO model)
	{
		detailServiceAsync.saveModel(this.scrId, model, new AsyncCallback<RpcStatusEnum>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				IndexPage.unmaskCenterComponent();
				MessageBox.alert("Alert", "Error encountered while saving", l);
			}

			@Override
			public void onSuccess(RpcStatusEnum result)
			{
				IndexPage.unmaskCenterComponent();
				editorGrid.unmask();
				if (result.compareTo(RpcStatusEnum.FAILURE) == 0)
				{
					MessageBox.alert("Alert", "Error encountered while saving", l);
				} else
				{
					// clearStores();
					Info.display("Screening Detail", "Save Completed Sucessfully.");
					IndexPage.reinitScreeningPanel();
				}
			}
		});
	}

	private void clearStores()
	{
		country.clearSelections();
		state.clearSelections();
		city.clearSelections();
		town.clearSelections();
		village.clearSelections();
		locality.clearSelections();
		chapterName.clearSelections();
		processType.clearSelections();
		typeOfLocation.clearSelections();
		address.clear();
		contactInformation.clear();
		screeningDate.clear();

		country.getStore().removeAll();
		state.getStore().removeAll();
		city.getStore().removeAll();
		town.getStore().removeAll();
		village.getStore().removeAll();
		locality.getStore().removeAll();
		chapterName.getStore().removeAll();
		processType.getStore().removeAll();
		typeOfLocation.getStore().removeAll();
		doctors.getStore().removeAll();
		volunteers.getStore().removeAll();
		editorGridStore.removeAll();
	}
}
