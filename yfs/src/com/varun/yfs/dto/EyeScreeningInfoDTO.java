package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class EyeScreeningInfoDTO extends BaseModelData
{
	private static final long serialVersionUID = -7398344755182826846L;
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
		set("EyeFindings",findings);
		this.findings = findings;
	}

	public String getTreatmentAdviced()
	{
		return treatmentAdviced;
	}

	public void setTreatmentAdviced(String treatmentAdviced)
	{
		set("EyeTreatment",treatmentAdviced);
		this.treatmentAdviced = treatmentAdviced;
	}


	public void setMedicine(String medicine)
	{
		set("EyeMedicine",medicine);
		this.medicine = medicine;
	}

	public String getMedicine()
	{
		return medicine;
	}

	public void setReferral(String referral)
	{
		set("EyeReferral",referral);
		this.referral = referral;
	}

	public String getReferral()
	{
		return referral;
	}

	public void setId(long id)
	{
		set("EyeScreeningInfoId",id);
		this.id = id;
	}

	public long getId()
	{
		return id;
	}


}
