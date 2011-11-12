package com.varun.yfs.client.screening.clinic.rpc;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.ClinicScreeningDetailDTO;

@RemoteServiceRelativePath("clinicScreeningDetailService")
public interface ClinicScreeningDetailService extends RemoteService
{
	ModelData getModel(String entityName);

	RpcStatusEnum saveModel(String entityName, ClinicScreeningDetailDTO model);


}
