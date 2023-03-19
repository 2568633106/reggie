package com.wtbu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wtbu.common.R;
import com.wtbu.entity.Category;
import com.wtbu.entity.Dish;
import com.wtbu.entity.DishDto;
import com.wtbu.entity.DishFlavor;
import com.wtbu.service.CategoryService;
import com.wtbu.service.DishFlavorService;
import com.wtbu.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    DishService dishService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    DishFlavorService dishFlavorService;

    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping
    public R<String> addDish(@RequestBody  DishDto dishDto){
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);


        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page<DishDto>> getList(int page,int pageSize,String name){
        Page<DishDto> dishDtoPage = new Page<>();
        Page<Dish> dishPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Dish::getName,name).orderByAsc(Dish::getSort);
        dishService.page(dishPage,queryWrapper);

        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");

        List<Dish> records = dishPage.getRecords();

        List<DishDto> collect = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            BeanUtils.copyProperties(item,dishDto);
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(collect);
        return R.success(dishDtoPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> show(@PathVariable Long id){
        Dish dish = dishService.getById(id);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavor = dishFlavorService.list(queryWrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(dishFlavor);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        String keys="dish_*";
        redisTemplate.delete(keys);

        dishService.updateById(dishDto);
        List<DishFlavor> flavors = dishDto.getFlavors();

        Long dishId = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(DishFlavor::getDishId,dishId);
        dishFlavorService.remove(queryWrapper);

        flavors = flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        if (flavors.size()!=0) {
            dishFlavorService.saveBatch(flavors);
        }

        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<DishDto>> getCategoryList(Dish dish){

        List<DishDto> dishDtoList=null;
        String key="dish_"+dish.getCategoryId()+" "+dish.getStatus();
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if (dishDtoList!=null&&dishDtoList.size()!=0){
            return R.success(dishDtoList);
        }


        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime)
                .like(Dish::getCategoryId,dish.getCategoryId());
        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.like(item.getId()!=null,DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);
            BeanUtils.copyProperties(item, dishDto);
            return dishDto;
        }).collect(Collectors.toList());

        //如果不存在，需要查询数据库，将查询到的菜品数据缓存到Redis
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);


        return R.success(dishDtoList);
    }

//    @GetMapping("/list")
//    public R<List<DishDto>> list(Dish dish){
//        //构造查询条件
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
//        //添加条件，查询状态为1（起售状态）的菜品
//        queryWrapper.eq(Dish::getStatus,1);
//        //添加排序条件
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//
//        List<Dish> list = dishService.list(queryWrapper);
//
//        List<DishDto> dishDtoList = list.stream().map((item) -> {
//            DishDto dishDto = new DishDto();
//            BeanUtils.copyProperties(item,dishDto);
//
//            Long categoryId = item.getCategoryId();//分类id
//            //根据id查询分类对象
//            Category category = categoryService.getById(categoryId);
//            if(category != null){
//                String categoryName = category.getName();
//                dishDto.setCategoryName(categoryName);
//            }
//
//            //当前菜品的id
//            Long dishId = item.getId();
//            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
//            //SQL:select * from dish_flavor where dish_id = ?
//            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
//            dishDto.setFlavors(dishFlavorList);
//
//            return dishDto;
//        }).collect(Collectors.toList());
//
//        return R.success(dishDtoList);
//    }

}
