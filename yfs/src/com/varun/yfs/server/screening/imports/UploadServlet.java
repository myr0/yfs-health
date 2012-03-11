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

public class UploadServlet extends HttpServlet
{
	private static final long serialVersionUID = -1926102074777460914L;
	private String tmpDir;
	private ServletFileUpload upload;
	private FileUploadProgressListener progressListener = new FileUploadProgressListener();

	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
		File temp = null;
		try
		{
			temp = File.createTempFile("temp-file-name", ".tmp");
			System.out.println("Temp file : " + temp.getAbsolutePath());

			String absolutePath = temp.getAbsolutePath();
			tmpDir = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));

			System.out.println("Temp file path : " + tmpDir);

			temp.delete();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException
	{
		HttpSession session = request.getSession();
		session.setAttribute("progressListener", progressListener);

		PrintWriter out = response.getWriter();
		response.setContentType("text/plain");

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
					out.println("Field Name = " + item.getFieldName() + ", File Name = " + item.getName()
							+ ", Content type = " + item.getContentType() + ", File Size = " + item.getSize());

					File file = new File(tmpDir, item.getName().concat(
							String.valueOf(UUID.randomUUID().getMostSignificantBits())));
					item.write(file);
					progressListener.setFilePath(file.getAbsolutePath());
				}
				out.close();
			}
		} catch (FileUploadException ex)
		{
			log("Error encountered while parsing the request", ex);
		} catch (Exception ex)
		{
			log("Error encountered while uploading file", ex);
		}

	}

}