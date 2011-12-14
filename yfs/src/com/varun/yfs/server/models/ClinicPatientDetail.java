package com.varun.yfs.server.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ClinicPatientDetail implements Serializable
{
	private static final long serialVersionUID = 8343184437177073237L;
	@Id
	@GeneratedValue
	@Column(name = "cliPatDetId")
	private long id;

	@Column(nullable = false)
	private String deleted;

	@Column(nullable = false)
	private String name;

	@Column(nullable = true)
	private String age;

	@Column(nullable = true)
	private String sex;

	@Column(nullable = true)
	private String occupation;

	@Column(nullable = true)
	private String height;

	@Column(nullable = true)
	private String weight;

	@Column(nullable = true)
	private String address;

	@Column(nullable = true)
	private String contactNo;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "clinicId", nullable = true, updatable = true, insertable = true)
	private Clinic clinic;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "CliPatDet_CliPatHis", joinColumns = { @JoinColumn(name = "cliPatDetId") }, inverseJoinColumns = { @JoinColumn(name = "cliPatHisId") })
	private List<ClinicPatientHistory> lstPatientHistory;

	public ClinicPatientDetail()
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

	public String getOccupation()
	{
		return occupation;
	}

	public void setOccupation(String occupation)
	{
		this.occupation = occupation;
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

	public List<ClinicPatientHistory> getLstPatientHistory()
	{
		return lstPatientHistory;
	}

	public void setLstPatientHistory(List<ClinicPatientHistory> lstPatientHistory)
	{
		this.lstPatientHistory = lstPatientHistory;
	}

	public void setClinic(Clinic clinic)
	{
		this.clinic = clinic;
	}

	public Clinic getClinic()
	{
		return clinic;
	}

}
