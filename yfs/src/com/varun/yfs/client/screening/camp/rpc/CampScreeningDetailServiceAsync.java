package com.varun.yfs.client.screening.camp.rpc;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.CampScreeningDetailDTO;

public interface CampScreeningDetailServiceAsync
{
	void getModel(String entityName, AsyncCallback<ModelData> callback);

	void saveModel(String entityName, CampScreeningDetailDTO model, AsyncCallback<RpcStatusEnum> callback);

}
