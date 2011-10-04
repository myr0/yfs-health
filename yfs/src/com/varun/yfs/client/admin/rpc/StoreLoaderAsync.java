package com.varun.yfs.client.admin.rpc;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StoreLoaderAsync
{

	void getModel(String entityName, AsyncCallback<ModelData> callback);

	void getListStore(String className, AsyncCallback<List<ModelData>> callback);

	void saveModel(String entityName, List<ModelData> lstModels, AsyncCallback<String> callback);

	void saveModel(String entityName, ModelData model, AsyncCallback<String> callback);

	void saveListStore(String className, List<ModelData> lstModels, AsyncCallback<String> callback);
}
