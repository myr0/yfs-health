package com.varun.yfs.client.screening.imports;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.varun.yfs.client.index.IndexPage;

public class ImportDetail extends LayoutContainer
{
	private FlowPanel panelImages = new FlowPanel();

	public ImportDetail()
	{
		setSize("600", "375");
	}

	@Override
	protected void onRender(Element parent, int index)
	{
		super.onRender(parent, index);

		setLayout(new FitLayout());

		ContentPanel mainContainerPanel = new ContentPanel();
		mainContainerPanel.setHeading("Import Screening Data");
		add(mainContainerPanel);
		mainContainerPanel.setSize("600px", "315px");

		LayoutContainer lcUploadComponent = buildUploadComponent();
		LayoutContainer lcTemplateComponent = buildTemplateComponent();

		mainContainerPanel.add(lcUploadComponent, new FitData(5));
		mainContainerPanel.add(lcTemplateComponent, new FitData(5));
		
		mainContainerPanel.setButtonAlign(HorizontalAlignment.CENTER);
		mainContainerPanel.addButton(new Button("Process", new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
			}
		}));
		
		add(mainContainerPanel);
	}

	private LayoutContainer buildTemplateComponent()
	{
		ContentPanel mainTemplateSettingsPanel = new ContentPanel();
		mainTemplateSettingsPanel.setHeading("Template Settings");
		mainTemplateSettingsPanel.setLayout(new TableLayout(2));

		FormPanel templateSettings1 = new FormPanel();
		templateSettings1.setLayout(new FormLayout());
		templateSettings1.setHeaderVisible(false);
		templateSettings1.setHeading("New FormPanel");
		templateSettings1.setSize("200", "230px");

		NumberField firstDataRow = new NumberField();
		firstDataRow.setMaxLength(2);
		templateSettings1.add(firstDataRow, new FormData("100%"));
		firstDataRow.setFieldLabel("First Data Row");

		NumberField nameColumn = new NumberField();
		nameColumn.setMaxLength(2);
		templateSettings1.add(nameColumn, new FormData("100%"));
		nameColumn.setFieldLabel("Name Column");

		NumberField ageColumn = new NumberField();
		ageColumn.setMaxLength(2);
		ageColumn.setFieldLabel("Age Column");
		templateSettings1.add(ageColumn, new FormData("50%"));

		NumberField sexColumn = new NumberField();
		sexColumn.setFieldLabel("Sex Column");
		templateSettings1.add(ageColumn, new FormData("100%"));

		NumberField standardColumn = new NumberField();
		standardColumn.setMaxLength(2);
		standardColumn.setFieldLabel("Standard Column");
		templateSettings1.add(standardColumn, new FormData("100%"));

		NumberField heightColumn = new NumberField();
		heightColumn.setMaxLength(2);
		heightColumn.setFieldLabel("Height Column");
		templateSettings1.add(heightColumn, new FormData("100%"));

		NumberField weightColumn = new NumberField();
		weightColumn.setMaxLength(2);
		weightColumn.setFieldLabel("Weight Column");
		templateSettings1.add(weightColumn, new FormData("100%"));

		NumberField addressColumn = new NumberField();
		addressColumn.setMaxLength(2);
		addressColumn.setFieldLabel("Address Column");
		templateSettings1.add(addressColumn, new FormData("100%"));
		
		mainTemplateSettingsPanel.add(templateSettings1);
		mainTemplateSettingsPanel.setSize("550", "266px");
		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		
		ColumnConfig clmncnfgNewColumn = new ColumnConfig("id", "Ailment Type", 100);
		configs.add(clmncnfgNewColumn);
		
		ColumnConfig clmncnfgNewColumn_1 = new ColumnConfig("id", "Ailment Type Column", 120);
		configs.add(clmncnfgNewColumn_1);
		
		ColumnConfig clmncnfgNewColumn_2 = new ColumnConfig("id", "Findings", 100);
		configs.add(clmncnfgNewColumn_2);
		
		FormPanel templateSettings2 = new FormPanel();
		templateSettings2.setLayout(new FormLayout());
		templateSettings2.setHeaderVisible(false);
		templateSettings2.setHeading("Template Details Grid");
		
		EditorGrid editorGrid = new EditorGrid(new ListStore(), new ColumnModel(configs));
		templateSettings2.add(editorGrid, new FormData("-15 -15"));
		editorGrid.setSize("210px", "330");
		editorGrid.setBorders(true);
		mainTemplateSettingsPanel.add(templateSettings2);
		templateSettings2.setSize("340px", "230px");
		return mainTemplateSettingsPanel;
	}
	
	private LayoutContainer buildUploadComponent()
	{
		LayoutContainer lcUploadComponent = new LayoutContainer();
		lcUploadComponent.setLayout(new TableLayout(2));

		LabelField lblFileImport = new LabelField("Select a file to Import:");
		lblFileImport.setSize("120", "20");

		MultiUploader defaultUploader = new MultiUploader();
		defaultUploader.setValidExtensions("xls", "xlsx");
		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		defaultUploader.setMaximumFiles(1);
		defaultUploader.setAvoidRepeatFiles(true);
		defaultUploader.setTitle("Select a file to upload:");

		lcUploadComponent.add(lblFileImport);
		lcUploadComponent.add(defaultUploader);
		lcUploadComponent.setWidth("400");
		return lcUploadComponent;
	}

	public void initialize(String title, String scrId)
	{
		IndexPage.unmaskCenterComponent();
	}

	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler()
	{
		public void onFinish(IUploader uploader)
		{
			if (uploader.getStatus() == Status.SUCCESS)
			{
				new PreloadedImage(uploader.fileUrl(), showImage);

				// The server sends useful information to the client by default
				UploadedInfo info = uploader.getServerInfo();
				System.out.println("File name " + info.name);
				System.out.println("File content-type " + info.ctype);
				System.out.println("File size " + info.size);

				// You can send any customized message and parse it
				System.out.println("Server message " + info.message);

				final MessageBox box = MessageBox.progress("Please wait", "Processing records...", "");
				final ProgressBar bar = box.getProgressBar();
				final Timer t = new Timer()
				{
					float i;

					@Override
					public void run()
					{
						bar.updateProgress(i / 100, (int) i + "% Complete");
						i += 5;
						if (i > 105)
						{
							cancel();
							box.close();
							Info.display("Message", "Items were loaded", "");
						}
					}
				};
				t.scheduleRepeating(500);
			}
		}
	};

	// Attach an image to the pictures viewer
	private OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler()
	{
		public void onLoad(PreloadedImage image)
		{
			image.setWidth("75px");
			panelImages.add(image);
		}
	};
	private LayoutContainer lcTemplateComponent_1;
}
