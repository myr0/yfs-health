package com.varun.yfs.client.util;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExportServiceAsync<E>
{

	void createExportFile(List<String> colHeaders, List<E> lstData, AsyncCallback<String> callback);

}
