package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.business.dto.request.CartRequestDTO;
import com.nhohantu.tcbookbe.business.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

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

}
