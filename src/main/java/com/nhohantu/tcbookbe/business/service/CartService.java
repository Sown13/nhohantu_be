package com.nhohantu.tcbookbe.business.service;
<<<<<<< HEAD
import com.nhohantu.tcbookbe.business.dto.request.CartRequestDTO;
import com.nhohantu.tcbookbe.business.repository.ICartItemRepository;
import com.nhohantu.tcbookbe.business.repository.ICartRepository;
import com.nhohantu.tcbookbe.business.repository.IProductRepository;
import com.nhohantu.tcbookbe.common.model.entity.CartItem;
import com.nhohantu.tcbookbe.common.model.entity.CartModel;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import com.nhohantu.tcbookbe.common.repository.BaseUserInfoRepo;
import com.nhohantu.tcbookbe.common.utils.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {
    private final ICartRepository cartRepository;
    private final ICartItemRepository cartItemRepository;
    private final AuthUtil authUtil;
    private final IProductRepository productRepository;
    private final BaseUserInfoRepo userInfoRepo;

    public CartModel addCart(CartRequestDTO cartRequestDTO) {

        UserDetails currentUser = authUtil.getCurrentUser();
        UserBasicInfoModel user = userInfoRepo.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));


        CartModel cartModel = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    CartModel newCart = new CartModel();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });


        ProductModel productModel = productRepository.findById(cartRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));


        List<CartItem> items = cartItemRepository.findByCart(cartModel);
        Optional<CartItem> existingItem = items.stream()
                .filter(e -> e.getProduct().getId().equals(productModel.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartRequestDTO.getQuantity());
            cartItemRepository.save(item);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cartModel);
            newCartItem.setProduct(productModel);
            newCartItem.setQuantity(cartRequestDTO.getQuantity());
            cartItemRepository.save(newCartItem);
        }

        return cartModel;
    }

    public void deleteCartItem(CartRequestDTO cartRequestDTO) {
        UserDetails currentUser = authUtil.getCurrentUser();
        UserBasicInfoModel user = userInfoRepo.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartModel cartModel = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> items = cartItemRepository.findByCart(cartModel);

        Optional<CartItem> existingItem = items.stream()
                .filter(e -> e.getProduct().getId().equals(cartRequestDTO.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {

            cartItemRepository.deleteById(existingItem.get().getId());


            if (cartItemRepository.findByCart(cartModel).isEmpty()) {
                cartRepository.delete(cartModel);
            }
        } else {
            throw new RuntimeException("Product not found in cart");
=======

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
>>>>>>> 52092c56e57812fd35f5e0a684f56b3eb63f817e
        }
    }
}
