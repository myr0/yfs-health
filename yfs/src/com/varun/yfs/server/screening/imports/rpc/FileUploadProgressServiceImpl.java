package com.varun.yfs.server.screening.imports.rpc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.screening.imports.rpc.FileUploadProgressService;
import com.varun.yfs.server.screening.imports.FileUploadProgressListener;

public class FileUploadProgressServiceImpl extends RemoteServiceServlet implements FileUploadProgressService
{
	private static final long serialVersionUID = -1814360221873793373L;
	private static final Logger LOGGER = Logger.getLogger(FileUploadProgressServiceImpl.class);

	@Override
	public RpcStatusEnum getStatus()
	{
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		if (session == null)
		{
			LOGGER.error("Session is not yet up.There is a problem in the configuration.");
			return RpcStatusEnum.FAILURE;
		}

		FileUploadProgressListener testProgressListener = (FileUploadProgressListener) session.getAttribute("progressListener");
		if (testProgressListener == null)
		{
			LOGGER.error("Progress listener is not set. Upload session is not yet up. Check your configuration.");
			return RpcStatusEnum.FAILURE;
		}

		String message = testProgressListener.getMessage();

		if (message.equalsIgnoreCase("100"))
			return RpcStatusEnum.COMPLETED;

		LOGGER.debug("Upload in progress : " + message);

		return RpcStatusEnum.RUNNING;
	}

}
