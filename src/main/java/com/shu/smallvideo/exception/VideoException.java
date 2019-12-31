package com.shu.smallvideo.exception;


import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * @author shuxibing
 * @date 2019/7/1 20:18
 * @uint d9lab
 * @Description:
 */
public abstract class VideoException extends RuntimeException {
    private Object errorData;

    public VideoException(String message) {
        super(message);
    }

    public VideoException(String message, Throwable cause) {
        super(message, cause);
    }

    @NonNull
    public abstract HttpStatus getStatus();

    @Nullable
    public Object getErrorData() {
        return errorData;
    }

    /**
     * Sets error errorData.
     *
     * @param errorData error data
     * @return current exception.
     */
    @NonNull
    public VideoException setErrorData(@Nullable Object errorData) {
        this.errorData = errorData;
        return this;
    }
}
