package com.leaf.uquiz.weixin.dto;

import com.leaf.uquiz.core.utils.EncodeUtils;
import com.leaf.uquiz.core.utils.RandomStringGenerator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 16/3/25
 */
public class WxConfig {
    private Logger logger = LoggerFactory.getLogger(WxConfig.class);

    private String noncestr;
    private String jsapi_ticket;
    private String timestamp;
    private String url;
    private String sign;

    public WxConfig() {
    }

    public WxConfig(String jsapi_ticket, String url) {
        setNoncestr(RandomStringGenerator.getRandomStringByLength(32));
        setTimestamp(String.valueOf(System.currentTimeMillis() / 1000));
        setJsapi_ticket(jsapi_ticket);
        setUrl(url);
        String sign = shaSign();
        setSign(sign);
    }

    private String shaSign() {
        Map<String, Object> map = toMap();
        ArrayList<String> list = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != "") {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        if (result.endsWith("&")) {
            result = result.substring(0, result.length() - 1);
        }
        logger.info("result:{}", result);
        result = EncodeUtils.encode("SHA1", result);
        return result;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getJsapi_ticket() {
        return jsapi_ticket;
    }

    public void setJsapi_ticket(String jsapi_ticket) {
        this.jsapi_ticket = jsapi_ticket;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if (obj != null) {
                    if (StringUtils.equals(field.getName(), "logger")) {
                        continue;
                    }
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
