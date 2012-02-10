package com.varun.yfs.client.admin.rpc;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.varun.yfs.client.common.RpcStatusEnum;

@RemoteServiceRelativePath("storeloader")
public interface StoreLoader extends RemoteService
{
	ModelData getModel(String entityName);

	RpcStatusEnum saveModel(String entityName, ModelData model);

	RpcStatusEnum setDeleted(String entityName, String id);

}
