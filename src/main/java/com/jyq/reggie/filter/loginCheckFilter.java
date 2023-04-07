package com.jyq.reggie.filter;


import com.alibaba.fastjson.JSON;
import com.jyq.reggie.common.BaseContext;
import com.jyq.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.acl.AclNotFoundException;

@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class loginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;

        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        //这里列举了不需要进行拦截的url

        String requestURI=request.getRequestURI();
        log.info("拦截到请求：{}",requestURI);
        //获取本次请求的URI

        Boolean check=Check(urls,requestURI);
        if(check){
            log.info("本次请求不需要处理，{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        //判断登陆状态，如果已登陆，则直接放行--管理端
        if(request.getSession().getAttribute("employee")!=null){
            Long emId=(Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(emId);
            filterChain.doFilter(request,response);
            return;
        }
        //判断登陆状态，如果已登陆，则直接放行--用户端
        if(request.getSession().getAttribute("user")!=null){
            Long userId=(Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;

    }

    /**
     * 路径匹配，判断本次登录能否被放行
     * @param urls
     * @param requestURI
     * @return
     */
    private Boolean Check(String[] urls, String requestURI) {
        for (String url : urls) {
            Boolean match=PATH_MATCHER.match(url,requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
