package com.varun.yfs.client.reports;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ChangeEvent;
import com.extjs.gxt.ui.client.data.PropertyChangeEvent;

//public class TeamSales extends LayoutContainer
//{
//	private ContentPanel cp_1;
//
//	public TeamSales()
//	{
//	}
//
//	@Override
//	protected void onRender(Element parent, int index)
//	{
//
//		super.onRender(parent, index);
//
//		final ListStore<TeamSales> store = new ListStore<TeamSales>();
//		TeamSales tmSales = new TeamSales("Requirement Analysis", 0, 10, 20);
//		store.add(tmSales);
//		tmSales = new TeamSales("Technology Spiking Effort", 12, 2, 3);
//		store.add(tmSales);
//		tmSales = new TeamSales("UI Prototypes", 12, 2, 3);
//		store.add(tmSales);
//		tmSales = new TeamSales("FS", 12, 2, 3);
//		store.add(tmSales);
//		tmSales = new TeamSales("Feature 1", 1, 2, 3);
//		store.add(tmSales);
//		tmSales = new TeamSales("Feature 2", 10, 232, 354);
//		store.add(tmSales);
//		tmSales = new TeamSales("Performance Test", 152, 422, 353);
//		store.add(tmSales);
//		tmSales = new TeamSales("System Test", 2, 20, 365);
//		store.add(tmSales);
//		tmSales = new TeamSales("Release", 12, 2, 3);
//		store.add(tmSales);
//		tmSales = new TeamSales("Handover", 1, 204, 305);
//		store.add(tmSales);
//
//		String url = "open-flash-chart.swf";
//		final Chart chart = new Chart(url);
//
//		ChartModel model = new ChartModel("Project progress report", "font-size: 14px; font-family: Verdana; text-align: center;");
//		model.setBackgroundColour("fefefe");
//		model.setLegend(new Legend(Position.TOP, true));
//		model.setScaleProvider(ScaleProvider.ROUNDED_NEAREST_SCALE_PROVIDER);
//
//		BarChart bar = new BarChart(BarStyle.GLASS);
//		bar.setColour("00aa00");
//		BarDataProvider barProvider = new BarDataProvider("alphasales", "month");
//		barProvider.bind(store);
//		bar.setDataProvider(barProvider);
//		model.addChartConfig(bar);
//
//		bar = new BarChart(BarStyle.GLASS);
//		bar.setColour("0000cc");
//		barProvider = new BarDataProvider("betasales");
//		barProvider.bind(store);
//		bar.setDataProvider(barProvider);
//		model.addChartConfig(bar);
//
//		bar = new BarChart(BarStyle.GLASS);
//		bar.setColour("ff6600");
//		barProvider = new BarDataProvider("gammasales");
//		barProvider.bind(store);
//		bar.setDataProvider(barProvider);
//		model.addChartConfig(bar);
//
//		LineChart line = new LineChart();
//		line.setAnimateOnShow(true);
//		line.setText("Average");
//		line.setColour("FF0000");
//		LineDataProvider lineProvider = new LineDataProvider("avgsales");
//		lineProvider.bind(store);
//		line.setDataProvider(lineProvider);
//		model.addChartConfig(line);
//
//		chart.setChartModel(model);
//
//		// grid
//		NumberPropertyEditor npe = new NumberPropertyEditor(Integer.class);
//		ArrayList<ColumnConfig> cols = new ArrayList<ColumnConfig>();
//
//		ColumnConfig qtr = new ColumnConfig("month", "Feature", 100);
//		cols.add(qtr);
//		qtr.setEditor(new CellEditor(new TextField<String>()));
//
//		ColumnConfig alpha = new ColumnConfig("alphasales", "On-Time Variance", 100);
//		cols.add(alpha);
//		NumberField nf = new NumberField();
//		nf.setPropertyEditor(npe);
//		alpha.setEditor(new CellEditor(nf));
//
//		ColumnConfig beta = new ColumnConfig("betasales", "Cumulative Actual Effort", 100);
//		cols.add(beta);
//		nf = new NumberField();
//		nf.setPropertyEditor(npe);
//		beta.setEditor(new CellEditor(nf));
//
//		ColumnConfig gamma = new ColumnConfig("gammasales", "Cumulative Planned Effort", 100);
//		cols.add(gamma);
//		nf = new NumberField();
//		nf.setPropertyEditor(npe);
//		gamma.setEditor(new CellEditor(nf));
//
//		ColumnModel cm = new ColumnModel(cols);
//
//		EditorGrid<TeamSales> teamSalesGrid = new EditorGrid<TeamSales>(store, cm);
//		teamSalesGrid.getView().setForceFit(true);
//		teamSalesGrid.getView().setShowDirtyCells(false);
//		teamSalesGrid.addListener(Events.AfterEdit, new Listener<GridEvent<TeamSales>>()
//		{
//			public void handleEvent(GridEvent<TeamSales> be)
//			{
//				store.commitChanges();
//			}
//		});
//
//		LayoutContainer lc = new LayoutContainer();
//		RowLayout rl = new RowLayout();
//		lc.setLayout(rl);
//		lc.setHeight(600);
//
//		RowData data;
//		data = new RowData(1, 300, new Margins(10));
//		ContentPanel cp = new ContentPanel(new FitLayout());
//		cp.setHeading("Chart");
//		cp.add(chart);
//		lc.add(cp, data);
//		cp.setHeight("600");
//
//		data = new RowData(1, 1, new Margins(10));
//		cp_1 = new ContentPanel(new FitLayout());
//		cp_1.setHeading("Project progress");
//		cp_1.add(teamSalesGrid);
//		lc.add(cp_1, data);
//		cp_1.setHeight("600");
//
//		data = new RowData(1, 60, new Margins(10));
//		LayoutContainer bbar = new LayoutContainer(new RowLayout(Orientation.HORIZONTAL));
//		lc.add(bbar, data);
//
//		add(lc);
//		lc.setHeight("700");
//
//	}
//
//	public ChartModel getHorizontalBarChartModel()
//	{
//
//		// setLayout(new FlowLayout(12));
//		//
//		// ContentPanel cp = new ContentPanel();
//		// cp.setHeading("Horizontal Bar chart");
//		// cp.setFrame(true);
//		// cp.setSize(550, 400);
//		// cp.setLayout(new FitLayout());
//		//
//		// String url = "open-flash-chart.swf";
//		// final Chart chart = new Chart(url);
//		//
//		// chart.setBorders(true);
//		// chart.setChartModel(getHorizontalBarChartModel());
//		// cp.add(chart);
//		//
//		// add(cp, new MarginData(10));
//
//		// Create a ChartModel with the Chart Title and some style attributes
//		ChartModel cm = new ChartModel("Students by Department", "font-size: 14px; font-family:      Verdana; text-align: center;");
//
//		XAxis xa = new XAxis();
//		// set the maximum, minimum and the step value for the X axis
//		xa.setRange(0, 200, 50);
//		cm.setXAxis(xa);
//
//		YAxis ya = new YAxis();
//		// Add the labels to the Y axis
//		ya.addLabels("CSE", "EEE", "CE", "ME", "CHE");
//		ya.setOffset(true);
//		cm.setYAxis(ya);
//
//		// create a Horizontal Bar Chart object and add bars to the object
//		HorizontalBarChart bchart = new HorizontalBarChart();
//		bchart.setTooltip("#val#Students");
//		bchart.addBars(new HorizontalBarChart.Bar(60, "#ffff00"));
//		// different color for different bars
//		bchart.addBars(new HorizontalBarChart.Bar(180, "#0000ff"));
//		bchart.addBars(new HorizontalBarChart.Bar(180, "#00ff00"));
//		bchart.addBars(new HorizontalBarChart.Bar(120, "#ff0000"));
//		bchart.addBars(new HorizontalBarChart.Bar(120, "#333ccc"));
//
//		// add the bchart as the Chart Config of the ChartModel
//		cm.addChartConfig(bchart);
//		cm.setTooltipStyle(new ToolTip(MouseStyle.FOLLOW));
//		return cm;
//	}
//
//}

public class TeamSales extends BaseModel
{

	private static final long serialVersionUID = 2103699184769341265L;

	public TeamSales(String month, int a, int b, int c)
	{
		setMonth(month);
//		setAlphaSales(a);
//		setBetaSales(b);
//		setGammaSales(c);
//		setAvgSales();
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
