package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SchoolPatientDetail")
public class SchoolPatientDetail implements Serializable
{
	private static final long serialVersionUID = 8343184437177073237L;
	@Id
	@GeneratedValue
	@Column(name = "schPatDetId")
	private long id;

	@Column(nullable = false)
	private String deleted;

	@Column(nullable = false)
	private String name;

	@Column(nullable = true)
	private Integer age;

	@Column(nullable = true)
	private String sex;

	@Column(nullable = true)
	private String standard;

	@Column(nullable = true)
	private Integer height;

	@Column(nullable = true)
	private Integer weight;

	@Column(nullable = true)
	private String address;

	@Column(nullable = true)
	private Integer contactNo;

	@Column(nullable = true)
	private String findings;

	@Column(nullable = true)
	private String treatment;

	@Column(nullable = true)
	private String referral1;

	@Column(nullable = true)
	private String referral2;

	@Column(nullable = true)
	private String referral3;

	@Column(nullable = true)
	private String emergency;

	@Column(nullable = true)
	private String caseClosed;

	@Column(nullable = true)
	private String surgeryCase;

	@Column(nullable = true)
	private String medicines;

	@Column(nullable = true)
	private String referralUpdates;

	public SchoolPatientDetail()
	{
		setDeleted("N");
	}

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

	public final void setDeleted(String deleted)
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

	public Integer getAge()
	{
		return age;
	}

	public void setAge(Integer age)
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

	public Integer getHeight()
	{
		return height;
	}

	public void setHeight(Integer height)
	{
		this.height = height;
	}

	public Integer getWeight()
	{
		return weight;
	}

	public void setWeight(Integer weight)
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

	public Integer getContactNo()
	{
		return contactNo;
	}

	public void setContactNo(Integer contactNo)
	{
		this.contactNo = contactNo;
	}

	public String getFindings()
	{
		return findings;
	}

	public void setFindings(String findings)
	{
		this.findings = findings;
	}

	public String getTreatment()
	{
		return treatment;
	}

	public void setTreatment(String treatment)
	{
		this.treatment = treatment;
	}

	public String getReferral1()
	{
		return referral1;
	}

	public void setReferral1(String referral1)
	{
		this.referral1 = referral1;
	}

	public String getReferral2()
	{
		return referral2;
	}

	public void setReferral2(String referral2)
	{
		this.referral2 = referral2;
	}

	public String getReferral3()
	{
		return referral3;
	}

	public void setReferral3(String referral3)
	{
		this.referral3 = referral3;
	}

	public String getEmergency()
	{
		return emergency;
	}

	public void setEmergency(String emergency)
	{
		this.emergency = emergency;
	}

	public String getCaseClosed()
	{
		return caseClosed;
	}

	public void setCaseClosed(String caseClosed)
	{
		this.caseClosed = caseClosed;
	}

	public String getSurgeryCase()
	{
		return surgeryCase;
	}

	public void setSurgeryCase(String surgeryCase)
	{
		this.surgeryCase = surgeryCase;
	}

	public void setMedicines(String medicines)
	{
		this.medicines = medicines;
	}

	public String getMedicines()
	{
		return medicines;
	}

	public void setReferralUpdates(String referralUpdates)
	{
		this.referralUpdates = referralUpdates;
	}

	public String getReferralUpdates()
	{
		return referralUpdates;
	}

}
