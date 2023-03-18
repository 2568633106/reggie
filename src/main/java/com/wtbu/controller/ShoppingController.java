package com.wtbu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wtbu.common.BaseContext;
import com.wtbu.common.R;
import com.wtbu.entity.DishDto;
import com.wtbu.entity.SetmealDto;
import com.wtbu.entity.ShoppingCart;
import com.wtbu.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<String> addShopping(HttpServletRequest request, @RequestBody ShoppingCart shoppingCart){
        Long userId = (Long) request.getSession().getAttribute("user");
        shoppingCart.setUserId(userId);
        shoppingCartService.save(shoppingCart);
        return R.success("新增成功");
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> getShoppingList(){
        return R.success(shoppingCartService.list());
    }

    @DeleteMapping("/clean")
    public R<String> cleanShoping(){
        shoppingCartService.remove(null);
        return R.success("清空购物车成功");
    }

}
