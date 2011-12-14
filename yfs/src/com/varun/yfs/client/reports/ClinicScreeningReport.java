package com.varun.yfs.client.reports;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
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
import com.varun.yfs.client.admin.rpc.StoreLoader;
import com.varun.yfs.client.admin.rpc.StoreLoaderAsync;
import com.varun.yfs.client.index.ModelDataEnum;
import com.varun.yfs.client.reports.rpc.ReportDetailService;
import com.varun.yfs.client.reports.rpc.ReportDetailServiceAsync;
import com.varun.yfs.client.reports.rpc.ReportType;

public class ClinicScreeningReport extends LayoutContainer
{
	private ReportDetailServiceAsync reportDetailService = GWT.create(ReportDetailService.class);
	private StoreLoaderAsync storeLoader = GWT.create(StoreLoader.class);
	private LabelField lblfldTotalScreened;
	private Grid<ModelData> gridNonSurgeryCases;
	private Grid<ModelData> gridSurgeryCases;

	private ComboBox<ModelData> clinics;

	final Listener<MessageBoxEvent> DUMMYLISTENER = new Listener<MessageBoxEvent>()
	{
		public void handleEvent(MessageBoxEvent ce)
		{
		}
	};

	public ClinicScreeningReport()
	{
		setHeight("700");
	}

	@Override
	protected void onRender(Element parent, int index)
	{

		super.onRender(parent, index);

		LayoutContainer layoutContainer = new LayoutContainer();
		layoutContainer.setLayout(new TableLayout(4));

		final DateField dtfldFromDate = new DateField();
		dtfldFromDate.setFieldLabel("From Date");
		LayoutContainer frmpnlFromDate = new LayoutContainer();
		frmpnlFromDate.setLayout(new FormLayout());
		frmpnlFromDate.add(dtfldFromDate, new FormData("100%"));

		TableData td_frmpnlFromDate = new TableData();
		td_frmpnlFromDate.setPadding(5);
		td_frmpnlFromDate.setMargin(5);
		layoutContainer.add(frmpnlFromDate, td_frmpnlFromDate);

		final DateField dtfldToDate = new DateField();
		dtfldToDate.setFieldLabel("To Date");
		LayoutContainer frmpnlToDate = new LayoutContainer();
		frmpnlToDate.setLayout(new FormLayout());
		frmpnlToDate.add(dtfldToDate, new FormData("100%"));

		TableData td_frmpnlToDate = new TableData();
		td_frmpnlToDate.setPadding(5);
		td_frmpnlToDate.setMargin(5);
		layoutContainer.add(frmpnlToDate, td_frmpnlToDate);

		clinics = new ComboBox<ModelData>();
		clinics.setFieldLabel("Select a Clinic");
		clinics.setStore(new ListStore<ModelData>());
		clinics.setDisplayField("clinicName");
		clinics.setValueField("clinicId");
		storeLoader.getModel(ModelDataEnum.Clinic.name(), new AsyncCallback<ModelData>()
		{

			@Override
			public void onFailure(Throwable caught)
			{
				MessageBox.info("Error", "Error encountered while loading report params.", DUMMYLISTENER);
			}

			@Override
			public void onSuccess(ModelData result)
			{
				clinics.getStore().add((List<ModelData>) result.get("data"));
			}
		});
		LayoutContainer frmpnlClinics = new LayoutContainer();
		frmpnlClinics.setLayout(new FormLayout());
		frmpnlClinics.add(clinics, new FormData("100%"));

		TableData td_frmpnlClinics = new TableData();
		td_frmpnlClinics.setPadding(5);
		td_frmpnlClinics.setMargin(5);
		layoutContainer.add(frmpnlClinics, td_frmpnlClinics);

		LayoutContainer frmpnlRefresh = new LayoutContainer();
		frmpnlRefresh.setLayout(new FormLayout());

		Button btnRefresh = new Button("Refresh");
		frmpnlRefresh.add(btnRefresh, new FormData("100%"));
		TableData td_frmpnlRefresh = new TableData();
		td_frmpnlRefresh.setPadding(5);
		td_frmpnlRefresh.setMargin(5);
		layoutContainer.add(frmpnlRefresh, td_frmpnlRefresh);
		frmpnlRefresh.setBorders(true);
		add(layoutContainer);

		btnRefresh.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				ModelData model = new BaseModelData();
				model.set("dateFrom", dtfldFromDate.getValue().getTime());
				model.set("dateTo", dtfldToDate.getValue().getTime());
				model.set("clinic", clinics.getValueField());
				reportDetailService.getModel(ReportType.MedicalCamp, model, new AsyncCallback<ModelData>()
				{
					@Override
					public void onSuccess(ModelData result)
					{
						lblfldTotalScreened.setText(lblfldTotalScreened.getText() + result.get("locationsCount"));
					}

					@Override
					public void onFailure(Throwable caught)
					{
						MessageBox.info("Error", "Error encountered while loading the report." + caught.getMessage(), DUMMYLISTENER);
					}
				});
			}
		});

		FormPanel lcReportingParams = new FormPanel();
		lcReportingParams.setHeaderVisible(false);
		lcReportingParams.setSize("", "700");

		lblfldTotalScreened = new LabelField("Total Number Screened:");
		lcReportingParams.add(lblfldTotalScreened, new FormData("100%"));

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();

		ColumnConfig clmncnfgNewColumn = new ColumnConfig("name", "Surgery Cases", 150);
		configs.add(clmncnfgNewColumn);

		ColumnConfig clmncnfgTypeOfSurgery = new ColumnConfig("typeOfSurgery", "Type Of Surgery", 150);
		configs.add(clmncnfgTypeOfSurgery);

		ColumnConfig clmncnfgClosedCases = new ColumnConfig("closedCases", "Closed Cases", 150);
		configs.add(clmncnfgClosedCases);

		ColumnConfig clmncnfgPendingCases = new ColumnConfig("pendingCases", "Pending Cases", 150);
		configs.add(clmncnfgPendingCases);

		ColumnConfig clmncnfgTotal = new ColumnConfig("total", "Total", 150);
		configs.add(clmncnfgTotal);

		gridSurgeryCases = new Grid<ModelData>(new ListStore<ModelData>(), new ColumnModel(configs));
		gridSurgeryCases.setHeight("150");
		gridSurgeryCases.setBorders(true);

		List<ColumnConfig> configsBreakupOfTreatments = new ArrayList<ColumnConfig>();

		ColumnConfig clmncnfgNewColumn_4 = new ColumnConfig("nonSurgicalCases", "Non-Surgical Cases", 150);
		configsBreakupOfTreatments.add(clmncnfgNewColumn_4);

		ColumnConfig clmncnfgNewColumn_5 = new ColumnConfig("noOfCases", "No. Of Cases", 150);
		configsBreakupOfTreatments.add(clmncnfgNewColumn_5);

		gridNonSurgeryCases = new Grid<ModelData>(new ListStore<ModelData>(), new ColumnModel(configsBreakupOfTreatments));
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
