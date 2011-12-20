package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.varun.yfs.client.util.Util;

public class ClinicPatientHistoryDTO extends BaseModelData
{
	private static final long serialVersionUID = 8343184437177073237L;

	private long id;
	private String deleted;

	private String findings;
	private String treatment;
	private String referral1;
	private String referral2;
	private String referral3;

	private String emergency;
	private String caseClosed;
	private String surgeryCase;
	private String medicines;

	private YesNoDTO yesNo;
	private GenderDTO gender;
	private ReferralTypeDTO referral;

	private Long screeningDate;

	public ClinicPatientHistoryDTO()
	{
		setDeleted("N");
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		set("id", id);
		this.id = id;
	}

	public String getDeleted()
	{
		return deleted;
	}

	public final void setDeleted(String deleted)
	{
		set("deleted", deleted);
		this.deleted = deleted;
	}

	public String getEmergency()
	{
		return emergency;
	}

	public void setEmergency(String emergency)
	{
		set("emergency", emergency);
		this.emergency = emergency;
	}

	public String getCaseClosed()
	{
		return caseClosed;
	}

	public void setCaseClosed(String caseClosed)
	{
		set("caseClosed", caseClosed);
		this.caseClosed = caseClosed;
	}

	public String getSurgeryCase()
	{
		return surgeryCase;
	}

	public void setSurgeryCase(String surgeryCase)
	{
		set("surgeryCase", surgeryCase);
		this.surgeryCase = surgeryCase;
	}

	public YesNoDTO getYesNo()
	{
		return yesNo;
	}

	public void setYesNo(YesNoDTO yesNo)
	{
		this.yesNo = yesNo;
	}

	public GenderDTO getGender()
	{
		return gender;
	}

	public void setGender(GenderDTO gender)
	{
		this.gender = gender;
	}

	public void setFindings(String findings)
	{
		set("findings", findings);
		this.findings = findings;
	}

	public String getFindings()
	{
		return findings;
	}

	public String getTreatment()
	{
		return treatment;
	}

	public void setTreatment(String treatment)
	{
		set("treatment", treatment);
		this.treatment = treatment;
	}

	public String getReferral1()
	{
		return referral1;
	}

	public void setReferral1(String referral1)
	{
		set("referral1", referral1);
		this.referral1 = referral1;
	}

	public String getReferral2()
	{
		return referral2;
	}

	public void setReferral2(String referral2)
	{
		set("referral2", referral2);
		this.referral2 = referral2;
	}

	public String getReferral3()
	{
		return referral3;
	}

	public void setReferral3(String referral3)
	{
		set("referral3", referral3);
		this.referral3 = referral3;
	}

	public void setReferral(ReferralTypeDTO referral)
	{
		this.referral = referral;
	}

	public ReferralTypeDTO getReferral()
	{
		return referral;
	}

	public void setMedicines(String medicines)
	{
		set("medicines", medicines);
		this.medicines = medicines;
	}

	public String getMedicines()
	{
		return medicines;
	}

	public void setScreeningDate(Long screeningDate)
	{
		set("screeningDate", screeningDate);
		this.screeningDate = screeningDate;
	}

	public Long getScreeningDate()
	{
		return screeningDate;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClinicPatientHistoryDTO other = (ClinicPatientHistoryDTO) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		// don't touch the order - just append to the last -- fragile code -
		// export will get screwed
		return id + "," + Util.safeCsvString(findings) + "," + Util.safeCsvString(treatment) + "," + Util.safeCsvString(referral1) + "," + Util.safeCsvString(referral2) + "," + Util.safeCsvString(referral3) + "," + Util.safeCsvString(emergency) + "," + Util.safeCsvString(surgeryCase) + "," + Util.safeCsvString(caseClosed);
	}

}
