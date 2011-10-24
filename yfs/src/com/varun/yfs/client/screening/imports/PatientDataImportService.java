package com.varun.yfs.client.screening.imports;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.varun.yfs.dto.PatientDetailDTO;

@RemoteServiceRelativePath("PatientDataImportService")
public interface PatientDataImportService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static PatientDataImportServiceAsync instance;
		public static PatientDataImportServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(PatientDataImportService.class);
			}
			return instance;
		}
	}
	
	public boolean startProcessing(String path);
	
	public String getProgress();
	
	public List<PatientDetailDTO> getProcessedRecords();
	
}
