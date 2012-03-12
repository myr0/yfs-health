package com.varun.yfs.server.admin.rpc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

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
	private static final Logger LOGGER = Logger.getLogger(StoreLoaderServlet.class);
	private static Map<String, Object> mapClassObjInstances = new HashMap<String, Object>();
	private static Map<String, Class> mapClassInstance = new HashMap<String, Class>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
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
			String entName = Util.stripSpace(entityName);
			Class clasLoaded;
			Object classObjInst;

			LOGGER.debug("Attempting to load entity models:" + entityName);

			if (mapClassInstance.containsKey(entName))
			{
				clasLoaded = mapClassInstance.get(entName);
				classObjInst = mapClassObjInstances.get(entName);
			} else
			{
				clasLoaded = Class.forName("com.varun.yfs.server.models.data." + entName + "Data");
				classObjInst = clasLoaded.newInstance();

				mapClassInstance.put(entName, clasLoaded);
				mapClassObjInstances.put(entName, classObjInst);
			}

			Method method = clasLoaded.getDeclaredMethod("getModel", new Class[] { UserDTO.class });
			modelStore = (ModelData) method.invoke(classObjInst, new Object[] { user });

			LOGGER.debug("Fetch entity completed:" + entityName);

		} catch (ClassNotFoundException e)
		{
			LOGGER.error("Class not found:" + e);
		} catch (InstantiationException e)
		{
			LOGGER.error("InstantiationException:" + e);
		} catch (IllegalAccessException e)
		{
			LOGGER.error("IllegalAccessException:" + e);
		} catch (IllegalArgumentException e)
		{
			LOGGER.error("IllegalArgumentException:" + e);
		} catch (SecurityException e)
		{
			LOGGER.error("SecurityException:" + e);
		} catch (InvocationTargetException e)
		{
			LOGGER.error("InvocationTargetException:" + e);
		} catch (NoSuchMethodException e)
		{
			LOGGER.error("NoSuchMethodException:" + e);
		}

		return modelStore;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public RpcStatusEnum saveModel(String entityName, ModelData model)
	{
		RpcStatusEnum status = RpcStatusEnum.SUCCESS;
		try
		{
			String entName = Util.stripSpace(entityName);
			Class clasLoaded;
			Object classObjInst;

			LOGGER.debug("Attempting to save entity:" + entityName);

			if (mapClassInstance.containsKey(entName))
			{
				clasLoaded = mapClassInstance.get(entName);
				classObjInst = mapClassObjInstances.get(entName);
			} else
			{
				clasLoaded = Class.forName("com.varun.yfs.server.models.data." + entName + "Data");
				classObjInst = clasLoaded.newInstance();

				mapClassInstance.put(entName, clasLoaded);
				mapClassObjInstances.put(entName, classObjInst);
			}

			Method method = clasLoaded.getDeclaredMethod("saveModel", new Class[] { ModelData.class });
			status = (RpcStatusEnum) method.invoke(classObjInst, (ModelData) model);

			LOGGER.debug("Save entity:" + entityName + " completed.");

		} catch (ClassNotFoundException e)
		{
			LOGGER.error("Class not found:" + e);
			status = RpcStatusEnum.FAILURE;
		} catch (InstantiationException e)
		{
			LOGGER.error("InstantiationException:" + e);
			status = RpcStatusEnum.FAILURE;
		} catch (IllegalAccessException e)
		{
			LOGGER.error("IllegalAccessException:" + e);
			status = RpcStatusEnum.FAILURE;
		} catch (IllegalArgumentException e)
		{
			LOGGER.error("IllegalArgumentException:" + e);
			status = RpcStatusEnum.FAILURE;
		} catch (SecurityException e)
		{
			LOGGER.error("SecurityException:" + e);
			status = RpcStatusEnum.FAILURE;
		} catch (InvocationTargetException e)
		{
			LOGGER.error("InvocationTargetException:" + e);
			status = RpcStatusEnum.FAILURE;
		} catch (NoSuchMethodException e)
		{
			LOGGER.error("NoSuchMethodException:" + e);
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
			LOGGER.debug("Attempting to delete entity - " + entityName);

			DataUtil.executeUpdate("update " + Util.stripSpace(entityName) + " set deleted = 'Y' where " + Util.stripSpace(entityName) + "id =" + id);

			LOGGER.info("Deleted entity - " + entityName + " successfully.");

		} catch (Exception ex)
		{
			LOGGER.error("Error encountered while deleting entity - " + entityName + " : " + ex);
			status = RpcStatusEnum.FAILURE;
		}
		return status;
	}

}
