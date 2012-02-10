package com.varun.yfs.server.admin.rpc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.admin.rpc.StoreLoader;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.util.Util;
import com.varun.yfs.dto.UserDTO;
import com.varun.yfs.server.common.data.DataUtil;

public class StoreLoaderServlet extends RemoteServiceServlet implements StoreLoader
{
	private static final long serialVersionUID = -3784282705749642889L;

	@SuppressWarnings("rawtypes")
	@Override
	public List<ModelData> getListStore(String className)
	{
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		UserDTO userObj = (UserDTO) session.getAttribute("user");

		List<ModelData> arrayList = new ArrayList<ModelData>();

		// if (ModelDataEnum.isEnumElement(Util.stripSpace(className)))
		// arrayList =
		// ModelDataEnum.valueOf(Util.stripSpace(className)).getListStoreContents();
		// else
		// arrayList =
		// ListModelDataEnum.valueOf(Util.stripSpace(className)).getListStoreContents();

		try
		{
			Class clasLoaded = Class.forName("com.varun.yfs.server.common.data." + Util.stripSpace(className) + "Data");
			Object obj = clasLoaded.newInstance();
			Method method = clasLoaded.getDeclaredMethod("getModelList", new Class[] {});
			arrayList = (List<ModelData>) method.invoke(obj, new Object[] {});
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (SecurityException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}

		return arrayList;
	}

	@Override
	public RpcStatusEnum saveListStore(String entityName, List<ModelData> lstModels)
	{
		RpcStatusEnum status = RpcStatusEnum.SUCCESS;
		// try
		// {
		// DataUtil.<ModelData> saveListStore(Util.stripSpace(entityName),
		// lstModels);
		// } catch (Exception ex)
		// {
		// status = RpcStatusEnum.FAILURE;
		// }
		try
		{
			Class clasLoaded = Class.forName("com.varun.yfs.server.common.data." + Util.stripSpace(entityName) + "Data");
			Object obj = clasLoaded.newInstance();
			Method method = clasLoaded.getDeclaredMethod("saveListStore", new Class[] { List.class });
			status = (RpcStatusEnum) method.invoke(obj, lstModels);
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (SecurityException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		} catch (Exception ex)
		{
			status = RpcStatusEnum.FAILURE;
		}
		return status;
	}

	@Override
	public ModelData getModel(String entityName)
	{

		ModelData modelStore = new BaseModelData();

		// modelStore =
		// ModelDataEnum.valueOf(Util.stripSpace(entityName)).getStoreContents();
		try
		{
			Class clasLoaded = Class.forName("com.varun.yfs.server.common.data." + Util.stripSpace(entityName) + "Data");
			Object obj = clasLoaded.newInstance();
			Method method = clasLoaded.getDeclaredMethod("getModel", new Class[] {});
			modelStore = (ModelData) method.invoke(obj, (Object) null);
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (SecurityException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}

		return modelStore;
	}

	@Override
	public RpcStatusEnum saveModel(String entityName, ModelData model)
	{
		RpcStatusEnum status = RpcStatusEnum.SUCCESS;
		// try
		// {
		// ModelDataEnum.valueOf(Util.stripSpace(entityName)).saveModel(model);
		// } catch (Exception ex)
		// {
		// ex.printStackTrace();
		// status = RpcStatusEnum.FAILURE;
		// }
		try
		{
			Class clasLoaded = Class.forName("com.varun.yfs.server.common.data." + Util.stripSpace(entityName) + "Data");
			Object obj = clasLoaded.newInstance();
			Method method = clasLoaded.getDeclaredMethod("saveModel", new Class[] { ModelData.class });
			status = (RpcStatusEnum) method.invoke(obj, (ModelData) model);
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		} catch (SecurityException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}catch (Exception ex)
		{
			status = RpcStatusEnum.FAILURE;
		}

		return status;
	}

	@Override
	public RpcStatusEnum setDeleted(String entityName, String id)
	{
		RpcStatusEnum status = RpcStatusEnum.SUCCESS;
		try
		{
			DataUtil.executeUpdate("update " + Util.stripSpace(entityName) + " set deleted = 'Y' where " + Util.stripSpace(entityName) + "id =" + id);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			status = RpcStatusEnum.FAILURE;
		}
		return status;
	}

}
