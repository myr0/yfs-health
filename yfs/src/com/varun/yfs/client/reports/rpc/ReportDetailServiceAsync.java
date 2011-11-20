package com.varun.yfs.client.reports.rpc;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReportDetailServiceAsync
{
	void getModel(ReportType report, ModelData params, AsyncCallback<ModelData> callback);
}
