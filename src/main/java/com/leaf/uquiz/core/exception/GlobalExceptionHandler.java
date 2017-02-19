package com.leaf.uquiz.core.exception;

import com.leaf.uquiz.core.messages.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/10/25
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ErrorInfo defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        ErrorInfo errorInfo = jsonErrorHandler(request, e);
        return errorInfo;
    }

    public ErrorInfo jsonErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.error(e.getMessage(), e);
        int code = ErrorInfo.ERROR;
        String message = e.getMessage();
        if (e instanceof MyException) {
            MyException myException = (MyException) e;
            if (myException.getCode() > 0) {
                code = myException.getCode();
                message = MessageUtil.getMessage(String.valueOf(code));
            }
        }
        ErrorInfo r = new ErrorInfo();
        r.setMessage(message);
        r.setCode(code);
        r.setUrl(request.getRequestURL().toString());
        return r;
    }
}
