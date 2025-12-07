package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.dto.response.CmsOrderResponse;
import com.nhohantu.tcbookbe.cms.service.CmsOrderService;
import com.nhohantu.tcbookbe.common.model.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cms/orders")
public class CmsOrderController {

    @Autowired
    private CmsOrderService cmsOrderService;

    // 1. Xem danh sách người mua + đơn hàng
    @GetMapping
    public List<CmsOrderResponse> getAllOrders() {
        return cmsOrderService.getAllOrders();
    }

    // 2. Xem chi tiết 1 đơn
    @GetMapping("/{id}")
    public CmsOrderResponse getOrderDetail(@PathVariable Long id) {
        return cmsOrderService.getOrderDetail(id);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CmsOrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) { // Dùng enum trực tiếp
        CmsOrderResponse updatedOrder = cmsOrderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }
}
