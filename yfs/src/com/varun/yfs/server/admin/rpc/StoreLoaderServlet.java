package com.varun.yfs.server.admin.rpc;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.admin.rpc.StoreLoader;
import com.varun.yfs.dto.Util;
import com.varun.yfs.server.common.data.DataUtil;

public class StoreLoaderServlet extends RemoteServiceServlet implements StoreLoader
{

	private static final long serialVersionUID = -3784282705749642889L;

	@Override
	public List<ModelData> getListStore(String className)
	{
		List<ModelData> arrayList = new ArrayList<ModelData>();

		if (ModelDataEnum.isEnumElement(normalize(className)))
			arrayList = ModelDataEnum.valueOf(normalize(className)).getListStoreContents();
		else
			arrayList = ListModelDataEnum.valueOf(normalize(className)).getListStoreContents();

		return arrayList;
	}

	@Override
	public String saveListStore(String entityName, List<ModelData> lstModels)
	{
		String status = "Success";
		try
		{
			DataUtil.<ModelData> saveListStore(normalize(entityName), lstModels);
		} catch (Exception ex)
		{
			status = "Failed";
		}
		return status;
	}

	@Override
	public ModelData getModel(String entityName)
	{

		ModelData modelStore = new BaseModelData();

		modelStore = ModelDataEnum.valueOf(normalize(entityName)).getStoreContents();

		return modelStore;
	}

	@Override
	public String saveModel(String entityName, ModelData model)
	{
		String status = "Success";
		try
		{
			ModelDataEnum.valueOf(normalize(entityName)).saveModel(model);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			status = "Failed";
		}
		return status;
	}
	
	@Override
	public String saveModel(String entityName, List<ModelData> lstModels)
	{
		return null;
	}

	private String normalize(String entityName)
	{
		return Util.normalize(entityName);
	}
}
