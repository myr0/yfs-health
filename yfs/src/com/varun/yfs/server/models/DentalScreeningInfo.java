package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "DentalScreeningInfo")
public class DentalScreeningInfo implements Serializable
{
	private static final long serialVersionUID = 8997290664082146399L;

	@Id
	@GeneratedValue
	@Column(name = "dentalScreeningInfoId")
	private long id;

	@Column(nullable = true)
	private String findings;

	@Column(nullable = true)
	private String treatmentAdviced;

	@Column(nullable = true)
	private String referral;

	@Column(nullable = true)
	private String medicine;

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

	public String getReferral()
	{
		return referral;
	}

	public void setReferral(String referral)
	{
		this.referral = referral;
	}

	public String getMedicine()
	{
		return medicine;
	}

	public void setMedicine(String medicine)
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
