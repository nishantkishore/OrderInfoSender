package com.nishant.KafkaProducer;

import org.springframework.util.concurrent.ListenableFuture;

/**
 * Created by z001ld8 on 7/30/18.
 */
public interface KafkaProducer {
    void send(Object object);
}
