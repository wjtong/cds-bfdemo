package com.banfftech.cdsbfdemo.controller;

import cds.gen.banfftech.user.UserLogin;
import cds.gen.banfftech.user.UserLogin_;
import com.banfftech.cdsbfdemo.Utils.AuthenticationUtil;
import com.banfftech.cdsbfdemo.Utils.ControllerRes;
import com.sap.cds.Result;
import com.sap.cds.ql.Insert;
import com.sap.cds.ql.Select;
import com.sap.cds.services.persistence.PersistenceService;
import lombok.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录接口
 *
 * @author scy
 * @date 2023/3/23
 */
@Controller
public class LoginController {

    @Resource
    PersistenceService dbService;

    /**
     * 用户注册
     *
     * @param userLoginId     登录账户
     * @param password        登陆密码
     * @param confirmPassword 确认密码
     */
    @ResponseBody
    @PostMapping("/bfRegister")
    public ControllerRes register(@NonNull String userLoginId, @NonNull String password, @NonNull String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return new ControllerRes(500, "Entered passwords differ");
        }
        Result result = dbService.run(Select.from(UserLogin_.class).where(userLogin -> userLogin.userLoginId().eq(userLoginId)));
        if (result.rowCount() > 0) {
            return new ControllerRes(500, "An existing account");
        }
        UserLogin registerUserLogin = UserLogin.create();
        registerUserLogin.setUserLoginId(userLoginId);
        String passStr = AuthenticationUtil.md5encrypt(password);
        System.out.println("passStr = " + passStr);
        registerUserLogin.setCurrentPassword(AuthenticationUtil.md5encrypt(password));
        dbService.run(Insert.into(UserLogin_.CDS_NAME).entry(registerUserLogin));
        return new ControllerRes(200, "success");
    }

    /**
     * 用户登录 核验账密并响应token
     *
     * @param userLoginId 登录账户
     * @param password    登陆密码
     * @return 响应一个Jwt token
     */
    @ResponseBody
    @PostMapping("/bfLogin")
    public ControllerRes login(@NonNull String userLoginId, @NonNull String password, HttpServletResponse response) {
        //查询用户
        Result result = dbService.run(Select.from(UserLogin_.class)
                .where(userLogin -> userLogin.userLoginId().eq(userLoginId)));
        if (result.rowCount() == 0) {
            return new ControllerRes(404, "Account does not exist");
        }
        //密码加密匹配
        String encryptedPassword = AuthenticationUtil.md5encrypt(password);
        UserLogin userLogin = result.single().as(UserLogin.class);
        if (!userLogin.getCurrentPassword().equals(encryptedPassword)) {
            return new ControllerRes(401, "Wrong password");
        }
        Map<String, String> tokenParam = new HashMap<>();
        //生成token, 可以放一些不敏感的数据 类似partyId或firstName
        tokenParam.put("userLoginId", userLogin.getUserLoginId());
        String newToken = AuthenticationUtil.generateToken(tokenParam);
        response.setHeader("access_token", newToken);
        return new ControllerRes(200, "success");
    }

    /**
     * 响应过滤器中抛出的异常
     */
    @ResponseBody
    @RequestMapping("/tokenErr")
    public ControllerRes tokenErr() {
        return new ControllerRes(401, "Authentication failure");
    }

}
