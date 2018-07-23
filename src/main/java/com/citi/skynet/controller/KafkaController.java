package com.citi.skynet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citi.skynet.service.KafkaService;

@RestController
public class KafkaController {
	@Autowired
	private KafkaService kafkaService;
	
	@RequestMapping("kafka")
	public void sendMessage(){
		String key = "msg";
		String value = "kafka test";
		kafkaService.SendMessage(key, value);
	}
	
}
