package com.banfftech.cdsbfdemo;

import org.springframework.stereotype.Component;

import com.sap.cds.services.request.ModifiableUserInfo;
import com.sap.cds.services.request.UserInfo;
import com.sap.cds.services.runtime.UserInfoProvider;

@Component
public class CustomUserInfoProvider implements UserInfoProvider {

    private UserInfoProvider defaultProvider;

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
        // userInfo = null;
        return userInfo;
    }

    @Override
    public void setPrevious(UserInfoProvider prev) {
        this.defaultProvider = prev;
    }
}
