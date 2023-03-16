package com.banfftech.cdsbfdemo;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import com.sap.cds.services.request.ModifiableUserInfo;
import com.sap.cds.services.request.UserInfo;
import com.sap.cds.services.runtime.UserInfoProvider;

@Component
@Order(1)
public class CustomUserInfoProvider implements UserInfoProvider {

    private UserInfoProvider defaultProvider;

    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    public UserInfo get() {
        System.out.println("---------------------------- in get UserInfo");
        ModifiableUserInfo userInfo = UserInfo.create();
        if (defaultProvider != null) {
            UserInfo prevUserInfo = defaultProvider.get();
            if (prevUserInfo != null) {
                userInfo = prevUserInfo.copy();
            }
        }
        System.out.println(userInfo.toString());
        userInfo.addRole("users");
        if (RequestContextHolder.getRequestAttributes() != null) {
            userInfo.setName(httpServletRequest.getHeader("custom-username-header"));
            
        }
        userInfo.setIsAuthenticated(true);
        // userInfo = null;
        return userInfo;
    }

    @Override
    public void setPrevious(UserInfoProvider prev) {
        this.defaultProvider = prev;
    }
}
