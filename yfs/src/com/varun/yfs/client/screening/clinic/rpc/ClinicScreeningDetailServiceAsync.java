package com.varun.yfs.client.screening.clinic.rpc;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.ClinicPatientDetailDTO;

public interface ClinicScreeningDetailServiceAsync
{
	void getModel(String entityName, AsyncCallback<ModelData> callback);

	void saveModel(String entityName, List<ClinicPatientDetailDTO> model, AsyncCallback<RpcStatusEnum> callback);

}
