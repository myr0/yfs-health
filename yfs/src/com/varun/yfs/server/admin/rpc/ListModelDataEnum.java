package com.varun.yfs.server.admin.rpc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.varun.yfs.client.reports.rpc.ReportType;
import com.varun.yfs.dto.CampScreeningDetailDTO;
import com.varun.yfs.dto.ChapterNameDTO;
import com.varun.yfs.dto.CityDTO;
import com.varun.yfs.dto.ClinicDTO;
import com.varun.yfs.dto.SchoolScreeningDetailDTO;
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
	SchoolScreeningLocations
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
				rootNode.set("children", chapterNodes);

				List<SchoolScreeningDetailDTO> lstScrDet = DataUtil.getSchoolScreeningDetail("ChapterName", "id", String.valueOf(chapterNameDTO.getId()));
				for (SchoolScreeningDetailDTO screeningDetailDTO : lstScrDet)
				{
					ModelData scrNode = new BaseModelData();
					scrNode.set("name", screeningDetailDTO.toString());
					scrNode.set("id", String.valueOf(screeningDetailDTO.getId()));
					scrNode.set("icon", "");
					chapterNodes.add(scrNode);
				}
			}

			return nodes;
		}
	},
	ClinicScreeningLocations
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			List<ModelData> nodes = new ArrayList<ModelData>();

			List<CityDTO> rootNodes = DataUtil.<CityDTO> getModelList("City");
			for (CityDTO cityDTO : rootNodes)
			{
				ModelData rootNode = new BaseModelData();
				rootNode.set("name", cityDTO.getName());
				rootNode.set("icon", "");
				nodes.add(rootNode);

				List<ModelData> chapterNodes = new ArrayList<ModelData>();
				rootNode.set("children", chapterNodes);

				List<ClinicDTO> lstScrDet = DataUtil.getClinics("City", "id", String.valueOf(cityDTO.getId()));
				for (ClinicDTO clinicDTO : lstScrDet)
				{
					ModelData scrNode = new BaseModelData();
					scrNode.set("name", clinicDTO.toString());
					scrNode.set("id", String.valueOf(clinicDTO.getId()));
					scrNode.set("icon", "");
					chapterNodes.add(scrNode);
				}
			}

			return nodes;
		}
	},
	CampScreeningLocations
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
				rootNode.set("children", chapterNodes);

				List<CampScreeningDetailDTO> lstScrDet = DataUtil.getCampScreeningDetail("ChapterName", "id", String.valueOf(chapterNameDTO.getId()));
				for (CampScreeningDetailDTO screeningDetailDTO : lstScrDet)
				{
					ModelData scrNode = new BaseModelData();
					scrNode.set("name", screeningDetailDTO.toString());
					scrNode.set("id", String.valueOf(screeningDetailDTO.getId()));
					scrNode.set("icon", "");
					chapterNodes.add(scrNode);
				}
			}

			return nodes;
		}
	},

	Reports
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			List<ModelData> arrayList = new ArrayList<ModelData>();
			ModelData m1 = newItem("Reports", "");
			arrayList.add(m1);

			List<ModelData> child = new ArrayList<ModelData>();
			m1.set("children", child);
			child.add(newItem(ReportType.School.getValue(), ""));
			child.add(newItem(ReportType.MedicalCamp.getValue(), ""));
			child.add(newItem(ReportType.Clinic.getValue(), ""));
			child.add(newItem(ReportType.Events.getValue(), ""));
//			child.add(newItem(ReportType.Overall.getValue(), ""));

			// m1 = newItem("Summary Reports", "");
			// arrayList.add(m1);
			//
			// child = new ArrayList<ModelData>();
			// m1.set("children", child);
			// child.add(newItem("Report 1", ""));
			// child.add(newItem("Report 2", ""));

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
			return DataUtil.<ModelData> getModelList("Entities");
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
	},
	ReferralType
	{
		@Override
		public List<ModelData> getListStoreContents()
		{
			return DataUtil.getModelList("ReferralType");
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
