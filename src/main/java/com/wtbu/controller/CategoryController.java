package com.wtbu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wtbu.common.R;
import com.wtbu.entity.Category;
import com.wtbu.entity.Dish;
import com.wtbu.service.CategoryService;
import com.wtbu.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/category")
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DishService dishService;

    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        if (categoryService.getById(category)!=null) {
            return R.success("分类已存在");
        }
        categoryService.saveOrUpdate(category);
        return R.success("添加分类成功");
    }

    @GetMapping("/page")
    public R<Page<Category>> getPage(int page,int pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        categoryService.CategoryremoveById(ids);
        return R.success("删除分类成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }
    @GetMapping("/list")
    public R<List<Category>> getCategoryList(){

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);

//        List<Category> collect = list.stream().map((item) -> {
//            LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            dishLambdaQueryWrapper.like(Dish::getCategoryId, item.getId());
//            if (dishService.count(dishLambdaQueryWrapper) != 0) {
//                Category category = new Category();
//                BeanUtils.copyProperties(item, category);
//                return category;
//            }
//            return null;
//        }).collect(Collectors.toList());

        return R.success(list);
    }
}
