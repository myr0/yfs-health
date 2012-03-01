package com.varun.yfs.server.screening.imports;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

public class UploadServlet extends UploadAction
{

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(UploadServlet.class);
//	private Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
//	private Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

	@Override
	public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException
	{
		String response = "";
		for (FileItem item : sessionFiles)
		{
			if (false == item.isFormField())
			{
				try
				{
					File file = File.createTempFile(item.getName(), "");
					item.write(file);

					LOGGER.debug("Writing the file to temporary folder." + file.getAbsolutePath());

					// receivedFiles.put(item.getFieldName(), file);
					// receivedContentTypes.put(item.getFieldName(),
					// item.getContentType());

					response += file.getAbsolutePath();

				} catch (Exception e)
				{
					LOGGER.error("Encountered an error while uploading the file. Action aborted: " + e.getMessage());
					throw new UploadActionException(e);
				}
			}
		}

		// removeSessionFileItems(request, false);

		return response;
	}
}
