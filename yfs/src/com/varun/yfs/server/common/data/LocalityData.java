package com.varun.yfs.server.common.data;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.server.admin.rpc.ModelDataEnum;
import com.varun.yfs.server.common.HibernateUtil;
import com.varun.yfs.server.models.City;
import com.varun.yfs.server.models.Locality;

public class LocalityData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(LocalityData.class);
	
	public ModelData getModel()
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

	public List<ModelData> getModelList()
	{
		return DataUtil.getModelList("Locality");
	}
}
