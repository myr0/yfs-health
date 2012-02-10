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
import com.varun.yfs.server.models.State;
import com.varun.yfs.server.models.Town;

public class TownData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(TownData.class);
	
	public ModelData getModel()
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

	public List<ModelData> getModelList()
	{
		return DataUtil.getModelList("Town");
	}
}
