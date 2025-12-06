package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.business.dto.request.AddItemToCartRequest;
import com.nhohantu.tcbookbe.business.dto.response.AddItemToCartResponse;
import com.nhohantu.tcbookbe.business.service.CartService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add-item")
    public ResponseEntity<ResponseDTO<Object>> addItemToCart(@RequestBody AddItemToCartRequest request) {
        return cartService.addItemToCart(request);
    }
}
