package com.varun.yfs.client.screening.school.rpc;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.SchoolScreeningDetailDTO;

public interface SchoolScreeningDetailServiceAsync
{
	void getModel(String entityName, AsyncCallback<ModelData> callback);

	void saveModel(String entityName, SchoolScreeningDetailDTO model, AsyncCallback<RpcStatusEnum> callback);

}
