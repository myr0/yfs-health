package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;


@Entity
@Table(name = "patientDetail")
public class PatientDetail implements Serializable
{
	private static final long serialVersionUID = 8343184437177073237L;

	public PatientDetail()
	{
		setDeleted("N");
	}
	
	@Id
	@GeneratedValue
	@Column(name = "patientDetailId")
	private long id;

	@Column(nullable = false)
	private String deleted;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String age;

	@Column(nullable = false)
	private String sex;

	@Column(nullable = false)
	private String standard;

	@Column(nullable = false)
	private String height;

	@Column(nullable = false)
	private String weight;

	@Column(nullable = true)
	private String address;

	@Column(nullable = true)
	private String contactNo;

	@OneToOne(optional = true)
	@ForeignKey(name = "paediatricScreeningInfoId")
	private PaediatricScreeningInfo paediatric;

	@OneToOne(optional = true)
	@ForeignKey(name="dentalScreeningInfoId")
	private DentalScreeningInfo dental;

	@OneToOne(optional = true)
	@ForeignKey(name="eyeScreeningInfoId")
	private EyeScreeningInfo eye;

	@OneToOne(optional = true)
	@ForeignKey(name="entScreeningInfoId")
	private ENTScreeningInfo ent;

	@OneToOne(optional = true)
	@ForeignKey(name="skinScreeningInfoId")
	private SkinScreeningInfo skin;

	@OneToOne(optional = true)
	@ForeignKey(name="cardiacScreeningInfoId")
	private CardiacScreeningInfo cardiac;

	@OneToOne(optional = true)
	@ForeignKey(name="otherScreeningInfoId")
	private OtherScreeningInfo other;

	@OneToOne(optional = true)
	private YesNoEnum emergency;

	@OneToOne(optional = true)
	private YesNoEnum caseClosed;

	@OneToOne(optional = true)
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

	public PaediatricScreeningInfo getPaediatric()
	{
		return paediatric;
	}

	public void setPaediatric(PaediatricScreeningInfo paediatric)
	{
		this.paediatric = paediatric;
	}

	public DentalScreeningInfo getDental()
	{
		return dental;
	}

	public void setDental(DentalScreeningInfo dental)
	{
		this.dental = dental;
	}

	public EyeScreeningInfo getEye()
	{
		return eye;
	}

	public void setEye(EyeScreeningInfo eye)
	{
		this.eye = eye;
	}

	public ENTScreeningInfo getEnt()
	{
		return ent;
	}

	public void setEnt(ENTScreeningInfo ent)
	{
		this.ent = ent;
	}

	public SkinScreeningInfo getSkin()
	{
		return skin;
	}

	public void setSkin(SkinScreeningInfo skin)
	{
		this.skin = skin;
	}

	public CardiacScreeningInfo getCardiac()
	{
		return cardiac;
	}

	public void setCardiac(CardiacScreeningInfo cardiac)
	{
		this.cardiac = cardiac;
	}

	public OtherScreeningInfo getOther()
	{
		return other;
	}

	public void setOther(OtherScreeningInfo other)
	{
		this.other = other;
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
