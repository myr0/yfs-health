package com.varun.yfs.server.screening.imports;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.log4j.Logger;

public class FileUploadProgressListener implements ProgressListener
{
	private int percentDone = 0;
	private String filePath;
	private static final Logger LOGGER = Logger.getLogger(FileUploadProgressListener.class);

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
		LOGGER.debug("We are currently reading item " + pItems);
		if (pContentLength == -1)
		{
			LOGGER.debug("So far, " + pBytesRead + " bytes have been read.");
		} else
		{
			LOGGER.debug("So far, " + pBytesRead + " of " + pContentLength + " bytes have been read.");
		}
		percentDone = (int) (100 * pBytesRead / pContentLength);
	}

	public String getMessage()
	{
		LOGGER.debug("Completed writing file to: " + this.filePath + " | " + this.percentDone);
		return String.valueOf(percentDone);
	}
};