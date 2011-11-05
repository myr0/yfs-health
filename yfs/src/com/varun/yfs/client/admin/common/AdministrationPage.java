package com.varun.yfs.client.admin.common;

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
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
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
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.varun.yfs.client.admin.rpc.StoreLoader;
import com.varun.yfs.client.admin.rpc.StoreLoaderAsync;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.icons.YfsImageBundle;

public class AdministrationPage extends LayoutContainer
{
	private StoreLoaderAsync storeLoader = GWT.create(StoreLoader.class);
	private String curAdminEntity = "Default";
	private List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
	private final ListStore<ModelData> editorGridStore = new ListStore<ModelData>();
	private EditorGrid<ModelData> editorGrid = new EditorGrid<ModelData>(editorGridStore, new ColumnModel(configs));
	private ContentPanel gridPanel = new ContentPanel();

	public AdministrationPage()
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

		ContentPanel lp = new ContentPanel();
		lp.setHeaderVisible(true);
		lp.setHeading("Administration");

		gridPanel.setLayout(new FitLayout());
		gridPanel.setHeading(curAdminEntity);

		ColumnConfig clmncnfgNewColumn = new ColumnConfig("name", "Name", 150);
		configs.add(clmncnfgNewColumn);
		for (ColumnConfig cc : configs)
		{
			cc.setEditor(new CellEditor(new TextField<String>()));
		}

		editorGrid = new EditorGrid<ModelData>(editorGridStore, new ColumnModel(configs));
		editorGrid.setBorders(true);
		editorGrid.setSelectionModel(new GridSelectionModel<ModelData>());
		editorGrid.setLoadMask(true);
		editorGrid.mask("Loading...");
		editorGrid.setAutoWidth(true);
		editorGrid.setClicksToEdit(EditorGrid.ClicksToEdit.ONE);
		editorGrid.setAutoExpandColumn("name");
		gridPanel.add(editorGrid);

		ToolBar toolBar = new ToolBar();
		Button add = new Button("Add");
		add.setIcon(AbstractImagePrototype.create(YfsImageBundle.INSTANCE.addButtonIcon()));
		add.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				ModelData plant = new BaseModelData();
				plant.set("name", "Type here...");
				editorGrid.stopEditing();
				editorGridStore.insert(plant, 0);
				editorGrid.startEditing(editorGridStore.indexOf(plant), 0);
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
				savePage(editorGrid.getStore().getModels());
			}
		}));

		gridPanel.setHeight("500px");
		gridPanel.setWidth("500px");
		lp.add(gridPanel, new FitData(5));

		add(lp);

	}

	public void savePage(final List<ModelData> lstModels)
	{
		storeLoader.saveListStore(curAdminEntity, lstModels, new AsyncCallback<RpcStatusEnum>()
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
		storeLoader.getListStore(entityName, new AsyncCallback<List<ModelData>>()
		{
			@Override
			public void onSuccess(List<ModelData> result)
			{
				ListStore<ModelData> store = editorGrid.getStore();
				store.removeAll();
				store.add(result);
				store.commitChanges();
				editorGrid.unmask();
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
