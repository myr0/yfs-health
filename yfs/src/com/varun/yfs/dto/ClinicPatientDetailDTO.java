package com.varun.yfs.dto;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ClinicPatientDetailDTO extends BaseModelData
{
	private static final long serialVersionUID = 8343184437177073237L;

	private long id;
	// private String deleted;
	//
	// private String name;
	// private String age;
	// private String sex;
	// private String occupation;
	// private String height;
	// private String weight;
	// private String address;
	// private String contactNo;

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
		return (Long) (get("id") == null ? 0L : get("id"));
	}

	public void setId(long id)
	{
		set("id", id);
	}

	public String getDeleted()
	{
		return get("deleted");
	}

	public final void setDeleted(String deleted)
	{
		set("deleted", deleted);
	}

	public String getName()
	{
		return get("name");
	}

	public void setName(String name)
	{
		set("name", name);
	}

	public String getAge()
	{
		return get("age");
	}

	public void setAge(String age)
	{
		set("age", age);
	}

	public String getSex()
	{
		return get("sex");
	}

	public void setSex(String sex)
	{
		set("sex", sex);
	}

	public String getOccupation()
	{
		return get("occupation");
	}

	public void setOccupation(String occupation)
	{
		set("occupation", occupation);
	}

	public String getHeight()
	{
		return get("height");
	}

	public void setHeight(String height)
	{
		set("height", height);
	}

	public String getWeight()
	{
		return get("weight");
	}

	public void setWeight(String weight)
	{
		set("weight", weight);
	}

	public String getAddress()
	{
		return get("address");
	}

	public void setAddress(String address)
	{
		set("address", address);
	}

	public String getContactNo()
	{
		return get("contactNo");
	}

	public void setContactNo(String contactNo)
	{
		set("contactNo", contactNo);
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
		return get("referral");
	}

	public List<ClinicPatientHistoryDTO> getLstPatientHistory()
	{
		return get("lstPatientHistory");
	}

	public void setLstPatientHistory(List<ClinicPatientHistoryDTO> lstPatientHistory)
	{
		set("lstPatientHistory", lstPatientHistory);
	}

	public void setClinic(ClinicDTO clinic)
	{
		set("clinic", clinic);
	}

	public ClinicDTO getClinic()
	{
		return get("clinic");
	}

//	@Override
//	public int hashCode()
//	{
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + (int) (id ^ (id >>> 32));
//		return result;
//	}

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
		return get("id") + "," + get("name") + "," + get("sex") + "," + get("occupation") + "," + get("age") + ","
				+ get("address") + "," + get("contactNo") + "," + get("height") + "," + get("weight");
	}

}
