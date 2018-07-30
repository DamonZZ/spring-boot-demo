package com.citi.skynet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citi.skynet.common.model.RfqHeader;
import com.citi.skynet.common.model.RfqMessage;
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

	public int insertRfqProperty(RfqProperty rfqProperty) {
		int id = rfqActiveMapper.insertRfqProperty(rfqProperty);
		return id;
	}

	public int updateRfqHeader(RfqHeader rfqHeader) {
		int id = rfqActiveMapper.updateRfqHeader(rfqHeader);
		return id;
	}

	public int updateRfqProerties(List<RfqProperty> rfqProperties) {
		int id = rfqActiveMapper.updateRfqProerties(rfqProperties);
		return id;
	}

	public int updateRfqProerty(RfqProperty rfqProperty) {
		int id = rfqActiveMapper.updateRfqProerty(rfqProperty);
		return id;
	}

	public RfqMessage updateActiveRfqCahce(RfqMessage comingRfqMessage, RfqMessage rfqCache) {
		rfqCache.setRfqHeader(comingRfqMessage.getRfqHeader());
		List<RfqProperty> comingRfqProperties = comingRfqMessage.getRfqProperties();
		List<RfqProperty> cacheRfqProperties = rfqCache.getRfqProperties();

		List<RfqProperty> updateProperties = comingRfqProperties.stream()
				.filter((p) -> cacheRfqProperties.stream().filter((pp) -> pp.getName().equals(p.getName())) != null)
				.collect(Collectors.toList());

		List<RfqProperty> unUpdateProperties = cacheRfqProperties.stream()
				.filter((p) -> updateProperties.stream().allMatch((pp) -> !pp.getName().equals(p.getName())))
				.collect(Collectors.toList());

		List<RfqProperty> newProperties = comingRfqProperties.stream()
				.filter((p) -> cacheRfqProperties.stream().allMatch((pp) -> !pp.getName().equals(p.getName())))
				.collect(Collectors.toList());

		rfqCache.setRfqProperties(new ArrayList<RfqProperty>());

		if (updateProperties != null && updateProperties.size() > 0) {
			for (RfqProperty updateProperty : updateProperties) {
				updateRfqProerty(updateProperty);
			}
			rfqCache.getRfqProperties().addAll(updateProperties);
		}

		if (unUpdateProperties != null && unUpdateProperties.size() > 0) {
			rfqCache.getRfqProperties().addAll(unUpdateProperties);
		}

		if (newProperties != null && newProperties.size() > 0) {
			for (RfqProperty newProperty : newProperties) {
				insertRfqProperty(newProperty);
			}
			rfqCache.getRfqProperties().addAll(newProperties);
		}

		return rfqCache;
	}

	// public Integer insertRfqMessage(RfqMessage rfqMessage) {
	// Integer id = rfqActiveMapper.insertRfqMessage(rfqMessage);
	// return id;
	// }
}
