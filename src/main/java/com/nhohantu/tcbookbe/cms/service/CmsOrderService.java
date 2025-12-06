package com.nhohantu.tcbookbe.cms.service;

import com.nhohantu.tcbookbe.cms.dto.response.CmsOrderDetailResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsOrderResponse;
import com.nhohantu.tcbookbe.cms.repository.ICmsOrderRepository;
import com.nhohantu.tcbookbe.common.model.entity.OrderDetailModel;
import com.nhohantu.tcbookbe.common.model.entity.OrderModel;
import com.nhohantu.tcbookbe.common.model.enums.OrderStatus; // THIẾU IMPORT NÀY
import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CmsOrderService {

    @Autowired
    private ICmsOrderRepository cmsOrderRepository;

    // 1. Lấy tất cả đơn hàng (Admin)
    public List<CmsOrderResponse> getAllOrders() {
        List<OrderModel> orders = cmsOrderRepository.findAll();

        return orders.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 2. Xem chi tiết 1 đơn
    public CmsOrderResponse getOrderDetail(Long orderId) {
        OrderModel order = cmsOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return mapToResponse(order);
    }

    // 3. Thêm method update status
    public CmsOrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        OrderModel order = cmsOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);

        OrderModel updatedOrder = cmsOrderRepository.save(order);

        return mapToResponse(updatedOrder);
    }

    // ================= MAP FUNCTION =================

    private CmsOrderResponse mapToResponse(OrderModel order) {

        CmsOrderResponse response = new CmsOrderResponse();

        UserBasicInfoModel user = order.getUser();

        response.setOrderId(order.getId());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());

        if (user != null) {
            response.setUsername(user.getUsername());
            response.setEmail(user.getEmail());
            response.setPhone(user.getPrimaryPhone());
            response.setAddress(user.getAddress());
            response.setFullName(user.getFirstName() + " " + user.getLastName());
        }

        List<CmsOrderDetailResponse> detailResponses = order.getOrderDetails()
                .stream()
                .map(this::mapToDetail)
                .collect(Collectors.toList());

        response.setOrderDetails(detailResponses);

        return response;
    }

    private CmsOrderDetailResponse mapToDetail(OrderDetailModel detail) {

        CmsOrderDetailResponse response = new CmsOrderDetailResponse();

        if (detail.getProduct() != null) {
            response.setProductId(detail.getProduct().getId());
        }

        response.setProductName(detail.getProductName());
        response.setProductImage(detail.getProductImage());
        response.setPrice(detail.getPrice());
        response.setQuantity(detail.getQuantity());

        return response;
    }
}