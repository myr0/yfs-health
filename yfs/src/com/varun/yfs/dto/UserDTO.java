package com.varun.yfs.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.varun.yfs.client.reports.rpc.ReportType;

public class UserDTO extends BaseModelData
{
	private static final long serialVersionUID = 3196402615838002153L;
	private long id;
	private List<UserChapterPermissionsDTO> chapterPermissions;
	private List<UserProjectPermissionsDTO> projectPermissions;
	private List<UserClinicPermissionsDTO> clinicPermissions;
	private List<UserEntityPermissionsDTO> entityPermissions;
	private List<UserReportPermissionsDTO> reportPermissions;

	private ReportType reportType;

	private static final String ADMIN_USER = "admin";

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
		List<UserChapterPermissionsDTO> list = get("chapterPermissions");
		return list == null ? Collections.EMPTY_LIST : list;
	}

	public void setProjectPermissions(List<UserProjectPermissionsDTO> projects)
	{
		set("projectPermissions", projects);
	}

	public List<UserProjectPermissionsDTO> getProjectPermissions()
	{
		List<UserChapterPermissionsDTO> list = get("projectPermissions");
		return list == null ? Collections.EMPTY_LIST : list;
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

	public void setClinicPermissions(List<UserClinicPermissionsDTO> clinicPermissions)
	{
		set("clinicPermissions", clinicPermissions);
	}

	public List<UserClinicPermissionsDTO> getClinicPermissions()
	{
		List<UserChapterPermissionsDTO> list = get("clinicPermissions");
		return list == null ? Collections.EMPTY_LIST : list;
	}

	public void setReportPermissions(List<UserReportPermissionsDTO> reportPermissions)
	{
		set("reportPermissions", reportPermissions);
	}

	public List<UserReportPermissionsDTO> getReportPermissions()
	{
		List<UserChapterPermissionsDTO> list = get("reportPermissions");
		return list == null ? Collections.EMPTY_LIST : list;
	}

	public void setEntityPermissions(List<UserEntityPermissionsDTO> entityPermissions)
	{
		set("entityPermissions", entityPermissions);
	}

	public List<UserEntityPermissionsDTO> getEntityPermissions()
	{
		List<UserChapterPermissionsDTO> list = get("entityPermissions");
		return list == null ? Collections.EMPTY_LIST : list;
	}

	public List<String> getChaptersWithPermission(PermissionTypeEnum type)
	{
		List<String> chaps = new ArrayList<String>();

		List<UserChapterPermissionsDTO> chapters = getChapterPermissions();
		for (UserChapterPermissionsDTO chapDto : chapters)
		{
			switch (type)
			{
			case READ:
				String read = chapDto.getRead();
				if (read != null && read.equalsIgnoreCase(type.name()))
					chaps.add(chapDto.getChapterName());
				break;
			case WRITE:
				String write = chapDto.getWrite();
				if (write != null && write.equalsIgnoreCase(type.name()))
					chaps.add(chapDto.getChapterName());
				break;
			case DELETE:
				String delete = chapDto.getDelete();
				if (delete != null && delete.equalsIgnoreCase(type.name()))
					chaps.add(chapDto.getChapterName());
				break;
			}
		}

		return chaps;
	}

	public List<String> getProjectWithPermission(PermissionTypeEnum type)
	{
		List<String> chaps = new ArrayList<String>();

		List<UserProjectPermissionsDTO> projects = getProjectPermissions();
		for (UserProjectPermissionsDTO projectDto : projects)
		{
			switch (type)
			{
			case READ:
				String read = projectDto.getRead();
				if (read != null && read.equalsIgnoreCase(type.name()))
					chaps.add(projectDto.getProjectName());
				break;
			case WRITE:
				String write = projectDto.getWrite();
				if (write != null && write.equalsIgnoreCase(type.name()))
					chaps.add(projectDto.getProjectName());
				break;
			case DELETE:
				String delete = projectDto.getDelete();
				if (delete != null && delete.equalsIgnoreCase(type.name()))
					chaps.add(projectDto.getProjectName());
				break;
			}
		}

		return chaps;
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
