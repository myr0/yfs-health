package com.varun.yfs.client.admin.rpc;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.varun.yfs.client.common.RpcStatusEnum;

@RemoteServiceRelativePath("storeloader")
public interface StoreLoader extends RemoteService
{
	ModelData getModel(String entityName);

	List<ModelData> getListStore(String className);

	RpcStatusEnum saveModel(String entityName, ModelData model);

	RpcStatusEnum saveListStore(String className, List<ModelData> lstModels);
	
	RpcStatusEnum setDeleted(String entityName, String id);

}
