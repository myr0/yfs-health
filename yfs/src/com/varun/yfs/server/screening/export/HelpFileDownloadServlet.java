package com.varun.yfs.server.screening.export;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class HelpFileDownloadServlet extends HttpServlet
{
	private static final long serialVersionUID = 1927635448345836755L;
	private static final Logger LOGGER = Logger.getLogger(HelpFileDownloadServlet.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		OutputStream out = null;
		try
		{
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "attachment; filename=DFSPatientManagement.pdf");

			BufferedInputStream iStream = new BufferedInputStream(new FileInputStream(getServletContext().getRealPath(
					"/")
					+ "/WEB-INF/DFSPatientManagement.pdf"));
			byte[] arg0 = new byte[8192];
			while (iStream.read(arg0) != -1)
				response.getOutputStream().write(arg0);

			LOGGER.debug("Finished streaming help file contents");

		} catch (Exception e)
		{
			LOGGER.error("Exception in Help File download Servlet: " + e);
			throw new ServletException("Exception in Help File download Servlet", e);
		} finally
		{
			if (out != null)
			{
				out.close();
			}
		}
	}
}
