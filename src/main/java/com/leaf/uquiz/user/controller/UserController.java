package com.leaf.uquiz.user.controller;

import com.leaf.uquiz.core.exception.MyException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/16
 */
@Service
@RequestMapping("/api/user")
public class UserController {

    @RequestMapping("/test")
    public String show() {
        throw new MyException(10000, "测试exception");
    }

    @RequestMapping("/test2")
    public String show2() throws Exception {
        throw new Exception("aaadasd");
    }
}
