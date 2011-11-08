package com.varun.yfs.server.admin.rpc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.admin.rpc.StoreLoader;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.util.Util;
import com.varun.yfs.server.common.data.DataUtil;

public class StoreLoaderServlet extends RemoteServiceServlet implements StoreLoader
{
	private static final Logger logger = Logger.getLogger(StoreLoaderServlet.class);
	private static final long serialVersionUID = -3784282705749642889L;

	@Override
	public List<ModelData> getListStore(String className)
	{
		List<ModelData> arrayList = new ArrayList<ModelData>();

		if (ModelDataEnum.isEnumElement(Util.stripSpace(className)))
			arrayList = ModelDataEnum.valueOf(Util.stripSpace(className)).getListStoreContents();
		else
			arrayList = ListModelDataEnum.valueOf(Util.stripSpace(className)).getListStoreContents();

		logger.info("*************" + getThreadLocalRequest().getSession().getId());
		return arrayList;
	}

	@Override
	public RpcStatusEnum saveListStore(String entityName, List<ModelData> lstModels)
	{
		RpcStatusEnum status = RpcStatusEnum.SUCCESS;
		try
		{
			DataUtil.<ModelData> saveListStore(Util.stripSpace(entityName), lstModels);
		} catch (Exception ex)
		{
			status = RpcStatusEnum.FAILURE;
		}
		logger.info("*************" + getThreadLocalRequest().getSession().getId());
		return status;
	}

	@Override
	public ModelData getModel(String entityName)
	{

		ModelData modelStore = new BaseModelData();

		modelStore = ModelDataEnum.valueOf(Util.stripSpace(entityName)).getStoreContents();

		logger.info("*************" + getThreadLocalRequest().getSession().getId());

		return modelStore;
	}

	@Override
	public RpcStatusEnum saveModel(String entityName, ModelData model)
	{
		RpcStatusEnum status = RpcStatusEnum.SUCCESS;
		try
		{
			ModelDataEnum.valueOf(Util.stripSpace(entityName)).saveModel(model);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			status = RpcStatusEnum.FAILURE;
		}
		logger.info("*************" + getThreadLocalRequest().getSession().getId());
		return status;
	}

	@Override
	public RpcStatusEnum saveModel(String entityName, List<ModelData> lstModels)
	{
		return null;
	}

}
