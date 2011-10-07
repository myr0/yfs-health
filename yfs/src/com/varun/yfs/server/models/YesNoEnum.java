package com.varun.yfs.server.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "YesNoEnum")
public enum YesNoEnum
{
	YES, NO;
	
	@Id
	@GeneratedValue
	@Column(name = "yesNoEnumId")
	private long id;
	
};
