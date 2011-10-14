package com.varun.yfs.client.screening.imports;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.varun.yfs.client.index.IndexPage;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Radio;

public class ImportDetail extends LayoutContainer
{
	private FlowPanel panelImages = new FlowPanel();

	public ImportDetail()
	{
	}

	@Override
	protected void onRender(Element parent, int index)
	{
		super.onRender(parent, index);

		setLayout(new FitLayout());

		ContentPanel mainContainerPanel = new ContentPanel();
		mainContainerPanel.setHeading("Import Screening Data");
		mainContainerPanel.setCollapsible(true);
		add(mainContainerPanel);

		LayoutContainer lcUploadComponent = buildUploadComponent();
		LayoutContainer lcTemplateComponent = buildTemplateComponent();

		mainContainerPanel.add(lcUploadComponent, new FitData(5));
		lcTemplateComponent_1.setLayout(new FitLayout());
		mainContainerPanel.add(lcTemplateComponent, new FitData(5));
		add(mainContainerPanel);
	}

	private LayoutContainer buildTemplateComponent()
	{
		lcTemplateComponent_1 = new LayoutContainer();
		
		ContentPanel mainTemplateSettingsPanel = new ContentPanel();
		mainTemplateSettingsPanel.setHeading("Template Settings");
		mainTemplateSettingsPanel.setCollapsible(true);
		mainTemplateSettingsPanel.setLayout(new TableLayout(2));
		
		FormPanel templateSettings1 = new FormPanel();
		templateSettings1.setLayout(new FormLayout());
		templateSettings1.setHeaderVisible(false);
		templateSettings1.setHeading("New FormPanel");
		templateSettings1.setCollapsible(true);
		templateSettings1.setWidth("250");
		
		TextField txtfldFirstDataRow = new TextField();
		templateSettings1.add(txtfldFirstDataRow, new FormData("100%"));
		txtfldFirstDataRow.setFieldLabel("First Data Row");
		
		FormPanel templateSettings2 = new FormPanel();
		templateSettings2.setHeaderVisible(false);
		templateSettings2.setHeading("New FormPanel");
		templateSettings2.setCollapsible(true);
		
		mainTemplateSettingsPanel.add(templateSettings1);
		mainTemplateSettingsPanel.add(templateSettings2);
		mainTemplateSettingsPanel.setHeight("200");
		
		lcTemplateComponent_1.add(mainTemplateSettingsPanel);
		return lcTemplateComponent_1;
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
