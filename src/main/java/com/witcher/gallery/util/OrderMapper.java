package com.witcher.gallery.util;

import com.witcher.gallery.enums.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderMapper() {}

    public Order convertToOrder(String s) {
        return Order.valueOf(s);
    }
}
