package com.citi.skynet.common.model;

public class RfqProperty {

	private Integer id;
	private String rfqId;
	private Integer version;
	private String name;
	private String value;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
