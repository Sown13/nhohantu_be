package com.nhohantu.tcbookbe.business.service;

import com.nhohantu.tcbookbe.business.dto.request.AddItemToCartRequest;
import com.nhohantu.tcbookbe.business.repository.ICartItemRepository;
import com.nhohantu.tcbookbe.business.repository.ICartRepository;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.CartItemModel;
import com.nhohantu.tcbookbe.common.model.entity.CartModel;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import com.nhohantu.tcbookbe.common.service.UserBasicInfoService;
import lombok.AllArgsConstructor;
import lombok.CustomLog;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@CustomLog
@AllArgsConstructor
public class CartService {
    //repo
    private final ICartRepository cartRepository;
    private final ICartItemRepository cartItemRepository;

    //service
    private final UserBasicInfoService userBasicInfoService;
    private final ModelMapper modelMapper;

    @Transactional
    public ResponseEntity<ResponseDTO<Object>> addItemToCart(AddItemToCartRequest request) {
        UserBasicInfoModel userBasicInfoModel;
        userBasicInfoModel = userBasicInfoService.getUserInfoFromContext();
        if (userBasicInfoModel == null) {
            return ResponseBuilder.badRequestResponse("Không tìm thấy thông tin user đang đăng nhập",
                    StatusCodeEnum.ERRORCODE4000);
        }

        //hiện tại thì 1 user 1 cart, mở rộng sau
        CartModel currentUserCart = cartRepository.findCartModelByUserId(userBasicInfoModel.getId()).orElse(null);
        boolean isNewCart = false;
        if (currentUserCart == null) {
            currentUserCart = new CartModel(userBasicInfoModel);
            isNewCart = true;
        }

        ProductModel productToAdd = modelMapper.map(request, ProductModel.class);

        CartItemModel cartItem = CartItemModel.builder()
                .cart(currentUserCart)
                .product(productToAdd)
                .quantity(request.getQuantity())
                .build();

        try {
            if (isNewCart) {
                cartRepository.save(currentUserCart);
            }

            //batch save
            cartItemRepository.save(cartItem);

            return ResponseBuilder.okResponse("Thêm vào giỏ hàng thành công", StatusCodeEnum.SUCCESS2000);
        } catch (Exception e) {
            return ResponseBuilder.badRequestResponse("Thêm vào giỏ hàng thất bại", StatusCodeEnum.ERRORCODE4000);
        }
    }
}
