package com.varun.yfs.server.admin.rpc;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.util.Util;
import com.varun.yfs.server.common.HibernateUtil;
import com.varun.yfs.server.common.data.DataUtil;
import com.varun.yfs.server.models.ChapterName;
import com.varun.yfs.server.models.City;
import com.varun.yfs.server.models.Clinic;
import com.varun.yfs.server.models.Country;
import com.varun.yfs.server.models.Locality;
import com.varun.yfs.server.models.Project;
import com.varun.yfs.server.models.State;
import com.varun.yfs.server.models.Town;
import com.varun.yfs.server.models.Village;

public enum ModelDataEnum
{
	City
	{
		@Override
		public ModelData getStoreContents()
		{
			ModelData modelData = new BaseModelData();

			List<ModelData> list = DataUtil.<ModelData> getModelList("City");
			modelData.set("data", list);
			modelData.set("parentStoreState", DataUtil.<ModelData> getModelList("State"));

			modelData.set("configIds", Arrays.asList("cityName", "stateName"));
			modelData.set("configCols", Arrays.asList("City", "State"));
			modelData.set("configType", Arrays.asList("Text", "combo"));

			return modelData;
		}

		@Override
		public String saveModel(ModelData model)
		{
			String status = "Failed";
			try
			{
				List<State> lstStates = DataUtil.<State> getRawList("State");
				List<ModelData> lstModels = model.get("data");

				Session session = HibernateUtil.getSessionFactory().openSession();
				Mapper dozerMapper = HibernateUtil.getDozerMapper();

				Transaction transact = session.beginTransaction();

				for (ModelData modelData : lstModels)
				{
					City hibObject = dozerMapper.map(modelData, City.class);
					String stateName = modelData.get("stateName").toString();

					hibObject.setState(findParent(lstStates, new State(stateName)));

					if (hibObject.getId() <= 0)
					{
						hibObject.setName(modelData.get("cityName").toString());
						session.save(hibObject);

					} else
					{
						session.saveOrUpdate(hibObject);
					}
				}

				transact.commit();

				session.flush();
				session.close();

				status = "Success";
			} catch (HibernateException ex)
			{
				LOGGER.error("Encountered error saving the model." + ex.getMessage());
			}
			return status;
		}

		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("City");
		}

	},

	Country
	{
		@Override
		public ModelData getStoreContents()
		{
			ModelData modelData = new BaseModelData();

			List<ModelData> list = DataUtil.<ModelData> getModelList("Country");
			modelData.set("data", list);

			modelData.set("configIds", Arrays.asList("countryName"));
			modelData.set("configCols", Arrays.asList("Country"));
			modelData.set("configType", Arrays.asList("Text"));
			return modelData;
		}

		@Override
		public String saveModel(ModelData model)
		{
			String status = "Failed";
			try
			{
				List<ModelData> lstModels = model.get("data");

				Session session = HibernateUtil.getSessionFactory().openSession();
				Mapper dozerMapper = HibernateUtil.getDozerMapper();

				Transaction transact = session.beginTransaction();

				for (ModelData modelData : lstModels)
				{
					Country hibObject = dozerMapper.map(modelData, Country.class);
					if (hibObject.getId() <= 0)
					{
						hibObject.setName(modelData.get("countryName").toString());
						session.save(hibObject);
					} else
					{
						session.saveOrUpdate(hibObject);
					}
				}
				transact.commit();
				session.close();
				status = "Success";
			} catch (HibernateException ex)
			{
				LOGGER.error("Encountered error saving the model." + ex.getMessage());
			}
			return status;
		}

		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("Country");
		}
	},

	Locality
	{
		@Override
		public ModelData getStoreContents()
		{
			ModelData modelData = new BaseModelData();

			List<ModelData> list = DataUtil.<ModelData> getModelList("Locality");
			modelData.set("data", list);
			modelData.set("parentStoreCity", DataUtil.<ModelData> getModelList("City"));

			modelData.set("configIds", Arrays.asList("localityName", "cityName"));
			modelData.set("configCols", Arrays.asList("Locality", "City"));
			modelData.set("configType", Arrays.asList("Text", "combo"));
			return modelData;
		}

		@Override
		public String saveModel(ModelData model)
		{
			String status = "Failed";
			try
			{
				List<City> lstCities = DataUtil.<City> getRawList("City");
				List<ModelData> lstModels = model.get("data");

				Session session = HibernateUtil.getSessionFactory().openSession();
				Mapper dozerMapper = HibernateUtil.getDozerMapper();

				Transaction transact = session.beginTransaction();

				for (ModelData modelData : lstModels)
				{
					Locality hibObject = dozerMapper.map(modelData, Locality.class);

					String cityName = modelData.get("cityName").toString();
					hibObject.setCity(findParent(lstCities, new City(cityName)));

					if (hibObject.getId() <= 0) // new state - find the parent
					{
						hibObject.setName(modelData.get("localityName").toString());
						session.save(hibObject);
					} else
					{
						session.saveOrUpdate(hibObject);
					}
				}
				transact.commit();
				session.flush();
				session.close();
				status = "Success";
			} catch (HibernateException ex)
			{
				LOGGER.error("Encountered error saving the model." + ex.getMessage());
			}
			return status;
		}

		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("Locality");
		}
	},

	State
	{
		@Override
		public ModelData getStoreContents()
		{
			ModelData modelData = new BaseModelData();

			List<ModelData> list = DataUtil.<ModelData> getModelList("State");
			modelData.set("data", list);
			modelData.set("parentStoreCountry", DataUtil.<ModelData> getModelList("Country"));

			modelData.set("configIds", Arrays.asList("stateName", "countryName"));
			modelData.set("configCols", Arrays.asList("State", "Country"));
			modelData.set("configType", Arrays.asList("Text", "combo"));
			return modelData;
		}

		@Override
		public String saveModel(ModelData model)
		{
			String status = "Failed";
			try
			{
				List<ModelData> lstModels = model.get("data");
				Session session = HibernateUtil.getSessionFactory().openSession();
				Mapper dozerMapper = HibernateUtil.getDozerMapper();

				Transaction transact = session.beginTransaction();

				List<Country> lstCountry = DataUtil.<Country> getRawList("Country");
				for (ModelData modelData : lstModels)
				{
					State hibObject = dozerMapper.map(modelData, State.class);
					String countryName = modelData.get("countryName").toString();

					hibObject.setCountry(findParent(lstCountry, new Country(countryName)));
					if (hibObject.getId() <= 0)
					{
						hibObject.setName(modelData.get("stateName").toString());
						session.save(hibObject);
					} else
					{
						session.saveOrUpdate(hibObject);
					}
				}
				transact.commit();
				session.flush();
				session.close();
				status = "Success";
			} catch (HibernateException ex)
			{
				LOGGER.error("Encountered error saving the model." + ex.getMessage());
			}
			return status;
		}

		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("State");
		}
	},
	Town
	{
		@Override
		public ModelData getStoreContents()
		{
			ModelData modelData = new BaseModelData();

			List<ModelData> list = DataUtil.<ModelData> getModelList("Town");
			modelData.set("data", list);
			modelData.set("parentStoreState", DataUtil.<ModelData> getModelList("State"));

			modelData.set("configIds", Arrays.asList("townName", "stateName"));
			modelData.set("configCols", Arrays.asList("Town", "State"));
			modelData.set("configType", Arrays.asList("Text", "combo"));
			return modelData;
		}

		@Override
		public String saveModel(ModelData model)
		{
			String status = "Failed";
			try
			{
				List<State> lstStates = DataUtil.<State> getRawList("State");
				List<ModelData> lstModels = model.get("data");

				Session session = HibernateUtil.getSessionFactory().openSession();
				Mapper dozerMapper = HibernateUtil.getDozerMapper();

				Transaction transact = session.beginTransaction();

				for (ModelData modelData : lstModels)
				{
					Town hibObject = dozerMapper.map(modelData, Town.class);

					String stateName = modelData.get("stateName").toString();
					hibObject.setState(findParent(lstStates, new State(stateName)));

					if (hibObject.getId() <= 0) // new state - find the parent
					{
						hibObject.setName(modelData.get("townName").toString());
						session.save(hibObject);
					} else
					{
						session.saveOrUpdate(hibObject);
					}
				}
				transact.commit();
				session.flush();
				session.close();
				status = "Success";
			} catch (HibernateException ex)
			{
				LOGGER.error("Encountered error saving the model." + ex.getMessage());
			}
			return status;
		}

		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("Town");
		}
	},

	Village
	{
		@Override
		public ModelData getStoreContents()
		{
			ModelData modelData = new BaseModelData();

			List<ModelData> list = DataUtil.<ModelData> getModelList("Village");
			modelData.set("data", list);
			modelData.set("parentStoreState", DataUtil.<ModelData> getModelList("State"));

			modelData.set("configIds", Arrays.asList("villageName", "stateName"));
			modelData.set("configCols", Arrays.asList("Village", "State"));
			modelData.set("configType", Arrays.asList("Text", "combo"));
			return modelData;
		}

		@Override
		public String saveModel(ModelData model)
		{
			String status = "Failed";
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction transact = session.beginTransaction();
			try
			{
				List<State> lstStates = DataUtil.<State> getRawList("State");
				List<ModelData> lstModels = model.get("data");

				Mapper dozerMapper = HibernateUtil.getDozerMapper();

				for (ModelData modelData : lstModels)
				{
					Village hibObject = dozerMapper.map(modelData, Village.class);

					String stateName = modelData.get("stateName").toString();
					hibObject.setState(findParent(lstStates, new State(stateName)));

					if (hibObject.getId() <= 0) // new state - find the parent
					{
						hibObject.setName(modelData.get("villageName").toString());
						session.save(hibObject);
					} else
					{
						session.saveOrUpdate(hibObject);
					}
				}
				transact.commit();
				session.flush();
				session.close();
				status = "Success";
			} catch (HibernateException ex)
			{
				if (session != null)
				{
					transact.rollback();
					session.close();
				}
				LOGGER.error("Encountered error saving the model." + ex.getMessage());
			}
			return status;
		}

		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("Village");
		}
	},
	ChapterName
	{

		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("ChapterName");
		}

		@Override
		public ModelData getStoreContents()
		{
			ModelData modelData = new BaseModelData();

			List<ModelData> list = DataUtil.<ModelData> getModelList("ChapterName");
			modelData.set("data", list);
			modelData.set("parentStoreCountry", DataUtil.<ModelData> getModelList("Country"));
			modelData.set("parentStoreState", DataUtil.<ModelData> getModelList("State"));
			modelData.set("parentStoreVillage", DataUtil.<ModelData> getModelList("Village"));
			modelData.set("parentStoreTown", DataUtil.<ModelData> getModelList("Town"));
			modelData.set("parentStoreCity", DataUtil.<ModelData> getModelList("City"));
			modelData.set("parentStoreLocality", DataUtil.<ModelData> getModelList("Locality"));

			modelData.set("configIds", Arrays.asList("chapterName", "countryName", "stateName", "villageName", "townName", "cityName", "localityName"));
			modelData.set("configCols", Arrays.asList("Chapter Name", "Country", "State", "Village", "Town", "City", "Locality"));
			modelData.set("configType", Arrays.asList("Text", "combo", "combo", "combo", "combo", "combo", "combo"));
			return modelData;
		}

		@Override
		public String saveModel(ModelData model)
		{
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction transact = session.beginTransaction();
			String status = "Failed";
			try
			{
				List<Country> lstCountry = DataUtil.<Country> getRawList("Country");
				List<State> lstState = DataUtil.<State> getRawList("State");
				List<City> lstCity = DataUtil.<City> getRawList("City");
				List<Town> lstTown = DataUtil.<Town> getRawList("Town");
				List<Village> lstVillage = DataUtil.<Village> getRawList("Village");
				List<Locality> lstLocality = DataUtil.<Locality> getRawList("Locality");

				List<ModelData> lstModels = model.get("data");

				Mapper dozerMapper = HibernateUtil.getDozerMapper();

				for (ModelData modelData : lstModels)
				{
					ChapterName hibObject = dozerMapper.map(modelData, ChapterName.class);
					String countryName = Util.safeToString(modelData.get("countryName"));
					String stateName = Util.safeToString(modelData.get("stateName"));
					String cityName = Util.safeToString(modelData.get("cityName"));
					String townName = Util.safeToString(modelData.get("townName"));
					String villageName = Util.safeToString(modelData.get("villageName"));
					String localityName = Util.safeToString(modelData.get("localityName"));

					hibObject.setCountry(findParent(lstCountry, new Country(countryName)));
					hibObject.setState(findParent(lstState, new State(stateName)));
					hibObject.setCity(findParent(lstCity, new City(cityName)));
					hibObject.setTown(findParent(lstTown, new Town(townName)));
					hibObject.setVillage(findParent(lstVillage, new Village(villageName)));
					hibObject.setLocality(findParent(lstLocality, new Locality(localityName)));

					if (hibObject.getId() <= 0) // new chaptername object - find
					{
						hibObject.setName(modelData.get("chapterName").toString());
						session.save(hibObject);
					} else
					{
						session.saveOrUpdate(hibObject);
					}
				}
				transact.commit();
				session.flush();
				session.close();
				status = "Success";
			} catch (HibernateException ex)
			{
				LOGGER.error("Encountered error saving the model." + ex.getMessage());
				if (session != null)
				{
					transact.rollback();
					session.close();
				}
			}
			return status;
		}

	},
	Project
	{

		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("Project");
		}

		@Override
		public ModelData getStoreContents()
		{
			ModelData modelData = new BaseModelData();

			List<ModelData> list = DataUtil.<ModelData> getModelList("Project");
			modelData.set("data", list);
			modelData.set("parentStoreChapter", DataUtil.<ModelData> getModelList("ChapterName"));

			modelData.set("configIds", Arrays.asList("projectName", "chapterName"));
			modelData.set("configCols", Arrays.asList("Project Name", "Chapter"));
			modelData.set("configType", Arrays.asList("Text", "combo"));
			return modelData;
		}

		@Override
		public String saveModel(ModelData model)
		{
			String status = "Failed";
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction transact = session.beginTransaction();
			try
			{
				List<ChapterName> lstChapterName = DataUtil.<ChapterName> getRawList("ChapterName");
				List<ModelData> lstModels = model.get("data");

				Mapper dozerMapper = HibernateUtil.getDozerMapper();

				for (ModelData modelData : lstModels)
				{
					Project hibObject = dozerMapper.map(modelData, Project.class);
					String chapterName = Util.safeToString(modelData.get("chapterName"));

					hibObject.setChapterName(findParent(lstChapterName, new ChapterName(chapterName)));

					if (hibObject.getId() <= 0) // new project object - find
					{
						hibObject.setName(modelData.get("projectName").toString());
						session.save(hibObject);
					} else
					{
						hibObject.setName(modelData.get("projectName").toString());
						session.saveOrUpdate(hibObject);
					}
				}
				transact.commit();
				session.flush();
				session.close();
				status = "Success";
			} catch (HibernateException ex)
			{
				LOGGER.error("Encountered error saving the model." + ex.getMessage());
				if (session != null)
				{
					transact.rollback();
					session.close();
				}
			}
			return status;
		}

	},

	Clinic
	{
		@Override
		public ModelData getStoreContents()
		{
			ModelData modelData = new BaseModelData();

			List<ModelData> list = DataUtil.<ModelData> getModelList("Clinic");
			modelData.set("data", list);
			modelData.set("parentStoreCity", DataUtil.<ModelData> getModelList("City"));

			modelData.set("configIds", Arrays.asList("clinicName", "cityName"));
			modelData.set("configCols", Arrays.asList("Clinic Name", "City"));
			modelData.set("configType", Arrays.asList("Text", "combo"));
			return modelData;
		}

		@Override
		public String saveModel(ModelData model)
		{
			String status = "Failed";
			try
			{
				List<City> lstCities = DataUtil.<City> getRawList("City");
				List<ModelData> lstModels = model.get("data");

				Session session = HibernateUtil.getSessionFactory().openSession();
				Mapper dozerMapper = HibernateUtil.getDozerMapper();

				Transaction transact = session.beginTransaction();

				for (ModelData modelData : lstModels)
				{
					Clinic hibObject = dozerMapper.map(modelData, Clinic.class);

					String cityName = modelData.get("cityName").toString();
					hibObject.setCity(findParent(lstCities, new City(cityName)));

					if (hibObject.getId() <= 0) // new state - find the parent
					{
						hibObject.setName(modelData.get("clinicName").toString());
						session.save(hibObject);
					} else
					{
						session.saveOrUpdate(hibObject);
					}
				}
				transact.commit();
				session.flush();
				session.close();
				status = "Success";
			} catch (HibernateException ex)
			{
				LOGGER.error("Encountered error saving the model." + ex.getMessage());
			}
			return status;
		}

		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("Clinic");
		}
	},
	Users
	{

		@Override
		public ModelData getStoreContents()
		{
			ModelData modelData = new BaseModelData();
			List<ModelData> modelList = DataUtil.<ModelData> getModelList("User");
			List<ModelData> lstChapterNames = DataUtil.<ModelData> getModelList("ChapterName");
			List<ModelData> lstProjects = DataUtil.<ModelData> getModelList("Project");

			modelData.set("users", modelList);
			modelData.set("lstChapterNames", lstChapterNames);
			modelData.set("lstProjects", lstProjects);
			return modelData;
		}

		@Override
		public String saveModel(ModelData model)
		{
			String status = "Failed";
			try
			{
				DataUtil.saveUserDetail(model);
				status = "Success";
			} catch (HibernateException ex)
			{
				ex.printStackTrace();
			}
			return status;
		}

		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("User");
		}
	};

	public static boolean isEnumElement(String entityName)
	{
		for (ModelDataEnum entity : values())
		{
			if (entity.name().equalsIgnoreCase(entityName))
				return true;
		}
		return false;
	}

	protected <E> E findParent(List<E> lst, E searchSeed)
	{
		int cntIndex = lst.indexOf(searchSeed);
		if (cntIndex < 0)
			return null;
		return lst.get(cntIndex);
	}

	// private static void updateParent(List<City> lstCities, Locality
	// hibObject, String cityName)
	// {
	// int cntIndex = lstCities.indexOf(new City(cityName));
	// hibObject.setCity(lstCities.get(cntIndex));
	// }
	//
	// private static void updateParent(List<Country> lstCountry, State
	// hibObject, String countryName)
	// {
	// int cntIndex = lstCountry.indexOf(new Country(countryName));
	// hibObject.setCountry(lstCountry.get(cntIndex));
	// }
	//
	// private static void updateParent(List<State> lstStates, City hibObject,
	// String stateName)
	// {
	// int cntIndex = lstStates.indexOf(new State(stateName));
	// hibObject.setState(lstStates.get(cntIndex));
	// }
	//
	// private static void updateParent(List<State> lstStates, Town hibObject,
	// String stateName)
	// {
	// int cntIndex = lstStates.indexOf(new State(stateName));
	// hibObject.setState(lstStates.get(cntIndex));
	// }

	// private static void updateParent(List<State> lstStates, Village
	// hibObject, String stateName)
	// {
	// int cntIndex = lstStates.indexOf(new State(stateName));
	// hibObject.setState(lstStates.get(cntIndex));
	// }

	abstract public List<ModelData> getListStoreContents();

	abstract public ModelData getStoreContents();

	abstract public String saveModel(ModelData model);

	private static final Logger LOGGER = Logger.getLogger(ModelDataEnum.class);

}
