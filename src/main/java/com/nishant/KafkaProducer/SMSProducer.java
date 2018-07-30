package com.nishant.KafkaProducer;

import com.nishant.service.OrderConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by z001ld8 on 7/30/18.
 */
@Component
public class SMSProducer implements KafkaProducer {
    private KafkaTemplate<String, String> orderProducer;
    private OrderConfigurationService orderConfigurationService;
    private KafkaProducerCallback kafkaProducerCallback;


    @Autowired
    public SMSProducer(KafkaTemplate<String, String> orderProducer, OrderConfigurationService orderConfigurationService,
                       KafkaProducerCallback kafkaProducerCallback) {
        this.orderProducer = orderProducer;
        this.orderConfigurationService = orderConfigurationService;
        this.kafkaProducerCallback = kafkaProducerCallback;
    }


    @Override
    public void send(Object object) {
        String orderTransferTopic = orderConfigurationService.getKafkaSMSTransferTopic();
        log.info("op={}, status=OK, desc=Producing order ({}) to topic {}",
                OP_NAME, orderJson, orderTransferTopic
        );

        return orderProducer.send(orderTransferTopic, object).addCallback(kafkaProducerCallback);
        /*
        add a callback here so that if produce message to kafka fails then the
        record will be inserted in db and which can be reprocessed later by a scheduler
         */
    }
}
