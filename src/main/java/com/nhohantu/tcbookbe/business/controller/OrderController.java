package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.business.dto.request.OrderRequest;
import com.nhohantu.tcbookbe.business.service.OrderService;
import com.nhohantu.tcbookbe.common.model.entity.OrderModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public String createOrder(@RequestBody List<OrderRequest> order) {
        return orderService.createOrder(order);
    }

    @DeleteMapping
    public String deleteOrder(@RequestBody OrderRequest order) {
        return orderService.deleteOrder(order);
    }
}
