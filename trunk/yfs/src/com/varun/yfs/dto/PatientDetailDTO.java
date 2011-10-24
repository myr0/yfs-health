package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

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

	private String findings;
	private String treatment;
	private String referral1;
	private String referral2;
	private String referral3;

	private String emergency;
	private String caseClosed;
	private String surgeryCase;
	private YesNoDTO yesNo;
	private GenderDTO gender;

	private ReferralTypeDTO referral;

	public PatientDetailDTO()
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

	public void setFindings(String findings)
	{
		set("findings", findings);
		this.findings = findings;
	}

	public String getFindings()
	{
		return findings;
	}

	public String getTreatment()
	{
		return treatment;
	}

	public void setTreatment(String treatment)
	{
		set("treatment", treatment);
		this.treatment = treatment;
	}

	public String getReferral1()
	{
		return referral1;
	}

	public void setReferral1(String referral1)
	{
		set("referral1", referral1);
		this.referral1 = referral1;
	}

	public String getReferral2()
	{
		return referral2;
	}

	public void setReferral2(String referral2)
	{
		set("referral2", referral2);
		this.referral2 = referral2;
	}

	public String getReferral3()
	{
		return referral3;
	}

	public void setReferral3(String referral3)
	{
		set("referral3", referral3);
		this.referral3 = referral3;
	}

	@Override
	public String toString()
	{
		return "PatientDetailDTO [id=" + id + ", deleted=" + deleted + ", name=" + name + ", age=" + age + ", sex=" + sex + ", standard=" + standard + ", height=" + height + ", weight=" + weight + ", address=" + address + ", contactNo=" + contactNo + ", findings=" + findings + ", treatment=" + treatment + ", referral1=" + referral1 + ", referral2=" + referral2 + ", referral3=" + referral3 + ", emergency=" + emergency + ", caseClosed=" + caseClosed + ", surgeryCase=" + surgeryCase + "]";
	}

}