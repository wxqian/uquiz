package com.leaf.uquiz.weixin.controller;

import com.leaf.uquiz.weixin.aes.AesException;
import com.leaf.uquiz.weixin.dto.WxConfig;
import com.leaf.uquiz.weixin.service.WeixinService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/19
 */
@RestController
@RequestMapping("/api/weixin")
public class WxCtl {

    @Autowired
    private WeixinService weixinService;

    @RequestMapping(value = "/openId", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "获取openId")})
    @ApiOperation(value = "获取openId", notes = "获取openId")
    public String openId() {
        return weixinService.openId();
    }

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "获取微信授权")})
    @ApiOperation(value = "获取微信授权", notes = "获取微信授权,前台调用")
    public String init(@ApiParam(name = "url", value = "页面地址") @RequestParam("url") String url,
                       @ApiParam(name = "userType", value = "授权类型,teacher 为教师,normal为普通用户") @RequestParam("userType") String userType) {
        return weixinService.initAuth(url, userType);
    }

    @RequestMapping(value = "/show", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "授权成功后跳转页面")})
    @ApiOperation(value = "授权获取code后跳转", notes = "授权获取code后跳转,微信跳转")
    public String show(@RequestParam("code") String code, @RequestParam("real_url") String realUrl, @RequestParam("state") String userType) {
        return weixinService.show(code, realUrl, userType);
    }

    @RequestMapping(value = "/handleMsg", method = {RequestMethod.POST, RequestMethod.GET})
    @ApiResponses({@ApiResponse(code = 200, message = "处理微信消息成功")})
    @ApiOperation(value = "处理微信消息", notes = "处理微信消息")
    public String handleMsg(HttpServletRequest request) throws IOException, AesException {
        return weixinService.handleMsg(request);
    }

    @RequestMapping(value = "/config", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "微信jsapi获取授权参数成功")})
    @ApiOperation(value = "微信jsapi获取授权参数", notes = "微信jsapi获取授权参数")
    public WxConfig config(@ApiParam(name = "configUrl", value = "EncodeURI(当前页面地址)") @RequestParam("configUrl") String configUrl) {
        return weixinService.config(configUrl);
    }

    @RequestMapping(value = "/voice", method = RequestMethod.POST)
    public Object downVoice(@RequestParam("mediaId") String mediaId) {
        return weixinService.downVoice(mediaId);
    }
}
