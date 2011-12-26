package com.varun.yfs.client.reports;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.BarDataProvider;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.Legend;
import com.extjs.gxt.charts.client.model.Legend.Position;
import com.extjs.gxt.charts.client.model.ScaleProvider;
import com.extjs.gxt.charts.client.model.charts.BarChart;
import com.extjs.gxt.charts.client.model.charts.BarChart.BarStyle;
import com.extjs.gxt.charts.client.model.charts.ChartConfig;
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
import com.extjs.gxt.ui.client.widget.form.LabelField;
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
import com.varun.yfs.client.reports.rpc.ReportDetailService;
import com.varun.yfs.client.reports.rpc.ReportDetailServiceAsync;
import com.varun.yfs.client.reports.rpc.ReportType;

public class MedicalCampProgramReport extends LayoutContainer
{
	private ReportDetailServiceAsync reportDetailService = GWT.create(ReportDetailService.class);
	private LabelField lblfldLocations;
	private LabelField lblfldTotalScreened;
	private Grid<ModelData> gridBreakupOfTreatments;
	private Grid<ModelData> gridStatusOfTreatment;

	final Listener<MessageBoxEvent> DUMMYLISTENER = new Listener<MessageBoxEvent>()
	{
		public void handleEvent(MessageBoxEvent ce)
		{
		}
	};

	public MedicalCampProgramReport()
	{
		setHeight("700");
	}

	@Override
	protected void onRender(Element parent, int index)
	{
		super.onRender(parent, index);

		ContentPanel cpOuterContainer = new ContentPanel();
		cpOuterContainer.setHeading("Reporting -> Reports -> Medical Camp");
		add(cpOuterContainer);

		setScrollMode(Scroll.AUTOY);
		final ListStore<ChartData> store = new ListStore<ChartData>();
		ChartData tmSales = new ChartData("", 0, 0, 0, 0, 0);
		store.add(tmSales);

		String url = "open-flash-chart.swf";
		final Chart chart = new Chart(url);

		ChartModel model = new ChartModel("Camp Screening report", "font-size: 14px; font-family: Verdana; text-align: center;");
		model.setBackgroundColour("fefefe");
		model.setLegend(new Legend(Position.TOP, true));
		model.setScaleProvider(ScaleProvider.ROUNDED_NEAREST_SCALE_PROVIDER);

		BarChart bar = new BarChart(BarStyle.GLASS);
		BarDataProvider barProvider = new BarDataProvider("screened", "month");
		barProvider.bind(store);
		bar.setColour("00aa00");
		bar.setDataProvider(barProvider);
		model.addChartConfig(bar);
		bar.setTooltip("Total Screened : #val#");

		bar = new BarChart(BarStyle.GLASS);
		barProvider = new BarDataProvider("surgeryCasesClosed");
		barProvider.bind(store);
		bar.setColour("0000cc");
		bar.setDataProvider(barProvider);
		bar.setTooltip("Surgery Cases Closed : #val#");
		model.addChartConfig(bar);

		bar = new BarChart(BarStyle.GLASS);
		barProvider = new BarDataProvider("pendingCases");
		barProvider.bind(store);
		bar.setColour("ff6600");
		bar.setDataProvider(barProvider);
		bar.setTooltip("Pending Cases: #val#");
		model.addChartConfig(bar);

		bar = new BarChart(BarStyle.GLASS);
		barProvider = new BarDataProvider("followUpMedicines");
		barProvider.bind(store);
		bar.setColour("ff6600");
		bar.setDataProvider(barProvider);
		bar.setTooltip("Follow up Cases :#val#");
		model.addChartConfig(bar);

		bar = new BarChart(BarStyle.GLASS);
		barProvider = new BarDataProvider("referredToHospital");
		barProvider.bind(store);
		bar.setColour("ff6600");
		bar.setDataProvider(barProvider);
		bar.setTooltip("Referred To Hospital : #val#");
		model.addChartConfig(bar);

		chart.setChartModel(model);

		LayoutContainer layoutContainer = new LayoutContainer();
		layoutContainer.setLayout(new TableLayout(3));

		final DateField dtfldFromDate = new DateField();
		dtfldFromDate.setFieldLabel("From Date");
		dtfldFromDate.setAllowBlank(false);
		LayoutContainer frmpnlFromDate = new LayoutContainer();
		frmpnlFromDate.setLayout(new FormLayout());
		frmpnlFromDate.add(dtfldFromDate, new FormData("100%"));

		TableData td_frmpnlFromDate = new TableData();
		td_frmpnlFromDate.setPadding(5);
		td_frmpnlFromDate.setMargin(5);
		layoutContainer.add(frmpnlFromDate, td_frmpnlFromDate);

		final DateField dtfldToDate = new DateField();
		dtfldToDate.setFieldLabel("To Date");
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

		Button btnRefresh = new Button("Get Report");
		frmpnlRefresh.add(btnRefresh, new FormData("100%"));
		TableData td_frmpnlRefresh = new TableData();
		td_frmpnlRefresh.setPadding(5);
		td_frmpnlRefresh.setMargin(5);
		layoutContainer.add(frmpnlRefresh, td_frmpnlRefresh);
		frmpnlRefresh.setBorders(true);
		btnRefresh.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				if (dtfldFromDate.getValue() == null)
				{
					MessageBox.info("Report Parameter Needed", "From-Date field cannot be empty", DUMMYLISTENER);
					return;
				}
				if (dtfldToDate.getValue() == null)
				{
					MessageBox.info("Report Parameter Needed", "To-Date field cannot be empty", DUMMYLISTENER);
					return;
				}
				ModelData model = new BaseModelData();
				model.set("dateFrom", dtfldFromDate.getValue().getTime());
				model.set("dateTo", dtfldToDate.getValue().getTime());
				reportDetailService.getModel(ReportType.MedicalCamp, model, new AsyncCallback<ModelData>()
				{
					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(ModelData result)
					{
						lblfldLocations.clear();
						lblfldLocations.setText("Location(s):" + result.get("locationsList"));

						lblfldTotalScreened.clear();
						lblfldTotalScreened.setText("Total Number Screened:" + result.get("locationsCount"));

						gridBreakupOfTreatments.getStore().removeAll();
						gridBreakupOfTreatments.getStore().add((List<? extends ModelData>) result.get("breakupOfTreatments"));
						
						List<ModelData> lstModels = new ArrayList<ModelData>();
						int pendingCasesCnt = 0, followUpMedCnt = 0, medCaseCnt = 0;
						for (ModelData model : (List<? extends ModelData>) result.get("statusOfTreatments"))
						{
							Object pendingCases = model.get("pendingCases");
							if (pendingCases != null)
								pendingCasesCnt += (Integer) pendingCases;

							Object followUpMedicines = model.get("followUpMedicines");
							if (followUpMedicines != null)
								followUpMedCnt += (Integer) followUpMedicines;

							Object medicalCaseCnt = model.get("medicineCasesClosed");
							if (medicalCaseCnt != null)
								medCaseCnt += (Integer) medicalCaseCnt;
						}
						ModelData tmpModel = new BaseModelData();
						tmpModel.set("statusOfTreatments", "Count");
						tmpModel.set("medicineCasesClosed", medCaseCnt);
						tmpModel.set("followUpMedicines", followUpMedCnt);
						tmpModel.set("pendingCases", pendingCasesCnt);
						lstModels.add(tmpModel);
						gridStatusOfTreatment.getStore().removeAll();
						gridStatusOfTreatment.getStore().add(lstModels);

						final ListStore<ChartData> store = new ListStore<ChartData>();
						Integer screened, surgeryCaseClosed, pendingCases, followUpMedicines, referredToHospital;
						for (ModelData model : gridBreakupOfTreatments.getStore().getModels())
						{
							String breakupOfTreatment = model.get("breakUpOfTreatment").toString();

							Object obj = model.get("screened");
							screened = (obj == null) ? 0 : (Integer) obj;

							obj = model.get("surgeryCasesClosed");
							surgeryCaseClosed = (obj == null) ? 0 : (Integer) obj;

							obj = model.get("pendingCases");
							pendingCases = (obj == null) ? 0 : (Integer) obj;

							obj = model.get("followUpMedicines");
							followUpMedicines = (obj == null) ? 0 : (Integer) obj;

							obj = model.get("referredToHospital");
							referredToHospital = (obj == null) ? 0 : (Integer) obj;

							ChartData tmSales = new ChartData(breakupOfTreatment, screened, surgeryCaseClosed, pendingCases, followUpMedicines, referredToHospital);
							store.add(tmSales);
						}
						List<ChartConfig> chartConfigs = chart.getChartModel().getChartConfigs();
//						chartConfigs.clear();
						for (ChartConfig chartConfig : chartConfigs)
						{
							chartConfig.getDataProvider().bind(store);
						}
						chart.refresh();
					}

					@Override
					public void onFailure(Throwable caught)
					{
						MessageBox.info("Error", "Error encountered while loading the report." + caught.getMessage(), DUMMYLISTENER);
					}
				});
			}
		});
		add(layoutContainer);

		FormPanel lcReportingParams = new FormPanel();
		lcReportingParams.setHeaderVisible(false);
		lcReportingParams.setHeading("Medical Health Program Report");
		lcReportingParams.setSize("", "700");

		chart.setHeight("250px");

		lblfldLocations = new LabelField("Location(s):");
		lblfldTotalScreened = new LabelField("Total Number Screened:");

		lcReportingParams.add(lblfldLocations, new FormData("100%"));
		lcReportingParams.add(lblfldTotalScreened, new FormData("100%"));
		lcReportingParams.add(chart, new FormData("90%"));

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig clmncnfgNewColumn = new ColumnConfig("statusOfTreatments", "Status Of Treatments", 80);
		configs.add(clmncnfgNewColumn);

		ColumnConfig clmncnfgNewColumn_1 = new ColumnConfig("medicineCasesClosed", "Medicines", 150);
		configs.add(clmncnfgNewColumn_1);

		ColumnConfig clmncnfgNewColumn_2 = new ColumnConfig("followUpMedicines", "Follow Up Medicines", 150);
		configs.add(clmncnfgNewColumn_2);

		ColumnConfig clmncnfgNewColumn_3 = new ColumnConfig("pendingCases", "Pending Cases", 150);
		configs.add(clmncnfgNewColumn_3);

		gridStatusOfTreatment = new Grid<ModelData>(new ListStore<ModelData>(), new ColumnModel(configs));
		gridStatusOfTreatment.setHeight("100");
		gridStatusOfTreatment.setBorders(true);
		gridStatusOfTreatment.getView().setForceFit(true);
		gridStatusOfTreatment.setColumnLines(true);

		List<ColumnConfig> configsBreakupOfTreatments = new ArrayList<ColumnConfig>();
		ColumnConfig breakupOfTreatment = new ColumnConfig("breakUpOfTreatment", "Breakup of Treatments", 80);
		configsBreakupOfTreatments.add(breakupOfTreatment);

		ColumnConfig screened = new ColumnConfig("screened", "Screened", 60);
		configsBreakupOfTreatments.add(screened);

		ColumnConfig casesClosed = new ColumnConfig("surgeryCasesClosed", "Surgery Cases Closed", 60);
		configsBreakupOfTreatments.add(casesClosed);

		ColumnConfig medicineClosedCases = new ColumnConfig("medicineCasesClosed", "Medicine Closed Cases", 60);
		configsBreakupOfTreatments.add(medicineClosedCases);

		ColumnConfig pendingSurgeryCases = new ColumnConfig("pendingSurgeryCases", "Pending Surgery Cases", 60);
		configsBreakupOfTreatments.add(pendingSurgeryCases);

		ColumnConfig followUpMedicines = new ColumnConfig("followUpMedicines", "Follow Up Medicines", 60);
		configsBreakupOfTreatments.add(followUpMedicines);

		ColumnConfig closedSurgeryCases = new ColumnConfig("closedSurgeryCases", "Surgery Cases Closed", 60);
		configsBreakupOfTreatments.add(closedSurgeryCases);

		ColumnConfig caseClosed = new ColumnConfig("caseClosed", "Case Closed", 60);
		configsBreakupOfTreatments.add(caseClosed);

		ColumnConfig referredToHospital = new ColumnConfig("referredToHospital", "Referred To Hospital", 60);
		configsBreakupOfTreatments.add(referredToHospital);

		ColumnConfig pendingCases = new ColumnConfig("pendingCases", "Pending Cases", 60);
		configsBreakupOfTreatments.add(pendingCases);

		ColumnConfig clmncnfgNewColumn_5 = new ColumnConfig("total", "Total", 60);
		configsBreakupOfTreatments.add(clmncnfgNewColumn_5);

		gridBreakupOfTreatments = new Grid<ModelData>(new ListStore<ModelData>(), new ColumnModel(configsBreakupOfTreatments));
		gridBreakupOfTreatments.setBorders(true);
		gridBreakupOfTreatments.setHeight("250");
		gridBreakupOfTreatments.setColumnLines(true);
		gridBreakupOfTreatments.getView().setForceFit(true);

		FormData fd_gridStatusOfTreatment = new FormData("80%");
		fd_gridStatusOfTreatment.setMargins(new Margins(0, 0, 5, 0));

		FormData fd_gridBreakupOfTreatments = new FormData("80%");
		fd_gridBreakupOfTreatments.setMargins(new Margins(0, 0, 5, 0));

		lcReportingParams.add(gridBreakupOfTreatments, fd_gridBreakupOfTreatments);
		lcReportingParams.add(gridStatusOfTreatment, fd_gridStatusOfTreatment);

		lcReportingParams.setLayoutData(new Margins(5, 5, 5, 5));
		add(lcReportingParams);

	}
}
