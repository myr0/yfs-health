package com.varun.yfs.client.admin.location;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.varun.yfs.client.admin.rpc.StoreLoader;
import com.varun.yfs.client.admin.rpc.StoreLoaderAsync;
import com.varun.yfs.client.common.RpcStatusEnum;

public class LocationAdministration extends LayoutContainer
{

	private StoreLoaderAsync storeLoader = GWT.create(StoreLoader.class);
	private String curAdminEntity = "Default";
	private List<ColumnConfig> configs;
	private ListStore<ModelData> editorGridStore;
	private EditorGrid<ModelData> editorGrid;
	private ContentPanel gridPanel = new ContentPanel();

	private List<ModelData> comboModels;
	private List<String> lstConfigsId;
	private List<String> lstconfigCols;
	private List<String> lstconfigType;

	public LocationAdministration()
	{
	}

	private Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>()
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

		ContentPanel lp = new ContentPanel();
		lp.setHeaderVisible(true);
		lp.setHeading("Administration");

		gridPanel.setLayout(new FitLayout());
		gridPanel.setHeading(curAdminEntity);

		editorGridStore = new ListStore<ModelData>();
		configs = new ArrayList<ColumnConfig>();
		editorGrid = new EditorGrid<ModelData>(editorGridStore, new ColumnModel(configs));
		editorGrid.setBorders(true);
		editorGrid.setSelectionModel(new GridSelectionModel<ModelData>());
		editorGrid.setLoadMask(true);
		editorGrid.mask("Loading...");
		editorGrid.setClicksToEdit(EditorGrid.ClicksToEdit.TWO);
		gridPanel.add(editorGrid);

		ToolBar toolBar = new ToolBar();
		Button add = new Button("Add");
		add.setIcon(IconHelper.createPath(GWT.getModuleBaseURL() + "images/add.png", 16, 16));
		add.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				ModelData plant = new BaseModelData();

				ColumnModel columnModel = editorGrid.getColumnModel();

				plant.set(columnModel.getColumn(0).getId(), "Type here...");
				if (columnModel.getColumnCount() > 1 && comboModels.size() > 0)
					plant.set(columnModel.getColumn(1).getId(), comboModels.get(0));

				editorGrid.stopEditing();
				editorGrid.getStore().add(plant);
				editorGrid.startEditing(editorGrid.getStore().getCount() - 1, 0);
			}
		});
		toolBar.add(add);

		Button remove = new Button("Remove");
		remove.setIcon(IconHelper.createPath(GWT.getModuleBaseURL() + "images/delete.png", 16, 16));
		remove.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				editorGrid.stopEditing();
				ModelData selectedItem = editorGrid.getSelectionModel().getSelectedItem();
				if (selectedItem != null)
				{
					List<ModelData> lstModels = editorGrid.getStore().getModels();

					selectedItem.set("deleted", "Y");

					editorGrid.getStore().remove(selectedItem);
					editorGrid.mask("Removing Entry...");

					ModelData modData = new BaseModelData();
					modData.set("data", lstModels);
					savePage(modData);
				}
			}
		});
		toolBar.add(remove);

		gridPanel.setTopComponent(toolBar);

		gridPanel.setButtonAlign(HorizontalAlignment.CENTER);
		gridPanel.addButton(new Button("Reset", new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				editorGrid.mask("Reloading...");
				reinitPage(curAdminEntity);
			}
		}));

		gridPanel.addButton(new Button("Save", new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				editorGrid.mask("Saving...");
				ModelData modData = new BaseModelData();
				modData.set("data", editorGrid.getStore().getModels());
				savePage(modData);
			}
		}));

		gridPanel.setHeight("500px");
		gridPanel.setWidth("350px");
		lp.add(gridPanel, new FitData(5));

		add(lp);

	}

	public void reinitPage(String entityName)
	{
		curAdminEntity = entityName;
		gridPanel.setHeading(entityName);
		storeLoader.getModel(entityName, new AsyncCallback<ModelData>()
		{
			@SuppressWarnings("unchecked")
			@Override
			public void onSuccess(ModelData result)
			{
				editorGrid.getStore().removeAll();

				lstConfigsId = result.get("configIds");
				lstconfigCols = result.get("configCols");
				lstconfigType = result.get("configType");

				List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
				for (int i = 0; i < lstConfigsId.size(); i++)
				{
					ColumnConfig clmncnfg = new ColumnConfig(lstConfigsId.get(i), lstconfigCols.get(i), 150);
					configs.add(clmncnfg);
					if (lstconfigType.get(i).equalsIgnoreCase("Text"))
					{
						clmncnfg.setEditor(new CellEditor(new TextField<String>()));
						editorGrid.setAutoExpandColumn(lstConfigsId.get(i));
					} else if (lstconfigType.get(i).equalsIgnoreCase("combo"))
					{
						comboModels = (List<ModelData>) result.get("parentStore" + lstconfigCols.get(i));
						ComboBox<ModelData> field = new ComboBox<ModelData>();
						field.setStore(new ListStore<ModelData>());
						field.setDisplayField(lstConfigsId.get(i));
						field.getStore().add(comboModels);
						field.setTriggerAction(TriggerAction.ALL);
						clmncnfg.setEditor(new CellEditor(field));
						editorGrid.setAutoExpandColumn(lstConfigsId.get(i));
					}
				}

				ListStore<ModelData> lstStore = new ListStore<ModelData>();
				List<ModelData> models = (List<ModelData>) result.get("data");
				lstStore.add(models);

				editorGrid.reconfigure(lstStore, new ColumnModel(configs));
				editorGrid.unmask();
			}

			@Override
			public void onFailure(Throwable caught)
			{
				editorGrid.unmask();
				MessageBox.alert("Alert", caught.getMessage(), l);
			}
		});

	}

	public void savePage(final ModelData model)
	{
		storeLoader.saveModel(curAdminEntity, model, new AsyncCallback<RpcStatusEnum>()
		{
			@Override
			public void onSuccess(RpcStatusEnum result)
			{
				if (result.compareTo(RpcStatusEnum.FAILURE) == 0)
				{
					MessageBox.alert("Alert", "Failed to save the Data", l);
				}

				reinitPage(curAdminEntity);
			}

			@Override
			public void onFailure(Throwable caught)
			{
				editorGrid.unmask();
				MessageBox.alert("Alert", caught.getMessage(), l);
			}
		});
	}
}
