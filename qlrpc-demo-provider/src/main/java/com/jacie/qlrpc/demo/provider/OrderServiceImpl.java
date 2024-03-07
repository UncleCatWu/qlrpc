package com.jacie.qlrpc.demo.provider;

import com.jacie.qlrpc.demo.api.Order;
import com.jacie.qlrpc.demo.api.OrderService;
import com.ql.qlrpc.core.annotation.QlProvider;
import org.springframework.stereotype.Component;

@Component
@QlProvider
public class OrderServiceImpl implements OrderService {
    @Override
    public Order findById(Integer id) {
        return new Order(id.longValue(), 15.6f);
    }
}
