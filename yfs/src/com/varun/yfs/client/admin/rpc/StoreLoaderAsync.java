package com.varun.yfs.client.admin.rpc;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.varun.yfs.client.common.RpcStatusEnum;

public interface StoreLoaderAsync
{

	void getModel(String entityName, AsyncCallback<ModelData> callback);

	void getListStore(String className, AsyncCallback<List<ModelData>> callback);


	void saveModel(String entityName, ModelData model, AsyncCallback<RpcStatusEnum> callback);

	void saveListStore(String className, List<ModelData> lstModels, AsyncCallback<RpcStatusEnum> callback);
}
