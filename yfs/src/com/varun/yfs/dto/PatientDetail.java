package com.varun.yfs.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class PatientDetail extends BaseModelData
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

//	private PaediatricScreeningInfo paediatric;
//
//	private DentalScreeningInfo dental;
//
//	private EyeScreeningInfo eye;
//
//	private ENTScreeningInfo ent;
//
//	private SkinScreeningInfo skin;
//
//	private CardiacScreeningInfo cardiac;
//
//	private OtherScreeningInfo other;

	private YesNoEnum emergency;

	private YesNoEnum caseClosed;

	private YesNoEnum surgeryCase;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getDeleted()
	{
		return deleted;
	}

	public void setDeleted(String deleted)
	{
		this.deleted = deleted;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAge()
	{
		return age;
	}

	public void setAge(String age)
	{
		this.age = age;
	}

	public String getSex()
	{
		return sex;
	}

	public void setSex(String sex)
	{
		this.sex = sex;
	}

	public String getStandard()
	{
		return standard;
	}

	public void setStandard(String standard)
	{
		this.standard = standard;
	}

	public String getHeight()
	{
		return height;
	}

	public void setHeight(String height)
	{
		this.height = height;
	}

	public String getWeight()
	{
		return weight;
	}

	public void setWeight(String weight)
	{
		this.weight = weight;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getContactNo()
	{
		return contactNo;
	}

	public void setContactNo(String contactNo)
	{
		this.contactNo = contactNo;
	}

	public YesNoEnum getEmergency()
	{
		return emergency;
	}

	public void setEmergency(YesNoEnum emergency)
	{
		this.emergency = emergency;
	}

	public YesNoEnum getCaseClosed()
	{
		return caseClosed;
	}

	public void setCaseClosed(YesNoEnum caseClosed)
	{
		this.caseClosed = caseClosed;
	}

	public YesNoEnum getSurgeryCase()
	{
		return surgeryCase;
	}

	public void setSurgeryCase(YesNoEnum surgeryCase)
	{
		this.surgeryCase = surgeryCase;
	}

}
