package com.nhohantu.tcbookbe.business.service;

import com.nhohantu.tcbookbe.business.dto.request.OrderRequest;
import com.nhohantu.tcbookbe.business.repository.IOrderDetailRepository;
import com.nhohantu.tcbookbe.business.repository.IOrderRepository;
import com.nhohantu.tcbookbe.business.repository.IProductRepository;
import com.nhohantu.tcbookbe.common.model.entity.OrderDetailModel;
import com.nhohantu.tcbookbe.common.model.entity.OrderModel;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import com.nhohantu.tcbookbe.common.repository.BaseUserInfoRepo;
import com.nhohantu.tcbookbe.common.utils.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final IOrderRepository orderRepository;
    private final AuthUtil authUtil;
    private final BaseUserInfoRepo userInfoRepo;
    private final IProductRepository productRepository;
    private final IOrderDetailRepository orderDetailRepository;

    public String createOrder (List<OrderRequest> order) {
        UserDetails getCurrentUser = authUtil.getCurrentUser();
        UserBasicInfoModel user = userInfoRepo.findByUsername(getCurrentUser.getUsername()).get();
        OrderModel orderModel = new OrderModel();
        orderModel.setUser(user);
        orderModel.setStatus("New");

        orderModel = orderRepository.save(orderModel);

        for (OrderRequest orderRequest : order) {
            OrderDetailModel orderDetailModel = new OrderDetailModel();
            orderDetailModel.setOrder(orderModel);
//            orderDetailModel.setPrice(orderRequest.getPrice());

            orderDetailModel.setProduct(productRepository.findById(orderRequest.getOrderId()).get());
            orderDetailRepository.save(orderDetailModel);
        }

        return "create ok";
    }

    public String deleteOrder(OrderRequest order) {
        UserDetails getCurrentUser = authUtil.getCurrentUser();
        UserBasicInfoModel user = userInfoRepo.findByUsername(getCurrentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("không tìm thấy user"));

        OrderModel orderModel = orderRepository.findById(order.getOrderId())
                .orElseThrow(() -> new RuntimeException("không tìm thấy order"));

        if (!orderModel.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("không xóa được order");
        }

        orderDetailRepository.deleteById(orderModel.getId());
        orderRepository.delete(orderModel);
        return "delete ok";
    }
}
