package com.citi.skynet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citi.skynet.common.model.RfqHeader;
import com.citi.skynet.common.model.RfqProperty;
import com.citi.skynet.mapper.history.RfqHistoryMapper;

@Service
public class RfqHistoryService {

	@Autowired
	private RfqHistoryMapper rfqHistoryMapper;

	public RfqHeader selectRfqHeader() {
		RfqHeader rfq = rfqHistoryMapper.selectRfqHeader();
		return rfq;
	}

	public RfqHeader selectRfqHeaderByRfqId(String rfqId) {
		RfqHeader rfqHeader = rfqHistoryMapper.selectRfqHeaderByRfqId(rfqId);
		return rfqHeader;
	}

	public List<RfqProperty> selectRfqPropertiesByRfqId(String rfqId) {
		List<RfqProperty> rfqProperties = rfqHistoryMapper.selectRfqPropertiesByRfqId(rfqId);
		return rfqProperties;
	}

	public Integer insertRfqHeader(RfqHeader rfqHeader) {
		Integer id = rfqHistoryMapper.insertRfqHeader(rfqHeader);
		return id;
	}

	public Integer insertRfqProperties(List<RfqProperty> rfqProperties) {
		Integer id = rfqHistoryMapper.insertRfqProperties(rfqProperties);
		return id;
	}

//	public Integer insertRfqMessage(RfqMessage rfqMessage) {
//		Integer id = rfqHistoryMapper.insertRfqMessage(rfqMessage);
//		return id;
//	}
}
