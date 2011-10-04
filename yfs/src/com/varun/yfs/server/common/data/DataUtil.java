package com.varun.yfs.server.common.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.dto.ScreeningDetailDTO;
import com.varun.yfs.server.common.HibernateUtil;
import com.varun.yfs.server.models.ScreeningDetail;

public class DataUtil
{
	public static final List<String> lstEntities = Arrays.asList(new String[] { "Entities", "Chapter Name", "City", "Country", "Doctor", "Locality", "Process Type", "State", "Town", "Type Of Location", "Village", "Volunteer", "Users" });
	@SuppressWarnings("rawtypes")
	private static Map<String, Class> nameToHibernateModelClass = new HashMap<String, Class>();
	@SuppressWarnings("rawtypes")
	private static Map<String, Class> nameToDtoClass = new HashMap<String, Class>();

	private static Logger logger = LoggerFactory.getLogger(DataUtil.class);

	static
	{
		for (String entityName : lstEntities)
		{
			String className = entityName.replaceAll(" ", "");
			try
			{
				nameToHibernateModelClass.put(className, Class.forName("com.varun.yfs.server.models." + className));
				nameToDtoClass.put(className, Class.forName("com.varun.yfs.dto." + className + "DTO"));
			} catch (ClassNotFoundException e)
			{
				logger.error("Encountered error loading specified class instance: " + e.getCause());
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <E> List<E> getRawList(String entityName)
	{
		List<E> lstEntities = Collections.EMPTY_LIST;
		try
		{
			Session session = HibernateUtil.getSessionFactory().openSession();
			Criteria criteria = session.createCriteria(nameToHibernateModelClass.get(entityName));
			criteria.add(Restrictions.eq("deleted", "N"));
			lstEntities = criteria.list();
			session.close();
		} catch (HibernateException ex)
		{
			logger.error("Encountered error retrieving objects: " + ex.getCause());
			throw ex;
		}
		return lstEntities;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <E> List<E> getModelList(String entityName)
	{
		List<E> lstDtoObjects = Collections.EMPTY_LIST;
		try
		{
			Session session = HibernateUtil.getSessionFactory().openSession();
			Criteria criteria = session.createCriteria(nameToHibernateModelClass.get(entityName));
			criteria.add(Restrictions.eq("deleted", "N"));
			List lstEntities = criteria.list();

			Class dtoClassInstance = nameToDtoClass.get(entityName);
			Mapper dozerMapper = HibernateUtil.getDozerMapper();
			lstDtoObjects = new ArrayList<E>();
			for (Object entity : lstEntities)
			{
				E dtoObject = (E) dozerMapper.map(entity, dtoClassInstance);
				lstDtoObjects.add(dtoObject);
			}
			session.close();
		} catch (HibernateException ex)
		{
			logger.error("Encountered error retrieving objects: " + ex.getCause());
			throw ex;
		}
		return lstDtoObjects;
	}

	@SuppressWarnings("unchecked")
	public static <E> void saveListStore(String entityName, List<E> lstModels)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Mapper dozerMapper = HibernateUtil.getDozerMapper();
		String className = entityName.replaceAll(" ", "");
		for (E e : lstModels)
		{
			ModelData modData = (ModelData) e;
			String name = modData.get("name");
			String id = modData.get("id");
			String deleted = modData.get("deleted");

			Object hibObject = dozerMapper.map(e, nameToHibernateModelClass.get(className));
			try
			{
				Method method = null;

				if (id != null)
				{
					method = hibObject.getClass().getMethod("setId", Long.TYPE);
					method.invoke(hibObject, id);
				}
				if (deleted != null)
				{
					method = hibObject.getClass().getMethod("setDeleted", String.class);
					method.invoke(hibObject, deleted);
				}

				method = hibObject.getClass().getMethod("setName", String.class);
				method.invoke(hibObject, name);

			} catch (SecurityException ex)
			{
				logger.error("Security Violation: " + ex.getCause());
				throw ex;
			} catch (NoSuchMethodException ex)
			{
				logger.error("No such method exists: " + ex.getCause());
				ex.printStackTrace();
			} catch (IllegalArgumentException ex)
			{
				logger.error("Inappropriate argument passed: " + ex.getCause());
				throw ex;
			} catch (IllegalAccessException ex2)
			{
				logger.error("Illegal access trying to invoke a method: " + ex2.getCause());
			} catch (InvocationTargetException ex)
			{
				logger.error("Unable to create a class instance: " + ex.getCause());
			}
			session.saveOrUpdate(hibObject);
		}
		session.flush();
		session.close();
	}

	public static void saveScreeningDetail(ScreeningDetailDTO screeningDetailDto)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Mapper dozerMapper = HibernateUtil.getDozerMapper();
		Transaction trans = session.beginTransaction();
		try
		{
			ScreeningDetail scrDetHibObj = dozerMapper.map(screeningDetailDto, ScreeningDetail.class);
			String id = screeningDetailDto.get("id");
			if (id == null)
			{
				session.save(scrDetHibObj);
			} else
			{
				session.saveOrUpdate(scrDetHibObj);
			}
		} catch (HibernateException ex)
		{
			trans.rollback();
			logger.error("Encountered error retrieving objects: " + ex.getMessage());
			throw ex;
		} finally
		{
			trans.commit();
			session.flush();
			session.close();
		}
	}
}
