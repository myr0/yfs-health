package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class SkinScreeningInfoDTO extends BaseModelData
{
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
		set("SkinFindings",findings);
		this.findings = findings;
	}

	public String getTreatmentAdviced()
	{
		return treatmentAdviced;
	}

	public void setTreatmentAdviced(String treatmentAdviced)
	{
		set("SkinTreatment",treatmentAdviced);
		this.treatmentAdviced = treatmentAdviced;
	}


	public void setMedicine(String medicine)
	{
		set("SkinMedicine",medicine);
		this.medicine = medicine;
	}

	public String getMedicine()
	{
		return medicine;
	}

	public void setReferral(String referral)
	{
		set("SkinReferral",referral);
		this.referral = referral;
	}

	public String getReferral()
	{
		return referral;
	}

	public void setId(long id)
	{
		set("SkinScreeningInfoId",id);
		this.id = id;
	}

	public long getId()
	{
		return id;
	}


}
