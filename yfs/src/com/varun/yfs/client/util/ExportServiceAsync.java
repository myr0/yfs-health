package com.varun.yfs.client.util;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExportServiceAsync
{

	void createExportFile(List<String> colHeaders, List<? extends ModelData> lstData, AsyncCallback<String> callback);

}
