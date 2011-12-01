package com.varun.yfs.dto;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ClinicPatientDetailDTO extends BaseModelData
{
	private static final long serialVersionUID = 8343184437177073237L;

	private long id;
	private String deleted;

	private String name;
	private String age;
	private String sex;
	private String occupation;
	private String height;
	private String weight;
	private String address;
	private String contactNo;

	private YesNoDTO yesNo;
	private GenderDTO gender;
	private ReferralTypeDTO referral;

	private ClinicDTO clinic;
	private List<ClinicPatientHistoryDTO> lstPatientHistory;

	public ClinicPatientDetailDTO()
	{
		setDeleted("N");
	}

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

	public final void setDeleted(String deleted)
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

	public String getOccupation()
	{
		return occupation;
	}

	public void setOccupation(String occupation)
	{
		set("occupation", occupation);
		this.occupation = occupation;
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

	public void setReferral(ReferralTypeDTO referral)
	{
		this.referral = referral;
	}

	public ReferralTypeDTO getReferral()
	{
		return referral;
	}

	public List<ClinicPatientHistoryDTO> getLstPatientHistory()
	{
		return lstPatientHistory;
	}

	public void setLstPatientHistory(List<ClinicPatientHistoryDTO> lstPatientHistory)
	{
		set("lstPatientHistory", lstPatientHistory);
		this.lstPatientHistory = lstPatientHistory;
	}

	public void setClinic(ClinicDTO clinic)
	{
		set("clinic", clinic);
		this.clinic = clinic;
	}

	public ClinicDTO getClinic()
	{
		return clinic;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClinicPatientDetailDTO other = (ClinicPatientDetailDTO) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		// don't touch the order - just append to the last -- fragile code -
		// export will get screwed
		return id + "," + name + "," + sex + "," + occupation + "," + age + "," + address + "," + contactNo + "," + height + "," + weight;
	}

}
