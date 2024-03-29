package com.varun.yfs.client.reports;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.charts.client.Chart;
import com.extjs.gxt.charts.client.model.BarDataProvider;
import com.extjs.gxt.charts.client.model.ChartModel;
import com.extjs.gxt.charts.client.model.Legend;
import com.extjs.gxt.charts.client.model.Legend.Position;
import com.extjs.gxt.charts.client.model.LineDataProvider;
import com.extjs.gxt.charts.client.model.ScaleProvider;
import com.extjs.gxt.charts.client.model.charts.BarChart;
import com.extjs.gxt.charts.client.model.charts.BarChart.BarStyle;
import com.extjs.gxt.charts.client.model.charts.LineChart;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.varun.yfs.client.images.YfsImageBundle;

public class OverallReport extends LayoutContainer
{
	public OverallReport()
	{
		setHeight("700");
	}

	@Override
	protected void onRender(Element parent, int index)
	{

		super.onRender(parent, index);

		final ListStore<ChartData> store = new ListStore<ChartData>();
		ChartData tmSales = new ChartData("Requirement Analysis", 0, 10, 20, 2, 3);
		store.add(tmSales);

		String url = "open-flash-chart.swf";
		final Chart chart = new Chart(url);
		chart.setSwfHeight("40%");
		chart.setSwfWidth("40%");

		ChartModel model = new ChartModel("Project progress report",
				"font-size: 14px; font-family: Verdana; text-align: center;");
		model.setBackgroundColour("fefefe");
		model.setLegend(new Legend(Position.TOP, true));
		model.setScaleProvider(ScaleProvider.ROUNDED_NEAREST_SCALE_PROVIDER);

		BarChart bar = new BarChart(BarStyle.GLASS);
		bar.setColour("00aa00");
		BarDataProvider barProvider = new BarDataProvider("alphasales", "month");
		barProvider.bind(store);
		bar.setDataProvider(barProvider);
		model.addChartConfig(bar);

		bar = new BarChart(BarStyle.GLASS);
		bar.setColour("0000cc");
		barProvider = new BarDataProvider("betasales");
		barProvider.bind(store);
		bar.setDataProvider(barProvider);
		model.addChartConfig(bar);

		bar = new BarChart(BarStyle.GLASS);
		bar.setColour("ff6600");
		barProvider = new BarDataProvider("gammasales");
		barProvider.bind(store);
		bar.setDataProvider(barProvider);
		model.addChartConfig(bar);

		LineChart line = new LineChart();
		line.setAnimateOnShow(true);
		line.setText("Average");
		line.setColour("FF0000");
		LineDataProvider lineProvider = new LineDataProvider("avgsales");
		lineProvider.bind(store);
		line.setDataProvider(lineProvider);
		model.addChartConfig(line);

		chart.setChartModel(model);

		LayoutContainer layoutContainer = new LayoutContainer();
		layoutContainer.setLayout(new TableLayout(3));

		DateField dtfldFromDate = new DateField();
		dtfldFromDate.setFieldLabel("From Date");
		LayoutContainer frmpnlFromDate = new LayoutContainer();
		frmpnlFromDate.setLayout(new FormLayout());
		frmpnlFromDate.add(dtfldFromDate, new FormData("100%"));

		layoutContainer.add(frmpnlFromDate);

		DateField dtfldToDate = new DateField();
		dtfldToDate.setFieldLabel("To Date");
		LayoutContainer frmpnlToDate = new LayoutContainer();
		frmpnlToDate.setLayout(new FormLayout());
		frmpnlToDate.add(dtfldToDate, new FormData("100%"));

		layoutContainer.add(frmpnlToDate);

		LayoutContainer frmpnlRefresh = new LayoutContainer();
		frmpnlRefresh.setLayout(new FormLayout());

		Button btnRefresh = new Button("", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.refreshButtonIcon()));
		frmpnlRefresh.add(btnRefresh, new FormData("100%"));
		layoutContainer.add(frmpnlRefresh);
		frmpnlRefresh.setBorders(true);

		add(layoutContainer);

		FormPanel lcReportingParams = new FormPanel();
		lcReportingParams.setHeading("Medical Health Program Report");
		lcReportingParams.setSize("500", "700");

		lcReportingParams.add(chart, new FormData("60%"));
		chart.setSize("80%", "150px");

		LabelField lblfldLocations = new LabelField("Location(s) :");
		lcReportingParams.add(lblfldLocations, new FormData("100%"));

		LabelField lblfldTotalScreened = new LabelField("Total Number Screened:");
		lcReportingParams.add(lblfldTotalScreened, new FormData("100%"));
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig clmncnfgNewColumn = new ColumnConfig("id", "Status Of Treatments", 150);
		configs.add(clmncnfgNewColumn);

		ColumnConfig clmncnfgNewColumn_1 = new ColumnConfig("id", "Surgery", 150);
		configs.add(clmncnfgNewColumn_1);

		ColumnConfig clmncnfgNewColumn_2 = new ColumnConfig("id", "Non-Surgery", 150);
		configs.add(clmncnfgNewColumn_2);

		ColumnConfig clmncnfgNewColumn_3 = new ColumnConfig("id", "Total", 150);
		configs.add(clmncnfgNewColumn_3);

		Grid<ModelData> gridStatusOfTreatment = new Grid<ModelData>(new ListStore<ModelData>(),
				new ColumnModel(configs));
		gridStatusOfTreatment.setHeight("150");
		gridStatusOfTreatment.setBorders(true);

		List<ColumnConfig> configsBreakupOfTreatments = new ArrayList<ColumnConfig>();

		ColumnConfig clmncnfgNewColumn_4 = new ColumnConfig("id", "Breakup of Treatments", 150);
		configsBreakupOfTreatments.add(clmncnfgNewColumn_4);

		ColumnConfig clmncnfgNewColumn_5 = new ColumnConfig("id", "Total", 150);
		configsBreakupOfTreatments.add(clmncnfgNewColumn_5);

		Grid<ModelData> gridBreakupOfTreatments = new Grid<ModelData>(new ListStore<ModelData>(), new ColumnModel(
				configsBreakupOfTreatments));
		gridBreakupOfTreatments.setBorders(true);

		FormData fd_gridStatusOfTreatment = new FormData("100%");
		fd_gridStatusOfTreatment.setMargins(new Margins(0, 0, 5, 0));
		lcReportingParams.add(gridStatusOfTreatment, fd_gridStatusOfTreatment);
		FormData fd_gridBreakupOfTreatments = new FormData("100%");
		fd_gridBreakupOfTreatments.setMargins(new Margins(0, 0, 5, 0));
		lcReportingParams.add(gridBreakupOfTreatments, fd_gridBreakupOfTreatments);
		gridBreakupOfTreatments.setHeight("150");

		lcReportingParams.setLayoutData(new Margins(5, 5, 5, 5));
		add(lcReportingParams);

	}
}
