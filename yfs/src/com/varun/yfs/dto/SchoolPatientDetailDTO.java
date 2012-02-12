package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.varun.yfs.client.util.Util;

public class SchoolPatientDetailDTO extends BaseModelData
{
	private static final long serialVersionUID = 8343184437177073237L;

	private long id;

	private YesNoDTO yesNo;
	private GenderDTO gender;
	private ReferralTypeDTO referral;

	private String medicines;
	private String referralUpdates;

	public SchoolPatientDetailDTO()
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

	public String getStandard()
	{
		return get("standard");
	}

	public void setStandard(String standard)
	{
		set("standard", standard);
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

	public String getEmergency()
	{
		return get("emergency");
	}

	public void setEmergency(String emergency)
	{
		set("emergency", emergency);
	}

	public String getCaseClosed()
	{
		return get("caseClosed");
	}

	public void setCaseClosed(String caseClosed)
	{
		set("caseClosed", caseClosed);
	}

	public String getSurgeryCase()
	{
		return get("surgeryCase");
	}

	public void setSurgeryCase(String surgeryCase)
	{
		set("surgeryCase", surgeryCase);
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
		return get("gender");
	}

	public void setGender(GenderDTO gender)
	{
		set("gender", gender);
	}

	public void setFindings(String findings)
	{
		set("findings", findings);
	}

	public String getFindings()
	{
		return get("findings");
	}

	public String getTreatment()
	{
		return get("treatment");
	}

	public void setTreatment(String treatment)
	{
		set("treatment", treatment);
	}

	public String getReferral1()
	{
		return get("referral1");
	}

	public void setReferral1(String referral1)
	{
		set("referral1", referral1);
	}

	public String getReferral2()
	{
		return get("referral2");
	}

	public void setReferral2(String referral2)
	{
		set("referral2", referral2);
	}

	public String getReferral3()
	{
		return get("referral3");
	}

	public void setReferral3(String referral3)
	{
		set("referral3", referral3);
	}

	public void setReferral(ReferralTypeDTO referral)
	{
		set("referral", referral);
	}

	public ReferralTypeDTO getReferral()
	{
		return get("referral");
	}

	public void setMedicines(String medicines)
	{
		set("medicines", medicines);
	}

	public String getMedicines()
	{
		return get("medicines");
	}

	public void setReferralUpdates(String referralUpdates)
	{
		set("referralUpdates", referralUpdates);
	}

	public String getReferralUpdates()
	{
		return get("referralUpdates");
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
		SchoolPatientDetailDTO other = (SchoolPatientDetailDTO) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		// don't touch the order - just append to the last -- fragile code -
		// export will get screwed
		return id + "," + get("name") + "," + get("sex") + "," + get("standard") + "," + get("age") + ","
				+ get("address") + "," + get("contactNo") + "," + get("height") + "," + get("weight") + ","
				+ Util.safeCsvString(get("findings")) + "," + Util.safeCsvString(get("treatment")) + ","
				+ Util.safeCsvString(get("referral1")) + "," + Util.safeCsvString(get("referral2")) + ","
				+ Util.safeCsvString(get("medicines")) + "," + Util.safeCsvString(get("emergency")) + ","
				+ Util.safeCsvString(get("surgeryCase")) + "," + Util.safeCsvString(get("caseClosed")) + ","
				+ Util.safeCsvString(get("referralUpdates"));
	}

}
