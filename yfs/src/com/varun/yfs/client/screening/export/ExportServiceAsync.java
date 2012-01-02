package com.varun.yfs.client.screening.export;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.varun.yfs.dto.ExportTableDTO;

public interface ExportServiceAsync
{

	void createExportFile(List<ExportTableDTO> exportTables, String base64Image, AsyncCallback<String> callback);

}
