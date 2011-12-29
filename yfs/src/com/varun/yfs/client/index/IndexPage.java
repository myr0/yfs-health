package com.varun.yfs.client.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelIconProvider;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.StoreFilterField;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.RootPanel;
import com.varun.yfs.client.admin.common.AdministrationPage;
import com.varun.yfs.client.admin.location.LocationAdministration;
import com.varun.yfs.client.admin.rpc.StoreLoader;
import com.varun.yfs.client.admin.rpc.StoreLoaderAsync;
import com.varun.yfs.client.admin.users.UserAdministration;
import com.varun.yfs.client.help.HelpPage;
import com.varun.yfs.client.images.YfsImageBundle;
import com.varun.yfs.client.landing.LandingPage;
import com.varun.yfs.client.login.Login;
import com.varun.yfs.client.login.LoginService;
import com.varun.yfs.client.reports.ClinicScreeningReport;
import com.varun.yfs.client.reports.EventsReport;
import com.varun.yfs.client.reports.MedicalCampProgramReport;
import com.varun.yfs.client.reports.OverallReport;
import com.varun.yfs.client.reports.SchoolHealthProgramReport;
import com.varun.yfs.client.screening.camp.CampScreeningDetail;
import com.varun.yfs.client.screening.clinic.ClinicScreeningDetail;
import com.varun.yfs.client.screening.school.SchoolScreeningDetail;
import com.varun.yfs.client.util.Util;

public class IndexPage extends LayoutContainer
{
	private static StoreLoaderAsync storeLoader = GWT.create(StoreLoader.class);
	private ContentPanel mainContentPanel;
	private static LayoutContainer layoutContainerCenter;
	private LayoutContainer layoutContainerEast;
	private LayoutContainer layoutContainerSouth;
	private LayoutContainer layoutContainerNorth;
	private LayoutContainer layoutContainerWest;

	private ContentPanel cpSchoolScreening;
	private static TreeStore<ModelData> schoolScreeningPanelStore = new TreeStore<ModelData>();
	private static TreePanel<ModelData> treeSchoolScreeningPanel = new TreePanel<ModelData>(schoolScreeningPanelStore);

	private static ContentPanel cpClinicScreening;
	private static TreeStore<ModelData> clinicScreeningPanelStore = new TreeStore<ModelData>();
	private static TreePanel<ModelData> treeClinicScreeningPanel = new TreePanel<ModelData>(clinicScreeningPanelStore);

	private ContentPanel cpCampScreening;
	private static TreeStore<ModelData> campScreeningPanelStore = new TreeStore<ModelData>();
	private static TreePanel<ModelData> treeCampScreeningPanel = new TreePanel<ModelData>(campScreeningPanelStore);

	private String userName;

	protected final static Listener<MessageBoxEvent> DUMMYLISTENER = new Listener<MessageBoxEvent>()
	{
		public void handleEvent(MessageBoxEvent ce)
		{
		}
	};

	public IndexPage()
	{
		this("Anonymous");
	}

	public IndexPage(String userLoginName)
	{
//		setHeight("100%");
		setLayout(new FitLayout());
		this.userName = userLoginName;

		mainContentPanel = new ContentPanel();
		layoutContainerCenter = new LayoutContainer();
		layoutContainerCenter.setBorders(true);
		layoutContainerEast = new LayoutContainer();
		layoutContainerSouth = new LayoutContainer();
		layoutContainerNorth = new LayoutContainer();
		layoutContainerWest = new LayoutContainer();
	}

	public static void reinitScreeningPanel()
	{
		refreshIndexPanel(true);
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

		mainContentPanel.setLayout(new BorderLayout());
		mainContentPanel.setHeading("YFS Health Chapter");

		buildToolBar();

		buildCenterLayout();

		buildEastLayout();

		buildWestLayout();

		buildNorthLayout();

		buildSouthLayout();

		add(mainContentPanel, new FitData(5, 5, 5, 5));
	}

	private void buildToolBar()
	{
		ToolBar toolbar = new ToolBar();

		Button home = new Button("", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.homeButtonIcon()));
		home.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				layoutContainerCenter.removeAll();
				layoutContainerCenter.setLayoutData(new FitData(15));

				cpSchoolScreening.expand();
				layoutContainerCenter.add(new LandingPage());
				layoutContainerCenter.layout(true);
			}
		});

		Button help = new Button("", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.helpButtonIcon()));
		help.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				Window dialog = new Window();
				dialog.setHeading("Help");
				dialog.setSize("840", "600");
				dialog.setScrollMode(Scroll.AUTO);
				dialog.setLayout(new FitLayout());
				dialog.add(new HelpPage(), new FitData());
				dialog.show();
			}
		});

		LabelToolItem userName = new LabelToolItem();
		userName.setLabel("Welcome, " + this.userName);

		Button logout = new Button("", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.exitButtonIcon()));
		logout.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				LoginService.Util.getInstance().logout(new AsyncCallback<Void>()
				{
					@Override
					public void onFailure(Throwable caught)
					{
						Login w = new Login();
						RootPanel.get().clear();
						RootPanel.get().add(w);
					}

					@Override
					public void onSuccess(Void result)
					{
						Login w = new Login();
						RootPanel.get().clear();
						RootPanel.get().add(w);
					}
				});
			}
		});

		toolbar.add(home);
		toolbar.add(new SeparatorToolItem());
		toolbar.add(userName);
		toolbar.add(new FillToolItem());
		toolbar.add(help);
		toolbar.add(new SeparatorToolItem());
		toolbar.add(logout);

		mainContentPanel.setTopComponent(toolbar);
	}

	private void buildCenterLayout()
	{
		layoutContainerCenter.setLayout(new FitLayout());

		mainContentPanel.add(layoutContainerCenter, new BorderLayoutData(LayoutRegion.CENTER));
		mainContentPanel.add(layoutContainerCenter);
		mainContentPanel.setHeight(com.google.gwt.user.client.Window.getClientHeight() + "px");

		layoutContainerCenter.add(new LandingPage());
	}

	private void buildSouthLayout()
	{
		BorderLayoutData bldSouth = new BorderLayoutData(LayoutRegion.SOUTH);
		bldSouth.setHidden(true);
		bldSouth.setCollapsible(true);
		mainContentPanel.add(layoutContainerSouth, bldSouth);
		layoutContainerSouth.setBorders(true);
	}

	private void buildEastLayout()
	{
		BorderLayoutData bldEast = new BorderLayoutData(LayoutRegion.EAST);
		bldEast.setHidden(true);
		bldEast.setFloatable(false);
		bldEast.setCollapsible(true);
		mainContentPanel.add(layoutContainerEast, bldEast);
		layoutContainerEast.setBorders(true);
	}

	private void buildNorthLayout()
	{
		BorderLayoutData bldNorth = new BorderLayoutData(LayoutRegion.NORTH, 60.0f);
		bldNorth.setHidden(true);
		bldNorth.setCollapsible(true);
		mainContentPanel.add(layoutContainerNorth, bldNorth);
		layoutContainerNorth.setBorders(true);
	}

	private void buildWestLayout()
	{
		layoutContainerWest.setLayout(new AccordionLayout());
		BorderLayoutData bldWest = new BorderLayoutData(LayoutRegion.WEST, 250.0f);
		bldWest.setCollapsible(true);
		bldWest.setSplit(true);
		layoutContainerWest.setHeight("300");
		layoutContainerWest.setBorders(true);
		mainContentPanel.add(layoutContainerWest, bldWest);

		buildSchoolScreeningPanel();

		buildCampScreeningPanel();

		buildClinicScreeningPanel();

		buildReportsPanel();

		buildAdministrationPanel();

		refreshIndexPanel(false);
	}

	private void buildAdministrationPanel()
	{
		final ContentPanel cpAdministration = new ContentPanel();
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
				MessageBox.info("Error", "Error Encountered while loading Admin Panel" + caught.getMessage(), DUMMYLISTENER);
				// System.out.println(caught.getMessage());
			}

			@Override
			public void onSuccess(List<ModelData> result)
			{
				final Map<String, List<ModelData>> mapGrpName2Model = new HashMap<String, List<ModelData>>();

				for (ModelData modelData : result)
				{
					String key = modelData.get("groupName").toString();
					if (mapGrpName2Model.containsKey(key))
					{
						mapGrpName2Model.get(key).add(modelData);
					} else
					{
						List<ModelData> lst = new ArrayList<ModelData>();
						lst.add(modelData);
						mapGrpName2Model.put(key, lst);
					}
				}
				for (String groupName : mapGrpName2Model.keySet())
				{
					ModelData rootNode = new BaseModelData();
					rootNode.set("name", groupName);
					rootNode.set("icon", "");
					tree.getStore().add(rootNode, false);

					tree.getStore().add(rootNode, mapGrpName2Model.get(groupName), false);
				}
				for (ModelData modelData : result)
				{
					tree.setExpanded(modelData, true);
				}
			}
		});
		cpAdministration.add(tree);
	}

	private void buildReportsPanel()
	{
		final ContentPanel cpReports = new ContentPanel();
		cpReports.setHeading("Reporting");
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
				}
				return null;
			}

		});
		tree.setDisplayProperty("name");

		tree.addListener(Events.OnClick, new Listener<BaseEvent>()
		{
			@Override
			public void handleEvent(BaseEvent be)
			{
				ModelData selectedItem = tree.getSelectionModel().getSelectedItem();
				boolean isLeaf = tree.isLeaf(selectedItem);
				if (!cpReports.isCollapsed() && isLeaf)
				{
					layoutContainerCenter.removeAll();
					layoutContainerCenter.setLayoutData(new FitData(15));
					String reportName = selectedItem.get("name");
					if (reportName.equalsIgnoreCase("School Health"))
					{
						layoutContainerCenter.add(new SchoolHealthProgramReport());
					} else if (reportName.equalsIgnoreCase("Medical Camp"))
					{
						layoutContainerCenter.add(new MedicalCampProgramReport());
					} else if (reportName.equalsIgnoreCase("Clinic"))
					{
						layoutContainerCenter.add(new ClinicScreeningReport());
					} else if (reportName.equalsIgnoreCase("Events"))
					{
						layoutContainerCenter.add(new EventsReport());
					} else if (reportName.equalsIgnoreCase("Overall"))
					{
						layoutContainerCenter.add(new OverallReport());
					}
					layoutContainerCenter.layout(true);
				}
			}
		});

		storeLoader.getListStore(MainPanelEnum.Reports.name(), new AsyncCallback<List<ModelData>>()
		{

			@Override
			public void onFailure(Throwable caught)
			{
				// System.out.println(caught.getMessage());
				MessageBox.info("Error", "Error Encountered while loading Reports Panel" + caught.getMessage(), DUMMYLISTENER);
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
					tree.setExpanded(modelData, true);
				}
			}
		});

		cpReports.add(tree);
	}

	private void buildClinicScreeningPanel()
	{
		cpClinicScreening = new ContentPanel();
		cpClinicScreening.setHeading("Clinic Screening");
		cpClinicScreening.setLayout(new FitLayout());
		layoutContainerWest.add(cpClinicScreening);

		ToolBar toolbar = new ToolBar();
		cpClinicScreening.setTopComponent(toolbar);

		// Button newScreening = new Button("New",
		// AbstractImagePrototype.create(YfsImageBundle.INSTANCE.addButtonIcon()));
		// toolbar.add(newScreening);
		// newScreening.addSelectionListener(new
		// SelectionListener<ButtonEvent>()
		// {
		// @Override
		// public void componentSelected(ButtonEvent ce)
		// {
		// layoutContainerCenter.mask("Loading...");
		// layoutContainerCenter.removeAll();
		// layoutContainerCenter.setLayoutData(new FitData(15));
		//
		// layoutContainerCenter.mask("Initializing ...");
		// final ClinicScreeningDetail widget = new ClinicScreeningDetail();
		// widget.initialize("New Screening", null);
		// layoutContainerCenter.add(widget);
		//
		// layoutContainerCenter.layout(true);
		// }
		// });

		treeClinicScreeningPanel.setIconProvider(new ModelIconProvider<ModelData>()
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
		treeClinicScreeningPanel.setDisplayProperty("name");
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
		filter.setWidth("100");
		filter.bind(clinicScreeningPanelStore);
		// toolbar.add(new SeparatorToolItem());
		toolbar.add(filter);

		treeClinicScreeningPanel.addListener(Events.OnClick, new Listener<BaseEvent>()
		{
			@Override
			public void handleEvent(BaseEvent be)
			{
				ModelData selectedItem = treeClinicScreeningPanel.getSelectionModel().getSelectedItem();
				boolean isLeaf = treeClinicScreeningPanel.isLeaf(selectedItem);
				if (!cpClinicScreening.isCollapsed() && isLeaf)
				{
					layoutContainerCenter.mask("Loading...");
					layoutContainerCenter.removeAll();

					layoutContainerCenter.setLayoutData(new FitData(15));

					final ClinicScreeningDetail widget = new ClinicScreeningDetail();
					layoutContainerCenter.add(widget);

					Object screeningDate = selectedItem.get("name");
					Object screeningId = selectedItem.get("id");

					if (screeningDate != null && screeningId != null)
					{
						String title = "Edit Screening " + screeningDate.toString();
						widget.initialize(title, screeningId.toString());
					} else
					{
						widget.initialize("Screening Detail", null);
					}
					layoutContainerCenter.layout(true);
				}
			}
		});

		cpClinicScreening.add(treeClinicScreeningPanel);
	}

	private void buildCampScreeningPanel()
	{
		cpCampScreening = new ContentPanel();
		cpCampScreening.setHeading("Medical Camp Screening");
		cpCampScreening.setLayout(new FitLayout());
		layoutContainerWest.add(cpCampScreening);

		ToolBar toolbar = new ToolBar();
		cpCampScreening.setTopComponent(toolbar);

		Button newScreening = new Button("New", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.addButtonIcon()));
		toolbar.add(newScreening);
		newScreening.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				layoutContainerCenter.mask("Loading...");
				layoutContainerCenter.removeAll();
				layoutContainerCenter.setLayoutData(new FitData(15));

				layoutContainerCenter.mask("Initializing ...");
				final CampScreeningDetail widget = new CampScreeningDetail();
				widget.initialize("New Screening", null);
				layoutContainerCenter.add(widget);

				layoutContainerCenter.layout(true);
			}
		});

		treeCampScreeningPanel.setIconProvider(new ModelIconProvider<ModelData>()
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
		treeCampScreeningPanel.setDisplayProperty("name");
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
		filter.setWidth("100");
		filter.bind(campScreeningPanelStore);
		toolbar.add(new SeparatorToolItem());
		toolbar.add(filter);

		treeCampScreeningPanel.addListener(Events.OnClick, new Listener<BaseEvent>()
		{
			@Override
			public void handleEvent(BaseEvent be)
			{
				ModelData selectedItem = treeCampScreeningPanel.getSelectionModel().getSelectedItem();
				boolean isLeaf = treeCampScreeningPanel.isLeaf(selectedItem);
				if (!cpCampScreening.isCollapsed() && isLeaf)
				{
					layoutContainerCenter.mask("Loading...");
					layoutContainerCenter.removeAll();

					layoutContainerCenter.setLayoutData(new FitData(15));

					final CampScreeningDetail widget = new CampScreeningDetail();
					layoutContainerCenter.add(widget);

					Object screeningDate = selectedItem.get("name");
					Object screeningId = selectedItem.get("id");

					if (screeningDate != null && screeningId != null)
					{
						String title = "Edit Screening " + screeningDate.toString();
						widget.initialize(title, screeningId.toString());
					} else
					{
						widget.initialize("Screening Detail", null);
					}
					layoutContainerCenter.layout(true);
				}
			}
		});

		cpCampScreening.add(treeCampScreeningPanel);
	}

	private void buildSchoolScreeningPanel()
	{
		cpSchoolScreening = new ContentPanel();
		cpSchoolScreening.setHeading("School Screening");
		cpSchoolScreening.setLayout(new FitLayout());
		layoutContainerWest.add(cpSchoolScreening);

		ToolBar toolbar = new ToolBar();
		cpSchoolScreening.setTopComponent(toolbar);

		Button newScreening = new Button("New", AbstractImagePrototype.create(YfsImageBundle.INSTANCE.addButtonIcon()));
		toolbar.add(newScreening);
		newScreening.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				layoutContainerCenter.removeAll();
				layoutContainerCenter.setLayoutData(new FitData(15));

				layoutContainerCenter.mask("Initializing ...");
				final SchoolScreeningDetail widget = new SchoolScreeningDetail();
				widget.initialize("New Screening", null);
				layoutContainerCenter.add(widget);

				layoutContainerCenter.layout(true);
			}
		});

		treeSchoolScreeningPanel.setIconProvider(new ModelIconProvider<ModelData>()
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
		treeSchoolScreeningPanel.setDisplayProperty("name");
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
		filter.setWidth("100");
		filter.bind(schoolScreeningPanelStore);
		toolbar.add(new SeparatorToolItem());
		toolbar.add(filter);

		treeSchoolScreeningPanel.addListener(Events.OnClick, new Listener<BaseEvent>()
		{
			@Override
			public void handleEvent(BaseEvent be)
			{
				ModelData selectedItem = treeSchoolScreeningPanel.getSelectionModel().getSelectedItem();
				boolean isLeaf = treeSchoolScreeningPanel.isLeaf(selectedItem);
				if (!cpSchoolScreening.isCollapsed() && isLeaf)
				{
					layoutContainerCenter.mask("Loading...");
					layoutContainerCenter.removeAll();

					layoutContainerCenter.setLayoutData(new FitData(15));

					final SchoolScreeningDetail widget = new SchoolScreeningDetail();
					layoutContainerCenter.add(widget);

					Object screeningDate = selectedItem.get("name");
					Object screeningId = selectedItem.get("id");

					if (screeningDate != null && screeningId != null)
					{
						String title = "Edit Screening " + screeningDate.toString();
						widget.initialize(title, screeningId.toString());
					} else
					{
						widget.initialize("Screening Detail", null);
					}
					layoutContainerCenter.layout(true);
				}
			}
		});

		cpSchoolScreening.add(treeSchoolScreeningPanel);
	}

	private static void refreshIndexPanel(boolean forceRemoveAll)
	{
		if (forceRemoveAll)
		{
			treeSchoolScreeningPanel.getStore().removeAll();
			treeCampScreeningPanel.getStore().removeAll();
			treeClinicScreeningPanel.getStore().removeAll();
		}

		storeLoader.getListStore(MainPanelEnum.SchoolScreeningLocations.name(), new AsyncCallback<List<ModelData>>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				// System.out.println(caught.getMessage());
				MessageBox.info("Error", "Error Encountered while loading School Screening Panel" + caught.getMessage(), DUMMYLISTENER);
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(List<ModelData> result)
			{

				for (ModelData modelData : result)
				{
					treeSchoolScreeningPanel.getStore().add(modelData, true); // chapter-name
					List<ModelData> children = (List<ModelData>) modelData.get("children"); // screening
					if (children != null)
					{
						treeSchoolScreeningPanel.getStore().add(modelData, children, true);
					}
				}
			}
		});

		storeLoader.getListStore(MainPanelEnum.ClinicScreeningLocations.name(), new AsyncCallback<List<ModelData>>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				MessageBox.info("Error", "Error Encountered while loading Clinic Panel" + caught.getMessage(), DUMMYLISTENER);
				// System.out.println(caught.getMessage());
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(List<ModelData> result)
			{

				for (ModelData modelData : result)
				{
					treeClinicScreeningPanel.getStore().add(modelData, true); // chapter-name
					List<ModelData> children = (List<ModelData>) modelData.get("children"); // screening
					if (children != null)
					{
						treeClinicScreeningPanel.getStore().add(modelData, children, true);
					}
					treeClinicScreeningPanel.setExpanded(modelData, true);
				}
			}
		});

		storeLoader.getListStore(MainPanelEnum.CampScreeningLocations.name(), new AsyncCallback<List<ModelData>>()
		{
			@Override
			public void onFailure(Throwable caught)
			{
				MessageBox.info("Error", "Error Encountered while loading Camp Panel" + caught.getMessage(), DUMMYLISTENER);
				// System.out.println(caught.getMessage());
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(List<ModelData> result)
			{

				for (ModelData modelData : result)
				{
					treeCampScreeningPanel.getStore().add(modelData, true); // chapter-name
					List<ModelData> children = (List<ModelData>) modelData.get("children"); // screening
					if (children != null)
					{
						treeCampScreeningPanel.getStore().add(modelData, children, true);
					}
				}
			}
		});
	}
}
