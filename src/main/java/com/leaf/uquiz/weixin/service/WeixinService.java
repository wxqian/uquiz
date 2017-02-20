package com.leaf.uquiz.weixin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leaf.uquiz.core.cache.StringCache;
import com.leaf.uquiz.core.config.WeixinConfig;
import com.leaf.uquiz.core.exception.MyException;
import com.leaf.uquiz.core.utils.HttpClientUtil;
import com.leaf.uquiz.core.utils.SessionUtils;
import com.leaf.uquiz.weixin.aes.AesException;
import com.leaf.uquiz.weixin.aes.SHA1;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/19
 */
@Service
public class WeixinService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static String ACCESS_TOKEN = "access_token";
    private static String JSAPI_TICKET = "jsapi_ticket";
    private static String WEIXIN_AUTHORIZE_URL =
            "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
    private static final String WEIXIN_TOKEN_URL =
            "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    // TODO: 2017/2/19  微信token地址修改
    private static final String WEIXIN_BASE_URL =
            "http://www.uquiz.com/api/weixin/show?real_url=s%";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static final String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
    private static final String TEMPLATE_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
    private static final String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

    @Autowired
    private WeixinConfig weixinConfig;

    @Autowired
    private StringCache stringCache;

    /**
     * 验证服务器地址的有效性
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    public String checkServer(String signature, String timestamp, String nonce, String echostr) {
        Assert.hasLength(signature, "signature为空");
        Assert.hasLength(timestamp, "timestamp为空");
        Assert.hasLength(nonce, "nonce为空");
        Assert.hasLength(echostr, "echostr为空");
        if (checkSignature(signature, timestamp, nonce)) {
            return echostr;
        } else {
            return StringUtils.EMPTY;
        }
    }

    private boolean checkSignature(String signature, String timestamp, String nonce) {
        try {
            String encrypt = SHA1.getSHA1(weixinConfig.getToken(), timestamp, nonce);
            logger.info("signature:{},encrypt:{}", signature, encrypt);
            return StringUtils.equals(signature, encrypt);
        } catch (AesException e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 获取session里的openid
     *
     * @return
     */
    public String openId() {
        return (String) SessionUtils.getSession().getAttribute("openId");
    }

    public String accessToken() {
        String accessToken = stringCache.get(ACCESS_TOKEN);
        if (StringUtils.isBlank(accessToken)) {
            JSONObject object = invoke(ACCESS_TOKEN_URL, new String[]{weixinConfig.getAppId(),
                    weixinConfig.getAppSecret()}, null);
            accessToken = object.getString(ACCESS_TOKEN);

        }
        return accessToken;
    }

    private JSONObject invoke(String url, String[] urlParams, Map<String, Object> postParams) {

        String postBody = CollectionUtils.isEmpty(postParams) ? null : JSON.toJSONString(postParams);
        if (ArrayUtils.isNotEmpty(urlParams)) {
            logger.info("params:{}", StringUtils.join(urlParams, ","));
        }
        if (!CollectionUtils.isEmpty(postParams)) {
            logger.info("map:{}", JSONObject.toJSONString(postParams));
        }
        return invoke2(url, urlParams, postBody);
    }

    private JSONObject invoke2(String url, String[] urlParams, String postBody) {
        if (urlParams != null) {
            url = String.format(url, urlParams);
        }
        if (StringUtils.isBlank(postBody)) {
            return parseJSON(HttpClientUtil.httpGet(url));
        }
        return parseJSON(HttpClientUtil.httpPost(url, postBody));
    }

    private JSONObject parseJSON(String response) {
        JSONObject jsonObject = JSON.parseObject(response);
        int code = jsonObject.getIntValue("errcode");
        if (code != 0 && code != 43004) {
            //43004 未关注公众号,暂不处理
            String msg = jsonObject.getString("errmsg");
            logger.error("weinxin api error , errcode:{} errmsg:{}", code, msg);
            throw new MyException(code, msg);
        }
        return jsonObject;
    }

    /**
     * 用户同意授权,获取code,静默授权
     *
     * @param url
     * @return
     */
    public String initAuth(String url) {
        Assert.hasLength(url, "url不能为空");
        logger.info("url:{1}", url);
        return "redirect:" + String.format(WEIXIN_AUTHORIZE_URL,
                weixinConfig.getAppId(),
                encode(String.format(WEIXIN_BASE_URL, url)));
    }

    private String encode(String url) {
        try {
            return URLEncoder.encode(url, Charsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 用户授权code,获取用户授权token
     *
     * @param code
     * @param realUrl
     * @return
     */
    public String show(String code, String realUrl) {
        Assert.hasLength(code, "用户未允许授权");
        Assert.hasLength(realUrl, "真实跳转url");
        logger.info("code:{},realUrl:{}", code, realUrl);
        JSONObject jsonObject = invoke(WEIXIN_TOKEN_URL, new String[]{weixinConfig.getAppId(), weixinConfig.getAppSecret(), code}, null);
        String openId = jsonObject.getString("openid");
        logger.info("openId:{}", openId);
        SessionUtils.getSession().setAttribute("openId", openId);
        String accessToken = jsonObject.getString(ACCESS_TOKEN);
        logger.info("accessToken:{}", accessToken);
        JSONObject obj = invoke(USER_INFO_URL, new String[]{accessToken, openId}, null);
        // TODO: 2017/2/19 用户信息
        return "redirect:" + realUrl;
    }


}
