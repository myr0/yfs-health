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
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
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
import com.varun.yfs.dto.ChapterNameDTO;
import com.varun.yfs.dto.ProjectDTO;
import com.varun.yfs.dto.UserDTO;

public class UserAdministration extends LayoutContainer
{
	private final StoreLoaderAsync storeLoader = GWT.create(StoreLoader.class);
	private EditorGrid<UserDTO> editorGrid;
	private final ContentPanel gridPanel = new ContentPanel();

	private final ContentPanel userDetailsViewHolder = new ContentPanel();
	private final TextField<String> txtfldUsrName = new TextField<String>();
	private final TextField<String> txtfldPassword = new TextField<String>();
	private SimpleComboBox<String> userRole = new SimpleComboBox<String>();
	private final CheckBoxListView<ChapterNameDTO> lstfldChapterNames = new CheckBoxListView<ChapterNameDTO>();
	private final CheckBoxListView<ProjectDTO> lstfldProjects = new CheckBoxListView<ProjectDTO>();

	private String curAdminEntity = "Default";
	private ModelData currentModelData = new BaseModelData();
	private boolean isAdd;

	public UserAdministration()
	{
		setHeight("700");
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
		mainPanel.setHeight("700");

		add(mainPanel, new FitData(5));

		gridPanel.setLayout(new FitLayout());
		gridPanel.setHeading(curAdminEntity);
		gridPanel.setSize("250px", "550px");

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		ColumnConfig clmncnfgNewColumn = new ColumnConfig("name", "Name", 150);
		configs.add(clmncnfgNewColumn);

		ListStore<ModelData> editorGridStore = new ListStore<ModelData>();

		editorGrid = new EditorGrid<UserDTO>(editorGridStore, new ColumnModel(configs));
		editorGrid.setHideHeaders(true);
		editorGrid.setSelectionModel(new GridSelectionModel<UserDTO>());
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
					lstfldChapterNames.getStore().removeAll();
					lstfldProjects.getStore().removeAll();

					ModelData modelData = selection.get(0);
					txtfldUsrName.setValue(modelData.get("name").toString());
					txtfldPassword.setValue(modelData.get("password").toString());
					lstfldChapterNames.getStore().add((List<ChapterNameDTO>) currentModelData.get("lstChapterNames"));
					lstfldProjects.getStore().add((List<ProjectDTO>) currentModelData.get("lstProjects"));
					userRole.findModel(modelData.get("role").toString());

					updateSelections((List<ModelData>) modelData.get("chapterNames"), lstfldChapterNames);
					updateSelections((List<ModelData>) modelData.get("projects"), lstfldProjects);

					lstfldChapterNames.refresh();
					lstfldProjects.refresh();

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
				lstfldChapterNames.getStore().getModels().clear();
				lstfldProjects.getStore().getModels().clear();

				lstfldChapterNames.refresh();
				lstfldProjects.refresh();

				userDetailsViewHolder.setVisible(true);
				userDetailsViewHolder.focus();

				isAdd = true;
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
				UserDTO selectedItem = editorGrid.getSelectionModel().getSelectedItem();
				if (selectedItem != null)
				{
					selectedItem.set("deleted", "Y");
					List<UserDTO> lstModels = editorGrid.getStore().getModels();
					editorGrid.getStore().remove(selectedItem);
					editorGrid.mask("Removing Entry...");
					savePage(lstModels);
				}
			}
		});
		toolBar.add(remove);

		userDetailsViewHolder.setHeading("Location Details");
		userDetailsViewHolder.setVisible(false);

		FormPanel frmpanelUserBasic = new FormPanel();
		frmpanelUserBasic.setHeaderVisible(false);
		frmpanelUserBasic.setCollapsible(false);
		frmpanelUserBasic.setBorders(false);
		frmpanelUserBasic.setFrame(false);

		txtfldUsrName.setMaxLength(255);
		txtfldUsrName.setName("userName");
		txtfldUsrName.setFieldLabel("User Name");
		frmpanelUserBasic.add(txtfldUsrName, new FormData("80%"));

		txtfldPassword.setPassword(true);
		txtfldPassword.setName("userPassword");
		txtfldPassword.setMaxLength(255);
		txtfldPassword.setFieldLabel("Password");
		frmpanelUserBasic.add(txtfldPassword, new FormData("80%"));

		frmpanelUserBasic.add(userRole, new FormData("80%"));
		userRole.setFieldLabel("Role");
		userRole.add("Administrator");
		userRole.add("Administrator - Chapter");
		userRole.add("Area Co-Ordinator");

		userDetailsViewHolder.add(frmpanelUserBasic, new FitData(5));
		lstfldChapterNames.setBorders(true);

		lstfldChapterNames.setStore(new ListStore<ChapterNameDTO>());
		lstfldChapterNames.setDisplayProperty("chapterName");
		lstfldChapterNames.setHeight("150px");
		lstfldChapterNames.setStyleAttribute("overflow-y", "scroll");
		final ContentPanel cPanelChapterNames = new ContentPanel();
		cPanelChapterNames.setHeading("Select Chapters");
		cPanelChapterNames.add(lstfldChapterNames);

		userDetailsViewHolder.add(cPanelChapterNames, new FitData(5));

		lstfldProjects.setStore(new ListStore<ProjectDTO>());
		lstfldProjects.setHeight("150px");
		lstfldProjects.setDisplayProperty("projectName");
		lstfldProjects.setStyleAttribute("overflow-y", "scroll");
		final ContentPanel cPanelProjects = new ContentPanel();
		cPanelProjects.setHeading("Select Projects");
		cPanelProjects.add(lstfldProjects);
		userDetailsViewHolder.add(cPanelProjects, new FitData(5));

		TableData td_lstViewHolder = new TableData();
		td_lstViewHolder.setHorizontalAlign(HorizontalAlignment.LEFT);
		td_lstViewHolder.setRowspan(2);
		td_lstViewHolder.setPadding(5);
		td_lstViewHolder.setMargin(5);
		mainPanel.add(userDetailsViewHolder, td_lstViewHolder);
		userDetailsViewHolder.setSize("300px", "550px");

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
				List<UserDTO> models = editorGrid.getStore().getModels();

				if (isAdd)
				{
					UserDTO modelData = new UserDTO();
					models.add(modelData);
					modelData.setName(txtfldUsrName.getValue());
					modelData.setPassword(txtfldPassword.getValue());
					modelData.setChapterNames(lstfldChapterNames.getChecked());
					modelData.setProjects(lstfldProjects.getChecked());
					modelData.setRole(userRole.getSimpleValue());
				} else
				{
					UserDTO modelData = editorGrid.getSelectionModel().getSelectedItem();
					if (modelData != null)
					{
						modelData.setName(txtfldUsrName.getValue());
						modelData.setPassword(txtfldPassword.getValue());
						modelData.setChapterNames(lstfldChapterNames.getChecked());
						modelData.setProjects(lstfldProjects.getChecked());
						modelData.setRole(userRole.getSimpleValue());
					}
				}
				savePage(models);
			}
		}));
	}

	public void savePage(final List<UserDTO> lstModels)
	{
		ModelData modelData = new BaseModelData();
		modelData.set("users", lstModels);
		storeLoader.saveModel(curAdminEntity, modelData, new AsyncCallback<RpcStatusEnum>()
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
				editorGrid.getStore().add((List<UserDTO>) currentModelData.get("users"));
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
