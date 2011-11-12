package com.varun.yfs.client.screening.clinic.rpc;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.ClinicScreeningDetailDTO;

public interface ClinicScreeningDetailServiceAsync
{
	void getModel(String entityName, AsyncCallback<ModelData> callback);

	void saveModel(String entityName, ClinicScreeningDetailDTO model, AsyncCallback<RpcStatusEnum> callback);

}
