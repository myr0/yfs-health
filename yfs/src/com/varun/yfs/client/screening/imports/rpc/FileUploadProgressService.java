package com.varun.yfs.client.screening.imports.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.varun.yfs.client.common.RpcStatusEnum;

@RemoteServiceRelativePath("FileUploadProgressService")
public interface FileUploadProgressService extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util
	{
		private static FileUploadProgressServiceAsync instance;

		public static FileUploadProgressServiceAsync getInstance()
		{
			if (instance == null)
			{
				instance = GWT.create(FileUploadProgressService.class);
			}
			return instance;
		}
	}

	RpcStatusEnum getStatus();


}
