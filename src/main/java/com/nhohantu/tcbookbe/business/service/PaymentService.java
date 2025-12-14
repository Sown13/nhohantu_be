package com.nhohantu.tcbookbe.business.service;

import com.nhohantu.tcbookbe.business.dto.OrderMailItem;
import com.nhohantu.tcbookbe.business.dto.request.PayOrderRequest;
import com.nhohantu.tcbookbe.business.dto.response.PayOrderResponse;
import com.nhohantu.tcbookbe.business.repository.IOrderRepository;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.OrderModel;
import com.nhohantu.tcbookbe.common.model.enums.OrderStatus;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import com.nhohantu.tcbookbe.common.service.MailService;
import com.nhohantu.tcbookbe.common.service.UserBasicInfoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final IOrderRepository orderRepository;
    private final UserBasicInfoService userBasicInfoService;
    private final MailService mailService;

    @Transactional
    public ResponseEntity<ResponseDTO<PayOrderResponse>> payOrder(PayOrderRequest request) {

        UserBasicInfoModel user = userBasicInfoService.getUserInfoFromContext();
        if (user == null) {
            return ResponseBuilder.badRequestResponse(
                    "Không tìm thấy user đang đăng nhập",
                    StatusCodeEnum.ERRORCODE4000
            );
        }

        OrderModel order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        if (!order.getUser().getId().equals(user.getId())) {
            return ResponseBuilder.badRequestResponse(
                    "Bạn không có quyền thanh toán đơn hàng này",
                    StatusCodeEnum.ERRORCODE4000
            );
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            return ResponseBuilder.badRequestResponse(
                    "Đơn hàng không ở trạng thái chờ thanh toán",
                    StatusCodeEnum.ERRORCODE4000
            );
        }

        // Update trạng thái
        order.setStatus(OrderStatus.COMPLETED);
//        order.setPaidAt(LocalDateTime.now());
        orderRepository.save(order);

        // Gửi mail
        List<OrderMailItem> mailItems = order.getOrderDetails().stream()
                .map(d -> OrderMailItem.builder()
                        .productName(d.getProductName())
                        .quantity(d.getQuantity())
                        .price(d.getPrice())
                        .build())
                .toList();

        mailService.sendPaymentSuccessMail(
                user.getEmail(),
                order.getId().toString(),
                order.getTotalPrice(),
                mailItems
        );

        // ===== MAP RESPONSE =====
        PayOrderResponse response = PayOrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
//                .paidAt(order.getPaidAt())
                .items(order.getOrderDetails().stream()
                        .map(d -> PayOrderResponse.OrderItem.builder()
                                .productId(d.getProduct().getId())
                                .productName(d.getProductName())
                                .price(d.getPrice())
                                .quantity(d.getQuantity())
                                .productImage(d.getProductImage())
                                .build())
                        .toList())
                .build();

        return ResponseBuilder.okResponse(
                "Thanh toán thành công",
                response,
                StatusCodeEnum.SUCCESS2000
        );
    }

}
