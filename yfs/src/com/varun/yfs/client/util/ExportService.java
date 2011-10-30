package com.varun.yfs.client.util;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ExportService")
public interface ExportService<E> extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util
	{
		private static ExportServiceAsync instance;

		public static ExportServiceAsync getInstance()
		{
			if (instance == null)
			{
				instance = GWT.create(ExportService.class);
			}
			return instance;
		}
	}

	public String createExportFile(List<String> colHeaders, List<E> lstData);
}
