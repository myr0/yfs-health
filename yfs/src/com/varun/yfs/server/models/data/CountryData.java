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
import com.varun.yfs.server.models.Country;

public class CountryData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(CountryData.class);

	@Override
	public ModelData getModel(UserDTO userDto)
	{
		ModelData modelData = new BaseModelData();

		List<ModelData> list = DataUtil.<ModelData> getModelList(ModelDataEnum.Country.name());
		modelData.set("data", list);

		modelData.set("configIds", Arrays.asList("countryName"));
		modelData.set("configCols", Arrays.asList("Country"));
		modelData.set("configType", Arrays.asList("Text"));
		
		modelData.set("permissions", userDto.getEntityPermissionsMap().get(ModelDataEnum.Country.name().toLowerCase()));
		return modelData;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		RpcStatusEnum status = RpcStatusEnum.FAILURE;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Mapper dozerMapper = HibernateUtil.getDozerMapper();
		Transaction transact = session.beginTransaction();
		try
		{
			List<ModelData> lstModels = model.get("data");

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
			status = RpcStatusEnum.SUCCESS;
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
