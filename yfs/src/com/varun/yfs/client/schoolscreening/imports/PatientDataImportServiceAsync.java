package com.varun.yfs.client.schoolscreening.imports;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.varun.yfs.dto.PatientDetailDTO;
import com.varun.yfs.dto.ProgressDTO;

public interface PatientDataImportServiceAsync
{

	void startProcessing(String path, boolean readId, AsyncCallback<String> callback);

	void getProgress(AsyncCallback<ProgressDTO> callback);

	void getProcessedRecords(AsyncCallback<List<PatientDetailDTO>> callback);

	void getErrorRecords(AsyncCallback<List<String>> callback);

}
