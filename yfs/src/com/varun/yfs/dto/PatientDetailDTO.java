package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;

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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		PatientDetailDTO other = (PatientDetailDTO) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
