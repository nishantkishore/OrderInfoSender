package com.nishant.Domain;

import java.util.List;

/**
 * Created by z001ld8 on 7/30/18.
 */

public class OrderInfo {

    private String orderId;
    private String orderStatus;
    private String templateId;
    private List<Item> items;
    private List<Payment> payments;
    private List<Address> addresses;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }


    static class Item {

    }
    static class Payment {

    }
    static class Address {

    }

}
