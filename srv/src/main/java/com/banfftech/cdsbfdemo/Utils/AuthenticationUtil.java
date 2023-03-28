package com.banfftech.cdsbfdemo.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * token Util
 *
 * @author scy
 * @date 2023/3/23
 */
public class AuthenticationUtil {
    /**
     * 签名密钥
     */
    private static final String SECRET = "Y29tLmJhbmZmdGVjaA==";

    /**
     * 生成token
     *
     * @param payload token携带的信息
     * @return token字符串
     */
    public static String generateToken(Map<String, String> payload) {
        // 指定token过期时间 24小时
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 24);
        JWTCreator.Builder builder = JWT.create();
        // 构建payload
        payload.forEach(builder::withClaim);
        // 指定签发时间、过期时间 和 签名算法，并返回token
        return builder.withIssuedAt(new Date()).withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC256(SECRET));
    }


    /**
     * 验证token
     *
     * @param token token字符串
     * @return 解析后的token类
     */
    public static DecodedJWT decodeToken(String token) {
        if (token == null) {
            throw new JWTVerificationException("Authentication failure");
        }
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return jwtVerifier.verify(token);
    }

    /**
     * MD5加密数据
     *
     * @param data 要加密的数据
     * @return 加密之后的数据
     */
    public static String md5encrypt(String data) {
        return DigestUtils.md5DigestAsHex(data.getBytes(StandardCharsets.UTF_8));
    }
}


