package com.varun.yfs.client.screening.imports;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
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
		setSize("400", "175");
	}

	@Override
	protected void onRender(Element parent, int index)
	{
		super.onRender(parent, index);

		setLayout(new FitLayout());

		ContentPanel mainContainerPanel = new ContentPanel();
		mainContainerPanel.setHeading("Import Screening Data");

		FieldSet fldstStepChoose = new FieldSet();

		LayoutContainer lcUploadComponent_1 = new LayoutContainer();
		fldstStepChoose.add(lcUploadComponent_1);
		lcUploadComponent_1.setLayout(new TableLayout(2));

		LabelField lblFileImport = new LabelField("Select a file to Import:");
		lblFileImport.setSize("120", "20");

		MultiUploader defaultUploader = new MultiUploader();
		defaultUploader.setValidExtensions("xls", "xlsx");
		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		defaultUploader.setMaximumFiles(1);
		defaultUploader.setAvoidRepeatFiles(true);
		defaultUploader.setTitle("Select a file to upload:");

		lcUploadComponent_1.add(lblFileImport);
		lcUploadComponent_1.add(defaultUploader);
		defaultUploader.setWidth("80%");
		lcUploadComponent_1.setWidth("400");
		FormData fd_fldstStepChoose = new FormData("80%");
		fd_fldstStepChoose.setMargins(new Margins(5, 5, 5, 5));
		mainContainerPanel.add(fldstStepChoose, fd_fldstStepChoose);
		fldstStepChoose.setHeading("Step 1: Choose a file");
		fldstStepChoose.setCollapsible(true);

		FieldSet fldstStepProcess = new FieldSet();
		Button btnStartProcessing = new Button("Start Processing", new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
			}
		});
		fldstStepProcess.add(btnStartProcessing);
		FormData fd_fldstStepProcess = new FormData("80%");
		fd_fldstStepProcess.setMargins(new Margins(5, 5, 5, 5));
		mainContainerPanel.add(fldstStepProcess, fd_fldstStepProcess);
		fldstStepProcess.setHeading("Step 2: Process");
		fldstStepProcess.setCollapsible(true);
		add(mainContainerPanel);
		mainContainerPanel.setSize("600px", "165px");
		mainContainerPanel.setLayout(new FormLayout());

		mainContainerPanel.setButtonAlign(HorizontalAlignment.CENTER);

		add(mainContainerPanel);
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
}
