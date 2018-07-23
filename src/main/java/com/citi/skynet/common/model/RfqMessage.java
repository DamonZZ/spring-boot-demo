package com.citi.skynet.common.model;

import java.util.List;

public class RfqMessage {

	private RfqHeader rfqHeader;
	private List<RfqProperty> rfqProperties;
	public RfqHeader getRfqHeader() {
		return rfqHeader;
	}
	public void setRfqHeader(RfqHeader rfqHeader) {
		this.rfqHeader = rfqHeader;
	}
	public List<RfqProperty> getRfqProperties() {
		return rfqProperties;
	}
	public void setRfqProperties(List<RfqProperty> rfqProperties) {
		this.rfqProperties = rfqProperties;
	}
	
	
}
