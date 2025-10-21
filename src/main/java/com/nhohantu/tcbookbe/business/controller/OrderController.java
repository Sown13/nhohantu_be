package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.business.dto.request.OrderCreateRequest;
import com.nhohantu.tcbookbe.business.dto.response.OrderResponse;
import com.nhohantu.tcbookbe.business.service.OrderService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /** Tạo đơn hàng mới */
    @PostMapping("/make-order")
    public ResponseEntity<ResponseDTO<OrderResponse>> createOrder(@RequestBody OrderCreateRequest request) {
        return orderService.createOrder(request);
    }

    /** Lấy danh sách đơn hàng của user đang đăng nhập */
    @GetMapping
    public ResponseEntity<ResponseDTO<List<OrderResponse>>> getMyOrders() {
        return orderService.getOrdersByCurrentUser();
    }

    /** Xem chi tiết đơn hàng theo ID */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<OrderResponse>> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
}
