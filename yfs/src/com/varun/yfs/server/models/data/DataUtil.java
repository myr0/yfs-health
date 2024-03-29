package com.varun.yfs.server.models.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.util.Util;
import com.varun.yfs.dto.CampScreeningDetailDTO;
import com.varun.yfs.dto.ClinicDTO;
import com.varun.yfs.dto.ClinicPatientDetailDTO;
import com.varun.yfs.dto.SchoolScreeningDetailDTO;
import com.varun.yfs.server.models.CampPatientDetail;
import com.varun.yfs.server.models.CampScreeningDetail;
import com.varun.yfs.server.models.Clinic;
import com.varun.yfs.server.models.ClinicPatientDetail;
import com.varun.yfs.server.models.SchoolPatientDetail;
import com.varun.yfs.server.models.SchoolScreeningDetail;
import com.varun.yfs.server.models.User;
import com.varun.yfs.server.models.UserChapterPermissions;
import com.varun.yfs.server.models.UserClinicPermissions;
import com.varun.yfs.server.models.UserEntityPermissions;
import com.varun.yfs.server.models.UserProjectPermissions;
import com.varun.yfs.server.models.UserReportPermissions;

public class DataUtil
{
	private static final List<String> ENTITIES = Arrays.asList(new String[] { "Entities", "Chapter Name", "City",
			"Country", "Doctor", "Locality", "Process Type", "State", "Town", "Type Of Location", "Village",
			"Volunteer", "User", "Referral Type", "Project", "Clinic" });
	@SuppressWarnings("rawtypes")
	private static Map<String, Class> nameToHibernateModelClass = new HashMap<String, Class>();
	@SuppressWarnings("rawtypes")
	private static Map<String, Class> nameToDtoClass = new HashMap<String, Class>();

	private static final Logger LOGGER = Logger.getLogger(DataUtil.class);

	static
	{
		for (String entityName : ENTITIES)
		{
			String className = entityName.replaceAll(" ", "");
			try
			{
				nameToHibernateModelClass.put(className, Class.forName("com.varun.yfs.server.models." + className));
				nameToDtoClass.put(className, Class.forName("com.varun.yfs.dto." + className + "DTO"));
			} catch (ClassNotFoundException e)
			{
				LOGGER.error("Encountered error loading specified class instance: " + e);
			}
		}
	}

	public static List<String> getEntitiesList()
	{
		return ENTITIES;
	}

	@SuppressWarnings("unchecked")
	public static <E> List<E> getRawList(String entityName)
	{
		List<E> lstEntities = Collections.EMPTY_LIST;
		try
		{
			Session session = HibernateUtil.getSessionFactory().openSession();
			Criteria criteria = session.createCriteria(nameToHibernateModelClass.get(entityName));
			criteria.add(Restrictions.eq("deleted", "N")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			lstEntities = criteria.list();
			session.close();
		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error retrieving objects: " + ex.getCause());
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
			criteria.add(Restrictions.eq("deleted", "N")).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
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
			LOGGER.error("Encountered error retrieving objects: " + ex.getCause());
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
				LOGGER.error("Security Violation: " + ex.getCause());
				throw ex;
			} catch (NoSuchMethodException ex)
			{
				LOGGER.error("No such method exists: " + ex.getCause());
				ex.printStackTrace();
			} catch (IllegalArgumentException ex)
			{
				LOGGER.error("Inappropriate argument passed: " + ex.getCause());
				throw ex;
			} catch (IllegalAccessException ex2)
			{
				LOGGER.error("Illegal access trying to invoke a method: " + ex2.getCause());
			} catch (InvocationTargetException ex)
			{
				LOGGER.error("Unable to create a class instance: " + ex.getCause());
			}
			session.saveOrUpdate(hibObject);
		}
		session.flush();
		session.close();
	}

	public static void saveScreeningDetail(String clinicId, List<ClinicPatientDetailDTO> lstModelData)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Mapper dozerMapper = HibernateUtil.getDozerMapper();
		Transaction trans = session.beginTransaction();
		try
		{
			Clinic clinic = null;
			if (!clinicId.isEmpty())
				clinic = (Clinic) session.createCriteria(Clinic.class)
						.add(Restrictions.eq("id", Long.parseLong(clinicId))).uniqueResult();

			final Calendar currentDate = Calendar.getInstance();
			currentDate.setTime(new Date());
			currentDate.set(Calendar.HOUR_OF_DAY, 0);
			currentDate.set(Calendar.MINUTE, 0);
			currentDate.set(Calendar.SECOND, 0);
			currentDate.set(Calendar.MILLISECOND, 0);

			for (ClinicPatientDetailDTO clinicPatientDetailDTO : lstModelData)
			{
				ClinicPatientDetail scrDetHibObj = dozerMapper.map(clinicPatientDetailDTO, ClinicPatientDetail.class);
				scrDetHibObj.setClinic(clinic);
				Long id = clinicPatientDetailDTO.get("id");
				if (id == null)
				{
					session.save(scrDetHibObj);
				} else
				{
					scrDetHibObj.setId(id);
					session.saveOrUpdate(scrDetHibObj);
				}
			}

			trans.commit();
			session.flush();
		} catch (HibernateException ex)
		{
			trans.rollback();
			LOGGER.error("Encountered error retrieving objects: " + ex);
			throw ex;
		} finally
		{
			session.close();
		}
	}

	public static void saveScreeningDetail(CampScreeningDetailDTO screeningDetailDto)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Mapper dozerMapper = HibernateUtil.getDozerMapper();
		Transaction trans = session.beginTransaction();
		try
		{
			CampScreeningDetail scrDetHibObj = dozerMapper.map(screeningDetailDto, CampScreeningDetail.class);
			for (CampPatientDetail campPatientDetail : scrDetHibObj.getPatientDetails())
			{
				if (campPatientDetail.getId() == 0)
					session.save(campPatientDetail);
				else
					session.saveOrUpdate(campPatientDetail);
			}
			session.flush();
			String id = screeningDetailDto.get("id");
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
			LOGGER.error("Encountered error retrieving objects: " + ex);
			throw ex;
		} finally
		{
			session.close();
		}
	}

	public static void saveScreeningDetail(SchoolScreeningDetailDTO screeningDetailDto)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Mapper dozerMapper = HibernateUtil.getDozerMapper();
		Transaction trans = session.beginTransaction();
		try
		{
			SchoolScreeningDetail scrDetHibObj = dozerMapper.map(screeningDetailDto, SchoolScreeningDetail.class);
			for (SchoolPatientDetail schoolPatientDetail : scrDetHibObj.getPatientDetails())
			{
				if (schoolPatientDetail.getId() == 0)
					session.save(schoolPatientDetail);
				else
					session.saveOrUpdate(schoolPatientDetail);
			}
			session.flush();
			Long id = screeningDetailDto.get("id");
			if (id == null)
			{
				session.save(scrDetHibObj);
			} else
			{
				scrDetHibObj.setId(id);
				session.saveOrUpdate(scrDetHibObj);
			}
			trans.commit();
			session.flush();
		} catch (HibernateException ex)
		{
			trans.rollback();
			LOGGER.error("Encountered error retrieving objects: " + ex);
			throw ex;
		} finally
		{
			session.close();
		}
	}

	public static CampScreeningDetailDTO getCampScreeningDetail(long scrId)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		CampScreeningDetailDTO dtoObject = null;

		Criteria filter = session.createCriteria(CampScreeningDetail.class);
		filter.add(Restrictions.eq("id", scrId)).add(Restrictions.eq("deleted", "N"));
		try
		{
			Mapper dozerMapper = HibernateUtil.getDozerMapper();
			CampScreeningDetail screeningDetail = (CampScreeningDetail) filter.uniqueResult();
			if (screeningDetail != null)
			{
				screeningDetail.getDoctors();
				screeningDetail.getVolunteers();
				screeningDetail.getPatientDetails();
				dtoObject = (CampScreeningDetailDTO) dozerMapper.map(screeningDetail, CampScreeningDetailDTO.class);
			}

		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error retrieving objects: " + ex);
			throw ex;
		} finally
		{
			session.close();
		}
		return dtoObject;
	}

	public static List<CampScreeningDetailDTO> getCampScreeningDetail(String joinTableName, String propertyName,
			String value)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query filter = session.createQuery("select sd from CampScreeningDetail sd, " + joinTableName + " tb where sd."
				+ Util.firstCharLower(joinTableName) + "." + propertyName + " = tb." + propertyName + " and tb."
				+ propertyName + " = " + value + " and sd.deleted = 'N'");
		List<CampScreeningDetailDTO> lstScreening = new ArrayList<CampScreeningDetailDTO>();
		try
		{
			Mapper dozerMapper = HibernateUtil.getDozerMapper();
			Calendar cal = Calendar.getInstance();
			for (Object entity : filter.list())
			{
				CampScreeningDetailDTO dtoObject = (CampScreeningDetailDTO) dozerMapper.map(entity,
						CampScreeningDetailDTO.class);
				cal.setTimeInMillis(Long.parseLong(dtoObject.getScreeningDate()));
				dtoObject.set("name", DateFormat.getDateInstance(DateFormat.SHORT).format(cal.getTime()));
				lstScreening.add(dtoObject);
			}

		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error retrieving objects: " + ex);
			throw ex;
		} finally
		{
			session.close();
		}
		return lstScreening;
	}

	public static List<SchoolScreeningDetailDTO> getSchoolScreeningDetail(String joinTableName, String propertyName,
			String value)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query filter = session.createQuery("select sd from SchoolScreeningDetail sd, " + joinTableName
				+ " tb where sd." + Util.firstCharLower(joinTableName) + "." + propertyName + " = tb." + propertyName
				+ " and tb." + propertyName + " = " + value + " and sd.deleted = 'N'");
		List<SchoolScreeningDetailDTO> lstScreening = new ArrayList<SchoolScreeningDetailDTO>();
		try
		{
			Mapper dozerMapper = HibernateUtil.getDozerMapper();
			Calendar cal = Calendar.getInstance();
			for (Object entity : filter.list())
			{
				SchoolScreeningDetailDTO dtoObject = (SchoolScreeningDetailDTO) dozerMapper.map(entity,
						SchoolScreeningDetailDTO.class);
				cal.setTimeInMillis(Long.parseLong(dtoObject.getScreeningDate()));
				dtoObject.set("name", DateFormat.getDateInstance(DateFormat.SHORT).format(cal.getTime()));
				lstScreening.add(dtoObject);
			}

		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error retrieving objects: " + ex);
			throw ex;
		} finally
		{
			session.close();
		}
		return lstScreening;
	}

	public static List<ClinicPatientDetailDTO> getClinicPatientDetail(Long scrId)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<ClinicPatientDetailDTO> lstClinicPatDetails = new ArrayList<ClinicPatientDetailDTO>();

		Criteria filter = session.createCriteria(ClinicPatientDetail.class);
		filter.add(Restrictions.eq("clinic.id", scrId)).add(Restrictions.eq("deleted", "N"));
		try
		{
			Mapper dozerMapper = HibernateUtil.getDozerMapper();
			@SuppressWarnings("unchecked")
			List<ClinicPatientDetail> screeningDetail = filter.list();
			if (screeningDetail != null)
			{
				for (ClinicPatientDetail clinicPatientDetail : screeningDetail)
				{
					lstClinicPatDetails.add(dozerMapper.map(clinicPatientDetail, ClinicPatientDetailDTO.class));
				}
			}

		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error retrieving objects: " + ex);
			throw ex;
		} finally
		{
			session.close();
		}
		return lstClinicPatDetails;
	}

	public static SchoolScreeningDetailDTO getScreeningDetail(long scrId)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		SchoolScreeningDetailDTO dtoObject = null;

		Criteria filter = session.createCriteria(SchoolScreeningDetail.class);
		filter.add(Restrictions.eq("id", scrId)).add(Restrictions.eq("deleted", "N"));
		try
		{
			Mapper dozerMapper = HibernateUtil.getDozerMapper();
			SchoolScreeningDetail screeningDetail = (SchoolScreeningDetail) filter.uniqueResult();
			if (screeningDetail != null)
			{
				screeningDetail.getDoctors();
				screeningDetail.getVolunteers();
				screeningDetail.getPatientDetails();
				dtoObject = (SchoolScreeningDetailDTO) dozerMapper.map(screeningDetail, SchoolScreeningDetailDTO.class);
			}

		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error retrieving objects: " + ex);
			throw ex;
		} finally
		{
			session.close();
		}
		return dtoObject;
	}

	public static void saveUserDetail(ModelData model)
	{
		try
		{
			List<ModelData> modelList = model.get("users");
			for (ModelData modelData : modelList)
			{
				saveUser(modelData);
			}
		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error retrieving objects: " + ex);
			throw ex;
		}
	}

	public static void saveUser(ModelData modelData)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Mapper dozerMapper = HibernateUtil.getDozerMapper();
		Transaction trans = session.beginTransaction();
		try
		{
			User usrObj = dozerMapper.map(modelData, User.class);
			for (UserChapterPermissions chapters : usrObj.getChapterPermissions())
			{
				if (chapters.getId() == 0)
					session.save(chapters);
				else
					session.saveOrUpdate(chapters);
			}
			for (UserProjectPermissions chapters : usrObj.getProjectPermissions())
			{
				if (chapters.getId() == 0)
					session.save(chapters);
				else
					session.saveOrUpdate(chapters);
			}
			for (UserClinicPermissions chapters : usrObj.getClinicPermissions())
			{
				if (chapters.getId() == 0)
					session.save(chapters);
				else
					session.saveOrUpdate(chapters);
			}
			for (UserEntityPermissions chapters : usrObj.getEntityPermissions())
			{
				if (chapters.getId() == 0)
					session.save(chapters);
				else
					session.saveOrUpdate(chapters);
			}
			for (UserReportPermissions chapters : usrObj.getReportPermissions())
			{
				if (chapters.getId() == 0)
					session.save(chapters);
				else
					session.saveOrUpdate(chapters);
			}
			session.flush();
			Long id = modelData.get("id");
			if (id == null)
			{
				session.save(usrObj);
			} else
			{
				usrObj.setId(id);
				session.saveOrUpdate(usrObj);
			}
			trans.commit();
			session.flush();
		} catch (HibernateException ex)
		{
			trans.rollback();
			LOGGER.error("Encountered error retrieving objects: " + ex);
			throw ex;
		} finally
		{
			session.close();
		}
	}

	public static List<ClinicDTO> getClinics(String joinTableName, String propertyName, String value)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query filter = session.createQuery("select sd from Clinic sd, " + joinTableName + " tb where sd."
				+ Util.firstCharLower(joinTableName) + "." + propertyName + " = tb." + propertyName + " and tb."
				+ propertyName + " = " + value);
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
			LOGGER.error("Encountered error retrieving objects: " + ex);
			throw ex;
		} finally
		{
			session.close();
		}
		return lstClinics;
	}

	public static Object executeQuery(String string)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query filter = session.createSQLQuery(string);
		Object obj;
		try
		{
			obj = filter.list();
		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error retrieving objects for string : " + string + ". Additional details: " + ex);
			throw ex;
		} finally
		{
			session.close();
		}
		return obj;
	}

	public static boolean executeUpdate(String queryString)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query filter = session.createSQLQuery(queryString);
		Transaction tran = session.beginTransaction();
		int updCount;
		try
		{
			updCount = filter.executeUpdate();
			tran.commit();
		} catch (HibernateException ex)
		{
			tran.rollback();
			LOGGER.error("Encountered error while updating objects: " + ex);
			throw ex;
		} finally
		{
			session.close();
		}
		return updCount >= 0;
	}

	protected <E> E findParent(List<E> lst, E searchSeed)
	{
		int cntIndex = lst.indexOf(searchSeed);
		if (cntIndex < 0)
			return null;
		return lst.get(cntIndex);
	}

}
