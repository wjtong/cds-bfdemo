package com.banfftech.cdsbfdemo.requestFilter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.banfftech.cdsbfdemo.Utils.AuthenticationUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.banfftech.cdsbfdemo.controller.LoginController.CSRF_TOKEN;


/**
 * 过滤器 用来做请求的身份验证
 *
 * @author scy
 * @date 2023/3/23
 */
@Configuration
@Order(value = 1)
@WebFilter(filterName = "authFilter", urlPatterns = {"*"})
public class AuthFilter implements Filter{

    /**
     * 需要认证的接口
     */
    private static final Set<String> AUTH_PATHS = Collections.unmodifiableSet(new HashSet<>(
            Arrays.asList("/odata/v4", "sap/bc/lrep")));

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUrl = request.getRequestURL().toString();
        try {
            if (authPath(request)) {
                //获取token数据 把当前登录用户添加到请求参数里以便内部使用
                DecodedJWT decodedToken = AuthenticationUtil.decodeToken(request.getHeader(CSRF_TOKEN));
                request.setAttribute("userLoginId", decodedToken.getClaim("userLoginId").asString());
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (JWTVerificationException e) {
            request.getRequestDispatcher("/toLogin").forward(request, response);
//            request.getRequestDispatcher("/tokenErr").forward(request, response);
        }
    }

    private boolean authPath(HttpServletRequest request) {
        String currentPath = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        for (String authPath : AUTH_PATHS) {
            if (currentPath.contains(authPath)) {
                return true;
            }
        }
        return false;
    }
}