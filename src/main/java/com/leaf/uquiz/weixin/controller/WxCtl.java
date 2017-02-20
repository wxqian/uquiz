package com.leaf.uquiz.weixin.controller;

import com.leaf.uquiz.weixin.service.WeixinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/19
 */
@Controller
@RequestMapping("/api/weixin")
public class WxCtl {

    @Autowired
    private WeixinService weixinService;

    @RequestMapping(value = "/checkServer", method = RequestMethod.GET)
    @ResponseBody
    public String checkServer(@RequestParam(name = "signature") String signature,
                              @RequestParam(name = "signature") String timestamp,
                              @RequestParam(name = "nonce") String nonce,
                              @RequestParam(name = "echostr") String echostr) {
        return weixinService.checkServer(signature, timestamp, nonce, echostr);
    }

    @RequestMapping(value = "/openId", method = RequestMethod.GET)
    @ResponseBody
    public String openId() {
        return weixinService.openId();
    }

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public String init(@RequestParam("url") String url) {
        return weixinService.initAuth(url);
    }

    @RequestMapping(value = "/show", method = RequestMethod.POST)
    public String show(@RequestParam("code") String code, @RequestParam("real_url") String realUrl) {
        return weixinService.show(code, realUrl);
    }
}
