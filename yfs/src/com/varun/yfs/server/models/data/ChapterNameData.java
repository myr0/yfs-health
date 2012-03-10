package com.varun.yfs.server.models.data;

import java.util.ArrayList;
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
import com.varun.yfs.dto.ChapterNameDTO;
import com.varun.yfs.dto.UserChapterPermissionsDTO;
import com.varun.yfs.dto.UserDTO;
import com.varun.yfs.dto.YesNoDTO;
import com.varun.yfs.server.models.ChapterName;

public class ChapterNameData extends AbstractData
{
	private static final Logger LOGGER = Logger.getLogger(ChapterNameData.class);

	@Override
	public ModelData getModel(UserDTO userDto)
	{
		ModelData modelData = new BaseModelData();

		List<ModelData> list = DataUtil.<ModelData> getModelList(ModelDataEnum.ChapterName.name());
		modelData.set("data", applyPermission(userDto, list));
		// modelData.set("parentStoreCountry", DataUtil.<ModelData>
		// getModelList("Country"));
		// modelData.set("parentStoreState", DataUtil.<ModelData>
		// getModelList("State"));
		// modelData.set("parentStoreVillage", DataUtil.<ModelData>
		// getModelList("Village"));
		// modelData.set("parentStoreTown", DataUtil.<ModelData>
		// getModelList("Town"));
		// modelData.set("parentStoreCity", DataUtil.<ModelData>
		// getModelList("City"));
		// modelData.set("parentStoreLocality", DataUtil.<ModelData>
		// getModelList("Locality"));

		modelData.set("configIds", Arrays.asList("chapterName"));
		// , "countryName", "stateName", "villageName", "townName", "cityName",
		// "localityName"));
		modelData.set("configCols", Arrays.asList("Chapter Name"));
		// Arrays.asList("Chapter Name", "Country", "State", "Village", "Town",
		// "City", "Locality"));
		modelData.set("configType", Arrays.asList("Text"));
		// , "combo", "combo", "combo", "combo", "combo", "combo"));

		modelData.set("permissions",
				userDto.getEntityPermissionsMap().get(ModelDataEnum.ChapterName.name().toLowerCase()));

		return modelData;
	}

	@Override
	public RpcStatusEnum saveModel(ModelData model)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transact = session.beginTransaction();
		RpcStatusEnum status = RpcStatusEnum.FAILURE;
		try
		{
			// List<Country> lstCountry = DataUtil.<Country>
			// getRawList("Country");
			// List<State> lstState = DataUtil.<State> getRawList("State");
			// List<City> lstCity = DataUtil.<City> getRawList("City");
			// List<Town> lstTown = DataUtil.<Town> getRawList("Town");
			// List<Village> lstVillage = DataUtil.<Village>
			// getRawList("Village");
			// List<Locality> lstLocality = DataUtil.<Locality>
			// getRawList("Locality");

			List<ModelData> lstModels = model.get("data");

			Mapper dozerMapper = HibernateUtil.getDozerMapper();

			for (ModelData modelData : lstModels)
			{
				ChapterName hibObject = dozerMapper.map(modelData, ChapterName.class);
				// String countryName =
				// Util.safeToString(modelData.get("countryName"));
				// String stateName =
				// Util.safeToString(modelData.get("stateName"));
				// String cityName =
				// Util.safeToString(modelData.get("cityName"));
				// String townName =
				// Util.safeToString(modelData.get("townName"));
				// String villageName =
				// Util.safeToString(modelData.get("villageName"));
				// String localityName =
				// Util.safeToString(modelData.get("localityName"));

				// hibObject.setCountry(findParent(lstCountry, new
				// Country(countryName)));
				// hibObject.setState(findParent(lstState, new
				// State(stateName)));
				// hibObject.setCity(findParent(lstCity, new City(cityName)));
				// hibObject.setTown(findParent(lstTown, new Town(townName)));
				// hibObject.setVillage(findParent(lstVillage, new
				// Village(villageName)));
				// hibObject.setLocality(findParent(lstLocality, new
				// Locality(localityName)));

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
			status = RpcStatusEnum.SUCCESS;
		} catch (HibernateException ex)
		{
			LOGGER.error("Encountered error saving the model." + ex.getMessage());
			if (session != null)
			{
				transact.rollback();
				session.close();
			}
			status = RpcStatusEnum.FAILURE;
		}
		return status;
	}

	protected List<ModelData> applyPermission(UserDTO userDto, List<ModelData> modelList)
	{
		List<ModelData> lstModels = new ArrayList<ModelData>();
		if (!userDto.isAdmin())
		{
			List<UserChapterPermissionsDTO> lstChapterPermissions = userDto.getChapterPermissions();
			for (UserChapterPermissionsDTO userChapterPermissionsDTO : lstChapterPermissions)
			{
				if (userChapterPermissionsDTO.getRead().equalsIgnoreCase(YesNoDTO.YES.toString()))
				{
					String entityName = userChapterPermissionsDTO.getChapterName();

					ChapterNameDTO tempModel = new ChapterNameDTO();
					tempModel.set("chapterName", entityName);

					int idx = modelList.indexOf(tempModel);
					if (idx >= 0)
						lstModels.add(modelList.get(idx));
				}
			}
		} else
			lstModels = modelList;
		return lstModels;
	}

}
