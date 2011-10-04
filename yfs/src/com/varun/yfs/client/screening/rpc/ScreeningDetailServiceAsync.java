package com.varun.yfs.client.screening.rpc;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.varun.yfs.dto.ScreeningDetailDTO;

public interface ScreeningDetailServiceAsync
{
	void getModel(String entityName, AsyncCallback<ModelData> callback);

	void saveModel(String entityName, ScreeningDetailDTO model, AsyncCallback<String> callback);

}
