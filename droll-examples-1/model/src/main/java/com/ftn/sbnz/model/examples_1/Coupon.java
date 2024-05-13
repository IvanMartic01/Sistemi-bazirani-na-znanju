package com.ftn.sbnz.model.examples_1;

import com.ftn.sbnz.model.examples_1.enums.CouponType;
import lombok.*;

import java.util.Date;

@Data
public class Coupon {

    private Customer customer;
    private Order order;
    private CouponType type;

    public Coupon(Customer customer, Order order, CouponType type) {
        this.customer = customer;
        this.order = order;
        this.type = type;
    }

    private Date validFrom;
    private Date validUntil;
}
