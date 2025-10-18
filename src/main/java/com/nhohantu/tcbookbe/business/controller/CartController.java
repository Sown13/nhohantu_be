package com.nhohantu.tcbookbe.business.controller;

<<<<<<< HEAD
import com.nhohantu.tcbookbe.business.dto.request.CartRequestDTO;
import com.nhohantu.tcbookbe.business.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
=======
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
>>>>>>> 52092c56e57812fd35f5e0a684f56b3eb63f817e

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
<<<<<<< HEAD

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addCart(@RequestBody CartRequestDTO cartRequestDTO) {
        cartService.addCart(cartRequestDTO);
        return ResponseEntity.ok("add item successfully");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCart(@RequestBody CartRequestDTO cartRequestDTO) {
        cartService.deleteCartItem(cartRequestDTO);
        return ResponseEntity.ok("Item deleted successfully");
    }

=======
    private final CartService cartService;

    @PostMapping("/add-item")
    public ResponseEntity<ResponseDTO<Object>> addItemToCart(@RequestBody AddItemToCartRequest request) {
        return cartService.addItemToCart(request);
    }
>>>>>>> 52092c56e57812fd35f5e0a684f56b3eb63f817e
}
