package com.varun.yfs.dto;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.varun.yfs.client.reports.rpc.ReportType;

public class UserDTO extends BaseModelData
{
	private static final long serialVersionUID = 3196402615838002153L;
	private static final String ADMIN_USER = "admin";

	private long id;
	private ReportType reportType;
	private UserChapterPermissionsDTO dummy1;
	private UserProjectPermissionsDTO dummy2;
	private UserClinicPermissionsDTO dummy3;
	private UserReportPermissionsDTO dummy4;
	private UserEntityPermissionsDTO dummy5;

	public UserDTO()
	{
		set("deleted", "N");
	}

	public UserDTO(String name, String password)
	{
		setName(name);
		setPassword(password);
		setLoggedIn(false);
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

	public String getName()
	{
		return get("name");
	}

	public final void setName(String name)
	{
		set("name", name);
	}

	public String getPassword()
	{
		return get("password");
	}

	public final void setPassword(String password)
	{
		set("password", password);
	}

	public void setChapterPermissions(List<UserChapterPermissionsDTO> chapterNames)
	{
		set("chapterPermissions", chapterNames);
	}

	public List<UserChapterPermissionsDTO> getChapterPermissions()
	{
		return get("chapterPermissions");
	}

	public void setProjectPermissions(List<UserProjectPermissionsDTO> projects)
	{
		set("projectPermissions", projects);
	}

	public List<UserProjectPermissionsDTO> getProjectPermissions()
	{
		return get("projectPermissions");
	}

	public void setClinicPermissions(List<UserClinicPermissionsDTO> clinicPermissions)
	{
		set("clinicPermissions", clinicPermissions);
	}

	public List<UserClinicPermissionsDTO> getClinicPermissions()
	{
		return get("clinicPermissions");
	}

	public void setReportPermissions(List<UserReportPermissionsDTO> reportPermissions)
	{
		set("reportPermissions", reportPermissions);
	}

	public List<UserReportPermissionsDTO> getReportPermissions()
	{
		return get("reportPermissions");
	}

	public void setEntityPermissions(List<UserEntityPermissionsDTO> entityPermissions)
	{
		set("entityPermissions", entityPermissions);
	}

	public List<UserEntityPermissionsDTO> getEntityPermissions()
	{
		return get("entityPermissions");
	}

	public String getDeleted()
	{
		return get("deleted");
	}

	public void setDeleted(String deleted)
	{
		set("deleted", deleted);
	}

	public final void setLoggedIn(boolean loggedIn)
	{
		set("loggedIn", loggedIn);
	}

	public boolean getLoggedIn()
	{
		return get("loggedIn");
	}

	public void setSessionId(String id2)
	{
		set("sessionId", id2);
	}

	public String getSessionId()
	{
		return get("sessionId");
	}

	public void setRole(String role)
	{
		set("role", role);
	}

	public String getRole()
	{
		return get("role");
	}

	public List<String> getChaptersWithPermission(PermissionTypeEnum type)
	{
		List<String> chaps = new ArrayList<String>();

		List<UserChapterPermissionsDTO> chapters = getChapterPermissions();
		if (chapters == null)
			return chaps;
		for (UserChapterPermissionsDTO chapDto : chapters)
		{
			switch (type)
			{
			case READ:
				String read = chapDto.getRead();
				if (read != null && read.equalsIgnoreCase(YesNoDTO.YES.getName()))
					chaps.add(chapDto.getChapterName());
				break;
			case WRITE:
				String write = chapDto.getWrite();
				if (write != null && write.equalsIgnoreCase(YesNoDTO.YES.getName()))
					chaps.add(chapDto.getChapterName());
				break;
			case DELETE:
				String delete = chapDto.getDelete();
				if (delete != null && delete.equalsIgnoreCase(YesNoDTO.YES.getName()))
					chaps.add(chapDto.getChapterName());
				break;
			}
		}

		return chaps;
	}

	public List<String> getProjectWithPermission(PermissionTypeEnum type)
	{
		List<String> projs = new ArrayList<String>();

		List<UserProjectPermissionsDTO> projects = getProjectPermissions();
		if (projects == null)
			return projs;
		for (UserProjectPermissionsDTO projectDto : projects)
		{
			switch (type)
			{
			case READ:
				String read = projectDto.getRead();
				if (read != null && read.equalsIgnoreCase(YesNoDTO.YES.getName()))
					projs.add(projectDto.getProjectName());
				break;
			case WRITE:
				String write = projectDto.getWrite();
				if (write != null && write.equalsIgnoreCase(YesNoDTO.YES.getName()))
					projs.add(projectDto.getProjectName());
				break;
			case DELETE:
				String delete = projectDto.getDelete();
				if (delete != null && delete.equalsIgnoreCase(YesNoDTO.YES.getName()))
					projs.add(projectDto.getProjectName());
				break;
			}
		}

		return projs;
	}

	public List<String> getClinicWithPermission(PermissionTypeEnum type)
	{
		List<String> clinic = new ArrayList<String>();

		List<UserClinicPermissionsDTO> clinics = getClinicPermissions();
		if (clinics == null)
			return clinic;
		for (UserClinicPermissionsDTO clinicDto : clinics)
		{
			switch (type)
			{
			case READ:
				String read = clinicDto.getRead();
				if (read != null && read.equalsIgnoreCase(YesNoDTO.YES.getName()))
					clinic.add(clinicDto.getClinicName());
				break;
			case WRITE:
				String write = clinicDto.getWrite();
				if (write != null && write.equalsIgnoreCase(YesNoDTO.YES.getName()))
					clinic.add(clinicDto.getClinicName());
				break;
			case DELETE:
				String delete = clinicDto.getDelete();
				if (delete != null && delete.equalsIgnoreCase(YesNoDTO.YES.getName()))
					clinic.add(clinicDto.getClinicName());
				break;
			}
		}

		return clinic;
	}

	public List<String> getReportWithPermission(PermissionTypeEnum type)
	{
		List<String> report = new ArrayList<String>();

		List<UserReportPermissionsDTO> reports = getReportPermissions();
		if (reports == null)
			return report;
		for (UserReportPermissionsDTO reportDto : reports)
		{
			switch (type)
			{
			case READ:
				String read = reportDto.getRead();
				if (read != null && read.equalsIgnoreCase(YesNoDTO.YES.getName()))
					report.add(reportDto.getReportName());
				break;
			case WRITE:
				String write = reportDto.getWrite();
				if (write != null && write.equalsIgnoreCase(YesNoDTO.YES.getName()))
					report.add(reportDto.getReportName());
				break;
			case DELETE:
				String delete = reportDto.getDelete();
				if (delete != null && delete.equalsIgnoreCase(YesNoDTO.YES.getName()))
					report.add(reportDto.getReportName());
				break;
			}
		}

		return report;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj.getClass() != this.getClass())
			return false;
		UserDTO user = (UserDTO) obj;
		return user.getName().equalsIgnoreCase(this.getName());
	}

	public boolean isAdmin()
	{
		return this.getName().equalsIgnoreCase(ADMIN_USER);
	}
}
