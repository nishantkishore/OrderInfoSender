package com.nishant.consumer;

import com.nishant.Domain.OrderInfo;
import com.nishant.service.InfoSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * Created by z001ld8 on 7/30/18.
 */
@Service
@Slf4j
@PropertySource("classpath:bootstrap.properties")
public class OrderInfoConsumer implements AcknowledgingMessageListener<ConsumerRecord, Acknowledgment>  {

    @Value("${orderInfoTopic}")
    private String orderInfoTopic;
    private final String OP_CONSUMING_ORDERINFO = "consume_orders";
    private InfoSenderService emailSenderService;
    private InfoSenderService smsSenderService;

    @Autowired
    public OrderInfoConsumer(@Qualifier("emailSenderService") InfoSenderService emailSenderService,
                             @Qualifier("smsSenderService") InfoSenderService smsSenderService
    )
    {
        this.emailSenderService = emailSenderService;
        this.smsSenderService = smsSenderService;
    }

    @Override
    @KafkaListener(topics = "${orderInfoTopic}")
    public void onMessage(ConsumerRecord record, Acknowledgment acknowledgment) {


        String orderId = (String) record.value();

        OrderInfo orderInfo = getOrderDetailsFromDB();

        log.info("op={}, status=OK, desc=Consumed order ({}) from topic {}",
                OP_CONSUMING_ORDERINFO, orderId, softdeclineTopic
        );

        emailSenderService.Send(orderInfo);
        smsSenderService.Send(orderInfo);



    }

    /**
     * This method will retrieve order details from DB to create and send data to different services
     */
    private OrderInfo getOrderDetailsFromDB() {
        return null;
    }
}
