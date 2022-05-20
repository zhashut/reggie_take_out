package com.zhashut.reggie.common;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/19
 * Time: 19:06
 * Description: No Description
 */
@SuppressWarnings({"all"})
/**
 * 基于ThreadLocal封装工具类，用于保存和获取当前用户id
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置值
     * @param id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取值
     * @return
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }

}
