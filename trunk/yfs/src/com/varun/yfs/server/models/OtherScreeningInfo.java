package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "OtherScreeningInfo")
public class OtherScreeningInfo  implements Serializable
{
	private static final long serialVersionUID = 6983775843313534773L;
	
	@Id
	@GeneratedValue
	@Column(name = "otherScreeningInfoId")
	private long id;
	
	@Column(nullable = true)
	private String findings;
	
	@Column(nullable = true)
	private String treatmentAdviced;
	
	@ManyToOne
	@Enumerated(EnumType.STRING)
	private YesNoEnum referral;
	
	@ManyToOne
	@Enumerated(EnumType.STRING)
	private YesNoEnum medicine;
	
	public String getFindings()
	{
		return findings;
	}

	public void setFindings(String findings)
	{
		this.findings = findings;
	}

	public String getTreatmentAdviced()
	{
		return treatmentAdviced;
	}

	public void setTreatmentAdviced(String treatmentAdviced)
	{
		this.treatmentAdviced = treatmentAdviced;
	}

	public YesNoEnum getReferral()
	{
		return referral;
	}

	public void setReferral(YesNoEnum referral)
	{
		this.referral = referral;
	}


	public void setMedicine(YesNoEnum medicine)
	{
		this.medicine = medicine;
	}

	public YesNoEnum getMedicine()
	{
		return medicine;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getId()
	{
		return id;
	}
}
