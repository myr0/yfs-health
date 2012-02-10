package com.varun.yfs.server.reports.rpc;

import java.io.BufferedOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ImageByteReader extends HttpServlet
{
	private static final long serialVersionUID = -2392101295764631865L;
	private static final String SESSION_VAR_NAME = "var";

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException
	{
		try
		{
			String variable = request.getParameter(SESSION_VAR_NAME);
			HttpSession session = request.getSession();
			byte[] imageBytes = (byte[]) session.getAttribute(variable);
			response.reset();
			int contentLength = imageBytes.length;
			response.setContentLength(contentLength);
			response.setContentType("application");
			response.setHeader("Content-Disposition", "attachment; filename=chart.png");
			BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream());
			output.write(imageBytes);
			output.flush();
			output.close();
		} catch (Exception e)
		{
		}
	}
}
