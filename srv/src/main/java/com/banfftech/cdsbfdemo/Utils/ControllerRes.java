package com.banfftech.cdsbfdemo.Utils;

import lombok.Data;

/**
 * @author scy
 * @date 2023/3/23
 */
@Data
public class ControllerRes {

    /**
     * 响应状态码
     */
    Integer code;

    /**
     * 响应数据
     */
    String data;

    public ControllerRes(Integer code, String data) {
        this.code = code;
        this.data = data;
    }
}
