package com.wtbu.entity;

import com.wtbu.service.DishFlavorService;
import jdk.internal.dynalink.linker.LinkerServices;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class DishDto extends Dish{
    private String categoryName;
    private List<DishFlavor> flavors = new ArrayList<>();
}
