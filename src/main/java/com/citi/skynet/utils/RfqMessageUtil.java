package com.citi.skynet.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.citi.skynet.common.model.RfqMessage;
import com.citi.skynet.common.model.RfqProperty;

public class RfqMessageUtil {

	private static Logger looger = LoggerFactory.getLogger(RfqMessageUtil.class);

	public static RfqMessage enRichRfqMessage(RfqMessage rfqMessage) {
		if (rfqMessage == null) {
			return rfqMessage;
		}

		// enrich rfq_id and message_id
		String rfqId = rfqMessage.getRfqHeader().getRfqId();
		if (rfqId == null || rfqId.isEmpty() || rfqId.compareToIgnoreCase("") == 0) {
			rfqMessage.getRfqHeader().setRfqId(generateRfqId());
			rfqId = rfqMessage.getRfqHeader().getRfqId();
			rfqMessage.getRfqHeader().setVersion(1);

		}

		// enrich messageId
		String messageId = rfqMessage.getRfqHeader().getMessageId();
		if (messageId == null || messageId.isEmpty()) {
			rfqMessage.getRfqHeader().setMessageId(rfqMessage.getRfqHeader().getRfqId());
		}

		// enrich parent_id
		if (rfqMessage.getRfqHeader().getParentId() == null) {
			rfqMessage.getRfqHeader().setParentId("");
			;
		}
		// enrich flow
		if (rfqMessage.getRfqHeader().getFlow() == null) {
			rfqMessage.getRfqHeader().setFlow("saas");
		}
		// enrich state
		if (rfqMessage.getRfqHeader().getState() == null) {
			rfqMessage.getRfqHeader().setState("");
		}
		// enrich sent_by
		if (rfqMessage.getRfqHeader().getSentBy() == null) {
			rfqMessage.getRfqHeader().setSentBy("");
		}
		// enrich event
		if (rfqMessage.getRfqHeader().getEvent() == null) {
			rfqMessage.getRfqHeader().setEvent("");
		}
		// enrich timestamp
		if (rfqMessage.getRfqHeader().getTimestamp() == null) {
			rfqMessage.getRfqHeader().setTimestamp(new Date());
		}
		// enrich roledefinition
		if (rfqMessage.getRfqHeader().getRoledefinition() == null) {
			rfqMessage.getRfqHeader().setRoutingtargets("");
		}
		// enrich routingtargets
		if (rfqMessage.getRfqHeader().getRoutingtargets() == null) {
			rfqMessage.getRfqHeader().setRoutingtargets("");
		}

		// enrich rfqproperties
		for (RfqProperty property : rfqMessage.getRfqProperties()) {
			property.setRfqId(rfqId);
			property.setVersion(rfqMessage.getRfqHeader().getVersion());
		}

		return rfqMessage;
	}

	private static String generateRfqId() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss");
		return "RFQ-" + format.format(new Date());
	}

}
