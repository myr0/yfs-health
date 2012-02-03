package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class UserChapterPermissionsDTO extends BaseModelData
{

	private static final long serialVersionUID = -913827435230713476L;

	private long id;

	private String chapterName;

	private String deleted;

	private String read;

	private String write;

	private String delete;

	public UserChapterPermissionsDTO()
	{
		setDeleted("N");
	}

	public UserChapterPermissionsDTO(String name, String pass)
	{
		setChapterName(name);
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

	public String getName()
	{
		return chapterName;
	}

	public final void setChapterName(String name)
	{
		set("chapterName", name);
		this.chapterName = name;
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

	public void setRead(String read)
	{
		set("read", read);
		this.read = read;
	}

	public String getRead()
	{
		return read;
	}

	public void setWrite(String write)
	{
		set("write", write);
		this.write = write;
	}

	public String getWrite()
	{
		return write;
	}

	public void setDelete(String delete)
	{
		set("delete", delete);
		this.delete = delete;
	}

	public String getDelete()
	{
		return delete;
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
		if(chapterName == null)
			return false;		
		UserChapterPermissionsDTO other = (UserChapterPermissionsDTO) obj;
		if (!chapterName.equalsIgnoreCase(other.chapterName))
			return false;
		return true;
	}

}
