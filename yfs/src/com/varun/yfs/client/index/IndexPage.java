package com.varun.yfs.client.index;

import java.util.List;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.SplitButton;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.varun.yfs.client.admin.common.AdministrationPage;
import com.varun.yfs.client.admin.location.LocationAdministration;
import com.varun.yfs.client.admin.rpc.StoreLoader;
import com.varun.yfs.client.admin.rpc.StoreLoaderAsync;
import com.varun.yfs.client.admin.users.UserAdministration;
import com.varun.yfs.client.landing.LandingPage;
import com.varun.yfs.client.reports.ReportPage;
import com.varun.yfs.client.screening.ScreeningDetail;
import com.varun.yfs.client.screening.imports.ImportDetail;
import com.varun.yfs.client.util.Util;


public class IndexPage extends LayoutContainer
{
	private StoreLoaderAsync storeLoader = GWT.create(StoreLoader.class);
	private LayoutContainer layoutContainer;
	private static LayoutContainer layoutContainerCenter;
	private LayoutContainer layoutContainerEast;
	private LayoutContainer layoutContainerSouth;
	private LayoutContainer layoutContainerNorth;
	private LayoutContainer layoutContainerWest;

	public IndexPage()
	{
		setHeight("700");
		layoutContainer = new LayoutContainer();
		layoutContainerCenter = new LayoutContainer();
		layoutContainerCenter.setBorders(true);
		layoutContainerEast = new LayoutContainer();
		layoutContainerSouth = new LayoutContainer();
		layoutContainerNorth = new LayoutContainer();
		layoutContainerWest = new LayoutContainer();
	}
	
	public static void maskCenterComponent(String message)
	{
		layoutContainerCenter.mask(message);
	}

	public static void unmaskCenterComponent()
	{
		layoutContainerCenter.unmask();
	}
	
	@Override
	protected void onRender(Element parent, int index)
	{
		super.onRender(parent, index);

		layoutContainer.setLayout(new BorderLayout());

		buildCenterLayout();

		buildEastLayout();

		buildWestLayout();

		buildNorthLayout();

		buildSouthLayout();

		add(layoutContainer);
	}

	private void buildCenterLayout()
	{
		layoutContainerCenter.setLayout(new FitLayout());

		layoutContainer.add(layoutContainerCenter, new BorderLayoutData(LayoutRegion.CENTER));
		layoutContainer.add(layoutContainerCenter);
		layoutContainer.setHeight("670px");

		layoutContainerCenter.add(new LandingPage());
	}

	private void buildSouthLayout()
	{
		BorderLayoutData bldSouth = new BorderLayoutData(LayoutRegion.SOUTH);
		bldSouth.setHidden(true);
		bldSouth.setCollapsible(true);
		layoutContainer.add(layoutContainerSouth, bldSouth);
		layoutContainerSouth.setBorders(true);
	}

	private void buildEastLayout()
	{
		BorderLayoutData bldEast = new BorderLayoutData(LayoutRegion.EAST);
		bldEast.setHidden(true);
		bldEast.setFloatable(false);
		bldEast.setCollapsible(true);
		layoutContainer.add(layoutContainerEast, bldEast);
		layoutContainerEast.setBorders(true);
	}

	private void buildNorthLayout()
	{
		BorderLayoutData bldNorth = new BorderLayoutData(LayoutRegion.NORTH, 60.0f);
		bldNorth.setHidden(true);
		bldNorth.setCollapsible(true);
		layoutContainer.add(layoutContainerNorth, bldNorth);
		layoutContainerNorth.setBorders(true);
	}

	private void buildWestLayout()
	{
		layoutContainerWest.setLayout(new AccordionLayout());
		BorderLayoutData bldWest = new BorderLayoutData(LayoutRegion.WEST, 200.0f);
		bldWest.setCollapsible(true);
		bldWest.setSplit(true);
		layoutContainerWest.setHeight("300");
		layoutContainerWest.setBorders(true);
		layoutContainer.add(layoutContainerWest, bldWest);

		buildScreeningPanel();

		buildReportsPanel();

		buildAdministrationPanel();
	}

	private void buildAdministrationPanel()
	{
		final ContentPanel cpAdministration = new ContentPanel();
		cpAdministration.setAnimCollapse(false);
		cpAdministration.setHeading("Administration");
		cpAdministration.setLayout(new FitLayout());
		layoutContainerWest.add(cpAdministration);

		TreeStore<ModelData> store = new TreeStore<ModelData>();
		final TreePanel<ModelData> tree = new TreePanel<ModelData>(store);
		tree.setIconProvider(new ModelIconProvider<ModelData>()
		{

			public AbstractImagePrototype getIcon(ModelData model)
			{
				if (model.get("icon") != null)
				{
					return IconHelper.createStyle((String) model.get("icon"));
				} else
				{
					return null;
				}
			}

		});
		tree.setDisplayProperty("name");
		final AdministrationPage widget = new AdministrationPage();

		tree.addListener(Events.OnClick, new Listener<BaseEvent>()
		{
			@Override
			public void handleEvent(BaseEvent be)
			{
				ModelData selectedItem = tree.getSelectionModel().getSelectedItem();
				boolean isLeaf = tree.isLeaf(selectedItem);
				if (!cpAdministration.isCollapsed() && isLeaf)
				{
					layoutContainerCenter.mask();
					layoutContainerCenter.removeAll();

					String adminEntityEdit = Util.stripSpace(selectedItem.get("name").toString());
					if (ModelDataEnum.isLocationAdmin(adminEntityEdit))
					{
						LocationAdministration widget = new LocationAdministration();
						widget.reinitPage(adminEntityEdit);
						layoutContainerCenter.add(widget);
					} else if (adminEntityEdit.equalsIgnoreCase("Users"))
					{
						UserAdministration widget = new UserAdministration();
						widget.reinitPage(adminEntityEdit);
						layoutContainerCenter.add(widget);
					} else
					{
						widget.reinitPage(adminEntityEdit);
						layoutContainerCenter.add(widget);
					}

					layoutContainerCenter.layout(true);
					layoutContainerCenter.unmask();
				}
			}
		});

		storeLoader.getListStore(MainPanelEnum.Administration.name(), new AsyncCallback<List<ModelData>>()
		{

			@Override
			public void onFailure(Throwable caught)
			{
				System.out.println(caught.getMessage());
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(List<ModelData> result)
			{

				for (ModelData modelData : result)
				{
					tree.getStore().add(modelData, false);
					List<ModelData> object = (List<ModelData>) modelData.get("children");
					if (object != null)
						tree.getStore().add(modelData, object, false);
				}
				tree.setExpanded(result.get(0), true);
			}
		});
		cpAdministration.add(tree);
	}

	private void buildReportsPanel()
	{
		final ContentPanel cpReports = new ContentPanel();
		cpReports.setAnimCollapse(false);
		cpReports.setHeading("Reports");
		cpReports.setLayout(new FitLayout());
		layoutContainerWest.add(cpReports);

		TreeStore<ModelData> store = new TreeStore<ModelData>();
		final TreePanel<ModelData> tree = new TreePanel<ModelData>(store);
		tree.setIconProvider(new ModelIconProvider<ModelData>()
		{

			public AbstractImagePrototype getIcon(ModelData model)
			{
				if (model.get("icon") != null)
				{
					return IconHelper.createStyle((String) model.get("icon"));
				} else
				{
					return null;
				}
			}

		});
		tree.setDisplayProperty("name");

		tree.addListener(Events.OnClick, new Listener<BaseEvent>()
		{
			@Override
			public void handleEvent(BaseEvent be)
			{
				boolean isLeaf = tree.isLeaf(tree.getSelectionModel().getSelectedItem());
				if (!cpReports.isCollapsed() && isLeaf)
				{
					layoutContainerCenter.removeAll();
					layoutContainerCenter.setLayoutData(new FitData(15));
					layoutContainerCenter.add(new ReportPage());
					layoutContainerCenter.layout(true);
				}
			}
		});

		storeLoader.getListStore(MainPanelEnum.Reports.name(), new AsyncCallback<List<ModelData>>()
		{

			@Override
			public void onFailure(Throwable caught)
			{
				System.out.println(caught.getMessage());
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(List<ModelData> result)
			{
				for (ModelData modelData : result)
				{
					tree.getStore().add(modelData, false);
					Object object = modelData.get("children");
					if (object != null)
						tree.getStore().add(modelData, (List<ModelData>) object, false);
				}
			}
		});

		cpReports.add(tree);
	}

	private void buildScreeningPanel()
	{
		final ContentPanel cpScreening = new ContentPanel();
		cpScreening.setAnimCollapse(false);
		cpScreening.setHeading("Screening");
		cpScreening.setLayout(new FitLayout());
		layoutContainerWest.add(cpScreening);

		
		ToolBar toolbar = new ToolBar();
		SplitButton splitItem = new SplitButton("");
		splitItem.setIcon(IconHelper.createPath(GWT.getModuleBaseURL() + "images/Home.png"));
	  
	    Menu menu = new Menu();  
	    splitItem.setMenu(menu);  
	  
	    toolbar.add(splitItem);  
	    

		final ScreeningDetail widget = new ScreeningDetail();

		MenuItem newScreening = new MenuItem("New Screening", IconHelper.createPath(GWT.getModuleBaseURL() + "images/add.png", 16, 16));
		newScreening.addSelectionListener(new SelectionListener<MenuEvent>()
		{

			@Override
			public void componentSelected(MenuEvent ce)
			{
				layoutContainerCenter.removeAll();
				layoutContainerCenter.setLayoutData(new FitData(15));
				
				layoutContainerCenter.mask("Loading...");
				layoutContainerCenter.add(widget);
				widget.initialize("New Screening");
				
				layoutContainerCenter.layout(true);
			}
		});
		menu.add(newScreening);
		
		MenuItem referralButton = new MenuItem("New Referral", IconHelper.createPath(GWT.getModuleBaseURL() + "images/arrow_refresh.png", 16, 16));
		referralButton.addSelectionListener(new SelectionListener<MenuEvent>()
		{
			@Override
			public void componentSelected(MenuEvent ce)
			{
				layoutContainerCenter.removeAll();
				layoutContainerCenter.setLayoutData(new FitData(15));
				
				layoutContainerCenter.mask("Loading...");
				layoutContainerCenter.add(widget);
				widget.initialize("New Referral");
				
				layoutContainerCenter.layout(true);
			}
		});
		menu.add(referralButton);

		MenuItem newImport = new MenuItem("Import", IconHelper.createPath(GWT.getModuleBaseURL() + "images/document_import.png", 16, 16));
		newImport.addSelectionListener(new SelectionListener<MenuEvent>()
		{
			@Override
			public void componentSelected(MenuEvent ce)
			{
				layoutContainerCenter.removeAll();
				layoutContainerCenter.setLayoutData(new FitData(15));
				
				layoutContainerCenter.mask("Loading...");
				layoutContainerCenter.add(new ImportDetail());
				
				layoutContainerCenter.layout(true);
			}
		});
		menu.add(newImport);

		cpScreening.setTopComponent(toolbar);

		TreeStore<ModelData> store = new TreeStore<ModelData>();
		final TreePanel<ModelData> tree = new TreePanel<ModelData>(store);
		tree.setIconProvider(new ModelIconProvider<ModelData>()
		{
			public AbstractImagePrototype getIcon(ModelData model)
			{
				if (model.get("icon") != null)
				{
					return IconHelper.createStyle((String) model.get("icon"));
				} else
				{
					return null;
				}
			}
		});
		tree.setDisplayProperty("name");
		StoreFilterField<ModelData> filter = new StoreFilterField<ModelData>()
		{
			@Override
			protected boolean doSelect(Store<ModelData> store, ModelData parent, ModelData record, String property, String filter)
			{
				String name = record.get("name");
				name = name.toLowerCase();
				if (name.startsWith(filter.toLowerCase()))
				{
					return true;
				}
				return false;
			}
		};
		filter.bind(store);
		toolbar.add(filter);

		storeLoader.getListStore(MainPanelEnum.ScreeningLocations.name(), new AsyncCallback<List<ModelData>>()
		{

			@Override
			public void onFailure(Throwable caught)
			{
				System.out.println(caught.getMessage());
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(List<ModelData> result)
			{
//				for (ModelData modelData : result)
//				{
//					tree.getStore().add(modelData, true); // country
//					List<ModelData> children = (List<ModelData>) modelData.get("children"); // states
//					if (children != null)
//					{
//						tree.getStore().add(modelData, children, true);
//						for (ModelData modelData2 : children) // Villages, Towns, Cities
//						{
//							List<ModelData> child = (List<ModelData>) modelData2.get("children");
//							tree.getStore().add(modelData2, child, true);
//							
//							for (ModelData modelData3 : child) // screening data
//							{
//								List<ModelData> childScr = (List<ModelData>) modelData3.get("children");
//								tree.getStore().add(modelData3, childScr, true);
//							}
//						}
//					}
//				}
				for (ModelData modelData : result)
				{
					tree.getStore().add(modelData, true); // chapter-name
					List<ModelData> children = (List<ModelData>) modelData.get("children"); // screening
					if (children != null)
					{
						tree.getStore().add(modelData, children, true);
					}
				}
			}
		});

		tree.addListener(Events.OnClick, new Listener<BaseEvent>()
		{
			@Override
			public void handleEvent(BaseEvent be)
			{
				boolean isLeaf = tree.isLeaf(tree.getSelectionModel().getSelectedItem());
				if (!cpScreening.isCollapsed() && isLeaf)
				{
					layoutContainerCenter.removeAll();
					layoutContainerCenter.setLayoutData(new FitData(15));
					String string = "Referral " + tree.getSelectionModel().getSelectedItem().get("name").toString();
					widget.initialize(string);
					layoutContainerCenter.add(widget);
					layoutContainerCenter.layout(true);
				}
			}
		});

		cpScreening.add(tree);
	}
}
