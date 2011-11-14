package com.varun.yfs.client.screening.imports;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.varun.yfs.dto.SchoolPatientDetailDTO;
import com.varun.yfs.dto.ProgressDTO;

public interface PatientDataImportServiceAsync
{

	void startProcessing(String path, boolean readId, AsyncCallback<String> callback);

	void getProgress(AsyncCallback<ProgressDTO> callback);

	void getProcessedRecords(AsyncCallback<List<SchoolPatientDetailDTO>> callback);

	void getErrorRecords(AsyncCallback<List<String>> callback);

}
