package com.zhashut.reggie.common;

/**
 * Created with IntelliJ IDEA.
 * User: 璇屿
 * Date: 2022/4/20
 * Time: 14:44
 * Description: No Description
 */
@SuppressWarnings({"all"})
/**
 * 自定义业务异常类
 */
public class CustomException extends  RuntimeException {

    public CustomException(String message) {
        super(message);
    }

}
