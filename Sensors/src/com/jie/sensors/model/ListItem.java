package com.jie.sensors.model;

import java.sql.Date;

public class ListItem {
	public enum ActType{
		Activity,
		Trip
	}
	
	private ActType actType;
	
	private Date start;
	
	private Date end;
	
	private String description;
	
	public ActType getActType(){
		return actType;
	}
	
	public Date getStart(){
		return start;
	}
	
	public Date getEnd(){
		return end;
	}
	
	public String getDescription(){
		return description;
	}
	
	public ListItem(ActType actType, Date start, Date end, String description){
		this.actType = actType;
		this.start = start;
		this.end = end;
		this.description = description;
	}
}
