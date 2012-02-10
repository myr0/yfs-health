package com.varun.yfs.client.reports;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.HiddenField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.varun.yfs.client.images.YfsImageBundle;
import com.varun.yfs.client.reports.rpc.ReportDetailService;
import com.varun.yfs.client.reports.rpc.ReportDetailServiceAsync;
import com.varun.yfs.client.reports.rpc.ReportType;
import com.varun.yfs.client.screening.export.ExportService;
import com.varun.yfs.client.screening.export.ExportServiceAsync;
import com.varun.yfs.dto.ExportTableDTO;
import com.varun.yfs.dto.ExportTableDataDTO;

public class EventsReport extends LayoutContainer
{
	private ReportDetailServiceAsync reportDetailService = GWT.create(ReportDetailService.class);
	private ExportServiceAsync exportServiceAsync = GWT.create(ExportService.class);
	private Grid<ExportTableDataDTO> gridEvents;

	public EventsReport()
	{
	}

	final Listener<MessageBoxEvent> DUMMYLISTENER = new Listener<MessageBoxEvent>()
	{
		public void handleEvent(MessageBoxEvent ce)
		{
		}
	};

	@Override
	protected void onRender(Element parent, int index)
	{

		super.onRender(parent, index);

		ContentPanel cpOuterContainer = new ContentPanel();
		cpOuterContainer.setHeading("Events Report");
		add(cpOuterContainer);

		setScrollMode(Scroll.AUTOY);
		LayoutContainer layoutContainer = new LayoutContainer();
		layoutContainer.setLayout(new TableLayout(4));

		final DateField dtfldFromDate = new DateField();
		dtfldFromDate.setFieldLabel("From Date");
		Date currentDate = new Date();
		dtfldFromDate.setValue(currentDate);
		dtfldFromDate.setAllowBlank(false);
		LayoutContainer frmpnlFromDate = new LayoutContainer();
		frmpnlFromDate.setLayout(new FormLayout());
		frmpnlFromDate.add(dtfldFromDate, new FormData("100%"));

		TableData td_frmpnlFromDate = new TableData();
		td_frmpnlFromDate.setMargin(5);
		td_frmpnlFromDate.setPadding(5);
		layoutContainer.add(frmpnlFromDate, td_frmpnlFromDate);

		final DateField dtfldToDate = new DateField();
		dtfldToDate.setFieldLabel("To Date");
		dtfldToDate.setValue(currentDate);
		dtfldToDate.setAllowBlank(false);
		LayoutContainer frmpnlToDate = new LayoutContainer();
		frmpnlToDate.setLayout(new FormLayout());
		frmpnlToDate.add(dtfldToDate, new FormData("100%"));

		TableData td_frmpnlToDate = new TableData();
		td_frmpnlToDate.setPadding(5);
		td_frmpnlToDate.setMargin(5);
		layoutContainer.add(frmpnlToDate, td_frmpnlToDate);

		LayoutContainer frmpnlRefresh = new LayoutContainer();
		frmpnlRefresh.setLayout(new FormLayout());

		Button btnRefresh = new Button("", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.refreshButtonIcon()));
		frmpnlRefresh.add(btnRefresh, new FormData("100%"));
		TableData td_frmpnlRefresh = new TableData();
		td_frmpnlRefresh.setPadding(5);
		td_frmpnlRefresh.setMargin(5);
		layoutContainer.add(frmpnlRefresh, td_frmpnlRefresh);
		frmpnlRefresh.setBorders(true);

		LayoutContainer frmpnlExport = new LayoutContainer();
		frmpnlExport.setLayout(new FormLayout());
		frmpnlExport.setBorders(true);
		Button btnExport = new Button("", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.excelExportIcon()));
		frmpnlExport.add(btnExport, new FormData("100%"));
		TableData td_frmpnlExport = new TableData();
		td_frmpnlExport.setPadding(5);
		td_frmpnlExport.setMargin(5);
		layoutContainer.add(frmpnlExport, td_frmpnlExport);

		final FormPanel formPanel = new FormPanel();
		final HiddenField<String> exportedFileName = new HiddenField<String>();
		exportedFileName.setName("ExportedFilename");
		formPanel.add(exportedFileName);
		formPanel.setVisible(false);
		add(formPanel);

		btnExport.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				if (!validateReportParams(dtfldFromDate, dtfldToDate))
					return;

				mask("Please wait.Generating Report...");

				ModelData model = new BaseModelData();
				model.set("dateFrom", dtfldFromDate.getValue().getTime());
				model.set("dateTo", dtfldToDate.getValue().getTime());

				reportDetailService.getModel(ReportType.Events, model, new AsyncCallback<ModelData>()
				{
					@Override
					public void onSuccess(ModelData result)
					{
						decodeResult(result);

						List<ExportTableDTO> lstExportTableDto = new ArrayList<ExportTableDTO>();
						List<String> headers = new ArrayList<String>();
						List<String> headerTags = new ArrayList<String>();
						List<ColumnConfig> columns = gridEvents.getColumnModel().getColumns();
						columns = columns.subList(1, columns.size());
						for (ColumnConfig columnConfig : columns)
						{
							headers.add(columnConfig.getHeader());
							headerTags.add(columnConfig.getId());
						}
						List<ExportTableDataDTO> models = gridEvents.getStore().getModels();
						ExportTableDTO expTab = new ExportTableDTO();
						expTab.setColHeaders(headers);
						expTab.setLstData(models);
						expTab.setColHeaderTags(headerTags);
						lstExportTableDto.add(expTab);

						exportServiceAsync.createExportFile(lstExportTableDto, null, new AsyncCallback<String>()
						{
							@Override
							public void onFailure(Throwable caught)
							{
								unmask();
								MessageBox.alert("Alert", "Error encountered while exporting." + caught.getMessage(),
										DUMMYLISTENER);
							}

							@Override
							public void onSuccess(String result)
							{
								exportedFileName.setValue(result);

								String url = GWT.getModuleBaseURL();
								url = url + "exportServlet";

								formPanel.setAction(url);
								formPanel.submit();
								unmask();
							}

						});
					}

					@Override
					public void onFailure(Throwable caught)
					{
						unmask();
						MessageBox.info("Error", "Error encountered while loading the report." + caught.getMessage(),
								DUMMYLISTENER);
					}
				});

			}
		});

		btnRefresh.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{

				if (!validateReportParams(dtfldFromDate, dtfldToDate))
					return;

				mask("Please wait.Generating Report...");

				ModelData model = new BaseModelData();
				model.set("dateFrom", dtfldFromDate.getValue().getTime());
				model.set("dateTo", dtfldToDate.getValue().getTime());
				reportDetailService.getModel(ReportType.Events, model, new AsyncCallback<ModelData>()
				{
					@Override
					public void onSuccess(ModelData result)
					{
						unmask();
						gridEvents.getStore().removeAll();
						gridEvents.getStore().add((List<? extends ExportTableDataDTO>) result.get("eventsInfo"));
					}

					@Override
					public void onFailure(Throwable caught)
					{
						unmask();
						MessageBox.info("Error", "Error encountered while loading the report." + caught.getMessage(),
								DUMMYLISTENER);
					}
				});
			}
		});

		add(layoutContainer);

		FormPanel lcReportingParams = new FormPanel();
		lcReportingParams.setHeaderVisible(false);
		// lcReportingParams.setSize("", "700");

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig clmncnfgNewColumn = new ColumnConfig("date", "Date", 150);
		configs.add(clmncnfgNewColumn);

		ColumnConfig clmncnfgNewColumn_1 = new ColumnConfig("eventType", "Event Type", 150);
		configs.add(clmncnfgNewColumn_1);

		ColumnConfig clmncnfgNewColumn_2 = new ColumnConfig("eventLocation", "Event Location", 150);
		configs.add(clmncnfgNewColumn_2);

		ColumnConfig clmncnfgNewColumn_3 = new ColumnConfig("noScreened", "Number Screened", 150);
		configs.add(clmncnfgNewColumn_3);

		// ColumnConfig clmncnfgNewColumn_4 = new
		// ColumnConfig("noRequiringTreatment", "Requiring Treatment", 150);
		// configs.add(clmncnfgNewColumn_4);

		// ColumnConfig clmncnfgNewColumn_5 = new ColumnConfig("noTreated",
		// "Number Treated", 150);
		// configs.add(clmncnfgNewColumn_5);

		ColumnConfig clmncnfgNewColumn_6 = new ColumnConfig("volunteers", "Volunteers", 150);
		configs.add(clmncnfgNewColumn_6);

		ColumnConfig clmncnfgNewColumn_7 = new ColumnConfig("medicalTeam", "Medical Team", 150);
		configs.add(clmncnfgNewColumn_7);

		gridEvents = new Grid<ExportTableDataDTO>(new ListStore<ExportTableDataDTO>(), new ColumnModel(configs));
		gridEvents.setHeight("350");
		gridEvents.setBorders(true);

		FormData fd_gridStatusOfTreatment = new FormData("100%");
		fd_gridStatusOfTreatment.setMargins(new Margins(0, 0, 5, 0));
		lcReportingParams.add(gridEvents, fd_gridStatusOfTreatment);

		lcReportingParams.setLayoutData(new Margins(5, 5, 5, 5));
		add(lcReportingParams);

	}

	private boolean validateReportParams(final DateField dtfldFromDate, final DateField dtfldToDate)
	{
		if (dtfldFromDate.getValue() == null)
		{
			MessageBox.info("Report Parameter Needed", "From-Date field cannot be empty", DUMMYLISTENER);
			return false;
		}
		if (dtfldToDate.getValue() == null)
		{
			MessageBox.info("Report Parameter Needed", "To-Date field cannot be empty", DUMMYLISTENER);
			return false;
		}

		return true;
	}

	private void decodeResult(ModelData result)
	{
		gridEvents.getStore().removeAll();
		gridEvents.getStore().add((List<? extends ExportTableDataDTO>) result.get("eventsInfo"));

		// final ListStore<ChartData> store = new ListStore<ChartData>();
		// Integer screened, surgeryCaseClosed, pendingCases, followUpMedicines,
		// referredToHospital;
		// for (ModelData model : gridEvents.getStore().getModels())
		// {
		// String breakupOfTreatment =
		// model.get("breakUpOfTreatment").toString();
		//
		// Object obj = model.get("screened");
		// screened = (obj == null) ? 0 : (Integer) obj;
		//
		// obj = model.get("surgeryCasesClosed");
		// surgeryCaseClosed = (obj == null) ? 0 : (Integer) obj;
		//
		// obj = model.get("pendingCases");
		// pendingCases = (obj == null) ? 0 : (Integer) obj;
		//
		// obj = model.get("followUpMedicines");
		// followUpMedicines = (obj == null) ? 0 : (Integer) obj;
		//
		// obj = model.get("referredToHospital");
		// referredToHospital = (obj == null) ? 0 : (Integer) obj;
		//
		// ChartData tmSales = new ChartData(breakupOfTreatment, screened,
		// surgeryCaseClosed, pendingCases, followUpMedicines,
		// referredToHospital);
		// store.add(tmSales);
		// }
	}
}
