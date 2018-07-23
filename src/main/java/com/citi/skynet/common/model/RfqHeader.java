package com.citi.skynet.common.model;

//import java.security.Timestamp;

//import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class RfqHeader {
	
	private Integer id;
	private String rfqId;
	private Integer version;
	private String parentId;
	private String flow;
	private String messageId;
	private String state;
	private String sentBy;
	private String event;
	private Date timestamp;
	private String roledefinition;
	private String routingtargets;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRfqId() {
		return rfqId;
	}
	public void setRfqId(String rfqId) {
		this.rfqId = rfqId;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getFlow() {
		return flow;
	}
	public void setFlow(String flow) {
		this.flow = flow;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSentBy() {
		return sentBy;
	}
	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getRoledefinition() {
		return roledefinition;
	}
	public void setRoledefinition(String roledefinition) {
		this.roledefinition = roledefinition;
	}
	public String getRoutingtargets() {
		return routingtargets;
	}
	public void setRoutingtargets(String routingtargets) {
		this.routingtargets = routingtargets;
	}

}
