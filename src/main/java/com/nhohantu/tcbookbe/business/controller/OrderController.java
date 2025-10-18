package com.nhohantu.tcbookbe.business.controller;

<<<<<<< HEAD
import com.nhohantu.tcbookbe.business.dto.request.OrderRequest;
import com.nhohantu.tcbookbe.business.service.OrderService;
import com.nhohantu.tcbookbe.common.model.entity.OrderModel;
import org.springframework.web.bind.annotation.*;
=======
import com.nhohantu.tcbookbe.business.dto.request.OrderCreateRequest;
import com.nhohantu.tcbookbe.business.dto.response.OrderResponse;
import com.nhohantu.tcbookbe.business.service.OrderService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
>>>>>>> 52092c56e57812fd35f5e0a684f56b3eb63f817e

import java.util.List;

@RestController
<<<<<<< HEAD
@RequestMapping("/order")
=======
@RequestMapping("/orders")
@RequiredArgsConstructor
>>>>>>> 52092c56e57812fd35f5e0a684f56b3eb63f817e
public class OrderController {

    private final OrderService orderService;

<<<<<<< HEAD
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
=======
    @PostMapping("/make-order")
    public ResponseEntity<ResponseDTO<OrderResponse>> createOrder(@RequestBody OrderCreateRequest request) {
        return orderService.createOrder(request);
    }

    /** Lấy danh sách đơn hàng của user đang đăng nhập */
    @GetMapping
    public ResponseEntity<ResponseDTO<List<OrderResponse>>> getMyOrders() {
        return orderService.getOrdersByCurrentUser();
    }

    /** Xem chi tiết đơn hàng */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<OrderResponse>> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
>>>>>>> 52092c56e57812fd35f5e0a684f56b3eb63f817e
    }
}
