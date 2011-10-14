package com.varun.yfs.server.screening.imports;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

public class UploadServlet extends UploadAction
{

	private static final long serialVersionUID = 1L;

	Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
	Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

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

					ExcelReader converter = null;
					try
					{
						converter = new ExcelReader();
						converter.convertExcelToCSV(file.getAbsolutePath(), "c:\\");
					} catch (Exception ex)
					{
						System.out.println("Caught an: " + ex.getClass().getName());
						System.out.println("Message: " + ex.getMessage());
						System.out.println("Stacktrace follows:.....");
						ex.printStackTrace(System.out);
					}

					receivedFiles.put(item.getFieldName(), file);
					receivedContentTypes.put(item.getFieldName(), item.getContentType());

					response += "File saved as " + file.getAbsolutePath();

				} catch (Exception e)
				{
					throw new UploadActionException(e);
				}
			}
		}

		removeSessionFileItems(request);

		return response;
	}

	@Override
	public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String fieldName = request.getParameter(PARAM_SHOW);
		File f = receivedFiles.get(fieldName);
		if (f != null)
		{
			response.setContentType(receivedContentTypes.get(fieldName));
			FileInputStream is = new FileInputStream(f);
			copyFromInputStreamToOutputStream(is, response.getOutputStream());
		} else
		{
			renderXmlResponse(request, response, ERROR_ITEM_NOT_FOUND);
		}
	}

	/**
	 * Remove a file when the user sends a delete request.
	 */
	@Override
	public void removeItem(HttpServletRequest request, String fieldName) throws UploadActionException
	{
		File file = receivedFiles.get(fieldName);
		receivedFiles.remove(fieldName);
		receivedContentTypes.remove(fieldName);
		if (file != null)
		{
			file.delete();
		}
	}
}
