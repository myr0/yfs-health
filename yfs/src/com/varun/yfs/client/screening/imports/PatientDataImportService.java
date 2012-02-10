package com.varun.yfs.client.screening.imports;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.varun.yfs.dto.ProgressDTO;

@RemoteServiceRelativePath("PatientDataImportService")
public interface PatientDataImportService extends RemoteService
{
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util
	{
		private static PatientDataImportServiceAsync instance;

		public static PatientDataImportServiceAsync getInstance()
		{
			if (instance == null)
			{
				instance = GWT.create(PatientDataImportService.class);
			}
			return instance;
		}
	}

	String startProcessing(ImportType type, String path, boolean readId);

	public ProgressDTO getProgress();

	public List<? extends BaseModelData> getProcessedRecords();

	public List<String> getErrorRecords();

}
