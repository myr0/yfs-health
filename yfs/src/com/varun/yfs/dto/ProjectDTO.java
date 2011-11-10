package com.varun.yfs.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ProjectDTO extends BaseModelData
{
	private static final long serialVersionUID = 6280337431842932388L;
	private long id;
	private String name;
	private String deleted;

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
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
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
		set("chapterName",chapterNameDto.getName());
		this.chapterNameDto = chapterNameDto;
	}

	public ChapterNameDTO getChapterName()
	{
		return chapterNameDto;
	}

}
