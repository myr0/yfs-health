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
import com.varun.yfs.client.index.ModelDataEnum;
import com.varun.yfs.dto.UserDTO;
import com.varun.yfs.server.common.HibernateUtil;
import com.varun.yfs.server.models.State;
import com.varun.yfs.server.models.Village;

public class VillageData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(VillageData.class);

	@Override
	public ModelData getModel(UserDTO userDto)
	{
		ModelData modelData = new BaseModelData();

		List<ModelData> list = DataUtil.<ModelData> getModelList(ModelDataEnum.Village.name());
		modelData.set("data", list);
		modelData.set("parentStoreState", DataUtil.<ModelData> getModelList(ModelDataEnum.State.name()));

		modelData.set("configIds", Arrays.asList("villageName", "stateName"));
		modelData.set("configCols", Arrays.asList("Village", "State"));
		modelData.set("configType", Arrays.asList("Text", "combo"));

		modelData.set("permissions", userDto.getEntityPermissionsMap().get(ModelDataEnum.Village.name().toLowerCase()));
		return modelData;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		RpcStatusEnum status = RpcStatusEnum.FAILURE;
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
			status = RpcStatusEnum.SUCCESS;
		} catch (HibernateException ex)
		{
			if (session != null)
			{
				transact.rollback();
				session.close();
			}
			LOGGER.error("Encountered error saving the model." + ex.getMessage());
			status = RpcStatusEnum.FAILURE;
		}
		return status;
	}
}
