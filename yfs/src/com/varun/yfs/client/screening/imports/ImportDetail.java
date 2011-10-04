package com.varun.yfs.client.screening.imports;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;

import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.google.gwt.user.client.ui.FlowPanel;
import com.varun.yfs.client.screening.ScreeningDetail;

public class ImportDetail extends ScreeningDetail
{

	private FlowPanel panelImages = new FlowPanel();

	public ImportDetail()
	{
	}

	@Override
	protected void beforeRender()
	{
		super.beforeRender();

		MultiUploader defaultUploader = new MultiUploader();
		defaultUploader.setValidExtensions("xls", "xlsx");
		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		defaultUploader.setMaximumFiles(1);
		defaultUploader.setAvoidRepeatFiles(true);
		defaultUploader.setTitle("Select a file to upload:");
		mainContainerPanel.add(defaultUploader, new FitData(5));
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
