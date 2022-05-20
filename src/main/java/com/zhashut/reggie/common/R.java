package com.zhashut.reggie.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过返回结果，服务端响应的数据最终会封装成此对象
 * @param <T>
 */
@Data
public class R<T> implements Serializable {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    //当响应成功后，会调用这个方法
    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object; //返回主体数据
        r.code = 1; //设置为登录成功状态
        return r;
    }

    //如果响应失败，则调用这个方法
    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg; //返回错误的登录信息
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
