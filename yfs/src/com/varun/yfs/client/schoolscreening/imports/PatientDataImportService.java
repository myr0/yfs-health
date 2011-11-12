package com.varun.yfs.client.schoolscreening.imports;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.varun.yfs.dto.PatientDetailDTO;
import com.varun.yfs.dto.ProgressDTO;

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
	
	String startProcessing(String path, boolean readId);
	
	public ProgressDTO getProgress();
	
	public List<PatientDetailDTO> getProcessedRecords();
	
	public List<String> getErrorRecords();
	
}
