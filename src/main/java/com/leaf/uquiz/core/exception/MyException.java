package com.leaf.uquiz.core.exception;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/10/25
 */
public class MyException extends RuntimeException {

    private int code;

    private String content;

    public MyException(String message) {
        super(message);
        this.content = message;
    }

    public MyException(String message, Throwable throwable) {
        super(message, throwable);
        this.content = message;
    }

    public MyException(int code, String message) {
        super(message);
        this.content = message;
        this.code = code;
    }

}
