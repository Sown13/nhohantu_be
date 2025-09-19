package com.nhohantu.tcbookbe.business.service;
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
}
