package com.varun.yfs.client.screening.rpc;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.varun.yfs.dto.ScreeningDetailDTO;

@RemoteServiceRelativePath("screeningDetailService")
public interface ScreeningDetailService extends RemoteService
{
	ModelData getModel(String entityName);

	String saveModel(String entityName, ScreeningDetailDTO model);


}
