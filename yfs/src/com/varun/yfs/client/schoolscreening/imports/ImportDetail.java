package com.varun.yfs.client.schoolscreening.imports;

import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnStartUploaderHandler;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.TableLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.index.IndexPage;
import com.varun.yfs.dto.PatientDetailDTO;
import com.varun.yfs.dto.ProgressDTO;

public class ImportDetail extends LayoutContainer
{
	private static final PatientDataImportServiceAsync patientDataImportService = PatientDataImportService.Util.getInstance();
	private String uploadPath;
	private FlowPanel panelImages = new FlowPanel();
	private EditorGrid<PatientDetailDTO> patientDetailGrid;
	private Dialog dialogImport;
	final MultiUploader defaultUploader = new MultiUploader();
	final boolean appendMode;

	public ImportDetail(EditorGrid<PatientDetailDTO> editorGrid, Dialog dialogImport, boolean appendMode)
	{
		this.patientDetailGrid = editorGrid;
		this.dialogImport = dialogImport;
		this.appendMode = appendMode;
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

		LayoutContainer lcUploadComponent = new LayoutContainer();
		lcUploadComponent.setLayout(new TableLayout(2));

		LabelField lblFileImport = new LabelField("Select a file:");
		lblFileImport.setWidth("70");

		defaultUploader.setValidExtensions("xls", "xlsx");
		defaultUploader.addOnStartUploadHandler(new OnStartUploaderHandler()
		{
			@Override
			public void onStart(IUploader uploader)
			{
				dialogImport.hide();
				defaultUploader.clear();
				defaultUploader.reset();
			}
		});
		defaultUploader.setMaximumFiles(1);
		defaultUploader.setAvoidRepeatFiles(true);
		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);

		lcUploadComponent.add(lblFileImport);
		lcUploadComponent.add(defaultUploader);

		FormData fdStep1 = new FormData("80%");
		fdStep1.setMargins(new Margins(10, 5, 5, 5));
		add(lcUploadComponent, fdStep1);
	}

	public void initialize(String title, String scrId)
	{
		IndexPage.unmaskCenterComponent();
	}

	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler()
	{

		public void onFinish(IUploader uploader)
		{
			if (uploader.getStatus() == gwtupload.client.IUploadStatus.Status.SUCCESS)
			{
				new PreloadedImage(uploader.fileUrl(), showImage);
				System.out.println(uploader.getServerResponse());
				uploadPath = uploader.getServerInfo().message;

				IndexPage.maskCenterComponent("Please wait...");

				defaultUploader.reset();
				defaultUploader.clear();
				dialogImport.hide();

				startProcessing();
			}
		}

		private void startProcessing()
		{
			patientDataImportService.startProcessing(uploadPath, appendMode, new AsyncCallback<String>()
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

									if (result.getStatus().equals(RpcStatusEnum.FAILURE))
									{
										cancel();
										box.close();
										Info.display("Screening Detail Import", "Processing failed", "");
									} else if (curProcessed >= totalProcessed)
									{
										cancel();
										box.close();
										Info.display("Import Completed", "Processing completed", "");

										patientDetailGrid.mask("Loading ...");
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
					patientDetailGrid.unmask();
					MessageBox.info("Import Failed", "Please try again. Additional Details : " + caught.getMessage(), l);
				}

				private void updateProcessedRecords()
				{
					patientDataImportService.getProcessedRecords(new AsyncCallback<List<PatientDetailDTO>>()
					{
						@Override
						public void onFailure(Throwable caught)
						{
							MessageBox.info("Preview Failed", "Failed to retrieve records. Please retry the operation again. Additional Details: " + caught.getMessage(), l);
							patientDetailGrid.unmask();
							return;
						}

						@Override
						public void onSuccess(List<PatientDetailDTO> result)
						{
							ListStore<PatientDetailDTO> store = patientDetailGrid.getStore();
							if (appendMode)
							{
								List<PatientDetailDTO> lstCurrentModels = store.getModels();
								for (PatientDetailDTO patientDetailDTO : result)
								{
									if (lstCurrentModels.contains(patientDetailDTO))
									{
										lstCurrentModels.set(lstCurrentModels.indexOf(patientDetailDTO), patientDetailDTO);
									}

								}
								result = lstCurrentModels;
							}
							store.removeAll();
							store.add(result);
							patientDetailGrid.unmask();
						}

					});
				}
			});
		}
	};

	private OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler()
	{
		public void onLoad(PreloadedImage image)
		{
			image.setWidth("75px");
			panelImages.add(image);
		}
	};
}
