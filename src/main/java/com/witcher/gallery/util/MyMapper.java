package com.witcher.gallery.util;

import com.witcher.gallery.enums.Order;
import org.springframework.stereotype.Component;

@Component
public class MyMapper {

    public MyMapper() {}

    public Order convertToOrder(String s) {
        return Order.valueOf(s);
    }

}
