package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "SkinScreeningInfo")
public class SkinScreeningInfo implements Serializable
{
	private static final long serialVersionUID = -2782269704762985200L;

	@Id
	@GeneratedValue
	@Column(name = "skinScreeningInfoId")
	private long id;

	@Column(nullable = true)
	private String findings;
	
	@Column(nullable = true)
	private String treatmentAdviced;
	
	@ManyToOne
	private YesNoEnum referral;
	
	@ManyToOne
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

	public YesNoEnum getMedicine()
	{
		return medicine;
	}

	public void setMedicine(YesNoEnum medicine)
	{
		this.medicine = medicine;
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
