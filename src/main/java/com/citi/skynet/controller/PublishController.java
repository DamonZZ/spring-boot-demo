package com.citi.skynet.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citi.skynet.common.model.RfqHeader;
import com.google.gson.Gson;

@RestController
public class PublishController {

	@RequestMapping("publishNew")
	public void Publish(String rfqJson) {
		Gson gson = new Gson();
		try {
			RfqHeader rfqHeader = gson.fromJson(rfqJson, RfqHeader.class);
			if(rfqHeader!=null) {
				
			}
		}catch(Exception ex) {
			
		}
	}
}
