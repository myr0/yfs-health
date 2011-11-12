package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ProjectDTO extends BaseModelData
{
	private static final long serialVersionUID = 6280337431842932388L;
	private long id;

	private ChapterNameDTO chapterNameDto;

	public ProjectDTO()
	{
		set("deleted", "N");
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
		return get("projectName");
	}

	public void setName(String name)
	{
		set("projectName", name);
	}

	public String getDeleted()
	{
		return get("deleted");
	}

	public void setDeleted(String deleted)
	{
		set("deleted", deleted);
	}

	public void setChapterName(ChapterNameDTO chapterNameDto)
	{
		set("chapterName", chapterNameDto.getName());
		this.chapterNameDto = chapterNameDto;
	}

	public ChapterNameDTO getChapterName()
	{
		return chapterNameDto;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	public boolean equals(Object arg0)
	{
		if (arg0 == null)
			return false;
		if (arg0.getClass() != this.getClass())
			return false;

		ProjectDTO chap = (ProjectDTO) arg0;
		if (chap.getName() == null)
			return false;
		return chap.getName().equalsIgnoreCase(this.getName());
	}

}
