package edu.unimelb.swen90007.mes.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Money {
    private final String currency;
    private final BigDecimal unitPrice;

    public Money(BigDecimal unitPrice, String currency) {
        this.unitPrice = unitPrice;
        this.currency = currency;
    }
}
