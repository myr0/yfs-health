package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;


public class ENTScreeningInfoDTO extends BaseModelData
{
	private static final long serialVersionUID = 5853783803280857524L;
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
		set("EntFindings",findings);
		this.findings = findings;
	}

	public String getTreatmentAdviced()
	{
		return treatmentAdviced;
	}

	public void setTreatmentAdviced(String treatmentAdviced)
	{
		set("EntTreatment",treatmentAdviced);
		this.treatmentAdviced = treatmentAdviced;
	}


	public void setMedicine(String medicine)
	{
		set("EntMedicine",medicine);
		this.medicine = medicine;
	}

	public String getMedicine()
	{
		return medicine;
	}

	public void setReferral(String referral)
	{
		set("EntReferral",referral);
		this.referral = referral;
	}

	public String getReferral()
	{
		return referral;
	}

	public void setId(long id)
	{
		set("ENTScreeningInfoInfoId",id);
		this.id = id;
	}

	public long getId()
	{
		return id;
	}

}
