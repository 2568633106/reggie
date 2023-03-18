package com.wtbu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wtbu.common.CustomException;
import com.wtbu.common.R;
import com.wtbu.entity.Category;
import com.wtbu.entity.Dish;
import com.wtbu.entity.Setmeal;
import com.wtbu.mapper.CategoryMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public R<String> CategoryremoveById(Long id) {
        Category category = getById(id);
        Integer type = category.getType();
        if (type==1){
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(Dish::getCategoryId,category.getId());
            if (dishService.list(queryWrapper).size()!=0) {
                throw new CustomException("菜品不为空");
            }
        }else {
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(Setmeal::getCategoryId,category.getId());
            if (setmealService.list(queryWrapper).size()!=0) {
                throw new CustomException("套餐不为空");
            }
        }
        removeById(category.getId());
        return R.success("删除成功");
    }
}
