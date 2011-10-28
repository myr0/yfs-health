package com.varun.yfs.client.screening.imports;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import gwtupload.client.SingleUploader;

import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.index.IndexPage;
import com.varun.yfs.client.screening.ScreeningDetail;
import com.varun.yfs.dto.PatientDetailDTO;
import com.varun.yfs.dto.ProgressDTO;

public class ImportDetail extends LayoutContainer
{
	private static final PatientDataImportServiceAsync patientDataImportService = PatientDataImportService.Util.getInstance();
	private FlowPanel panelImages = new FlowPanel();
	private String uploadPath;
	private ScreeningDetail screeningDetail;

	public ImportDetail()
	{
	}

	protected final Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>()
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

		ContentPanel mainContainerPanel = new ContentPanel();
		mainContainerPanel.setHeading("Import Screening Data");

		FieldSet fldstStep1 = new FieldSet();

		LayoutContainer lcUploadComponent = new LayoutContainer();
		fldstStep1.add(lcUploadComponent);
		lcUploadComponent.setLayout(new TableLayout(2));

		LabelField lblFileImport = new LabelField("Select a file:");
		lblFileImport.setWidth("120");

		SingleUploader defaultUploader = new SingleUploader();
		defaultUploader.setValidExtensions("xls", "xlsx");
		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		defaultUploader.setAvoidRepeatFiles(true);
		defaultUploader.setTitle("Select a file to upload:");

		lcUploadComponent.add(lblFileImport);
		lcUploadComponent.add(defaultUploader);

		FormData fdStep1 = new FormData("80%");
		fdStep1.setMargins(new Margins(5, 5, 5, 5));
		fldstStep1.setHeading("Step 1: Choose a file");
		fldstStep1.setCollapsible(true);

		FormData fdStep2 = new FormData("90%");
		fdStep2.setMargins(new Margins(5, 5, 5, 5));
		screeningDetail = new ScreeningDetail();
		screeningDetail.setHeight("700");
		screeningDetail.initialize("New Screening", null);

		mainContainerPanel.add(fldstStep1, fdStep1);
		mainContainerPanel.add(screeningDetail, fdStep2);
		mainContainerPanel.setScrollMode(Scroll.AUTOY);
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
				System.out.println("Servlet Raw message " + uploader.getServerResponse());
				System.out.println("file URL: " + uploader.fileUrl());
				System.out.println("server message: " + info.message);

				uploadPath = info.message;
				IndexPage.maskCenterComponent("Please wait...");
				startProcessing();
			}
		}

		private void startProcessing()
		{
			patientDataImportService.startProcessing(uploadPath, new AsyncCallback<String>()
			{
				@Override
				public void onSuccess(String result)
				{
					IndexPage.unmaskCenterComponent();
					if (!result.equalsIgnoreCase(RpcStatusEnum.SUCCESS.name()))
					{
						MessageBox.info("Import Failed", result, l);
						return;
					}

					final MessageBox box = MessageBox.progress("Please wait", "Processing records...", "");
					final ProgressBar bar = box.getProgressBar();
					final Timer t = new Timer()
					{
						@Override
						public void run()
						{
							updateProgress();
						}

						private void updateProgress()
						{
							patientDataImportService.getProgress(new AsyncCallback<ProgressDTO>()
							{
								@Override
								public void onFailure(Throwable caught)
								{
									cancel();
									box.close();
									Info.display("Import Failed", "Processing failed", "");
								}

								@Override
								public void onSuccess(ProgressDTO result)
								{
									String progress = result.getProgress();
									bar.updateText(progress);

									int curProcessed = Integer.parseInt(progress.split("/")[0]);
									int totalProcessed = Integer.parseInt(progress.split("/")[1]);
									
									if(result.getStatus().equals(RpcStatusEnum.FAILURE))
									{
										cancel();
										box.close();
										Info.display("Screening Detail Import", "Processing failed", "");
									}
									else if (curProcessed >= totalProcessed)
									{
										cancel();
										box.close();
										Info.display("Import Completed", "Processing completed", "");

										screeningDetail.getEditorGrid().mask("Please wait...");
										updateProcessedRecords();
									}
								}
							});
						}
					};
					t.scheduleRepeating(500);
				}

				@Override
				public void onFailure(Throwable caught)
				{
					IndexPage.unmaskCenterComponent();
					Info.display("Import Failed", "Processing Aborted. Please try again", "");
				}

				private void updateProcessedRecords()
				{
					patientDataImportService.getProcessedRecords(new AsyncCallback<List<PatientDetailDTO>>()
					{
						@Override
						public void onFailure(Throwable caught)
						{
							MessageBox.info("Preview Failed", "Failed to retrieve records. " + caught.getMessage(), l);
							return;
						}

						@Override
						public void onSuccess(List<PatientDetailDTO> result)
						{
							ListStore<PatientDetailDTO> store = screeningDetail.getEditorGrid().getStore();
							store.removeAll();
							store.add(result);
							screeningDetail.getEditorGrid().unmask();
						}

					});
				}
			});
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
