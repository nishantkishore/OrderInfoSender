package com.nishant.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by z001ld8 on 7/30/18.
 * Here all the configs required for kafka consumer are loaded during server startup from configurations table
 */
@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean(name = "consumerFactory")
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, orderConfigurationService.getKafkaBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, orderConfigurationService.getConsumerGroupId()); //"order.softdeclined.group.id"
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, orderConfigurationService.getConsumerClientID()); //"order.softdeclined.client.id"
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, orderConfigurationService.isKafkaEnableAutoCommitEnabled());
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 15000);

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, orderConfigurationService.getKafkaAutoOffsetReset());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        enhancePropsWithSSL(props);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    @Autowired
    @DependsOn("consumerFactory")
    public ConcurrentKafkaListenerContainerFactory kafkaListenerContainerFactory(ConsumerFactory consumerFactory) {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        factory.setConcurrency(orderConfigurationService.getDefaultKafkaConsumers());
        factory.getContainerProperties().setPollTimeout(Long.parseLong(orderConfigurationService.getConsumerPollTimeout()));
        factory.getContainerProperties().setAckMode(AbstractMessageListenerContainer.AckMode.MANUAL);

        return factory;
    }
}
