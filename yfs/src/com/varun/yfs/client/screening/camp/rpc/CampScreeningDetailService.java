package com.varun.yfs.client.screening.camp.rpc;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.CampScreeningDetailDTO;

@RemoteServiceRelativePath("campScreeningDetailService")
public interface CampScreeningDetailService extends RemoteService
{
	ModelData getModel(String entityName);

	RpcStatusEnum saveModel(String entityName, CampScreeningDetailDTO model);

}
