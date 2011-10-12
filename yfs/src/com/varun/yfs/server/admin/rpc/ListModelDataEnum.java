package com.varun.yfs.server.admin.rpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.dto.ChapterNameDTO;
import com.varun.yfs.dto.ScreeningDetailDTO;
import com.varun.yfs.server.common.data.DataUtil;

public enum ListModelDataEnum
{

	Address
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			return getSampleData();
		}
	},

	ContactInformation
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			return getSampleData();
		}
	},
	Doctor
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("Doctor");
		}
	},

	PatientDetail
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			return getSampleData();
		}
	},
	ProcessType
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("ProcessType");
		}
	},

	TypeOfLocation
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("TypeOfLocation");
		}
	},

	Volunteer
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("Volunteer");
		}
	},
	ScreeningLocations
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			List<ModelData> nodes = new ArrayList<ModelData>();

			List<ChapterNameDTO> rootNodes = DataUtil.<ChapterNameDTO> getModelList("ChapterName");
			for (ChapterNameDTO chapterNameDTO : rootNodes)
			{
				ModelData rootNode = new BaseModelData();
				rootNode.set("name", chapterNameDTO.getName());
				rootNode.set("icon", "");
				nodes.add(rootNode);
				
				List<ModelData> chapterNodes = new ArrayList<ModelData>();
				rootNode.set("children",chapterNodes );

				List<ScreeningDetailDTO> lstScrDet = DataUtil.getScreeningDetail("ChapterName", "id", String.valueOf(chapterNameDTO.getId()));
				for (ScreeningDetailDTO screeningDetailDTO : lstScrDet)
				{
					ModelData scrNode = new BaseModelData();
					scrNode.set("name", screeningDetailDTO.toString());
					scrNode.set("id", String.valueOf(screeningDetailDTO.getId()));
					scrNode.set("icon", "");
					chapterNodes.add(scrNode);
				}
			}

			// List<CountryDTO> rootNodes = DataUtil.<CountryDTO>
			// getModelList("Country");
			//
			// for (CountryDTO country : rootNodes)
			// {
			// ModelData rootNode = new BaseModelData();
			// rootNode.set("name", country.getName());
			// rootNode.set("icon", "");
			// nodes.add(rootNode);
			//
			// Set<StateDTO> states = country.getStates();
			//
			// List<ModelData> countrysChild = new ArrayList<ModelData>();
			// rootNode.set("children", countrysChild);
			// for (StateDTO stateDTO : states)
			// {
			// ModelData stateNode = new BaseModelData();
			// stateNode.set("name", stateDTO.getName());
			// stateNode.set("icon", "");
			// countrysChild.add(stateNode);
			//
			// List<ModelData> statesChild = new ArrayList<ModelData>();
			// stateNode.set("children", statesChild);
			//
			// List<ScreeningDetailDTO> lstScreeningDet;
			//
			// Set<VillageDTO> villages = stateDTO.getVillages();
			// for (VillageDTO villageDTO : villages)
			// {
			// ModelData villageNode = new BaseModelData();
			// villageNode.set("name", villageDTO.getName());
			// villageNode.set("icon", "");
			// statesChild.add(villageNode);
			//
			// lstScreeningDet = new ArrayList<ScreeningDetailDTO>();
			// villageNode.set("children", lstScreeningDet);
			// List<ScreeningDetailDTO> screeningDetail =
			// DataUtil.getScreeningDetail("Village", "id",
			// String.valueOf(villageDTO.getId()));
			// lstScreeningDet.addAll(screeningDetail);
			// }
			//
			// Set<TownDTO> towns = stateDTO.getTowns();
			// for (TownDTO townDTO : towns)
			// {
			// ModelData townNode = new BaseModelData();
			// townNode.set("name", townDTO.getName());
			// townNode.set("icon", "");
			// statesChild.add(townNode);
			//
			// lstScreeningDet = new ArrayList<ScreeningDetailDTO>();
			// townNode.set("children", lstScreeningDet);
			// List<ScreeningDetailDTO> screeningDetail =
			// DataUtil.getScreeningDetail("Town","id",
			// String.valueOf(townDTO.getId()));
			// lstScreeningDet.addAll(screeningDetail);
			// }
			//
			// Set<CityDTO> cities = stateDTO.getCities();
			// for (CityDTO cityDTO : cities)
			// {
			// ModelData cityNode = new BaseModelData();
			// cityNode.set("name", cityDTO.getName());
			// cityNode.set("icon", "");
			// statesChild.add(cityNode);
			//
			// lstScreeningDet = new ArrayList<ScreeningDetailDTO>();
			// cityNode.set("children", lstScreeningDet);
			// List<ScreeningDetailDTO> screeningDetail =
			// DataUtil.getScreeningDetail("City","id",
			// String.valueOf(cityDTO.getId()));
			// lstScreeningDet.addAll(screeningDetail);
			// }
			// }
			// }
			return nodes;
		}
	},
	Reports
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			List<ModelData> arrayList = new ArrayList<ModelData>();
			ModelData m1 = newItem("Reports By Category", "");
			arrayList.add(m1);

			List<ModelData> child = new ArrayList<ModelData>();
			m1.set("children", child);
			child.add(newItem("Report 1", ""));
			child.add(newItem("Report 2", ""));

			m1 = newItem("Summary Reports", "");
			arrayList.add(m1);

			child = new ArrayList<ModelData>();
			m1.set("children", child);
			child.add(newItem("Report 1", ""));
			child.add(newItem("Report 2", ""));

			return arrayList;
		}

		private ModelData newItem(String text, String iconStyle)
		{
			ModelData m = new BaseModelData();
			m.set("name", text);
			m.set("icon", iconStyle);
			return m;
		}
	},
	Administration
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			List<ModelData> arrayList = new ArrayList<ModelData>();
			ModelData m1 = newItem("Entities", "");
			arrayList.add(m1);

			List<ModelData> child = new ArrayList<ModelData>();
			m1.set("children", child);
			child.addAll(DataUtil.<ModelData> getModelList("Entities"));

			return arrayList;
		}

		private ModelData newItem(String text, String iconStyle)
		{
			ModelData m = new BaseModelData();
			m.set("name", text);
			m.set("icon", iconStyle);
			return m;
		}
	},
	Entity
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("Entities");
		}
	},
	EntityDetail
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			return getSampleData();
		}
	},
	Default
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			return Collections.emptyList();
		}
	};

	public static boolean isEnumElement(String entityName)
	{
		for (ListModelDataEnum entity : values())
		{
			if (entity.name().equalsIgnoreCase(entityName))
				return true;
		}
		return false;
	}

	private static List<ModelData> getSampleData()
	{
		List<ModelData> arrayList = new ArrayList<ModelData>();

		BaseModelData bm = getBaseModelData();

		arrayList.add(bm);
		return arrayList;
	}

	private static BaseModelData getBaseModelData()
	{
		BaseModelData bm = new BaseModelData();
		bm.set("id", "1");
		bm.set("name", "India");
		return bm;
	}

	abstract public List<ModelData> getListStoreContents();
}
