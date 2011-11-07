package com.varun.yfs.client.admin.users;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.varun.yfs.client.admin.rpc.StoreLoader;
import com.varun.yfs.client.admin.rpc.StoreLoaderAsync;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.images.YfsImageBundle;
import com.varun.yfs.dto.LocalityDTO;
import com.varun.yfs.dto.TownDTO;
import com.varun.yfs.dto.VillageDTO;

public class UserAdministration extends LayoutContainer
{
	private final StoreLoaderAsync storeLoader = GWT.create(StoreLoader.class);
	private final List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
	private final ListStore<ModelData> editorGridStore = new ListStore<ModelData>();
	private final EditorGrid<ModelData> editorGrid = new EditorGrid<ModelData>(editorGridStore, new ColumnModel(configs));
	private final ContentPanel gridPanel = new ContentPanel();

	private final ContentPanel userDetailsViewHolder = new ContentPanel();
	private final TextField<String> txtfldUsrName = new TextField<String>();
	private final TextField<String> txtfldPassword = new TextField<String>();
	private final CheckBoxListView<LocalityDTO> lstfldLocality = new CheckBoxListView<LocalityDTO>();
	private final CheckBoxListView<VillageDTO> lstfldVillages = new CheckBoxListView<VillageDTO>();
	private final CheckBoxListView<TownDTO> lstfldTowns = new CheckBoxListView<TownDTO>();

	private String curAdminEntity = "Default";
	private ModelData currentModelData = new BaseModelData();

	public UserAdministration()
	{
	}

	final Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>()
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

		ContentPanel mainPanel = new ContentPanel();
		mainPanel.setHeaderVisible(true);
		mainPanel.setHeading("Administration");
		mainPanel.setLayout(new FitLayout());

		TableLayout tl_lp = new TableLayout(2);
		tl_lp.setCellPadding(5);
		mainPanel.setLayout(tl_lp);

		TableData td_gridPanel = new TableData();
		td_gridPanel.setRowspan(2);
		mainPanel.add(gridPanel, td_gridPanel);

		mainPanel.setScrollMode(Scroll.AUTOY);
		// mainPanel.setSize("600", "700");

		add(mainPanel, new FitData(5));

		gridPanel.setLayout(new FitLayout());
		gridPanel.setHeading(curAdminEntity);
		// gridPanel.setSize("350px", "350px");
		gridPanel.setHeight("350px");

		ColumnConfig clmncnfgNewColumn = new ColumnConfig("name", "Name", 150);
		configs.add(clmncnfgNewColumn);

		editorGrid.reconfigure(editorGridStore, new ColumnModel(configs));
		editorGrid.setBorders(true);
		editorGrid.setSelectionModel(new GridSelectionModel<ModelData>());
		editorGrid.setLoadMask(true);
		editorGrid.setAutoExpandColumn("name");
		editorGrid.mask("Loading...");
		editorGrid.setAutoWidth(true);
		editorGrid.setClicksToEdit(EditorGrid.ClicksToEdit.ONE);
		gridPanel.add(editorGrid);

		editorGrid.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionChangedEvent<ModelData>>()
		{
			@SuppressWarnings("unchecked")
			public void handleEvent(SelectionChangedEvent<ModelData> be)
			{
				List<ModelData> selection = be.getSelection();
				if (selection.size() > 0)
				{
					txtfldUsrName.clear();
					txtfldPassword.clear();
					lstfldLocality.getStore().removeAll();
					lstfldVillages.getStore().removeAll();
					lstfldTowns.getStore().removeAll();

					ModelData modelData = selection.get(0);
					txtfldUsrName.setValue(modelData.get("name").toString());
					txtfldPassword.setValue(modelData.get("password").toString());

					lstfldLocality.getStore().add((List<LocalityDTO>) currentModelData.get("lstLocalities"));
					lstfldVillages.getStore().add((List<VillageDTO>) currentModelData.get("lstVillage"));
					lstfldTowns.getStore().add((List<TownDTO>) currentModelData.get("lstTown"));

					updateSelections((List<ModelData>) modelData.get("localities"), lstfldLocality);
					updateSelections((List<ModelData>) modelData.get("villages"), lstfldVillages);
					updateSelections((List<ModelData>) modelData.get("towns"), lstfldTowns);

					// lstfldLocality.getStore().add((List<LocalityDTO>)
					// modelData.get("localities"));
					// lstfldVillages.getStore().add((List<VillageDTO>)
					// modelData.get("villages"));
					// lstfldTowns.getStore().add((List<TownDTO>)
					// modelData.get("towns"));

					lstfldLocality.refresh();
					lstfldVillages.refresh();
					lstfldTowns.refresh();

					userDetailsViewHolder.setVisible(true);
					userDetailsViewHolder.focus();
				}
			}

			@SuppressWarnings({ "rawtypes", "unchecked" })
			private <E> void updateSelections(List<? extends ModelData> modelData, CheckBoxListView view)
			{
				for (ModelData modelData1 : modelData)
				{
					int idx = view.getStore().getModels().indexOf(modelData1);
					if (idx >= 0)
					{
						view.setChecked(view.getStore().getAt(idx), true);
					}
				}
			}
		});

		ToolBar toolBar = new ToolBar();
		mainPanel.setTopComponent(toolBar);

		Button add = new Button("Add");
		add.setIcon(AbstractImagePrototype.create(YfsImageBundle.INSTANCE.addButtonIcon()));
		add.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{

				txtfldUsrName.clear();
				txtfldPassword.clear();
				lstfldLocality.getStore().getModels().clear();
				lstfldVillages.getStore().getModels().clear();
				lstfldTowns.getStore().getModels().clear();

				lstfldLocality.refresh();
				lstfldVillages.refresh();
				lstfldTowns.refresh();

				userDetailsViewHolder.setVisible(true);
				userDetailsViewHolder.focus();

			}
		});
		toolBar.add(add);

		Button remove = new Button("Remove");
		remove.setIcon(AbstractImagePrototype.create(YfsImageBundle.INSTANCE.deleteButtonIcon()));
		remove.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				editorGrid.stopEditing();
				ModelData selectedItem = editorGrid.getSelectionModel().getSelectedItem();
				if (selectedItem != null)
				{
					selectedItem.set("deleted", "Y");
					List<ModelData> lstModels = editorGrid.getStore().getModels();
					editorGrid.getStore().remove(selectedItem);
					editorGrid.mask("Removing Entry...");
					savePage(lstModels);
				}
			}
		});
		toolBar.add(remove);

		userDetailsViewHolder.setHeading("Location Details");
		userDetailsViewHolder.setVisible(false);

		FormPanel frmpanel = new FormPanel();
		frmpanel.setHeaderVisible(false);
		frmpanel.setCollapsible(false);
		frmpanel.setBorders(false);
		frmpanel.setFrame(false);

		txtfldUsrName.setMaxLength(255);
		txtfldUsrName.setName("userName");
		txtfldUsrName.setFieldLabel("User Name");
		frmpanel.add(txtfldUsrName, new FormData("80%"));

		txtfldPassword.setPassword(true);
		txtfldPassword.setName("userPassword");
		txtfldPassword.setMaxLength(255);
		txtfldPassword.setFieldLabel("Password");
		frmpanel.add(txtfldPassword, new FormData("80%"));
		frmpanel.setSize("250px", "70px");

		userDetailsViewHolder.add(frmpanel);

		lstfldLocality.setStore(new ListStore<LocalityDTO>());
		lstfldLocality.setDisplayProperty("localityName");
		lstfldLocality.setSize("350px", "100px");
		final ContentPanel cPanelLocality = new ContentPanel();
		cPanelLocality.setScrollMode(Scroll.AUTOY);
		cPanelLocality.setSize("250px", "100px");
		cPanelLocality.setHeading("Select Locality");
		cPanelLocality.add(lstfldLocality);

		userDetailsViewHolder.add(cPanelLocality);

		lstfldVillages.setStore(new ListStore<VillageDTO>());
		lstfldVillages.setSize("350px", "100px");
		lstfldVillages.setDisplayProperty("villageName");
		final ContentPanel cPanelVillage = new ContentPanel();
		cPanelVillage.setScrollMode(Scroll.AUTOY);
		cPanelVillage.setHeading("Select Villages");
		cPanelVillage.setSize("250px", "100px");
		cPanelVillage.add(lstfldVillages);
		userDetailsViewHolder.add(cPanelVillage);

		lstfldTowns.setStore(new ListStore<TownDTO>());
		lstfldTowns.setDisplayProperty("townName");
		lstfldTowns.setSize("350px", "100px");
		final ContentPanel cPanelTown = new ContentPanel();
		cPanelTown.setScrollMode(Scroll.AUTOY);
		cPanelTown.setSize("250px", "100px");
		cPanelTown.setHeading("Select Town");
		cPanelTown.add(lstfldTowns);
		userDetailsViewHolder.add(cPanelTown);

		TableData td_lstViewHolder = new TableData();
		td_lstViewHolder.setHorizontalAlign(HorizontalAlignment.LEFT);
		td_lstViewHolder.setRowspan(2);
		td_lstViewHolder.setPadding(5);
		td_lstViewHolder.setMargin(5);
		mainPanel.add(userDetailsViewHolder, td_lstViewHolder);
		userDetailsViewHolder.setHeight("550px");

		userDetailsViewHolder.setButtonAlign(HorizontalAlignment.CENTER);
		userDetailsViewHolder.addButton(new Button("Reset", new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				reinitPage(curAdminEntity);
				userDetailsViewHolder.setVisible(false);
			}
		}));

		userDetailsViewHolder.addButton(new Button("Save", new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				userDetailsViewHolder.setVisible(false);

				ModelData modelData = new BaseModelData();

				List<ModelData> models = editorGrid.getStore().getModels();
				models.add(modelData);

				modelData.set("name", txtfldUsrName.getValue());
				modelData.set("password", txtfldPassword.getValue());
				modelData.set("localities", lstfldLocality.getChecked());
				modelData.set("villages", lstfldVillages.getChecked());
				modelData.set("towns", lstfldTowns.getChecked());

				savePage(models);
			}
		}));

	}

	public void savePage(final List<ModelData> lstModels)
	{
		ModelData modelData = new BaseModelData();
		modelData.set("users", lstModels);
		storeLoader.saveModel(curAdminEntity, lstModels, new AsyncCallback<RpcStatusEnum>()
		{
			@Override
			public void onSuccess(RpcStatusEnum result)
			{
				reinitPage(curAdminEntity);
				if (result.compareTo(RpcStatusEnum.FAILURE) == 0)
				{
					MessageBox.alert("Alert", "Error encountered while saving", l);
				}
			}

			@Override
			public void onFailure(Throwable caught)
			{
				editorGrid.unmask();
				MessageBox.alert("Alert", "Error encountered while saving", l);
			}
		});
	}

	public void reinitPage(String entityName)
	{
		this.curAdminEntity = entityName;
		gridPanel.setHeading(entityName);
		storeLoader.getModel(entityName, new AsyncCallback<ModelData>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(ModelData result)
			{
				currentModelData = result;
				editorGrid.getStore().removeAll();
				editorGrid.getStore().add((List<ModelData>) currentModelData.get("users"));
				editorGrid.getStore().commitChanges();
				editorGrid.unmask();
				editorGrid.setAutoWidth(true);
			}

			@Override
			public void onFailure(Throwable caught)
			{
				editorGrid.unmask();
				MessageBox.alert("Alert", "Error encountered while loading", l);
			}
		});

	}

}
