package com.varun.yfs.server.admin.rpc;

import java.util.Arrays;
import java.util.List;

import org.dozer.Mapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.dto.Util;
import com.varun.yfs.server.common.HibernateUtil;
import com.varun.yfs.server.common.data.DataUtil;
import com.varun.yfs.server.models.City;
import com.varun.yfs.server.models.Country;
import com.varun.yfs.server.models.Locality;
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
			modelData.set("parentStore", DataUtil.<ModelData> getModelList("State"));

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

					if (hibObject.getId() <= 0)
					{
						updateParent(lstStates, hibObject, stateName);

						hibObject.setName(modelData.get("cityName").toString());
						session.save(hibObject);

					} else
					{
						updateParent(lstStates, hibObject, stateName);

						session.saveOrUpdate(hibObject);
					}
				}

				transact.commit();

				session.flush();
				session.close();

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
				ex.printStackTrace();
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
			modelData.set("parentStore", DataUtil.<ModelData> getModelList("City"));

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
					if (hibObject.getId() <= 0) // new state - find the parent
					{
						updateParent(lstCities, hibObject, cityName);

						hibObject.setName(modelData.get("localityName").toString());
						session.save(hibObject);
					} else
					{
						updateParent(lstCities, hibObject, cityName);
						session.saveOrUpdate(hibObject);
					}
				}
				transact.commit();
				session.flush();
				session.close();
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
			modelData.set("parentStore", DataUtil.<ModelData> getModelList("Country"));

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

					if (hibObject.getId() <= 0)
					{
						updateParent(lstCountry, hibObject, countryName);

						hibObject.setName(modelData.get("stateName").toString());
						session.save(hibObject);
					} else
					{
						updateParent(lstCountry, hibObject, countryName);

						session.saveOrUpdate(hibObject);
					}
				}
				transact.commit();
				session.flush();
				session.close();
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
			modelData.set("parentStore", DataUtil.<ModelData> getModelList("State"));

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

					if (hibObject.getId() <= 0) // new state - find the parent
					{
						updateParent(lstStates, hibObject, stateName);

						hibObject.setName(modelData.get("townName").toString());
						session.save(hibObject);
					} else
					{
						updateParent(lstStates, hibObject, stateName);
						session.saveOrUpdate(hibObject);
					}
				}
				transact.commit();
				session.flush();
				session.close();
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
			modelData.set("parentStore", DataUtil.<ModelData> getModelList("State"));

			modelData.set("configIds", Arrays.asList("villageName", "stateName"));
			modelData.set("configCols", Arrays.asList("Village", "State"));
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
					Village hibObject = dozerMapper.map(modelData, Village.class);
					String stateName = modelData.get("stateName").toString();

					if (hibObject.getId() <= 0) // new state - find the parent
					{
						updateParent(lstStates, hibObject, stateName);

						hibObject.setName(modelData.get("villageName").toString());
						session.save(hibObject);
					} else
					{
						updateParent(lstStates, hibObject, stateName);
						session.saveOrUpdate(hibObject);
					}
				}
				transact.commit();
				session.flush();
				session.close();
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
			return DataUtil.getModelList("Village");
		}
	},
	Users
	{

		@Override
		public ModelData getStoreContents()
		{
			ModelData modelData = new BaseModelData();
			List<ModelData> modelList = DataUtil.<ModelData> getModelList("Users");
			List<ModelData> lstLocalities = DataUtil.<ModelData> getModelList("Locality");
			List<ModelData> lstTown = DataUtil.<ModelData> getModelList("Town");
			List<ModelData> lstVillage = DataUtil.<ModelData> getModelList("Village");

			modelData.set("users", modelList);
			modelData.set("lstLocalities", lstLocalities);
			modelData.set("lstTown", lstTown);
			modelData.set("lstVillage", lstVillage);
			return modelData;
		}

		@Override
		public String saveModel(ModelData model)
		{
			String status = "Failed";
			try
			{
				List<ModelData> modelList = model.get("users");
				DataUtil.<ModelData> saveListStore(Util.normalize("Users"), modelList);
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
			return DataUtil.getModelList("Users");
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

	private static void updateParent(List<City> lstCities, Locality hibObject, String cityName)
	{
		int cntIndex = lstCities.indexOf(new City(cityName));
		hibObject.setCity(lstCities.get(cntIndex));
	}

	private static void updateParent(List<Country> lstCountry, State hibObject, String countryName)
	{
		int cntIndex = lstCountry.indexOf(new Country(countryName));
		hibObject.setCountry(lstCountry.get(cntIndex));
	}

	private static void updateParent(List<State> lstStates, City hibObject, String stateName)
	{
		int cntIndex = lstStates.indexOf(new State(stateName));
		hibObject.setState(lstStates.get(cntIndex));
	}

	private static void updateParent(List<State> lstStates, Town hibObject, String stateName)
	{
		int cntIndex = lstStates.indexOf(new State(stateName));
		hibObject.setState(lstStates.get(cntIndex));
	}

	private static void updateParent(List<State> lstStates, Village hibObject, String stateName)
	{
		int cntIndex = lstStates.indexOf(new State(stateName));
		hibObject.setState(lstStates.get(cntIndex));
	}

	abstract public List<ModelData> getListStoreContents();
	
	abstract public ModelData getStoreContents();

	abstract public String saveModel(ModelData model);

}
