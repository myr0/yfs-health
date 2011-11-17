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
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.PropertyChangeEvent;
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

public class ClinicScreeningReport extends LayoutContainer
{
	public ClinicScreeningReport()
	{
		setHeight("700");
	}

	@Override
	protected void onRender(Element parent, int index)
	{

		super.onRender(parent, index);

		final ListStore<TeamSales> store = new ListStore<TeamSales>();
		TeamSales tmSales = new TeamSales("Requirement Analysis", 0, 10, 20);
		store.add(tmSales);
		tmSales = new TeamSales("Technology Spiking Effort", 12, 2, 3);
		store.add(tmSales);
		tmSales = new TeamSales("UI Prototypes", 12, 2, 3);
		store.add(tmSales);
		tmSales = new TeamSales("FS", 12, 2, 3);
		store.add(tmSales);
		tmSales = new TeamSales("Feature 1", 1, 2, 3);
		store.add(tmSales);
		tmSales = new TeamSales("Feature 2", 10, 232, 354);
		store.add(tmSales);
		tmSales = new TeamSales("Performance Test", 152, 422, 353);
		store.add(tmSales);
		tmSales = new TeamSales("System Test", 2, 20, 365);
		store.add(tmSales);
		tmSales = new TeamSales("Release", 12, 2, 3);
		store.add(tmSales);
		tmSales = new TeamSales("Handover", 1, 204, 305);
		store.add(tmSales);

		String url = "open-flash-chart.swf";
		final Chart chart = new Chart(url);
		chart.setSwfHeight("40%");
		chart.setSwfWidth("40%");

		ChartModel model = new ChartModel("Project progress report", "font-size: 14px; font-family: Verdana; text-align: center;");
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

		Button btnRefresh = new Button("Refresh");
		frmpnlRefresh.add(btnRefresh, new FormData("100%"));
		layoutContainer.add(frmpnlRefresh);
		frmpnlRefresh.setBorders(true);

		add(layoutContainer);

		FormPanel lcReportingParams = new FormPanel();
		lcReportingParams.setHeading("Clinic Screening Report");
		lcReportingParams.setSize("500", "700");

		lcReportingParams.add(chart, new FormData("60%"));
		chart.setSize("80%", "150px");

		LabelField lblfldTotalScreened = new LabelField("Total Number Screened:");
		lcReportingParams.add(lblfldTotalScreened, new FormData("100%"));

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig clmncnfgNewColumn = new ColumnConfig("surgeryCases", "Surgery Cases", 150);
		configs.add(clmncnfgNewColumn);

		ColumnConfig clmncnfgTypeOfSurgery = new ColumnConfig("typeOfSurgery", "Type Of Surgery", 150);
		configs.add(clmncnfgTypeOfSurgery);

		ColumnConfig clmncnfgClosedCases = new ColumnConfig("closedCases", "Closed Cases", 150);
		configs.add(clmncnfgClosedCases);

		ColumnConfig clmncnfgPendingCases = new ColumnConfig("pendingCases", "Pending Cases", 150);
		configs.add(clmncnfgPendingCases);

		ColumnConfig clmncnfgTotal = new ColumnConfig("total", "Total", 150);
		configs.add(clmncnfgTotal);

		Grid<ModelData> gridSurgeryCases = new Grid<ModelData>(new ListStore<ModelData>(), new ColumnModel(configs));
		gridSurgeryCases.setHeight("150");
		gridSurgeryCases.setBorders(true);

		List<ColumnConfig> configsBreakupOfTreatments = new ArrayList<ColumnConfig>();

		ColumnConfig clmncnfgNewColumn_4 = new ColumnConfig("nonSurgicalCases", "Non-Surgical Cases", 150);
		configsBreakupOfTreatments.add(clmncnfgNewColumn_4);

		ColumnConfig clmncnfgNewColumn_5 = new ColumnConfig("noOfCases", "No. Of Cases", 150);
		configsBreakupOfTreatments.add(clmncnfgNewColumn_5);

		Grid<ModelData> gridNonSurgeryCases = new Grid<ModelData>(new ListStore<ModelData>(), new ColumnModel(configsBreakupOfTreatments));
		gridNonSurgeryCases.setBorders(true);

		FormData fd_gridStatusOfTreatment = new FormData("100%");
		fd_gridStatusOfTreatment.setMargins(new Margins(0, 0, 5, 0));
		lcReportingParams.add(gridSurgeryCases, fd_gridStatusOfTreatment);
		FormData fd_gridBreakupOfTreatments = new FormData("100%");
		fd_gridBreakupOfTreatments.setMargins(new Margins(0, 0, 5, 0));
		lcReportingParams.add(gridNonSurgeryCases, fd_gridBreakupOfTreatments);
		gridNonSurgeryCases.setHeight("150");

		lcReportingParams.setLayoutData(new Margins(5, 5, 5, 5));
		add(lcReportingParams);

	}
}

class TeamSales extends BaseModel
{

	private static final long serialVersionUID = 2103699184769341265L;

	public TeamSales(String month, int a, int b, int c)
	{
		setMonth(month);
		setAlphaSales(a);
		setBetaSales(b);
		setGammaSales(c);
		setAvgSales();
	}

	public int getAlphaSales()
	{
		return (Integer) get("alphasales");
	}

	public int getBetaSales()
	{
		return (Integer) get("betasales");
	}

	public int getGammaSales()
	{
		return (Integer) get("gammasales");
	}

	public String getMonth()
	{
		return (String) get("month");
	}

	@Override
	public void notify(ChangeEvent evt)
	{
		super.notify(evt);

		PropertyChangeEvent e = (PropertyChangeEvent) evt;
		if (!e.getName().equals("avgsales"))
		{
			setAvgSales();
		}
	}

	public void setAlphaSales(int sales)
	{
		set("alphasales", sales);
	}

	public void setAvgSales()
	{
		if (get("alphasales") != null && get("gammasales") != null && get("betasales") != null)
		{
			double avg = (getAlphaSales() + getBetaSales() + getGammaSales()) / 3.0;
			set("avgsales", avg);
		}
	}

	public void setBetaSales(int sales)
	{
		set("betasales", sales);
	}

	public void setGammaSales(int sales)
	{
		set("gammasales", sales);
	}

	public void setMonth(String month)
	{
		set("month", month);
	}
}
