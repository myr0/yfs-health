package com.varun.yfs.server.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "patientDetail")
public class PatientDetail implements Serializable
{
	private static final long serialVersionUID = 8343184437177073237L;
	
	@Id
	@GeneratedValue
	@Column(name = "patientDetailId")
	private long id;

	@Column(nullable = false)
	private String deleted;

	public long getId()
	{
		return id;
	}

	public String getDeleted()
	{
		return deleted;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public void setDeleted(String deleted)
	{
		this.deleted = deleted;
	}
	

}
