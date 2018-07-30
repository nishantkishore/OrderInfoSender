package com.nishant.service;

import com.nishant.Domain.EmailData;
import com.nishant.Domain.InvoiceStatus;
import com.nishant.Domain.OrderInfo;
import com.nishant.KafkaProducer.EmailProducer;
import com.nishant.constants.OrderStatus;
import org.springframework.stereotype.Service;

/**
 * Created by z001ld8 on 7/30/18.
 */
@Service
public class EmailSenderServiceImpl implements InfoSenderService {

    private InvoiceServiceImpl invoiceService;
    private EmailProducer emailProducer;

    public EmailSenderServiceImpl(InvoiceServiceImpl invoiceServiceImpl) {
        this.invoiceService = invoiceServiceImpl;
    }

    @Override
    public void Send(OrderInfo orderInfo) {

        //don't send mail if the payment system was down and payment got soft declined.
        if (OrderStatus.SOFTDECLINED.getStatus().equals(orderInfo.getOrderStatus())) {
            return;
        }

        if (OrderStatus.COMPLETED.getStatus().equals(orderInfo.getOrderStatus())) {
            orderInfo.setTemplateId("idForCompletedOrder");
        } else {
            orderInfo.setTemplateId("idForCancelledOrder");
        }

        EmailData emailData = mapEmailDataFromOrderInfo();

        InvoiceStatus invoiceStatus = invoiceService.getInvoiceStatus(orderInfo.getOrderId());
        /*
        1. if invoice status is generated add all the invoices to be sent to kafka queue
        2. if status is not generated add the message ""We will send invoice soon"
         */

        emailProducer.send(emailData);
    }

    private EmailData mapEmailDataFromOrderInfo() {
        return null;
    }
}
