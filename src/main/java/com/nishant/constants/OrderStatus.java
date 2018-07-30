package com.nishant.constants;

/**
 * Created by z001ld8 on 7/30/18.
 */
public enum OrderStatus {
    COMPLETED("C"), CANCELLED("X"), SOFTDECLINED("SD");
    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
