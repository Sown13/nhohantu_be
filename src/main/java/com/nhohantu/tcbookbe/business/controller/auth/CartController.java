package com.nhohantu.tcbookbe.business.controller.auth;

import com.nhohantu.tcbookbe.business.dto.request.CartRequestDTO;
import com.nhohantu.tcbookbe.business.service.CartService;
import com.nhohantu.tcbookbe.common.model.base.entity.BaseModel;
import com.nhohantu.tcbookbe.common.model.entity.CartModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartModel addCart(@RequestBody CartRequestDTO cartRequestDTO) {
        return cartService.addCart(cartRequestDTO);
    }
}
