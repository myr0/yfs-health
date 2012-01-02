package com.varun.yfs.client.screening.export;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.varun.yfs.dto.ExportTableDTO;

@RemoteServiceRelativePath("ExportService")
public interface ExportService extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	class Util
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

	String createExportFile(List<ExportTableDTO> exportTables, String base64Image);
}
