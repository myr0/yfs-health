package com.varun.yfs.client.screening.imports;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.varun.yfs.dto.PatientDetailDTO;

public interface PatientDataImportServiceAsync
{

	void startProcessing(String path, AsyncCallback<Boolean> callback);

	void getProgress(AsyncCallback<String> callback);

	void getProcessedRecords(AsyncCallback<List<PatientDetailDTO>> callback);

}
