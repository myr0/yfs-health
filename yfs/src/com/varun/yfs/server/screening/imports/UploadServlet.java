package com.varun.yfs.server.screening.imports;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

public class UploadServlet extends HttpServlet
{
	private static final long serialVersionUID = -1926102074777460914L;
	private String tmpDir;
	private ServletFileUpload upload;
	private FileUploadProgressListener progressListener = new FileUploadProgressListener();
	private static final Logger LOGGER = Logger.getLogger(UploadServlet.class);

	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		File temp = null;
		try
		{
			temp = File.createTempFile("temp-file-name", ".tmp");
			LOGGER.debug("Temp file : " + temp.getAbsolutePath());

			String absolutePath = temp.getAbsolutePath();
			tmpDir = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));

			LOGGER.debug("Temp file path : " + tmpDir);

			temp.delete();
		} catch (IOException e)
		{
			LOGGER.error("Encountered exception initializing the upload servlet: " + e);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");

		LOGGER.debug("Upload processing begins now");

		boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
		if (!isMultipartContent)
		{
			return;
		}

		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
		fileItemFactory.setSizeThreshold(1 * 1024 * 1024); // 1 MB
		fileItemFactory.setRepository(new File(tmpDir));

		upload = new ServletFileUpload(fileItemFactory);
		upload.setProgressListener(progressListener);

		HttpSession session = request.getSession();
		session.setAttribute("progressListener", progressListener);

		try
		{
			List items = upload.parseRequest(request);
			Iterator itr = items.iterator();
			while (itr.hasNext())
			{
				FileItem item = (FileItem) itr.next();
				if (item.isFormField())
				{
					out.println("File Name = " + item.getFieldName() + ", Value = " + item.getString());
				} else
				{
					out.println("Field Name = " + item.getFieldName() + ", File Name = " + item.getName() + ", Content type = " + item.getContentType() + ", File Size = " + item.getSize());

					File file = new File(tmpDir, item.getName().concat(String.valueOf(UUID.randomUUID().getMostSignificantBits())));
					item.write(file);

					progressListener.setFilePath(file.getAbsolutePath());
				}
				out.close();
				LOGGER.debug("File saved succesfully to: " + progressListener.getFilePath());
			}
		} catch (FileUploadException ex)
		{
			LOGGER.error("Error encountered while parsing the request" + ex);
		} catch (Exception ex)
		{
			LOGGER.error("Error encountered while uploading file" + ex);
		}

	}

}