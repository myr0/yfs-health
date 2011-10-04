package com.varun.yfs.client.admin.rpc;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("storeloader")
public interface StoreLoader extends RemoteService
{
	ModelData getModel(String entityName);

	List<ModelData> getListStore(String className);

	String saveModel(String entityName, ModelData model);

	String saveModel(String entityName, List<ModelData> lstModels);

	String saveListStore(String className, List<ModelData> lstModels);

}
