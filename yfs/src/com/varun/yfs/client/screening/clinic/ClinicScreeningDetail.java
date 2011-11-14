package com.varun.yfs.client.screening.clinic;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.RowNumberer;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
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
import com.varun.yfs.client.screening.clinic.rpc.ClinicScreeningDetailService;
import com.varun.yfs.client.screening.clinic.rpc.ClinicScreeningDetailServiceAsync;
import com.varun.yfs.client.screening.export.ExportService;
import com.varun.yfs.client.screening.export.ExportServiceAsync;
import com.varun.yfs.client.screening.imports.ImportDetail;
import com.varun.yfs.client.screening.imports.ImportType;
import com.varun.yfs.dto.ClinicPatientDetailDTO;
import com.varun.yfs.dto.ClinicScreeningDetailDTO;
import com.varun.yfs.dto.GenderDTO;
import com.varun.yfs.dto.ReferralTypeDTO;
import com.varun.yfs.dto.YesNoDTO;

public class ClinicScreeningDetail extends LayoutContainer
{
	private String headerText = "Clinic Screening";
	private ClinicScreeningDetailServiceAsync detailServiceAsync = GWT.create(ClinicScreeningDetailService.class);
	private ExportServiceAsync exportServiceAsync = GWT.create(ExportService.class);

	protected ContentPanel mainContainerPanel = new ContentPanel();

	private ListStore<ClinicPatientDetailDTO> editorGridStore;
	private EditorGrid<ClinicPatientDetailDTO> editorGrid;
	private String scrId;

	public EditorGrid<ClinicPatientDetailDTO> getEditorGrid()
	{
		return editorGrid;
	}

	public void setEditorGrid(EditorGrid<ClinicPatientDetailDTO> editorGrid)
	{
		this.editorGrid = editorGrid;
	}

	public ClinicScreeningDetail()
	{
		setHeight("700");
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

		editorGridStore = new ListStore<ClinicPatientDetailDTO>();
		ColumnModel columnModel = getColumnModel();
		editorGrid = new EditorGrid<ClinicPatientDetailDTO>(editorGridStore, columnModel);
		// editorGrid.reconfigure(editorGridStore, columnModel);
		editorGrid.setBorders(true);
		editorGrid.setSelectionModel(new GridSelectionModel<ClinicPatientDetailDTO>());
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
				ClinicPatientDetailDTO patientDetail = new ClinicPatientDetailDTO();
				patientDetail.setDeleted("N");
				editorGrid.stopEditing();
				editorGridStore.insert(patientDetail, 0);
				editorGrid.startEditing(editorGridStore.indexOf(patientDetail), 0);
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
				ClinicPatientDetailDTO selectedItem = editorGrid.getSelectionModel().getSelectedItem();
				if (selectedItem != null)
				{
					selectedItem.set("deleted", "Y");
					editorGrid.mask("Removing Entry...");
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

		MenuItem exportAll = new MenuItem("Export All", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.exportButtonIcon()));
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
				List<ClinicPatientDetailDTO> models = editorGridStore.getModels();
				exportServiceAsync.createExportFile(headers, models, new AsyncCallback<String>()
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

		MenuItem exportReferral = new MenuItem("Export Referrals", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.exportButtonIcon()));
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

				StoreFilter<ClinicPatientDetailDTO> filterReferrals = new StoreFilter<ClinicPatientDetailDTO>()
				{
					@Override
					public boolean select(Store<ClinicPatientDetailDTO> store, ClinicPatientDetailDTO parent, ClinicPatientDetailDTO item, String property)
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

				List<ClinicPatientDetailDTO> models = editorGridStore.getModels();
				exportServiceAsync.createExportFile(headers, models, new AsyncCallback<String>()
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

						editorGridStore.clearFilters();
					}

				});

			}
		});
		menu.add(exportReferral);

		Button importPatientDetail = new Button("Import", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.importButtonIcon()));
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
				dialogImport.add(new ImportDetail(ImportType.CLINIC, editorGrid, dialogImport, processIds), new FitData(5));
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
		mainContainerPanel.setHeight("700");
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

		ClinicScreeningDetailDTO modelData = extractFormData();
		savePage(modelData);
	}

	private boolean validateFormEntry()
	{

		return true;
	}

	private ClinicScreeningDetailDTO extractFormData()
	{
		IndexPage.maskCenterComponent("Saving...");
		ClinicScreeningDetailDTO modelData = new ClinicScreeningDetailDTO();

		editorGrid.stopEditing();
		editorGridStore.commitChanges();
		List<ClinicPatientDetailDTO> models = editorGridStore.getModels();
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
		NumberField numField = new NumberField();
		numField.setAllowBlank(false);
		numField.setMinLength(1);
		numField.setMaxLength(3);
		numField.setPropertyEditorType(Integer.class);
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

		ColumnConfig emergency = new ColumnConfig("emergency", "Emergency", 100);
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

				// columnById =
				// editorGrid.getColumnModel().getColumnById("referral3");
				// final SimpleComboBox<String> fieldReferral3 = new
				// SimpleComboBox<String>();
				// fieldReferral3.setEditable(false);
				// fieldReferral3.setTriggerAction(TriggerAction.ALL);
				// CellEditor editorReferral3 = new CellEditor(fieldReferral3)
				// {
				// @Override
				// public Object preProcessValue(Object value)
				// {
				// if (value == null)
				// {
				// return value;
				// }
				// return fieldReferral3.findModel(value.toString());
				// }
				//
				// @Override
				// public Object postProcessValue(Object value)
				// {
				// if (value == null)
				// {
				// return value;
				// }
				// return ((ModelData) value).get("value");
				// }
				// };
				// columnById.setEditor(editorReferral3);
				// fieldReferral3.add(lstReferrals);

				ClinicScreeningDetailDTO scrDto = modelData.get("data");
				if (scrDto != null)
				{
					editorGridStore.removeAll();
					List<ClinicPatientDetailDTO> patientDetails = scrDto.getPatientDetails();
					editorGridStore.add(patientDetails);
				}
				IndexPage.unmaskCenterComponent();
			}

			@Override
			public void onFailure(Throwable caught)
			{
				MessageBox.alert("Alert", "Error encountered while loading the screen. Please retry the operation. Additional Details: " + caught.getMessage(), l);
			}
		});

	}

	private void savePage(ClinicScreeningDetailDTO model)
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
					clearStores();
					Info.display("Screening Detail", "Save Completed Sucessfully.");
					IndexPage.reinitScreeningPanel();
				}
			}
		});
	}

	private void clearStores()
	{
		editorGridStore.removeAll();
	}
}
