package com.varun.yfs.client.screening.school;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.RowNumberer;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.images.YfsImageBundle;
import com.varun.yfs.client.index.IndexPage;
import com.varun.yfs.client.screening.export.ExportService;
import com.varun.yfs.client.screening.export.ExportServiceAsync;
import com.varun.yfs.client.screening.imports.ImportDetail;
import com.varun.yfs.client.screening.imports.ImportType;
import com.varun.yfs.client.screening.school.rpc.SchoolScreeningDetailService;
import com.varun.yfs.client.screening.school.rpc.SchoolScreeningDetailServiceAsync;
import com.varun.yfs.dto.ChapterNameDTO;
import com.varun.yfs.dto.CityDTO;
import com.varun.yfs.dto.CountryDTO;
import com.varun.yfs.dto.DoctorDTO;
import com.varun.yfs.dto.ExportTableDTO;
import com.varun.yfs.dto.GenderDTO;
import com.varun.yfs.dto.LocalityDTO;
import com.varun.yfs.dto.ProcessTypeDTO;
import com.varun.yfs.dto.ProjectDTO;
import com.varun.yfs.dto.ReferralTypeDTO;
import com.varun.yfs.dto.SchoolPatientDetailDTO;
import com.varun.yfs.dto.SchoolScreeningDetailDTO;
import com.varun.yfs.dto.StateDTO;
import com.varun.yfs.dto.TownDTO;
import com.varun.yfs.dto.TypeOfLocationDTO;
import com.varun.yfs.dto.VillageDTO;
import com.varun.yfs.dto.VolunteerDTO;
import com.varun.yfs.dto.YesNoDTO;

public class SchoolScreeningDetail extends LayoutContainer
{
	private final String headerText = "School Screening";
	private final SchoolScreeningDetailServiceAsync detailServiceAsync = GWT.create(SchoolScreeningDetailService.class);
	private final ExportServiceAsync exportServiceAsync = GWT.create(ExportService.class);

	protected ContentPanel mainContainerPanel = new ContentPanel();
	private final ComboBox<ModelData> country = new ComboBox<ModelData>();
	private final ComboBox<ModelData> state = new ComboBox<ModelData>();
	private final ComboBox<ModelData> city = new ComboBox<ModelData>();
	private final ComboBox<ModelData> town = new ComboBox<ModelData>();
	private final ComboBox<ModelData> village = new ComboBox<ModelData>();
	private final ComboBox<ModelData> chapterName = new ComboBox<ModelData>();
	private final ComboBox<ModelData> projectName = new ComboBox<ModelData>();
	private final ComboBox<ModelData> locality = new ComboBox<ModelData>();
	private final ComboBox<ModelData> processType = new ComboBox<ModelData>();
	private final ComboBox<ModelData> typeOfLocation = new ComboBox<ModelData>();
	private final CheckBoxListView<VolunteerDTO> volunteers = new CheckBoxListView<VolunteerDTO>();
	private final CheckBoxListView<DoctorDTO> doctors = new CheckBoxListView<DoctorDTO>();
	private final TextArea address = new TextArea();
	private final TextArea contactInformation = new TextArea();
	private final DateField screeningDate = new DateField();

	private ListStore<SchoolPatientDetailDTO> editorGridStore;
	private EditorGrid<SchoolPatientDetailDTO> editorGrid;
	private String scrId;

	public EditorGrid<SchoolPatientDetailDTO> getEditorGrid()
	{
		return editorGrid;
	}

	public void setEditorGrid(EditorGrid<SchoolPatientDetailDTO> editorGrid)
	{
		this.editorGrid = editorGrid;
	}

	public SchoolScreeningDetail()
	{
	}

	protected final Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>()
	{
		@Override
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
		cpMain.setLayout(new TableLayout(4));

		LayoutContainer cpPart1 = new LayoutContainer();
		cpPart1.setLayout(new FormLayout());
		TableData td_cpPart1 = new TableData();
		td_cpPart1.setPadding(5);
		cpMain.add(cpPart1, td_cpPart1);

		cpPart1.add(chapterName, new FormData("90%"));
		chapterName.setFieldLabel("Chapter Name");
		chapterName.setDisplayField("chapterName");
		chapterName.setTriggerAction(TriggerAction.ALL);
		chapterName.setStore(new ListStore<ModelData>());
		chapterName.setAllowBlank(false);
		chapterName.setForceSelection(true);

		cpPart1.add(projectName, new FormData("90%"));
		projectName.setFieldLabel("Project Name");
		projectName.setDisplayField("projectName");
		projectName.setTriggerAction(TriggerAction.ALL);
		projectName.setStore(new ListStore<ModelData>());
		projectName.setAllowBlank(false);
		projectName.setForceSelection(true);

		cpPart1.add(screeningDate, new FormData("90%"));
		screeningDate.setFieldLabel("Date");
		screeningDate.setAllowBlank(false);

		cpPart1.add(country, new FormData("90%"));
		country.setEditable(false);
		country.setFieldLabel("Country");
		country.setDisplayField("countryName");
		country.setForceSelection(true);
		country.setTriggerAction(TriggerAction.ALL);
		country.setStore(new ListStore<ModelData>());
		country.setForceSelection(true);

		cpPart1.add(state, new FormData("90%"));
		state.setEditable(false);
		state.setFieldLabel("State");
		state.setDisplayField("stateName");
		state.setTriggerAction(TriggerAction.ALL);
		state.setStore(new ListStore<ModelData>());
		state.setForceSelection(true);

		cpPart1.add(city, new FormData("90%"));
		city.setEditable(false);
		city.setFieldLabel("City");
		city.setDisplayField("cityName");
		city.setTriggerAction(TriggerAction.ALL);
		city.setStore(new ListStore<ModelData>());
		city.setForceSelection(true);

		cpPart1.add(town, new FormData("90%"));
		town.setFieldLabel("Town");
		town.setDisplayField("townName");
		town.setTriggerAction(TriggerAction.ALL);
		town.setStore(new ListStore<ModelData>());
		town.setForceSelection(true);

		cpPart1.add(village, new FormData("90%"));
		village.setFieldLabel("Village");
		village.setDisplayField("villageName");
		village.setTriggerAction(TriggerAction.ALL);
		village.setStore(new ListStore<ModelData>());
		village.setForceSelection(true);

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
		locality.setForceSelection(true);

		cpPart2.add(processType, new FormData("90%"));
		processType.setFieldLabel("Process Type");
		processType.setDisplayField("name");
		processType.setTriggerAction(TriggerAction.ALL);
		processType.setStore(new ListStore<ModelData>());
		processType.setForceSelection(true);

		cpPart2.add(typeOfLocation, new FormData("90%"));
		typeOfLocation.setFieldLabel("Type of Location");
		typeOfLocation.setDisplayField("name");
		typeOfLocation.setTriggerAction(TriggerAction.ALL);
		typeOfLocation.setStore(new ListStore<ModelData>());
		typeOfLocation.setForceSelection(true);

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
		cpMain.add(cpPart3, td_cpPart3);
		cpPart3.setSize("33%", "280");

		final ContentPanel cPanelDoctors = new ContentPanel();
		cPanelDoctors.setScrollMode(Scroll.AUTOY);
		cPanelDoctors.setHeading("Select Doctors");
		cPanelDoctors.setWidth("150");
		cPanelDoctors.add(doctors);
		cPanelDoctors.setBodyBorder(false);
		cPanelDoctors.setFrame(false);
		cPanelDoctors.setBorders(false);
		cpPart3.add(cPanelDoctors, new FitData());
		doctors.setStore(new ListStore<DoctorDTO>());
		doctors.setDisplayProperty("name");

		LayoutContainer cpPart4 = new LayoutContainer();
		TableData td_cpPart4 = new TableData();
		td_cpPart4.setPadding(5);
		cpMain.add(cpPart4, td_cpPart4);
		cpPart4.setSize("33%", "280");

		final ContentPanel cPanelVolunteers = new ContentPanel();
		cPanelVolunteers.setScrollMode(Scroll.AUTOY);
		cPanelVolunteers.setHeading("Select Volunteers");
		cPanelVolunteers.setWidth("150");
		cPanelVolunteers.add(volunteers);
		cPanelVolunteers.setBodyBorder(false);
		cPanelVolunteers.setFrame(false);
		cPanelVolunteers.setBorders(false);
		cpPart4.add(cPanelVolunteers, new FitData());
		volunteers.setStore(new ListStore<VolunteerDTO>());
		volunteers.setDisplayProperty("name");

		editorGridStore = new ListStore<SchoolPatientDetailDTO>();
		ColumnModel columnModel = getColumnModel();
		editorGrid = new EditorGrid<SchoolPatientDetailDTO>(editorGridStore, columnModel);
		editorGrid.setBorders(true);
//		editorGrid.setSelectionModel(new GridSelectionModel<SchoolPatientDetailDTO>());
		editorGrid.setLoadMask(true);
		editorGrid.setColumnLines(true);
		editorGrid.setLoadMask(true);
		editorGrid.setHeight("300px");
		editorGrid.setClicksToEdit(EditorGrid.ClicksToEdit.ONE);

		final ContentPanel gridHolderPanel = new ContentPanel();
		gridHolderPanel.setHeading("Patient Details");
		gridHolderPanel.setHeaderVisible(true);

		ToolBar toolBar = new ToolBar();
		Button add = new Button("Add", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.addButtonIcon()));
		add.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				editorGrid.unmask();
				editorGrid.stopEditing();
				
				SchoolPatientDetailDTO patientDetail = new SchoolPatientDetailDTO();
				patientDetail.setDeleted("N");

				editorGridStore.insert(patientDetail, 0);
				editorGrid.startEditing(editorGridStore.indexOf(patientDetail), 0);
				editorGrid.getSelectionModel().deselectAll();
			}
		});
		toolBar.add(add);

		toolBar.add(new SeparatorToolItem());

		Button remove = new Button("Remove", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.deleteButtonIcon()));
		remove.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				editorGrid.stopEditing();
				SchoolPatientDetailDTO selectedItem = editorGrid.getSelectionModel().getSelectedItem();
				if (selectedItem != null)
				{
					selectedItem.set("deleted", "Y");
					// editorGrid.mask("Removing Entry...");
					validateAndSave();
					editorGrid.getStore().remove(selectedItem);
				}
			}
		});
		toolBar.add(remove);
		toolBar.add(new SeparatorToolItem());

		final FormPanel formPanel = new FormPanel();

		final HiddenField<String> exportedFileName = new HiddenField<String>();
		exportedFileName.setName("ExportedFilename");
		formPanel.add(exportedFileName);

		toolBar.add(new FillToolItem());

		SplitButton splitItem = new SplitButton("");
		splitItem.setIcon(AbstractImagePrototype.create(YfsImageBundle.INSTANCE.exportButtonIcon()));

		Menu menu = new Menu();
		splitItem.setMenu(menu);

		toolBar.add(splitItem);

		MenuItem exportAll = new MenuItem("Export All", AbstractImagePrototype.create(YfsImageBundle.INSTANCE
				.exportButtonIcon()));
		exportAll.addSelectionListener(new SelectionListener<MenuEvent>()
		{
			@Override
			public void componentSelected(MenuEvent ce)
			{
				List<String> headers = new ArrayList<String>();
				List<ColumnConfig> columns = editorGrid.getColumnModel().getColumns();
				columns = columns.subList(1, columns.size());
				for (ColumnConfig columnConfig : columns)
				{
					headers.add(columnConfig.getHeader());
				}
				List<SchoolPatientDetailDTO> models = editorGridStore.getModels();
				ExportTableDTO expTab = new ExportTableDTO();
				expTab.setColHeaders(headers);
				expTab.setLstData(models);
				exportServiceAsync.createExportFile(Arrays.asList(expTab), null, new AsyncCallback<String>()
				{
					@Override
					public void onFailure(Throwable caught)
					{
						IndexPage.unmaskCenterComponent();
						MessageBox.alert("Alert", "Error encountered while exporting." + caught.getMessage(), l);
					}

					@Override
					public void onSuccess(String result)
					{
						exportedFileName.setValue(result);

						String url = GWT.getModuleBaseURL();
						url = url + "exportServlet";

						formPanel.setAction(url);
						formPanel.submit();
					}

				});

			}
		});
		menu.add(exportAll);

		MenuItem exportReferral = new MenuItem("Export Referrals",
				AbstractImagePrototype.create(YfsImageBundle.INSTANCE.exportButtonIcon()));
		exportReferral.addSelectionListener(new SelectionListener<MenuEvent>()
		{
			@Override
			public void componentSelected(MenuEvent ce)
			{
				List<String> headers = new ArrayList<String>();
				List<ColumnConfig> columns = editorGrid.getColumnModel().getColumns();
				columns = columns.subList(1, columns.size());
				for (ColumnConfig columnConfig : columns)
				{
					headers.add(columnConfig.getHeader());
				}

				StoreFilter<SchoolPatientDetailDTO> filterReferrals = new StoreFilter<SchoolPatientDetailDTO>()
				{
					@Override
					public boolean select(Store<SchoolPatientDetailDTO> store, SchoolPatientDetailDTO parent,
							SchoolPatientDetailDTO item, String property)
					{
						if (item.getReferral1() != null || item.getReferral2() != null)
							return true;

						// if (item.getReferral1().isEmpty() ||
						// item.getReferral2().isEmpty())
						// return false;

						// if (item.getReferral3() == null)
						// return false;
						return false;
					}
				};
				editorGridStore.addFilter(filterReferrals);
				editorGridStore.applyFilters("referral1");

				List<SchoolPatientDetailDTO> models = editorGridStore.getModels();
				ExportTableDTO expTab = new ExportTableDTO();
				expTab.setColHeaders(headers);
				expTab.setLstData(models);
				exportServiceAsync.createExportFile(Arrays.asList(expTab), null, new AsyncCallback<String>()
				{
					@Override
					public void onFailure(Throwable caught)
					{
						IndexPage.unmaskCenterComponent();
						MessageBox.alert("Alert", "Error encountered while exporting." + caught.getCause(), l);
					}

					@Override
					public void onSuccess(String result)
					{
						exportedFileName.setValue(result);

						String url = GWT.getModuleBaseURL();
						url = url + "exportServlet";

						formPanel.setAction(url);
						formPanel.submit();

						editorGridStore.clearFilters();
					}

				});

			}
		});
		menu.add(exportReferral);

		Button importPatientDetail = new Button("Import", AbstractImagePrototype.create(YfsImageBundle.INSTANCE
				.importButtonIcon()));
		importPatientDetail.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				Dialog dialogImport = new Dialog();
				dialogImport.setHeading("Import Patient Detail");
				dialogImport.setWidth("400");
				boolean processIds = false;
				if (scrId != null)
					processIds = true;
				dialogImport.add(new ImportDetail(ImportType.SCHOOL, editorGrid, dialogImport, processIds),
						new FitData(5));
				dialogImport.show();
			}
		});
		toolBar.add(importPatientDetail);

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
				validateAndSave();
			}

		}));

		gridHolderPanel.setLayout(new FitLayout());
		gridHolderPanel.add(editorGrid);
		gridHolderPanel.setHeight("350");

		mainContainerPanel.add(gridHolderPanel, new FitData(5));
		add(mainContainerPanel);
		// mainContainerPanel.setSize("700px", "600px");
		mainContainerPanel.add(formPanel);
		formPanel.setVisible(false);
	}

	private void validateAndSave()
	{
		if (!validateFormEntry())
		{
			editorGrid.unmask();
			return;
		}

		SchoolScreeningDetailDTO modelData = extractFormData();
		savePage(modelData);
	}

	private boolean validateFormEntry()
	{
		// if (!country.validate())
		// return false;
		//
		// if (!state.validate())
		// return false;
		//
		// if (!city.validate())
		// return false;
		//
		// if (!town.validate())
		// return false;
		//
		// if (!village.validate())
		// return false;

		if (!chapterName.validate())
			return false;

		if (!projectName.validate())
			return false;

		// if (!locality.validate())
		// return false;

		if (!screeningDate.validate())
			return false;

		// if (!processType.validate())
		// return false;
		//
		// if (!typeOfLocation.validate())
		// return false;
		//
		// if (!address.validate())
		// return false;
		//
		// if (!contactInformation.validate())
		// return false;

		return true;
	}

	private SchoolScreeningDetailDTO extractFormData()
	{
		IndexPage.maskCenterComponent("Saving...");
		SchoolScreeningDetailDTO modelData = new SchoolScreeningDetailDTO();
		modelData.setChapterName((ChapterNameDTO) chapterName.getSelection().get(0));
		modelData.setProjectName((ProjectDTO) projectName.getSelection().get(0));
		modelData.setScreeningDate(String.valueOf(screeningDate.getValue().getTime()));

		if (country.getSelection().size() > 0)
			modelData.setCountry((CountryDTO) country.getSelection().get(0));

		if (state.getSelection().size() > 0)
			modelData.setState((StateDTO) state.getSelection().get(0));

		if (city.getSelection().size() > 0)
			modelData.setCity((CityDTO) city.getSelection().get(0));

		if (town.getSelection().size() > 0)
			modelData.setTown((TownDTO) town.getSelection().get(0));

		if (village.getSelection().size() > 0)
			modelData.setVillage((VillageDTO) village.getSelection().get(0));

		if (locality.getSelection().size() > 0)
			modelData.setLocality((LocalityDTO) locality.getSelection().get(0));

		if (processType.getSelection().size() > 0)
			modelData.setProcessType((ProcessTypeDTO) processType.getSelection().get(0));

		if (typeOfLocation.getSelection().size() > 0)
			modelData.setTypeOfLocation((TypeOfLocationDTO) typeOfLocation.getSelection().get(0));

		if (contactInformation.getValue() != null)
			modelData.setContactInformation(contactInformation.getValue());

		if (address.getValue() != null)
			modelData.setAddress(address.getValue());

		modelData.setVolunteers(volunteers.getChecked());
		modelData.setDoctors(doctors.getChecked());

		editorGrid.stopEditing();
		editorGridStore.commitChanges();
		List<SchoolPatientDetailDTO> models = editorGridStore.getModels();
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

		RowNumberer rowNumber = new RowNumberer();
		configs.add(rowNumber);

		ColumnConfig nameColumn = new ColumnConfig("id", "Id", 20);
		configs.add(nameColumn);

		nameColumn = new ColumnConfig("name", "Name", 150);
		TextField<String> textField = new TextField<String>();
		textField.setAllowBlank(false);
		textField.setMinLength(2);
		textField.setMaxLength(255);
		nameColumn.setEditor(new CellEditor(textField));
		configs.add(nameColumn);

		ColumnConfig sexColumn = new ColumnConfig("sex", "Sex", 50);
		final SimpleComboBox<String> field = new SimpleComboBox<String>();
		field.setTriggerAction(TriggerAction.ALL);
		field.setForceSelection(true);
		field.add(GenderDTO.getStringValues());
		CellEditor editor = new CellEditor(field)
		{
			@Override
			public Object preProcessValue(Object value)
			{
				if (value == null)
				{
					return value;
				}
				return field.findModel(value.toString());
			}

			@Override
			public Object postProcessValue(Object value)
			{
				if (value == null)
				{
					return value;
				}
				return ((ModelData) value).get("value");
			}
		};
		sexColumn.setEditor(editor);
		configs.add(sexColumn);

		ColumnConfig classColumn = new ColumnConfig("standard", "Standard", 100);
		textField = new TextField<String>();
		textField.setAllowBlank(false);
		textField.setMinLength(2);
		textField.setMaxLength(4);
		classColumn.setEditor(new CellEditor(textField));
		configs.add(classColumn);

		ColumnConfig ageColumn = new ColumnConfig("age", "Age", 50);
		TextField<String> numField = new TextField<String>();
		numField.setAllowBlank(false);
		numField.setMinLength(1);
		numField.setMaxLength(3);
		ageColumn.setEditor(new CellEditor(numField));
		configs.add(ageColumn);

		ColumnConfig addressColumn = new ColumnConfig("address", "Address", 100);
		textField = new TextField<String>();
		textField.setAllowBlank(false);
		textField.setMinLength(2);
		textField.setMaxLength(255);
		addressColumn.setEditor(new CellEditor(textField));
		configs.add(addressColumn);

		ColumnConfig contactNoColumn = new ColumnConfig("contactNo", "Contact No.", 100);
		textField = new TextField<String>();
		textField.setAllowBlank(false);
		textField.setMinLength(2);
		textField.setMaxLength(15);
		contactNoColumn.setEditor(new CellEditor(textField));
		configs.add(contactNoColumn);

		ColumnConfig heightColumn = new ColumnConfig("height", "Height(cm)", 100);
		textField = new TextField<String>();
		textField.setAllowBlank(false);
		textField.setMinLength(2);
		textField.setMaxLength(15);
		heightColumn.setEditor(new CellEditor(textField));
		configs.add(heightColumn);

		ColumnConfig weightColumn = new ColumnConfig("weight", "Weight(kg)", 100);
		textField = new TextField<String>();
		textField.setAllowBlank(false);
		textField.setMinLength(1);
		textField.setMaxLength(3);
		weightColumn.setEditor(new CellEditor(textField));
		configs.add(weightColumn);

		ColumnConfig findingsPColumn = new ColumnConfig("findings", "Findings", 100);
		textField = new TextField<String>();
		textField.setAllowBlank(false);
		textField.setMinLength(2);
		textField.setMaxLength(1024);
		findingsPColumn.setEditor(new CellEditor(textField));
		configs.add(findingsPColumn);

		ColumnConfig treatment = new ColumnConfig("treatment", "Treatment", 100);
		textField = new TextField<String>();
		textField.setAllowBlank(false);
		textField.setMinLength(2);
		textField.setMaxLength(1024);
		treatment.setEditor(new CellEditor(textField));
		configs.add(treatment);

		ColumnConfig referral1Column = new ColumnConfig("referral1", "Referral 1", 100);
		configs.add(referral1Column);

		ColumnConfig medicines2Column = new ColumnConfig("referral2", "Referral 2", 100);
		configs.add(medicines2Column);

		// ColumnConfig medicines3Column = new ColumnConfig("referral3",
		// "Referral 3", 100);
		// configs.add(medicines3Column);

		ColumnConfig medicines = new ColumnConfig("medicines", "Medicines", 100);
		final SimpleComboBox<String> yesNoDto = new SimpleComboBox<String>();
		yesNoDto.setTriggerAction(TriggerAction.ALL);
		yesNoDto.setForceSelection(true);
		yesNoDto.add(YesNoDTO.getStringValues());
		editor = new CellEditor(yesNoDto)
		{
			@Override
			public Object preProcessValue(Object value)
			{
				if (value == null)
				{
					return value;
				}
				return yesNoDto.findModel(value.toString());
			}

			@Override
			public Object postProcessValue(Object value)
			{
				if (value == null)
				{
					return value;
				}
				return ((ModelData) value).get("value");
			}
		};
		medicines.setEditor(editor);
		configs.add(medicines);

		ColumnConfig emergency = new ColumnConfig("emergency", "Emergency", 100);
		final SimpleComboBox<String> yesNoDtoEmergency = new SimpleComboBox<String>();
		yesNoDtoEmergency.setTriggerAction(TriggerAction.ALL);
		yesNoDtoEmergency.setForceSelection(true);
		yesNoDtoEmergency.add(YesNoDTO.getStringValues());
		editor = new CellEditor(yesNoDtoEmergency)
		{
			@Override
			public Object preProcessValue(Object value)
			{
				if (value == null)
				{
					return value;
				}
				return yesNoDtoEmergency.findModel(value.toString());
			}

			@Override
			public Object postProcessValue(Object value)
			{
				if (value == null)
				{
					return value;
				}
				return ((ModelData) value).get("value");
			}
		};
		emergency.setEditor(editor);
		configs.add(emergency);

		ColumnConfig surgeryCase = new ColumnConfig("surgeryCase", "Surgery Case", 100);
		final SimpleComboBox<String> yesNoDtoSurgeryCase = new SimpleComboBox<String>();
		yesNoDtoSurgeryCase.setEditable(false);
		yesNoDtoSurgeryCase.add(YesNoDTO.getStringValues());
		yesNoDtoSurgeryCase.setTriggerAction(TriggerAction.ALL);
		editor = new CellEditor(yesNoDtoSurgeryCase)
		{
			@Override
			public Object preProcessValue(Object value)
			{
				if (value == null)
				{
					return value;
				}
				return yesNoDtoSurgeryCase.findModel(value.toString());
			}

			@Override
			public Object postProcessValue(Object value)
			{
				if (value == null)
				{
					return value;
				}
				return ((ModelData) value).get("value");
			}
		};
		surgeryCase.setEditor(editor);
		configs.add(surgeryCase);

		ColumnConfig caseClosed = new ColumnConfig("caseClosed", "Case Closed", 100);
		final SimpleComboBox<String> yesNoDtoCaseClosed = new SimpleComboBox<String>();
		yesNoDtoCaseClosed.setEditable(false);
		yesNoDtoCaseClosed.add(YesNoDTO.getStringValues());
		yesNoDtoCaseClosed.setTriggerAction(TriggerAction.ALL);
		editor = new CellEditor(yesNoDtoCaseClosed)
		{
			@Override
			public Object preProcessValue(Object value)
			{
				if (value == null)
				{
					return value;
				}
				return yesNoDtoCaseClosed.findModel(value.toString());
			}

			@Override
			public Object postProcessValue(Object value)
			{
				if (value == null)
				{
					return value;
				}
				return ((ModelData) value).get("value");
			}
		};
		caseClosed.setEditor(editor);
		configs.add(caseClosed);

		ColumnConfig referralUpdates = new ColumnConfig("referralUpdates", "Referral Updates", 100);
		textField = new TextField<String>();
		textField.setAllowBlank(false);
		textField.setMinLength(2);
		textField.setMaxLength(4096);
		referralUpdates.setEditor(new CellEditor(textField));
		configs.add(referralUpdates);

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
				projectName.getStore().add((List<ModelData>) modelData.get("lstProjectName"));
				processType.getStore().add((List<ModelData>) modelData.get("lstProcessType"));
				typeOfLocation.getStore().add((List<ModelData>) modelData.get("lstTypeOfLocation"));
				volunteers.getStore().add((List<VolunteerDTO>) modelData.get("lstVolunteers"));
				doctors.getStore().add((List<DoctorDTO>) modelData.get("lstDoctors"));

				List<ReferralTypeDTO> lst = (List<ReferralTypeDTO>) modelData.get("lstReferralTypes");
				List<String> lstReferrals = new ArrayList<String>(lst.size());
				for (ReferralTypeDTO referralTypeDTO : lst)
				{
					lstReferrals.add(referralTypeDTO.toString());
				}

				ColumnConfig columnById = editorGrid.getColumnModel().getColumnById("referral1");
				final SimpleComboBox<String> fieldReferral1 = new SimpleComboBox<String>();
				fieldReferral1.setEditable(false);
				fieldReferral1.setTriggerAction(TriggerAction.ALL);
				CellEditor editorReferral1 = new CellEditor(fieldReferral1)
				{
					@Override
					public Object preProcessValue(Object value)
					{
						if (value == null)
						{
							return value;
						}
						return fieldReferral1.findModel(value.toString());
					}

					@Override
					public Object postProcessValue(Object value)
					{
						if (value == null)
						{
							return value;
						}
						return ((ModelData) value).get("value");
					}
				};
				columnById.setEditor(editorReferral1);
				fieldReferral1.add(lstReferrals);

				columnById = editorGrid.getColumnModel().getColumnById("referral2");
				final SimpleComboBox<String> fieldReferral2 = new SimpleComboBox<String>();
				fieldReferral2.setEditable(false);
				fieldReferral2.setTriggerAction(TriggerAction.ALL);
				CellEditor editorReferral2 = new CellEditor(fieldReferral2)
				{
					@Override
					public Object preProcessValue(Object value)
					{
						if (value == null)
						{
							return value;
						}
						return fieldReferral2.findModel(value.toString());
					}

					@Override
					public Object postProcessValue(Object value)
					{
						if (value == null)
						{
							return value;
						}
						return ((ModelData) value).get("value");
					}
				};
				columnById.setEditor(editorReferral2);
				fieldReferral2.add(lstReferrals);

				SchoolScreeningDetailDTO scrDto = modelData.get("data");
				if (scrDto != null)
				{
					address.setValue(scrDto.getAddress());
					chapterName.setValue(scrDto.getChapterName());
					projectName.setValue(scrDto.getProjectName());
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

					screeningDate.setReadOnly(true);
					projectName.setReadOnly(true);
					chapterName.setReadOnly(true);

					if (scrDto.getDoctors() != null)
						for (DoctorDTO doctor : scrDto.getDoctors())
						{
							doctors.setChecked(doctor, true);
						}

					if (scrDto.getVolunteers() != null)
						for (VolunteerDTO volunteer : scrDto.getVolunteers())
						{
							volunteers.setChecked(volunteer, true);
						}

					editorGridStore.removeAll();
					List<SchoolPatientDetailDTO> patientDetails = scrDto.getPatientDetails();
					editorGridStore.add(patientDetails);
				}
				IndexPage.unmaskCenterComponent();
			}

			@Override
			public void onFailure(Throwable caught)
			{
				MessageBox.alert("Alert",
						"Error encountered while loading the screen. Please retry the operation. Additional Details: "
								+ caught.getMessage(), l);
			}
		});

	}

	private void savePage(SchoolScreeningDetailDTO model)
	{
		detailServiceAsync.saveModel(this.scrId, model, new AsyncCallback<RpcStatusEnum>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				editorGrid.unmask();
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
					screeningDate.setReadOnly(true);
					chapterName.setReadOnly(true);
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
		projectName.clearSelections();
		processType.clearSelections();
		typeOfLocation.clearSelections();
		address.clear();
		contactInformation.clear();
		screeningDate.clear();

		screeningDate.setReadOnly(false);
		chapterName.setReadOnly(false);
		projectName.setReadOnly(false);

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
