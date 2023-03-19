package com.wtbu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wtbu.common.CustomException;
import com.wtbu.common.R;
import com.wtbu.entity.*;
import com.wtbu.service.DishFlavorService;
import com.wtbu.service.DishService;
import com.wtbu.service.SetmealDishService;
import com.wtbu.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;
    @PostMapping
    public R<String> saveSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId().toString());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
        return R.success("新增套餐成功");
    }

    @GetMapping("/page")
    public R<Page<Setmeal>> getList(Integer page,Integer pageSize,String name){
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name)
                .orderByAsc(Setmeal::getUpdateTime);
        return R.success(setmealService.page(pageInfo,queryWrapper));
    }

    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> deleteSetmeal(Long ids){
        if (setmealService.getById(ids).getStatus()==1) {
            throw new CustomException("套餐正在售卖，不能删除");
        }
        setmealService.removeById(ids);
        setmealDishService.removeById(ids);
        return R.success("删除套餐成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(Long[] ids,@PathVariable Integer status){
        for (Long id :
                ids) {
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }
        return R.success("修改成功");
    }

    @GetMapping("/list")
    @Cacheable(value = "setmealCache" ,key = "#setmeal.categoryId+'_'+setmeal.status")
    public R<List<DishDto>> getList(Long categoryId, Integer status){
        if (status==0) return null;
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Dish::getCategoryId,categoryId);
        List<Dish> dishs = dishService.list(queryWrapper);

        List<DishDto> collect = dishs.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            LambdaQueryWrapper<DishFlavor> dishFlavorWrapper = new LambdaQueryWrapper<>();
            dishFlavorWrapper.like(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorWrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(collect);
    }


}
