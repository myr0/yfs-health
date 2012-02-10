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
import com.varun.yfs.client.common.RpcStatusEnum;
import com.varun.yfs.server.common.HibernateUtil;
import com.varun.yfs.server.models.City;
import com.varun.yfs.server.models.State;

public class CityData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(CityData.class);

	public ModelData getModel()
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

	public RpcStatusEnum saveModel(ModelData model)
	{
		RpcStatusEnum status = RpcStatusEnum.FAILURE;
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

			status = RpcStatusEnum.SUCCESS;
		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error saving the model." + ex.getMessage());
			status = RpcStatusEnum.FAILURE;
		}
		return status;
	}

}
