package com.citi.skynet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaService {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void SendMessage(String key, String value) {
		kafkaTemplate.send("skynet.topic", key, value);
	}

}
