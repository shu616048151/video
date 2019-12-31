package com.shu.smallvideo.exception;

import org.springframework.http.HttpStatus;

/**
 * @author shuxibing
 * @date 2019/7/5 8:33
 * @uint d9lab
 * @Description:
 */
public class BeanUtilsException extends  VideoException {
    //两个构造方法
    public BeanUtilsException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanUtilsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
