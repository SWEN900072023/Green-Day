package edu.unimelb.swen90007.mes.model;

import java.math.BigDecimal;

public class Money {
    private final String currency;
    private BigDecimal price;

    public Money(BigDecimal price, String currency) {
        this.price = price;
        this.currency = currency;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }
}
