package com.leaf.uquiz.weixin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leaf.uquiz.core.cache.StringCache;
import com.leaf.uquiz.core.config.SystemConfig;
import com.leaf.uquiz.core.config.WeixinConfig;
import com.leaf.uquiz.core.exception.MyException;
import com.leaf.uquiz.core.utils.HttpClientUtil;
import com.leaf.uquiz.core.utils.IOUtils;
import com.leaf.uquiz.core.utils.SessionUtils;
import com.leaf.uquiz.core.utils.XMLUtil;
import com.leaf.uquiz.file.config.FileSettings;
import com.leaf.uquiz.teacher.domain.Teacher;
import com.leaf.uquiz.teacher.service.TeacherService;
import com.leaf.uquiz.weixin.aes.AesException;
import com.leaf.uquiz.weixin.aes.SHA1;
import com.leaf.uquiz.weixin.aes.WXBizMsgCrypt;
import com.leaf.uquiz.weixin.dto.WxConfig;
import com.leaf.uquiz.weixin.message.handler.RequestHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
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
            "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect";
    private static final String WEIXIN_TOKEN_URL =
            "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
    // TODO: 2017/2/19  微信token地址修改
    private static final String WEIXIN_BASE_URL =
            "http://api.study.pointshare.com/api/weixin/show?real_url=s%";
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static final String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";
    private static final String TEMPLATE_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";
    private static final String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
    private static final String qrUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";
    private static final String fileUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
    private static final String AMR_SUFFIX = ".amr";
    private static final String MP3_SUFFIX = ".mp3";

    @Autowired
    private WeixinConfig weixinConfig;

    @Autowired
    private StringCache stringCache;

    @Autowired
    @Lazy
    private TeacherService teacherService;

    @Autowired
    private RequestHandler requestHandler;

    private WXBizMsgCrypt wxBizMsgCrypt;

    @Autowired
    private SystemConfig systemConfig;

    @Autowired
    private FileSettings fileSettings;

    @PostConstruct
    public void init() {
        try {
            wxBizMsgCrypt = new WXBizMsgCrypt(weixinConfig.getToken(), weixinConfig.getAesKey(), weixinConfig.getAppId());
        } catch (AesException e) {
            logger.error("init WxBizMsgCrypt occurs error");
        }
    }

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
        logger.info("accessToken:{}", accessToken);
        if (StringUtils.isBlank(accessToken)) {
            logger.info("get Access_token from weixin");
            logger.info("appid:{},appSecret:{}", weixinConfig.getAppId(), weixinConfig.getAppSecret());
            JSONObject object = invoke(ACCESS_TOKEN_URL, new String[]{weixinConfig.getAppId(),
                    weixinConfig.getAppSecret()}, null);
            accessToken = object.getString(ACCESS_TOKEN);
            logger.info("get Access_token from weixin end:{}", accessToken);

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
     * @param userType
     * @return
     */
    public String initAuth(String url, String userType) {
        Assert.hasLength(url, "url不能为空");
        logger.info("url:{1}", url);
        return "redirect:" + String.format(WEIXIN_AUTHORIZE_URL,
                weixinConfig.getAppId(),
                encode(String.format(WEIXIN_BASE_URL, url)), userType);
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
     * @param userType
     * @return
     */
    public String show(String code, String realUrl, String userType) {
        Assert.hasLength(code, "用户未允许授权");
        Assert.hasLength(realUrl, "真实跳转url");
        logger.info("code:{},realUrl:{}", code, realUrl);
        JSONObject jsonObject = invoke(WEIXIN_TOKEN_URL, new String[]{weixinConfig.getAppId(), weixinConfig.getAppSecret(), code}, null);
        String openId = jsonObject.getString("openid");
        logger.info("openId:{}", openId);
        SessionUtils.getSession().setAttribute("openId", openId);
        if (StringUtils.equals(userType, "teacher")) {
            Teacher teacher = loginTeacher(openId);
            SessionUtils.getSession().setAttribute("teacher", teacher);
        }
        return "redirect:" + realUrl;
    }

    /**
     * 根据openId 获取或创建教师
     *
     * @param openId
     * @return
     */
    public Teacher loginTeacher(String openId) {
        Teacher teacher = teacherService.findTeacherByOpenId(openId);
        if (teacher == null) {
            JSONObject obj = invoke(USER_INFO_URL, new String[]{accessToken(), openId}, null);
            String nickName = obj.getString("nickname");
            String headImg = obj.getString("headimgurl");
            logger.info("nickName:{},headImg:{}", nickName, headImg);
            teacher = teacherService.createTeacher(openId, nickName, headImg);
        }
        return teacher;
    }


    /**
     * 生成临时二维码,用于PC端扫码登录,有效期5分钟
     *
     * @return
     */
    public String scanView() {
        Map<String, Object> map = new HashMap<>();
        map.put("expire_seconds", 5 * 60);//设置该二维码300秒过期
        map.put("action_name", "QR_SCENE");//二维码类型为临时
        Map<String, Object> actionInfo = new HashMap<>();
        Map<String, Object> scene = new HashMap<>();
        scene.put("scene_id", 1000);
        actionInfo.put("scene", scene);
        map.put("action_info", actionInfo);
        JSONObject object = invoke(qrUrl, new String[]{accessToken()}, map);
        return object.getString("ticket");
    }

    /**
     * 处理微信服务器发送的消息
     *
     * @param request
     * @return
     */
    public String handleMsg(HttpServletRequest request) throws IOException, AesException {
        if (StringUtils.isNotEmpty(request.getParameter("echostr"))) {
            return checkServer(request.getParameter("signature"),
                    request.getParameter("timestamp"),
                    request.getParameter("nonce"),
                    request.getParameter("echostr"));
        }
        return requestHandler.handle(checkAndDecrypt(request));
    }

    /**
     * 对接收的消息进行解密并加解密的内容转化成map
     *
     * @param request
     * @return
     * @throws Exception
     */
    private Map<String, String> checkAndDecrypt(HttpServletRequest request) throws IOException, AesException {
        logger.info("check signature start");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String msgSignature = request.getParameter("msg_signature");
        logger.info("nonce:{},timestamp:{},msgSignature:{}", nonce, timestamp, msgSignature);
        if (StringUtils.isBlank(nonce) || StringUtils.isBlank(timestamp) ||
                StringUtils.isBlank(msgSignature)) {
            throw new RuntimeException("arguments is invalid");
        }

        try {
            Map<String, String> map = XMLUtil.parse(request.getInputStream());
            String encrypt = map.get("Encrypt");
            String result = wxBizMsgCrypt.decryptMsg(msgSignature, timestamp, nonce, encrypt);
            return XMLUtil.parse(new ByteArrayInputStream(result.getBytes(Charsets.UTF_8.displayName())));
        } catch (Exception e) {
            logger.error("check signature error", e);
            throw e;
        }

    }

    /**
     * 微信分享先授权验证
     *
     * @param configUrl
     * @return
     */
    public WxConfig config(String configUrl) {
        Assert.hasLength(configUrl, "页面地址不能为空");
        return new WxConfig(getJsapiTicket(), configUrl);
    }

    /**
     * 获取js api ticket
     *
     * @return
     */
    private String getJsapiTicket() {
        String jsapiTicket = stringCache.get(JSAPI_TICKET);
        if (StringUtils.isBlank(jsapiTicket)) {
            String accessToken = accessToken();
            logger.info("***access_token:{}******", accessToken);
            JSONObject json = invoke(JSAPI_TICKET_URL, new String[]{accessToken}, null);
            jsapiTicket = json.getString("ticket");
            long expire = json.getLong("expires_in");
            logger.info("jsapi_ticket:{}", jsapiTicket);
            stringCache.set(JSAPI_TICKET, jsapiTicket, expire);
        }
        return jsapiTicket;
    }

    /**
     * 从微信下载音频,并保存到file中
     *
     * @param mediaId
     * @return
     */
    public void downVoice(String mediaId) {
        Assert.hasLength(mediaId, "无效的media_id");
        String url = String.format(fileUrl, accessToken(), mediaId);
        HttpClient client = new HttpClient();

        // 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
        HttpMethod method = new GetMethod(url);

        try {
            int responseCode = client.executeMethod(method);

            logger.info("http responseCode:{}", responseCode);
            // 打印服务器返回的状态
            logger.info("http response StatusLine:{}", method.getStatusLine());
            Header header = method.getResponseHeader("Content-Type");
            if (StringUtils.indexOf(header.getValue(), "application/json") != -1) {
                // 打印返回的信息
                String responseBodyAsString = new String(method.getResponseBodyAsString().getBytes("ISO-8859-1"), "UTF-8");
                logger.info("http responseBodyAsString:{}", responseBodyAsString);
                JSONObject obj = parseJSON(responseBodyAsString);
                throw new RuntimeException(obj.getString("errmsg"));
            }
            FileOutputStream fos = new FileOutputStream(fileSettings.getAmrPath() + mediaId + AMR_SUFFIX);
            IOUtils.copy(method.getResponseBodyAsStream(), fos);

        } catch (IOException e) {
            throw new RuntimeException("HTTP GET请求发生异常", e);
        } finally {
            // 释放连接
            method.releaseConnection();
            ((SimpleHttpConnectionManager) client.getHttpConnectionManager()).shutdown();
        }
    }
}
