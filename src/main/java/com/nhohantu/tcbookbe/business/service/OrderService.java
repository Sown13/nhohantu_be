package com.nhohantu.tcbookbe.business.service;

import com.nhohantu.tcbookbe.business.dto.request.OrderCreateRequest;
import com.nhohantu.tcbookbe.business.dto.response.OrderResponse;
import com.nhohantu.tcbookbe.business.repository.IOrderDetailRepository;
import com.nhohantu.tcbookbe.business.repository.IOrderRepository;
import com.nhohantu.tcbookbe.business.repository.IProductRepository;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.OrderDetailModel;
import com.nhohantu.tcbookbe.common.model.entity.OrderModel;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import com.nhohantu.tcbookbe.common.model.enums.OrderStatus;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import com.nhohantu.tcbookbe.common.service.UserBasicInfoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final IOrderRepository orderRepository;
    private final IOrderDetailRepository orderDetailRepository;
    private final IProductRepository productRepository;
    private final UserBasicInfoService userBasicInfoService;
    private final ModelMapper modelMapper;

    @Transactional
    public ResponseEntity<ResponseDTO<OrderResponse>> createOrder(OrderCreateRequest request) {
        try {
            //ấy user đang đăng nhập
            UserBasicInfoModel userBasicInfoModel = userBasicInfoService.getUserInfoFromContext();
            if (userBasicInfoModel == null) {
                return ResponseBuilder.badRequestResponse(
                        "Không tìm thấy thông tin user đang đăng nhập",
                        StatusCodeEnum.ERRORCODE4000
                );
            }

            //Tạo đơn hàng mới
            OrderModel order = OrderModel.builder()
                    .user(userBasicInfoModel)
                    .status(OrderStatus.PENDING)
                    .totalPrice(request.getTotal())
                    .build();

            order = orderRepository.save(order);

            //Tạo chi tiết đơn hàng
            OrderModel finalOrder = order;
            var details = request.getItems().stream().map(item -> {
                ProductModel product = productRepository.findById(item.getId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + item.getId()));

                if (product.getQuantity() < item.getQuantity()) {
                    throw new RuntimeException("Sản phẩm '" + product.getName() + "' không đủ hàng trong kho.");
                }

                product.setQuantity(product.getQuantity() - item.getQuantity());
                product.setSold(product.getSold() + item.getQuantity());
                productRepository.save(product);

                return OrderDetailModel.builder()
                        .order(finalOrder)
                        .product(product)
                        .productName(product.getName())
                        .productImage(product.getMainImageUrl())
                        .price(product.getSalePrice() != null ? product.getSalePrice() : product.getPrice())
                        .quantity(item.getQuantity())
                        .build();
            }).collect(Collectors.toList());

            orderDetailRepository.saveAll(details);

            //Map sang DTO để trả về
            OrderResponse orderResponse = modelMapper.map(order, OrderResponse.class);

            return ResponseBuilder.okResponse(
                    "Đặt hàng thành công",
                    orderResponse,
                    StatusCodeEnum.SUCCESS2000
            );

        } catch (Exception e) {
            log.error("Error while creating order", e);
            return ResponseBuilder.badRequestResponse(
                    "Có lỗi xảy ra trong quá trình đặt hàng",
                    StatusCodeEnum.ERRORCODE4000
            );
        }
    }

    /** Lấy danh sách đơn hàng của user */
    public ResponseEntity<ResponseDTO<List<OrderResponse>>> getOrdersByCurrentUser() {
        UserBasicInfoModel user = userBasicInfoService.getUserInfoFromContext();
        if (user == null) {
            return ResponseBuilder.badRequestResponse("Không tìm thấy user đang đăng nhập", StatusCodeEnum.ERRORCODE4000);
        }

        List<OrderModel> orders = orderRepository.findByUserId(user.getId());
        List<OrderResponse> responses = orders.stream()
                .map(order -> modelMapper.map(order, OrderResponse.class))
                .toList();

        return ResponseBuilder.okResponse("SUCCESS", responses, StatusCodeEnum.SUCCESS2000);
    }

    /** Lấy chi tiết đơn hàng theo id */
    public ResponseEntity<ResponseDTO<OrderResponse>> getOrderById(Long id) {
        Optional<OrderModel> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            return ResponseBuilder.badRequestResponse("Không tìm thấy đơn hàng", StatusCodeEnum.ERRORCODE4000);
        }

        OrderModel order = orderOpt.get();

        order.getOrderDetails().size();

        OrderResponse response = modelMapper.map(order, OrderResponse.class);

        // Tự map thủ công phần chi tiết
        List<OrderResponse.OrderItemResponse> detailResponses = order.getOrderDetails().stream()
                .map(detail -> OrderResponse.OrderItemResponse.builder()
                        .id(detail.getId())
                        .name(detail.getProductName())
                        .price(detail.getPrice())
                        .quantity(detail.getQuantity())
                        .productImage(detail.getProductImage())
                        .build())
                .toList();

        response.setOrderDetails(detailResponses);

        return ResponseBuilder.okResponse("SUCCESS", response, StatusCodeEnum.SUCCESS2000);
    }
}
