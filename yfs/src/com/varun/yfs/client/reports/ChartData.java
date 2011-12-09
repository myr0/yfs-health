package com.varun.yfs.client.reports;

import com.extjs.gxt.ui.client.data.BaseModel;

public class ChartData extends BaseModel
{

	private static final long serialVersionUID = 2103699184769341265L;

	public ChartData(String month, int screened, int surgeryCaseClosed, int pendingCases, Integer followUpMedicines, Integer referredToHospital)
	{
		setMonth(month);
		setTotalScreened(screened);
		setSurgeryCasesClosed(surgeryCaseClosed);
		setPendingCases(pendingCases);
		setFollowUpMedicines(followUpMedicines);
		setReferredToHospital(referredToHospital);
	}

	public int getTotalScreened()
	{
		return (Integer) get("screened");
	}

	public int getSurgeryCasesClosed()
	{
		return (Integer) get("surgeryCasesClosed");
	}

	public int getPendingCases()
	{
		return (Integer) get("pendingCases");
	}

	public String getMonth()
	{
		return (String) get("month");
	}

	public Integer getReferredToHospital()
	{
		return get("referredToHospital");
	}

	public Integer getFollowUpMedicines()
	{
		return get("followUpMedicines");
	}

	public void setTotalScreened(int sales)
	{
		set("screened", sales);
	}

	private void setReferredToHospital(Integer referredToHospital)
	{
		set("referredToHospital", referredToHospital);
	}

	private void setFollowUpMedicines(Integer followUpMedicines)
	{
		set("followUpMedicines", followUpMedicines);
	}

	public void setSurgeryCasesClosed(int sales)
	{
		set("surgeryCasesClosed", sales);
	}

	public void setPendingCases(int sales)
	{
		set("pendingCases", sales);
	}

	public void setMonth(String month)
	{
		set("month", month);
	}
}
