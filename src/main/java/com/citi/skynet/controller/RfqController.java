package com.citi.skynet.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.citi.skynet.common.model.RfqHeader;
import com.citi.skynet.common.model.RfqMessage;
import com.citi.skynet.common.model.RfqProperty;
import com.citi.skynet.service.KafkaService;
import com.citi.skynet.service.RedisService;
import com.citi.skynet.service.RfqActiveService;
import com.citi.skynet.service.RfqHistoryService;
import com.google.gson.Gson;

@RestController
public class RfqController {

	private Integer rfqIdCache = 0;

	private String rfqId = "";

	@Autowired
	private RfqHistoryService rfqHistoryService;

	@Autowired
	private RfqActiveService rfqActiveService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private KafkaService kafkaService;

	@RequestMapping("rfq")
	public RfqHeader selectRfqHeader() {
		RfqHeader header = rfqHistoryService.selectRfqHeader();
		return header;
	}

	@RequestMapping("select")
	public RfqMessage selectRfqMessageByRfqId() {
		String rfqId = "RFQ-20180723205313-0";
		RfqMessage rfqMessage = new RfqMessage();
		RfqHeader rfqHeader = new RfqHeader();
		List<RfqProperty> rfqProperties = new ArrayList<RfqProperty>();
		rfqHeader = rfqHistoryService.selectRfqHeaderByRfqId(rfqId);
		rfqProperties = rfqHistoryService.selectRfqPropertiesByRfqId(rfqId);

		rfqMessage.setRfqHeader(rfqHeader);
		rfqMessage.setRfqProperties(rfqProperties);
		return rfqMessage;
	}

	@RequestMapping(value = "persist", method = RequestMethod.POST)
	public void persist(@RequestParam("rfqMessage") String rfqJson) {
		if (rfqJson == null || rfqJson.isEmpty()) {
			Debug("persit json null");
			return;
		}
		Gson gson = new Gson();
		RfqMessage rfqMessage = gson.fromJson(rfqJson, RfqMessage.class);
		if (rfqMessage == null) {
			Debug("persit rfqMessage null");
			return;
		}

		if (rfqMessage.getRfqHeader() == null) {
			Debug("persit rfqHeader null");
			return;
		}

		// judge rfqId
		String rfqId = rfqMessage.getRfqHeader().getRfqId();	
		if (rfqId == null || rfqId.isEmpty()) {
			rfqMessage.getRfqHeader().setRfqId(generateRfqId());
			rfqMessage.getRfqHeader().setVersion(this.rfqIdCache);
			String messageId = rfqMessage.getRfqHeader().getMessageId();
			if (messageId == null || messageId.isEmpty()) {
				rfqMessage.getRfqHeader().setMessageId(rfqMessage.getRfqHeader().getRfqId());
			}
		}

		String strRfqMessageCache = redisService.get(rfqId);
		gson.fromJson(redisService.get(rfqId), RfqMessage.class);
		if (strRfqMessageCache == null || strRfqMessageCache.isEmpty()) {
			Debug("persit rfqCache null, insert rfqMessage");
			redisService.add(rfqId, rfqJson, new Long(1));
			rfqMessage.getRfqHeader().setVersion(1);
			rfqActiveService.insertRfqHeader(rfqMessage.getRfqHeader());
			rfqActiveService.insertRfqProperties(rfqMessage.getRfqProperties());
			rfqHistoryService.insertRfqHeader(rfqMessage.getRfqHeader());
			rfqHistoryService.insertRfqProperties(rfqMessage.getRfqProperties());
			
			String strRfqMessage = gson.toJson(rfqMessage);
			kafkaService.SendMessage(rfqMessage.getRfqHeader().getRfqId(), strRfqMessage);
			
		} else {
			RfqMessage rfqMessageCache = gson.fromJson(strRfqMessageCache, RfqMessage.class);
			if (rfqMessageCache == null) {
				Debug("persit rfqMessageCache error");
				return;
			}
			Integer versionCache = rfqMessageCache.getRfqHeader().getVersion();
			Integer vesion = rfqMessage.getRfqHeader().getVersion();
			if (vesion.intValue() > versionCache.intValue()) {
				Debug("persit update rfqMessage");
			} else {
				Debug("persit version expired");
			}
		}
	}

	@RequestMapping("publish")
	public void publish() {
		RfqMessage rfqMessage = new RfqMessage();
		RfqHeader rfqHeader = new RfqHeader();
		List<RfqProperty> rfqProperties = new ArrayList<RfqProperty>();
		rfqHeader.setRfqId("");
		rfqHeader.setFlow("saas");
		rfqHeader.setMessageId("");
		rfqHeader.setParentId("parentid");
		rfqHeader.setEvent("test");
		rfqHeader.setVersion(0);
		rfqHeader.setState("test start");
		rfqHeader.setSentBy("damon");
		rfqHeader.setRoledefinition("pouprole:closerole");
		rfqHeader.setRoutingtargets("popuprole:jz54863");
		rfqHeader.setTimestamp(new Date());
		RfqProperty rfqProperty1 = new RfqProperty();
		RfqProperty rfqProperty2 = new RfqProperty();
		RfqProperty rfqProperty3 = new RfqProperty();
		RfqProperty rfqProperty4 = new RfqProperty();
		rfqProperty1.setName("TemplateKey");
		rfqProperty1.setValue("test template");
		rfqProperty1.setRfqId(this.rfqId);
		rfqProperty1.setVersion(this.rfqIdCache);
		rfqProperty2.setName("customer");
		rfqProperty2.setValue("alibaba");
		rfqProperty2.setRfqId(this.rfqId);
		rfqProperty2.setVersion(this.rfqIdCache);
		rfqProperty3.setName("product");
		rfqProperty3.setValue("tesla");
		rfqProperty3.setRfqId(this.rfqId);
		rfqProperty3.setVersion(this.rfqIdCache);
		rfqProperty4.setName("quantity");
		rfqProperty4.setValue("100");
		rfqProperty4.setRfqId(this.rfqId);
		rfqProperty4.setVersion(this.rfqIdCache);
		rfqProperties.add(rfqProperty1);
		rfqProperties.add(rfqProperty2);
		rfqProperties.add(rfqProperty3);
		rfqProperties.add(rfqProperty4);
		rfqMessage.setRfqHeader(rfqHeader);
		rfqMessage.setRfqProperties(rfqProperties);

		Gson gson = new Gson();
		String strRfqMessage = gson.toJson(rfqMessage);
		Debug(strRfqMessage);
		String url = "http://localhost:8090/persist";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("rfqMessage", strRfqMessage);
		String result = sendHttpRequest(url, params);
		Debug("reslut=" + result);
	}

	@RequestMapping("update")
	public void update() {
		RfqMessage rfqMessage = new RfqMessage();
		RfqHeader rfqHeader = new RfqHeader();
		List<RfqProperty> rfqProperties = new ArrayList<RfqProperty>();
		rfqHeader.setRfqId("RFQ-20180723205911-0");
		rfqHeader.setFlow("saas");
		rfqHeader.setMessageId(this.rfqId);
		rfqHeader.setParentId("parentid");
		rfqHeader.setEvent("test");
		rfqHeader.setVersion(this.rfqIdCache);
		rfqHeader.setState("test start");
		rfqHeader.setSentBy("damon");
		rfqHeader.setRoledefinition("pouprole:closerole");
		rfqHeader.setRoutingtargets("popuprole:jz54863");
		rfqHeader.setTimestamp(new Date());
		RfqProperty rfqProperty1 = new RfqProperty();
		RfqProperty rfqProperty2 = new RfqProperty();
		RfqProperty rfqProperty3 = new RfqProperty();
		RfqProperty rfqProperty4 = new RfqProperty();
		rfqProperty1.setName("TemplateKey");
		rfqProperty1.setValue("test template");
		rfqProperty1.setRfqId(this.rfqId);
		rfqProperty1.setVersion(this.rfqIdCache);
		rfqProperty2.setName("customer");
		rfqProperty2.setValue("alibaba");
		rfqProperty2.setRfqId(this.rfqId);
		rfqProperty2.setVersion(this.rfqIdCache);
		rfqProperty3.setName("product");
		rfqProperty3.setValue("tesla");
		rfqProperty3.setRfqId(this.rfqId);
		rfqProperty3.setVersion(this.rfqIdCache);
		rfqProperty4.setName("quantity");
		rfqProperty4.setValue("100");
		rfqProperty4.setRfqId(this.rfqId);
		rfqProperty4.setVersion(this.rfqIdCache);
		rfqProperties.add(rfqProperty1);
		rfqProperties.add(rfqProperty2);
		rfqProperties.add(rfqProperty3);
		rfqProperties.add(rfqProperty4);
		rfqMessage.setRfqHeader(rfqHeader);
		rfqMessage.setRfqProperties(rfqProperties);

		Gson gson = new Gson();
		String strRfqMessage = gson.toJson(rfqMessage);
		Debug(strRfqMessage);
		String url = "http://localhost:8090/persist";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("rfqMessage", strRfqMessage);
		String result = sendHttpRequest(url, params);
		Debug("reslut=" + result);
	}
	
	private String generateRfqId() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		rfqId = "RFQ-" + format.format(new Date()) + "-" + this.rfqIdCache;
		this.rfqIdCache++;
		return rfqId;
	}

	private String sendHttpRequest(String url, MultiValueMap<String, String> params) {
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpMethod method = HttpMethod.POST;
		// 以表单的方式提交
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		// 将请求头部和参数合成一个请求
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params,
				headers);
		// 执行HTTP请求，将返回的结构使用ResultVO类格式化
		ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
		return response.getBody();
	}

	private void Debug(String log) {
		System.out.println(new Date() + " : " + log);
	}

}
