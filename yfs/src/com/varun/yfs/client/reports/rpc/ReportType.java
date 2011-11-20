package com.varun.yfs.client.reports.rpc;

public enum ReportType
{
	School("School Health"), Clinic("Clinic"), Medical("Medical Camp"), Events("Events"), Overall("Overall");

	ReportType(String value)
	{
		this.value = value;
	}

	private String value;
	
	public String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		this.value = value;
	}

}
