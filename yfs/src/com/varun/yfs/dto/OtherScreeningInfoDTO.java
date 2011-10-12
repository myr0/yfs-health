package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;


public class OtherScreeningInfoDTO extends BaseModelData
{
	private static final long serialVersionUID = -4081247143044464606L;
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
		set("OtherFindings",findings);
		this.findings = findings;
	}

	public String getTreatmentAdviced()
	{
		return treatmentAdviced;
	}

	public void setTreatmentAdviced(String treatmentAdviced)
	{
		set("OtherTreatment",treatmentAdviced);
		this.treatmentAdviced = treatmentAdviced;
	}


	public void setMedicine(String medicine)
	{
		set("OtherMedicine",medicine);
		this.medicine = medicine;
	}

	public String getMedicine()
	{
		return medicine;
	}

	public void setReferral(String referral)
	{
		set("OtherReferral",referral);
		this.referral = referral;
	}

	public String getReferral()
	{
		return referral;
	}

	public void setId(long id)
	{
		set("OtherScreeningInfoId",id);
		this.id = id;
	}

	public long getId()
	{
		return id;
	}

}
