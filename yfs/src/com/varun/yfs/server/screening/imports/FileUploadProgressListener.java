package com.varun.yfs.server.screening.imports;

import org.apache.commons.fileupload.ProgressListener;

public class FileUploadProgressListener implements ProgressListener
{
	private int percentDone = 0;
	private String filePath;

	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public String getFilePath()
	{
		return this.filePath;
	}

	public void update(long pBytesRead, long pContentLength, int pItems)
	{
		System.out.println("We are currently reading item " + pItems);
		if (pContentLength == -1)
		{
			System.out.println("So far, " + pBytesRead + " bytes have been read.");
		} else
		{
			System.out.println("So far, " + pBytesRead + " of " + pContentLength + " bytes have been read.");
		}
		percentDone = (int) (100 * pBytesRead / pContentLength);
	}

	public String getMessage()
	{
		System.out.println("Completed writing file to: " + this.filePath + " | " + this.percentDone);
		return String.valueOf(percentDone);
	}
};