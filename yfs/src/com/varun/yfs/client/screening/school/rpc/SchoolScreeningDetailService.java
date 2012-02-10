package com.varun.yfs.client.screening.school.rpc;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.dto.SchoolScreeningDetailDTO;

@RemoteServiceRelativePath("schoolScreeningDetailService")
public interface SchoolScreeningDetailService extends RemoteService
{
	ModelData getModel(String entityName);

	RpcStatusEnum saveModel(String entityName, SchoolScreeningDetailDTO model);

}
