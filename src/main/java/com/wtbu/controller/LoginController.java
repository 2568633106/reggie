package com.wtbu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wtbu.common.BaseContext;
import com.wtbu.common.R;
import com.wtbu.common.ValidateCodeUtils;
import com.wtbu.entity.User;
import com.wtbu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/user")
public class LoginController {

    @Autowired
    UserService userService;



    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            //生成随机的4位验证码
//            String code = ValidateCodeUtils.generateValidateCode(4).toString();
//            log.info("code={}",code);

            //调用阿里云提供的短信服务API完成发送短信
            //SMSUtils.sendMessage("瑞吉外卖","",phone,code);

            //需要将生成的验证码保存到Session
//            session.setAttribute(phone,code);
            return R.success("手机验证码短信发送成功");
        }
        return R.error("短信发送失败");
    }



    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */

    //这里由于不能发送短信，不能获取code验证码，因此我直接取消了验证码验证
    //登录一定成功，有电话就直接获取登录，没有的话就生成一个用户存储到数据库让然后登录
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        User user;

            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);

            user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                user.setId(Long.valueOf(UUID.randomUUID().toString()));
                session.setAttribute("user", user.getId());
                userService.save(user);
            } else {
                user.setPhone(map.get("phone").toString());
                session.setAttribute("user", user.getId());
            }
        BaseContext.setCurrentId(user.getId());
        return R.success(user);
    }



}
