package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class ClinicPatientHistory implements Serializable
{
	private static final long serialVersionUID = 9208610044970730535L;

	@Id
	@GeneratedValue
	@Column(name = "cliPatHisId")
	private long id;

	@Column(nullable = false)
	private String deleted;

	@Column(nullable = true)
	private String findings;

	@Column(nullable = true)
	private String treatment;

	@Column(nullable = true)
	private String referral1;

	@Column(nullable = true)
	private String referral2;

	@Column(nullable = true)
	private String referral3;

	@Column(nullable = true)
	private String emergency;

	@Column(nullable = true)
	private String caseClosed;

	@Column(nullable = true)
	private String surgeryCase;
	
	@Column(nullable = true)
	private String medicines;
	
	@Column(nullable = true)
	private Long screeningDate;

	public ClinicPatientHistory()
	{
		setDeleted("N");
	}

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getDeleted()
	{
		return deleted;
	}

	public final void setDeleted(String deleted)
	{
		this.deleted = deleted;
	}

	public String getFindings()
	{
		return findings;
	}

	public void setFindings(String findings)
	{
		this.findings = findings;
	}

	public String getTreatment()
	{
		return treatment;
	}

	public void setTreatment(String treatment)
	{
		this.treatment = treatment;
	}

	public String getReferral1()
	{
		return referral1;
	}

	public void setReferral1(String referral1)
	{
		this.referral1 = referral1;
	}

	public String getReferral2()
	{
		return referral2;
	}

	public void setReferral2(String referral2)
	{
		this.referral2 = referral2;
	}

	public String getReferral3()
	{
		return referral3;
	}

	public void setReferral3(String referral3)
	{
		this.referral3 = referral3;
	}

	public String getEmergency()
	{
		return emergency;
	}

	public void setEmergency(String emergency)
	{
		this.emergency = emergency;
	}

	public String getCaseClosed()
	{
		return caseClosed;
	}

	public void setCaseClosed(String caseClosed)
	{
		this.caseClosed = caseClosed;
	}

	public String getSurgeryCase()
	{
		return surgeryCase;
	}

	public void setSurgeryCase(String surgeryCase)
	{
		this.surgeryCase = surgeryCase;
	}

	public void setMedicines(String medicines)
	{
		this.medicines = medicines;
	}

	public String getMedicines()
	{
		return medicines;
	}

	public void setScreeningDate(Long screeningDate)
	{
		this.screeningDate = screeningDate;
	}

	public Long getScreeningDate()
	{
		return screeningDate;
	}
}
