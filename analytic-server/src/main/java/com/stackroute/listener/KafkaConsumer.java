package com.stackroute.listener;

import com.stackroute.domain.Paragraph;
import com.stackroute.service.ParagraphProviderService;
import com.stackroute.service.ParagraphService;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {
    private ParagraphService paragraphService;
    private KafkaProducer kafkaProducer;
    private ParagraphProviderService paragraphProviderService;

    @Autowired
    public KafkaConsumer(ParagraphProviderService paragraphProviderService, ParagraphService paragraphService, KafkaProducer kafkaProducer) {
        this.paragraphService = paragraphService;
        this.kafkaProducer = kafkaProducer;
        this.paragraphProviderService = paragraphProviderService;
    }
    @KafkaListener(topics = "ParagraphContents", groupId = "group_id")
    public void consume(String message) {
        JSONObject object = (JSONObject) JSONValue.parse(message);
        Paragraph paragraph = new Paragraph(object.get("paragraphId").toString(), object.get("paragraphText").toString(), object.get("documentId").toString());
        paragraphService.takeParagraph(paragraph);
        paragraphProviderService.setParagraph(paragraph);
        kafkaProducer.postservice();
    }
}