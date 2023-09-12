package edu.unimelb.swen90007.mes.model;

import java.math.BigDecimal;

public class Money {
    private final String currency;
    private BigDecimal unitPrice;

    public Money(BigDecimal unitPrice, String currency) {
        this.unitPrice = unitPrice;
        this.currency = currency;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCurrency() {
        return currency;
    }
}
