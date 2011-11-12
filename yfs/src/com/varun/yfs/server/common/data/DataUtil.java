package com.varun.yfs.server.common.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dozer.Mapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.util.Util;
import com.varun.yfs.dto.CampScreeningDetailDTO;
import com.varun.yfs.dto.ClinicDTO;
import com.varun.yfs.dto.ClinicScreeningDetailDTO;
import com.varun.yfs.dto.SchoolScreeningDetailDTO;
import com.varun.yfs.server.common.HibernateUtil;
import com.varun.yfs.server.models.PatientDetail;
import com.varun.yfs.server.models.ScreeningDetail;
import com.varun.yfs.server.models.User;

public class DataUtil
{
	public static final List<String> lstEntities = Arrays.asList(new String[] { "Entities", "Chapter Name", "City", "Country", "Doctor", "Locality", "Process Type", "State", "Town", "Type Of Location", "Village", "Volunteer", "User", "Referral Type", "Project", "Clinic" });
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

	public static void saveScreeningDetail(SchoolScreeningDetailDTO screeningDetailDto)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Mapper dozerMapper = HibernateUtil.getDozerMapper();
		Transaction trans = session.beginTransaction();
		try
		{
			ScreeningDetail scrDetHibObj = dozerMapper.map(screeningDetailDto, ScreeningDetail.class);
			String id = screeningDetailDto.get("id");
			extractPatientDetailData(session, screeningDetailDto, scrDetHibObj);
			if (id == null)
			{
				session.save(scrDetHibObj);
			} else
			{
				scrDetHibObj.setId(Long.parseLong(id));
				session.saveOrUpdate(scrDetHibObj);
			}
			trans.commit();
			session.flush();
		} catch (HibernateException ex)
		{
			trans.rollback();
			logger.error("Encountered error retrieving objects: " + ex.getMessage());
			throw ex;
		} finally
		{
			session.close();
		}
	}
	
	public static List<CampScreeningDetailDTO> getCampScreeningDetail(String joinTableName, String propertyName, String value)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query filter = session.createQuery("select sd from ScreeningDetail sd, " + joinTableName + " tb where sd." + Util.firstCharLower(joinTableName) + "." + propertyName + " = tb." + propertyName + " and tb." + propertyName + " = " + value);
		List<CampScreeningDetailDTO> lstScreening = new ArrayList<CampScreeningDetailDTO>();
		try
		{
			Mapper dozerMapper = HibernateUtil.getDozerMapper();
			Calendar cal = Calendar.getInstance();
			for (Object entity : filter.list())
			{
				CampScreeningDetailDTO dtoObject = (CampScreeningDetailDTO) dozerMapper.map(entity, CampScreeningDetailDTO.class);
				cal.setTimeInMillis(Long.parseLong(dtoObject.getScreeningDate()));
				dtoObject.set("name", DateFormat.getDateInstance(DateFormat.SHORT).format(cal.getTime()));
				lstScreening.add(dtoObject);
			}

		} catch (HibernateException ex)
		{
			logger.error("Encountered error retrieving objects: " + ex.getMessage());
			throw ex;
		} finally
		{
			session.close();
		}
		return lstScreening;
	}

	public static List<SchoolScreeningDetailDTO> getSchoolScreeningDetail(String joinTableName, String propertyName, String value)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query filter = session.createQuery("select sd from ScreeningDetail sd, " + joinTableName + " tb where sd." + Util.firstCharLower(joinTableName) + "." + propertyName + " = tb." + propertyName + " and tb." + propertyName + " = " + value);
		List<SchoolScreeningDetailDTO> lstScreening = new ArrayList<SchoolScreeningDetailDTO>();
		try
		{
			Mapper dozerMapper = HibernateUtil.getDozerMapper();
			Calendar cal = Calendar.getInstance();
			for (Object entity : filter.list())
			{
				SchoolScreeningDetailDTO dtoObject = (SchoolScreeningDetailDTO) dozerMapper.map(entity, SchoolScreeningDetailDTO.class);
				cal.setTimeInMillis(Long.parseLong(dtoObject.getScreeningDate()));
				dtoObject.set("name", DateFormat.getDateInstance(DateFormat.SHORT).format(cal.getTime()));
				lstScreening.add(dtoObject);
			}

		} catch (HibernateException ex)
		{
			logger.error("Encountered error retrieving objects: " + ex.getMessage());
			throw ex;
		} finally
		{
			session.close();
		}
		return lstScreening;
	}

	public static SchoolScreeningDetailDTO getScreeningDetail(long scrId)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		SchoolScreeningDetailDTO dtoObject = null;

		Criteria filter = session.createCriteria(ScreeningDetail.class);
		filter.add(Restrictions.eq("id", scrId)).add(Restrictions.eq("deleted", "N"));
		filter.createCriteria("lstPatientDetails").add(Restrictions.eq("deleted", "N"));
		try
		{
			Mapper dozerMapper = HibernateUtil.getDozerMapper();
			ScreeningDetail screeningDetail = (ScreeningDetail) filter.uniqueResult();
			screeningDetail.getDoctors();
			screeningDetail.getVolunteers();
			screeningDetail.getPatientDetails();
			dtoObject = (SchoolScreeningDetailDTO) dozerMapper.map(screeningDetail, SchoolScreeningDetailDTO.class);

		} catch (HibernateException ex)
		{
			logger.error("Encountered error retrieving objects: " + ex.getMessage());
			throw ex;
		} finally
		{
			session.close();
		}
		return dtoObject;
	}

	public static void saveUserDetail(ModelData model)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Mapper dozerMapper = HibernateUtil.getDozerMapper();
		Transaction trans = session.beginTransaction();
		try
		{
			List<ModelData> modelList = model.get("users");
			for (ModelData modelData : modelList)
			{
				User usrObj = dozerMapper.map(modelData, User.class);
				Long id = modelData.get("id");
				if (id == null)
				{
					session.save(usrObj);
				} else
				{
					usrObj.setId(id);
					session.saveOrUpdate(usrObj);
				}
			}
			trans.commit();
			session.flush();
		} catch (HibernateException ex)
		{
			trans.rollback();
			logger.error("Encountered error retrieving objects: " + ex.getMessage());
			throw ex;
		} finally
		{
			session.close();
		}
	}
	
	public static List<ClinicDTO> getClinics(String joinTableName, String propertyName, String value)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query filter = session.createQuery("select sd from Clinic sd, " + joinTableName + " tb where sd." + Util.firstCharLower(joinTableName) + "." + propertyName + " = tb." + propertyName + " and tb." + propertyName + " = " + value);
		List<ClinicDTO> lstClinics = new ArrayList<ClinicDTO>();
		try
		{
			Mapper dozerMapper = HibernateUtil.getDozerMapper();
			for (Object entity : filter.list())
			{
				ClinicDTO dtoObject = (ClinicDTO) dozerMapper.map(entity, ClinicDTO.class);
				dtoObject.set("name", dtoObject.getName());
				lstClinics.add(dtoObject);
			}

		} catch (HibernateException ex)
		{
			logger.error("Encountered error retrieving objects: " + ex.getMessage());
			throw ex;
		} finally
		{
			session.close();
		}
		return lstClinics;
	}


	private static void extractPatientDetailData(Session session, SchoolScreeningDetailDTO screeningDetailDto, ScreeningDetail scrDetHibObj)
	{
		int index = 0;
		for (ModelData modelData : screeningDetailDto.getPatientDetails())
		{
			PatientDetail patientDetail = scrDetHibObj.getPatientDetails().get(index++);

			patientDetail.setName(Util.safeToString(modelData.get("name")));
			patientDetail.setAge(Util.safeToString(modelData.get("age")));

			Object object = modelData.get("sex");
			if (object != null)
				patientDetail.setSex(object.toString());

			patientDetail.setStandard(Util.safeToString(modelData.get("standard")));
			patientDetail.setHeight(Util.safeToString(modelData.get("height")));
			patientDetail.setWeight(Util.safeToString(modelData.get("weight")));
			patientDetail.setAddress(Util.safeToString(modelData.get("address")));
			patientDetail.setContactNo(Util.safeToString(modelData.get("contactNo")));
			patientDetail.setDeleted(modelData.get("deleted").toString());

			patientDetail.setFindings(Util.safeToString(modelData.get("findings")));
			patientDetail.setTreatment(Util.safeToString(modelData.get("treatment")));

			object = modelData.get("referral1");
			if (object != null)
				patientDetail.setReferral1(object.toString());

			object = modelData.get("referral2");
			if (object != null)
				patientDetail.setReferral2(object.toString());

			object = modelData.get("referral3");
			if (object != null)
				patientDetail.setReferral3(object.toString());

			object = modelData.get("emergency");
			if (object != null)
				patientDetail.setEmergency(object.toString());

			object = modelData.get("caseClosed");
			if (object != null)
				patientDetail.setCaseClosed(object.toString());

			object = modelData.get("surgeryCase");
			if (object != null)
				patientDetail.setSurgeryCase(object.toString());

			if (patientDetail.getId() > 0)
			{
				session.saveOrUpdate(patientDetail);
			} else
			{
				session.save(patientDetail);
			}
			session.flush();
		}
	}

	public static void saveScreeningDetail(CampScreeningDetailDTO modelData)
	{
		
	}

	public static void saveScreeningDetail(ClinicScreeningDetailDTO modelData)
	{
		
	}
}
