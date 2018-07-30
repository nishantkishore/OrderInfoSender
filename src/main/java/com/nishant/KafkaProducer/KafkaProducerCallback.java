package com.nishant.KafkaProducer;

import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Created by z001ld8 on 7/30/18.
 */
public class KafkaProducerCallback implements ListenableFutureCallback<SendResult<String, String>> {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = forPattern("yyyy-MM-dd:HH:mm");
    private final String topic;
    private OrderId orderId;
    private OrderTransferFailureEventsRepository orderTransferFailureEventsRepository;

    public KafkaProducerCallback(OrderId orderId, String topic, OrderFailureEventsRepository orderTransferFailureEventsRepository) {
        this.orderId = orderId;
        this.topic = topic;
        this.orderTransferFailureEventsRepository = orderTransferFailureEventsRepository;
    }

    /**
     * In the case that Kafka topic is down, write to failure repo instead
     *
     * @param ex is the exception thrown by KafkaProducer
     */
    @Override
    public void onFailure(Throwable ex) {
        UUID orderUUID = orderId.getUUIDValue();
        log.error("op={}, status=KO, desc=KafkaProducer onFailure() Failed to produce order {} to topic {}, ex {}", OP_NAME, orderUUID, topic, ex);

        log.info("op={}, status=OK, desc=Writing order {} to FailureEventsRepo instead", OP_NAME, orderUUID);
        orderTransferFailureEventsRepository.addOrderTransferFailureEventFor(bucketKey(5), orderUUID);
    }

    @Override
    public void onSuccess(SendResult<String, String> result) {
        String orderIdStr = orderId.getValue();

        if (log.isDebugEnabled()) {
            log.debug("op={}, status=OK, desc=KafkaProducer Successfully produced order ({}) to topic {} with offset: {}",
                    OP_NAME, orderIdStr, topic, result.getRecordMetadata().offset()
            );
        } else {
            log.info("op={}, status=OK, desc=KafkaProducer onSuccess() Successfully produced order ({}) to topic {}",
                    OP_NAME, orderIdStr, topic
            );
        }
    }

}