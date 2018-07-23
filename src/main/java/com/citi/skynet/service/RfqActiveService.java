package com.citi.skynet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citi.skynet.common.model.RfqHeader;
import com.citi.skynet.common.model.RfqProperty;
import com.citi.skynet.mapper.active.RfqActiveMapper;

@Service
public class RfqActiveService {
	@Autowired
	private RfqActiveMapper rfqActiveMapper;

	public RfqHeader selectRfqHeader() {
		RfqHeader rfq = rfqActiveMapper.selectRfqHeader();
		return rfq;
	}

	public RfqHeader selectRfqHeaderByRfqId(String rfqId) {
		RfqHeader rfqHeader = rfqActiveMapper.selectRfqHeaderByRfqId(rfqId);
		return rfqHeader;
	}

	public List<RfqProperty> selectRfqPropertiesByRfqId(String rfqId) {
		List<RfqProperty> rfqProperties = rfqActiveMapper.selectRfqPropertiesByRfqId(rfqId);
		return rfqProperties;
	}

	public Integer insertRfqHeader(RfqHeader rfqHeader) {
		Integer id = rfqActiveMapper.insertRfqHeader(rfqHeader);
		return id;
	}

	public Integer insertRfqProperties(List<RfqProperty> rfqProperties) {
		Integer id = rfqActiveMapper.insertRfqProperties(rfqProperties);
		return id;
	}

//	public Integer insertRfqMessage(RfqMessage rfqMessage) {
//		Integer id = rfqActiveMapper.insertRfqMessage(rfqMessage);
//		return id;
//	}
}
