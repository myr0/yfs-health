package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;


public class PaediatricScreeningInfoDTO  extends BaseModelData
{
	private static final long serialVersionUID = 3113549689208120572L;
	private long id;
	private String findings;
	private String treatmentAdviced;
	private String referral;
	private String medicine;
	
	public String getFindings()
	{
		return findings;
	}

	public void setFindings(String findings)
	{
		set("PaediatricFindings",findings);
		this.findings = findings;
	}

	public String getTreatmentAdviced()
	{
		return treatmentAdviced;
	}

	public void setTreatmentAdviced(String treatmentAdviced)
	{
		set("PaediatricTreatment",treatmentAdviced);
		this.treatmentAdviced = treatmentAdviced;
	}


	public void setMedicine(String medicine)
	{
		set("PaediatricMedicine",medicine);
		this.medicine = medicine;
	}

	public String getMedicine()
	{
		return medicine;
	}

	public void setReferral(String referral)
	{
		set("PaediatricReferral",referral);
		this.referral = referral;
	}

	public String getReferral()
	{
		return referral;
	}

	public void setId(long id)
	{
		set("PaediatricScreeningInfoId",id);
		this.id = id;
	}

	public long getId()
	{
		return id;
	}

}
