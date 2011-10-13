package com.varun.yfs.dto;

import java.util.HashMap;
import java.util.Map;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.varun.yfs.client.util.Util;

public class PatientDetailDTO extends BaseModelData
{
	private static final long serialVersionUID = 8343184437177073237L;

	private long id;
	private String deleted;
	private String name;
	private String age;
	private String sex;
	private String standard;
	private String height;
	private String weight;
	private String address;
	private String contactNo;
	private String emergency;
	private String caseClosed;
	private String surgeryCase;
	private YesNoDTO yesNo;
	private GenderDTO gender;

	private PaediatricScreeningInfoDTO paediatric;
	private DentalScreeningInfoDTO dental;
	private EyeScreeningInfoDTO eye;
	private ENTScreeningInfoDTO ent;
	private SkinScreeningInfoDTO skin;
	private CardiacScreeningInfoDTO cardiac;
	private OtherScreeningInfoDTO other;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		set("id", id);
		this.id = id;
	}

	public String getDeleted()
	{
		return deleted;
	}

	public void setDeleted(String deleted)
	{
		set("deleted", deleted);
		this.deleted = deleted;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		set("name", name);
		this.name = name;
	}

	public String getAge()
	{
		return age;
	}

	public void setAge(String age)
	{
		set("age", age);
		this.age = age;
	}

	public String getSex()
	{
		return sex;
	}

	public void setSex(String sex)
	{
		set("sex", sex);
		this.sex = sex;
	}

	public String getStandard()
	{
		return standard;
	}

	public void setStandard(String standard)
	{
		set("standard", standard);
		this.standard = standard;
	}

	public String getHeight()
	{
		return height;
	}

	public void setHeight(String height)
	{
		set("height", height);
		this.height = height;
	}

	public String getWeight()
	{
		return weight;
	}

	public void setWeight(String weight)
	{
		set("weight", weight);
		this.weight = weight;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		set("address", address);
		this.address = address;
	}

	public String getContactNo()
	{
		return contactNo;
	}

	public void setContactNo(String contactNo)
	{
		set("contactNo", contactNo);
		this.contactNo = contactNo;
	}

	public String getEmergency()
	{
		return emergency;
	}

	public void setEmergency(String emergency)
	{
		set("emergency", emergency);
		this.emergency = emergency;
	}

	public String getCaseClosed()
	{
		return caseClosed;
	}

	public void setCaseClosed(String caseClosed)
	{
		set("caseClosed", caseClosed);
		this.caseClosed = caseClosed;
	}

	public String getSurgeryCase()
	{
		return surgeryCase;
	}

	public void setSurgeryCase(String surgeryCase)
	{
		set("surgeryCase", surgeryCase);
		this.surgeryCase = surgeryCase;
	}

	public YesNoDTO getYesNo()
	{
		return yesNo;
	}

	public void setYesNo(YesNoDTO yesNo)
	{
		this.yesNo = yesNo;
	}

	public GenderDTO getGender()
	{
		return gender;
	}

	public void setGender(GenderDTO gender)
	{
		this.gender = gender;
	}

	public PaediatricScreeningInfoDTO getPaediatric()
	{
		return paediatric;
	}

	public void setPaediatric(PaediatricScreeningInfoDTO paediatric)
	{
		this.paediatric = paediatric;
	}

	public DentalScreeningInfoDTO getDental()
	{
		return dental;
	}

	public void setDental(DentalScreeningInfoDTO dental)
	{
		this.dental = dental;
	}

	public EyeScreeningInfoDTO getEye()
	{
		return eye;
	}

	public void setEye(EyeScreeningInfoDTO eye)
	{
		this.eye = eye;
	}

	public ENTScreeningInfoDTO getEnt()
	{
		return ent;
	}

	public void setEnt(ENTScreeningInfoDTO ent)
	{
		this.ent = ent;
	}

	public SkinScreeningInfoDTO getSkin()
	{
		return skin;
	}

	public void setSkin(SkinScreeningInfoDTO skin)
	{
		this.skin = skin;
	}

	public CardiacScreeningInfoDTO getCardiac()
	{
		return cardiac;
	}

	public void setCardiac(CardiacScreeningInfoDTO cardiac)
	{
		this.cardiac = cardiac;
	}

	public OtherScreeningInfoDTO getOther()
	{
		return other;
	}

	public void setOther(OtherScreeningInfoDTO other)
	{
		this.other = other;
	}

	public void unFlattenObject()
	{
		if (paediatric == null)
		{
			paediatric = new PaediatricScreeningInfoDTO();
		}
		paediatric.setFindings(this.get("PaediatricFindings", Util.EMPTY));
		paediatric.setMedicine(this.get("PaediatricMedicines", YesNoDTO.NO.toString()));
		paediatric.setReferral(this.get("PaediatricReferral", YesNoDTO.NO.toString()));
		paediatric.setTreatmentAdviced(this.get("PaediatricTreatment", Util.EMPTY));

		if (this.dental == null)
		{
			this.dental = new DentalScreeningInfoDTO();
		}
		dental.setFindings(this.get("DentalFindings", Util.EMPTY));
		dental.setMedicine(this.get("DentalMedicines", YesNoDTO.NO.toString()));
		dental.setReferral(this.get("DentalReferral", YesNoDTO.NO.toString()));
		dental.setTreatmentAdviced(this.get("DentalTreatment", Util.EMPTY));

		if (eye == null)
		{
			this.eye = new EyeScreeningInfoDTO();
		}
		eye.setFindings(this.get("EyeFindings", Util.EMPTY));
		eye.setMedicine(this.get("EyeMedicines", YesNoDTO.NO.toString()));
		eye.setReferral(this.get("EyeReferral", YesNoDTO.NO.toString()));
		eye.setTreatmentAdviced(this.get("EyeTreatment", Util.EMPTY));

		if (ent == null)
		{
			this.ent = new ENTScreeningInfoDTO();
		}
		ent.setFindings(this.get("EntFindings", Util.EMPTY));
		ent.setMedicine(this.get("EntMedicines", YesNoDTO.NO.toString()));
		ent.setReferral(this.get("EntReferral", YesNoDTO.NO.toString()));
		ent.setTreatmentAdviced(this.get("EntTreatment", Util.EMPTY));

		if (skin == null)
		{
			this.skin = new SkinScreeningInfoDTO();
		}
		skin.setFindings(this.get("SkinFindings", Util.EMPTY));
		skin.setMedicine(this.get("SkinMedicines", YesNoDTO.NO.toString()));
		skin.setReferral(this.get("SkinReferral", YesNoDTO.NO.toString()));
		skin.setTreatmentAdviced(this.get("SkinTreatment", Util.EMPTY));

		if (cardiac == null)
		{
			this.cardiac = new CardiacScreeningInfoDTO();
		}
		cardiac.setFindings(this.get("CardiacFindings", Util.EMPTY));
		cardiac.setMedicine(this.get("CardiacMedicines", YesNoDTO.NO.toString()));
		cardiac.setReferral(this.get("CardiacReferral", YesNoDTO.NO.toString()));
		cardiac.setTreatmentAdviced(this.get("CardiacTreatment", Util.EMPTY));

		if (other == null)
		{
			other = new OtherScreeningInfoDTO();
		}
		other.setFindings(this.get("OtherFindings", Util.EMPTY));
		other.setMedicine(this.get("OtherMedicines", YesNoDTO.NO.toString()));
		other.setReferral(this.get("OtherReferral", YesNoDTO.NO.toString()));
		other.setTreatmentAdviced(this.get("OtherTreatment", Util.EMPTY));

		this.setEmergency(this.get("emergency", YesNoDTO.NO.toString()));
		this.setCaseClosed(this.get("caseClosed", YesNoDTO.NO.toString()));
		this.setSurgeryCase(this.get("surgeryCase", YesNoDTO.NO.toString()));

	}

	public void flattenObject()
	{
		Map<String, Object> mapAllProperties = new HashMap<String, Object>();

		PaediatricScreeningInfoDTO paediatric2 = this.getPaediatric();
		if (paediatric2 != null)
			mapAllProperties.putAll(paediatric2.getProperties());

		DentalScreeningInfoDTO dental2 = this.getDental();
		if (dental2 != null)
			mapAllProperties.putAll(dental2.getProperties());

		EyeScreeningInfoDTO eye2 = this.getEye();
		if (eye2 != null)
			mapAllProperties.putAll(eye2.getProperties());

		ENTScreeningInfoDTO ent2 = this.getEnt();
		if (ent2 != null)
			mapAllProperties.putAll(ent2.getProperties());

		SkinScreeningInfoDTO skin2 = this.getSkin();
		if (skin2 != null)
			mapAllProperties.putAll(skin2.getProperties());

		CardiacScreeningInfoDTO cardiac2 = this.getCardiac();
		if (cardiac2 != null)
			mapAllProperties.putAll(cardiac2.getProperties());

		OtherScreeningInfoDTO other2 = this.getOther();
		if (other2 != null)
			mapAllProperties.putAll(other2.getProperties());

		mapAllProperties.putAll(this.getProperties());

		this.setProperties(mapAllProperties);
	}

}
