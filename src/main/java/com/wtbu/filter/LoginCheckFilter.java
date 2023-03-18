package com.wtbu.filter;

import com.alibaba.fastjson.JSON;
import com.wtbu.common.BaseContext;
import com.wtbu.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String[] urls=new String[]{
                "/backend/**",
                "/front/**",
                "/employee/login",
                "/employee/logout",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        String url=request.getRequestURI();

        if (check(urls,url)) {
            filterChain.doFilter(request,response);
            return;
        }

        //4-2、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }


        if (request.getSession().getAttribute("id")!=null){
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("id"));
            filterChain.doFilter(request,response);
            return;
        }

        //尝试未登录直接跳转登录页面（不确定）
//        if (PATH_MATCHER.match(url,"/backend/**")){

//            response.sendRedirect("http://localhost:8080/backend/page/login.html");
//        if (PATH_MATCHER.match(url,"/front/**")){
//            response.sendRedirect("http://localhost:8080/front/page/login.html");
//            return;
//        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));


//            response.sendRedirect("http://localhost:8080/backend/page/login/login.html");

//        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
//        response.sendRedirect(request.getContextPath() + "/backend/page/login/login.html");
    }

    private Boolean check(String[] urls, String url) {
        for (String u:
             urls) {
            if (PATH_MATCHER.match(u,url)) {
                return true;
            }
        }
        return false;
    }
}
