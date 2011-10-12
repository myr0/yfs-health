package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class CardiacScreeningInfoDTO extends BaseModelData
{
	private static final long serialVersionUID = 5813246046684857833L;
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
		set("CardiacScreeningInfoFindings",findings);
		this.findings = findings;
	}

	public String getTreatmentAdviced()
	{
		return treatmentAdviced;
	}

	public void setTreatmentAdviced(String treatmentAdviced)
	{
		set("CardiacScreeningInfoTreatmentAdviced",treatmentAdviced);
		this.treatmentAdviced = treatmentAdviced;
	}


	public void setMedicine(String medicine)
	{
		set("CardiacScreeningInfoMedicine",medicine);
		this.medicine = medicine;
	}

	public String getMedicine()
	{
		return medicine;
	}

	public void setReferral(String referral)
	{
		set("CardiacScreeningInfoReferral",referral);
		this.referral = referral;
	}

	public String getReferral()
	{
		return referral;
	}

	public void setId(long id)
	{
		set("CardiacScreeningInfoId",id);
		this.id = id;
	}

	public long getId()
	{
		return id;
	}

}
