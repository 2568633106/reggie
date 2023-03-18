package com.wtbu.controller;

import com.wtbu.service.DishFlavorImpl;
import com.wtbu.service.DishFlavorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DishFlavorController {
    @Autowired
    DishFlavorService dishFlavorService;
}
