package com.varun.yfs.server.models.data;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.Mapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.client.index.ModelDataEnum;
import com.varun.yfs.dto.UserDTO;
import com.varun.yfs.server.models.City;
import com.varun.yfs.server.models.Locality;

public class LocalityData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(LocalityData.class);

	@Override
	public ModelData getModel(UserDTO userDto)
	{
		LOGGER.debug("Attempting data load");
		
		ModelData modelData = new BaseModelData();

		List<ModelData> list = DataUtil.<ModelData> getModelList(ModelDataEnum.Locality.name());
		modelData.set("data", list);
		modelData.set("parentStoreCity", DataUtil.<ModelData> getModelList(ModelDataEnum.City.name()));

		modelData.set("configIds", Arrays.asList("localityName", "cityName"));
		modelData.set("configCols", Arrays.asList("Locality", "City"));
		modelData.set("configType", Arrays.asList("Text", "combo"));

		modelData.set("permissions", userDto.getEntityPermissionsMap().get(ModelDataEnum.Locality.name().toLowerCase()));
		
		LOGGER.debug("Data load complete.");
		return modelData;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		LOGGER.debug("Attempting to save model");
		
		RpcStatusEnum status = RpcStatusEnum.FAILURE;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Mapper dozerMapper = HibernateUtil.getDozerMapper();
		Transaction transact = session.beginTransaction();
		try
		{
			List<City> lstCities = DataUtil.<City> getRawList("City");
			List<ModelData> lstModels = model.get("data");

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
			status = RpcStatusEnum.SUCCESS;
			
			LOGGER.debug("Save model completed successfully.");
		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error saving the model." + ex.getMessage());
			status = RpcStatusEnum.FAILURE;
			if (session != null)
			{
				transact.rollback();
				session.close();
			}
		}
		return status;
	}
}
