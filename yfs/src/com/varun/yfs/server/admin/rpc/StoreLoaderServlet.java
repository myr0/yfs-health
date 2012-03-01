package com.varun.yfs.server.admin.rpc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.varun.yfs.client.admin.rpc.StoreLoader;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.util.Util;
import com.varun.yfs.dto.UserDTO;
import com.varun.yfs.server.models.data.DataUtil;

public class StoreLoaderServlet extends RemoteServiceServlet implements StoreLoader
{
	private static final long serialVersionUID = -3784282705749642889L;

	@SuppressWarnings("rawtypes")
	@Override
	public ModelData getModel(String entityName)
	{
		UserDTO user = null;
		HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
		HttpSession session = httpServletRequest.getSession();
		Object userObj = session.getAttribute("user");
		if (userObj != null && userObj instanceof UserDTO)
		{
			user = (UserDTO) userObj;
		}
		ModelData modelStore = new BaseModelData();
		try
		{
			Class clasLoaded = Class
					.forName("com.varun.yfs.server.common.data." + Util.stripSpace(entityName) + "Data");
			Object obj = clasLoaded.newInstance();
			Method method = clasLoaded.getDeclaredMethod("getModel", new Class[] { UserDTO.class });
			modelStore = (ModelData) method.invoke(obj, new Object[] { user });
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
		try
		{
			Class clasLoaded = Class
					.forName("com.varun.yfs.server.common.data." + Util.stripSpace(entityName) + "Data");
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
		} catch (Exception ex)
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
			DataUtil.executeUpdate("update " + Util.stripSpace(entityName) + " set deleted = 'Y' where "
					+ Util.stripSpace(entityName) + "id =" + id);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			status = RpcStatusEnum.FAILURE;
		}
		return status;
	}

}
