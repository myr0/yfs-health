package com.varun.yfs.client.screening.export;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ExportService")
public interface ExportService extends RemoteService
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

	public String createExportFile(List<String> colHeaders, List<? extends ModelData> lstData);
}
