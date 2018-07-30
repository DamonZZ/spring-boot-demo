package com.citi.skynet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.citi.skynet.common.model.RfqMessage;
import com.citi.skynet.service.KafkaService;
import com.citi.skynet.service.RedisService;
import com.citi.skynet.service.RfqActiveService;
import com.citi.skynet.service.RfqHistoryService;
import com.citi.skynet.utils.RfqMessageUtil;
import com.google.gson.Gson;

@RestController
@CrossOrigin
public class RfqController {

	private static Logger looger = LoggerFactory.getLogger(RfqController.class);

	@Autowired
	private RfqHistoryService rfqHistoryService;

	@Autowired
	private RfqActiveService rfqActiveService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private KafkaService kafkaService;

	@RequestMapping(value = "persist", method = RequestMethod.POST)
	@ResponseBody
	public String persist(@RequestBody RfqMessage rfqMessage) {

		String persistResult = "";
		
		if (rfqMessage == null) {
			persistResult = "persit rfqMessage null";
			looger.debug(persistResult);
			return persistResult;
		}

		if (rfqMessage.getRfqHeader() == null) {
			persistResult = "persit rfqHeader null";
			looger.debug(persistResult);
			return persistResult;
		}

		Gson gson = new Gson();
		
		rfqMessage = RfqMessageUtil.enRichRfqMessage(rfqMessage);

		String rfqId = rfqMessage.getRfqHeader().getRfqId();

		String rfqJson = gson.toJson(rfqMessage);

		String strRfqMessageCache = redisService.get(rfqId);

		// new rfq
		if (strRfqMessageCache == null || strRfqMessageCache.isEmpty()) {
			looger.debug("persit rfqCache null, insert rfqMessage");
			redisService.add(rfqId, rfqJson, 1L);
			rfqMessage.getRfqHeader().setVersion(1);
			rfqActiveService.insertRfqHeader(rfqMessage.getRfqHeader());
			rfqActiveService.insertRfqProperties(rfqMessage.getRfqProperties());
			rfqHistoryService.insertRfqHeader(rfqMessage.getRfqHeader());
			rfqHistoryService.insertRfqProperties(rfqMessage.getRfqProperties());
			String strRfqMessage = gson.toJson(rfqMessage);
			kafkaService.SendMessage(rfqMessage.getRfqHeader().getRfqId(), strRfqMessage);
			persistResult = strRfqMessage;
			return persistResult;		
		} else {
			looger.debug("persit rfqCache !=null, update rfqMessage");
			RfqMessage rfqMessageCache = gson.fromJson(strRfqMessageCache, RfqMessage.class);

			if (rfqMessageCache == null) {
				persistResult = "persit rfqMessageCache error";
				looger.debug(persistResult);
				return persistResult;
			}
			Integer versionCache = rfqMessageCache.getRfqHeader().getVersion();
			Integer vesion = rfqMessage.getRfqHeader().getVersion();
			if (vesion.intValue() > versionCache.intValue()) {
				looger.debug("persit update rfqMessage");

				RfqMessage updatedRfqMessage = rfqActiveService.updateActiveRfqCahce(rfqMessage, rfqMessageCache);
				String strUpdatedRfqMessage = gson.toJson(updatedRfqMessage);
				int updateResut = rfqActiveService.updateRfqHeader(rfqMessage.getRfqHeader());
				looger.debug("insert activr header result:" + updateResut);
				updateResut = rfqHistoryService.insertRfqHeader(rfqMessage.getRfqHeader());
				looger.debug("insert history header result:" + updateResut);
				updateResut = rfqHistoryService.insertRfqProperties(rfqMessage.getRfqProperties());
				looger.debug("insert history properties result:" + updateResut);

				redisService.add(rfqId, strUpdatedRfqMessage, 1L);
				kafkaService.SendMessage(rfqMessage.getRfqHeader().getRfqId(), rfqJson);
				persistResult = rfqJson;
				return persistResult;
			} else {
				persistResult = "";
				looger.debug(persistResult);
				return persistResult;
			}
		}
	}

}
