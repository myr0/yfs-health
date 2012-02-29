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
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
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
import com.varun.yfs.client.images.YfsImageBundle;
import com.varun.yfs.dto.PermissionsDTO;
import com.varun.yfs.dto.YesNoDTO;

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
	private PermissionsDTO permissions;

	public LocationAdministration()
	{
	}

	private Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>()
	{
		@Override
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
		editorGrid.setAutoWidth(true);
		editorGrid.setClicksToEdit(EditorGrid.ClicksToEdit.ONE);
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

				ColumnModel columnModel = editorGrid.getColumnModel();

				plant.set(columnModel.getColumn(0).getId(), "Type here...");
				// if (columnModel.getColumnCount() > 1 && comboModels.size() >
				// 0)
				// {
				// ColumnConfig column = columnModel.getColumn(1);
				// // plant.set(column.getId(), comboModels.get(0));
				// }

				editorGrid.stopEditing();
				editorGrid.getStore().add(plant);
				editorGrid.startEditing(editorGrid.getStore().getCount() - 1, 0);
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
		gridPanel.setWidth("500px");
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

				permissions = result.get("permissions");
				if(permissions.getWrite().equalsIgnoreCase(YesNoDTO.YES.getName()))
				{
				}
				else
				{
				}
				
				if(permissions.getDelete().equalsIgnoreCase(YesNoDTO.YES.getName()))
				{
				}
				else
				{
				}


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
						List<String> lstValues = new ArrayList<String>();
						for (ModelData modelData : comboModels)
						{
							lstValues.add(modelData.toString());
						}
						final SimpleComboBox<String> field = new SimpleComboBox<String>();
						field.setTriggerAction(TriggerAction.ALL);
						CellEditor editor = new CellEditor(field)
						{
							@Override
							public Object preProcessValue(Object value)
							{
								if (value == null)
								{
									return value;
								}
								return field.findModel(value.toString());
							}

							@Override
							public Object postProcessValue(Object value)
							{
								if (value == null)
								{
									return value;
								}
								return ((ModelData) value).get("value");
							}
						};
						field.add(lstValues);
						clmncnfg.setEditor(editor);
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
					editorGrid.unmask();
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
