package com.nishant.service;

import com.nishant.Domain.OrderInfo;
import com.nishant.Domain.SMSData;
import com.nishant.KafkaProducer.SMSProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by z001ld8 on 7/30/18.
 */
@Service
public class SMSSenderServiceImpl implements InfoSenderService {
    private SMSProducer smsProducer;

    @Autowired
    public SMSSenderServiceImpl(SMSProducer smsProducer) {
        this.smsProducer = smsProducer;
    }

    @Override
    public void Send(OrderInfo orderInfo) {

        SMSData smsData = mapSMSDataFromOrderInfo();

        smsProducer.send(smsData);
    }

    private SMSData mapSMSDataFromOrderInfo() {
       return null;
    }
}
