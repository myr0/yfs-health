package com.varun.yfs.client.login.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.varun.yfs.client.reports.rpc.ReportType;
import com.varun.yfs.dto.CampPatientDetailDTO;
import com.varun.yfs.dto.ChapterNameDTO;
import com.varun.yfs.dto.CityDTO;
import com.varun.yfs.dto.CountryDTO;
import com.varun.yfs.dto.DoctorDTO;
import com.varun.yfs.dto.GenderDTO;
import com.varun.yfs.dto.LocalityDTO;
import com.varun.yfs.dto.PermissionsDTO;
import com.varun.yfs.dto.ProcessTypeDTO;
import com.varun.yfs.dto.ProjectDTO;
import com.varun.yfs.dto.ReferralTypeDTO;
import com.varun.yfs.dto.SchoolPatientDetailDTO;
import com.varun.yfs.dto.StateDTO;
import com.varun.yfs.dto.TownDTO;
import com.varun.yfs.dto.TypeOfLocationDTO;
import com.varun.yfs.dto.UserChapterPermissionsDTO;
import com.varun.yfs.dto.UserClinicPermissionsDTO;
import com.varun.yfs.dto.UserDTO;
import com.varun.yfs.dto.UserEntityPermissionsDTO;
import com.varun.yfs.dto.UserProjectPermissionsDTO;
import com.varun.yfs.dto.UserReportPermissionsDTO;
import com.varun.yfs.dto.VillageDTO;
import com.varun.yfs.dto.VolunteerDTO;

@RemoteServiceRelativePath("LoginService")
public interface LoginService extends RemoteService
{
	public static class Util
	{
		private static LoginServiceAsync instance;

		public static LoginServiceAsync getInstance()
		{
			if (instance == null)
			{
				instance = GWT.create(LoginService.class);
			}
			return instance;
		}
	}

	UserDTO loginServer(String name, String password);

	UserDTO loginFromSessionServer();

	boolean changePassword(String name, String newPassword);

	void logout();

	ReportType reportType = null;
	UserChapterPermissionsDTO dummy1 = null;
	UserProjectPermissionsDTO dummy2 = null;
	UserClinicPermissionsDTO dummy3 = null;
	UserReportPermissionsDTO dummy4 = null;
	UserEntityPermissionsDTO dummy5 = null;
	PermissionsDTO permDTO = null;

	GenderDTO gender = null;
	ReferralTypeDTO referral = null;

	CountryDTO country = null;
	StateDTO state = null;
	CityDTO city = null;
	TownDTO town = null;
	VillageDTO village = null;
	ChapterNameDTO chapterName = null;
	LocalityDTO locality = null;
	ProcessTypeDTO processType = null;
	TypeOfLocationDTO typeOfLocation = null;
	ProjectDTO projectDTO = null;
	VolunteerDTO lstVolunteers = null;
	DoctorDTO lstDoctors = null;
	SchoolPatientDetailDTO lstPatientDetails = null;
	CampPatientDetailDTO campPatientDetailDTO = null;
}
