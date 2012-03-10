package com.varun.yfs.server.screening.imports.rpc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.screening.imports.rpc.FileUploadProgressService;
import com.varun.yfs.server.screening.imports.FileUploadProgressListener;

public class FileUploadProgressServiceImpl extends RemoteServiceServlet implements FileUploadProgressService
{
	private static final long serialVersionUID = -1814360221873793373L;

	@Override
	public RpcStatusEnum getStatus()
	{
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		if (session == null)
		{
			return RpcStatusEnum.FAILURE;
		}

		FileUploadProgressListener testProgressListener = (FileUploadProgressListener) session
				.getAttribute("progressListener");
		if (testProgressListener == null)
		{
			return RpcStatusEnum.FAILURE;
		}

		if (testProgressListener.getMessage().equalsIgnoreCase("100"))
			return RpcStatusEnum.COMPLETED;

		return RpcStatusEnum.RUNNING;
	}

}
