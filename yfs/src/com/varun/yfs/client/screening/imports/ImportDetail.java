package com.varun.yfs.client.screening.imports;

import java.util.List;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.index.IndexPage;
import com.varun.yfs.client.screening.imports.rpc.FileUploadProgressService;
import com.varun.yfs.client.screening.imports.rpc.PatientDataImportService;
import com.varun.yfs.client.screening.imports.rpc.PatientDataImportServiceAsync;
import com.varun.yfs.dto.ProgressDTO;
import com.extjs.gxt.ui.client.widget.form.LabelField;

@SuppressWarnings("rawtypes")
public class ImportDetail extends LayoutContainer
{
	private final PatientDataImportServiceAsync patientDataImportService = PatientDataImportService.Util.getInstance();
	private EditorGrid dataGrid;
	private Dialog dialogImport;
	final boolean appendMode;

	final ImportType importType;
	private boolean importInProgress;

	public ImportDetail(ImportType type, EditorGrid editorGrid, Dialog dialogImport, boolean appendMode)
	{
		this.dataGrid = editorGrid;
		this.dialogImport = dialogImport;
		this.appendMode = appendMode;
		this.importType = type;
	}

	protected final Listener<MessageBoxEvent> l = new Listener<MessageBoxEvent>()
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

		final FormPanel panel = new FormPanel();
		panel.setHeaderVisible(false);
		panel.setFrame(true);
		panel.setAction("/yfs/UploadServlet");
		panel.setEncoding(Encoding.MULTIPART);
		panel.setMethod(Method.POST);
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		panel.setWidth(350);

		FileUploadField file = new FileUploadField();
		file.setName("fileUpload");
		file.setAllowBlank(false);
		file.setFieldLabel("File");
		panel.add(file);

		Button btn = new Button("Start");
		btn.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			@Override
			public void componentSelected(ButtonEvent ce)
			{
				if (!panel.isValid())
				{
					return;
				}

				panel.mask("Uploading file...");

				panel.submit();

				final Timer timer = new Timer()
				{
					@Override
					public void run()
					{
						FileUploadProgressService.Util.getInstance().getStatus(new AsyncCallback<RpcStatusEnum>()
						{
							@Override
							public void onFailure(Throwable caught)
							{
								MessageBox.info("Action", "Unable to read progress status. Retrying...", null);
								importInProgress = false;
							}

							@Override
							public void onSuccess(RpcStatusEnum result)
							{
								if (result.equals(RpcStatusEnum.COMPLETED))
								{
									cancel();
									panel.unmask();
									dialogImport.hide();
									startProcessing();
									importInProgress = true;
								} else if (result.equals(RpcStatusEnum.FAILURE))
								{
									MessageBox.info("Action", "Unable to read progress.Will retry in a moment.", null);
								}
							}

						});
					}
				};
				timer.scheduleRepeating(150);
			}
		});

		LabelField lblfldNoteUploadWill = new LabelField(
				"Note: File has to be uploaded.Please be patient, it may take few minutes.");
		panel.add(lblfldNoteUploadWill, new FormData("100%"));
		panel.addButton(btn);

		FormData fdStep1 = new FormData("80%");
		fdStep1.setMargins(new Margins(10, 5, 5, 5));
		add(panel, fdStep1);
		panel.setHeight("140px");
	}

	public void initialize(String title, String scrId)
	{
		IndexPage.unmaskCenterComponent();
	}

	@SuppressWarnings("unchecked")
	protected void onImportComplete(List<? extends BaseModelData> result)
	{
		ListStore store = dataGrid.getStore();
		if (appendMode)
		{
			List curStoreModel = store.getModels();
			for (BaseModelData newRecord : result)
			{
				if (curStoreModel.contains(newRecord))
				{
					if (newRecord.get("id") == null)
						curStoreModel.add(newRecord);
					else if (newRecord.get("id").equals(""))
					{
						curStoreModel.add(newRecord);
					} else
					{
						int index = curStoreModel.indexOf(newRecord);
						curStoreModel.set(index, newRecord);
						System.out.println("updating record");
					}
				} else
				// store doesnt have it - add it
				{
					curStoreModel.add(newRecord);
					System.out.println("Adding new record");
				}
			}
			store.removeAll();
			store.add(curStoreModel);
		} else
		{
			store.add(result);
		}
		store.commitChanges();
		dataGrid.unmask();
	}

	private void startProcessing()
	{
		if (importInProgress)
			return;

		final MessageBox box = MessageBox.progress("Please wait", "Processing records...", "");
		final ProgressBar bar = box.getProgressBar();
		box.show();
		patientDataImportService.startProcessing(importType, appendMode, new AsyncCallback<String>()
		{
			@Override
			public void onSuccess(String result)
			{
				IndexPage.unmaskCenterComponent();
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
								importInProgress = false;
							}

							@Override
							public void onSuccess(ProgressDTO result)
							{
								String progress = result.getProgress();
								bar.updateText(progress);

								// int curProcessed =
								// Integer.parseInt(progress.split("/")[0]);
								// int totalProcessed =
								// Integer.parseInt(progress.split("/")[1]);

								if (result.getStatus().equals(RpcStatusEnum.FAILURE))
								{
									cancel();
									box.close();
									importInProgress = false;
								} else if (result.getStatus().equals(RpcStatusEnum.COMPLETED))
								{
									cancel();
									box.close();

									dataGrid.mask("Loading ...");
									updateProcessedRecords();
								}

							}
						});
					}
				};
				
				if (!result.equalsIgnoreCase(RpcStatusEnum.SUCCESS.name()))
				{
					MessageBox.info("Import Failed", result, l);
					importInProgress = false;
					t.cancel();
					box.close();
					return;
				}
				
				t.scheduleRepeating(100);
			}

			@Override
			public void onFailure(Throwable caught)
			{
				IndexPage.unmaskCenterComponent();
				dataGrid.unmask();
				importInProgress = false;
				MessageBox.info("Import Failed", "Please try again. Additional Details : " + caught.getMessage(), l);
			}

			private void updateProcessedRecords()
			{
				patientDataImportService.getProcessedRecords(new AsyncCallback<List<? extends BaseModelData>>()
				{
					@Override
					public void onFailure(Throwable caught)
					{
						MessageBox.info("Preview Failed",
								"Failed to retrieve records. Please retry the operation again. Additional Details: "
										+ caught.getMessage(), l);
						dataGrid.unmask();
						importInProgress = false;
						return;
					}

					@Override
					public void onSuccess(List<? extends BaseModelData> result)
					{
						onImportComplete(result);
						importInProgress = false;
					}

				});
			}
		});
	}
	// private final IUploader.OnFinishUploaderHandler onFinishUploaderHandler =
	// new IUploader.OnFinishUploaderHandler()
	// {
	//
	// @Override
	// public void onFinish(IUploader uploader)
	// {
	// if (uploader.getStatus() ==
	// gwtupload.client.IUploadStatus.Status.SUCCESS)
	// {
	// new PreloadedImage(uploader.fileUrl(), showImage);
	// GWT.log(uploader.getServerResponse());
	// uploadPath = uploader.getServerInfo().message;
	//
	// IndexPage.maskCenterComponent("Please wait...");
	//
	// startProcessing();
	//
	// // defaultUploader.reset();
	// // defaultUploader.clear();
	// dialogImport.hide();
	// }
	// }
	//
	// private void startProcessing()
	// {
	// patientDataImportService.startProcessing(importType, uploadPath,
	// appendMode, new AsyncCallback<String>()
	// {
	// @Override
	// public void onSuccess(String result)
	// {
	// IndexPage.unmaskCenterComponent();
	// if (!result.equalsIgnoreCase(RpcStatusEnum.SUCCESS.name()))
	// {
	// MessageBox.info("Import Failed", result, l);
	// return;
	// }
	//
	// final MessageBox box = MessageBox.progress("Please wait",
	// "Processing records...", "");
	// final ProgressBar bar = box.getProgressBar();
	// final Timer t = new Timer()
	// {
	// @Override
	// public void run()
	// {
	// updateProgress();
	// }
	//
	// private void updateProgress()
	// {
	// patientDataImportService.getProgress(new AsyncCallback<ProgressDTO>()
	// {
	// @Override
	// public void onFailure(Throwable caught)
	// {
	// cancel();
	// box.close();
	// Info.display("Import Failed", "Processing failed", "");
	// }
	//
	// @Override
	// public void onSuccess(ProgressDTO result)
	// {
	// String progress = result.getProgress();
	// bar.updateText(progress);
	//
	// int curProcessed = Integer.parseInt(progress.split("/")[0]);
	// int totalProcessed = Integer.parseInt(progress.split("/")[1]);
	//
	// if (result.getStatus().equals(RpcStatusEnum.FAILURE))
	// {
	// cancel();
	// box.close();
	// // Info.display("Screening Detail Import",
	// // "Processing failed", "");
	// } else if (curProcessed >= totalProcessed)
	// {
	// cancel();
	// box.close();
	// // Info.display("Import Completed",
	// // "Processing completed", "");
	//
	// patientDetailGrid.mask("Loading ...");
	// updateProcessedRecords();
	// }
	// }
	// });
	// }
	// };
	// t.scheduleRepeating(100);
	// }
	//
	// @Override
	// public void onFailure(Throwable caught)
	// {
	// IndexPage.unmaskCenterComponent();
	// patientDetailGrid.unmask();
	// MessageBox.info("Import Failed",
	// "Please try again. Additional Details : " + caught.getMessage(), l);
	// }
	//
	// private void updateProcessedRecords()
	// {
	// patientDataImportService.getProcessedRecords(new AsyncCallback<List<?
	// extends BaseModelData>>()
	// {
	// @Override
	// public void onFailure(Throwable caught)
	// {
	// MessageBox.info("Preview Failed",
	// "Failed to retrieve records. Please retry the operation again. Additional Details: "
	// + caught.getMessage(), l);
	// patientDetailGrid.unmask();
	// return;
	// }
	//
	// @Override
	// public void onSuccess(List<? extends BaseModelData> result)
	// {
	// onImportComplete(result);
	// }
	//
	// });
	// }
	// });
	// }
	// };
	//
	// private final OnLoadPreloadedImageHandler showImage = new
	// OnLoadPreloadedImageHandler()
	// {
	// @Override
	// public void onLoad(PreloadedImage image)
	// {
	// image.setWidth("75px");
	// panelImages.add(image);
	// }
	// };
}
