package com.varun.yfs.client.reports.rpc;

import java.util.ArrayList;

public enum ReportType
{
	School("School Health"), Clinic("Clinic"), MedicalCamp("Medical Camp"), Events("Events");
	// , Overall("Overall");

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

	@Override
	public String toString()
	{
		return this.value;
	}

	public static ArrayList<String> getValues()
	{
		ArrayList<String> values = new ArrayList<String>();
		for (ReportType reportType : ReportType.values())
		{
			values.add(reportType.toString());
		}

		return values;
	}

}
