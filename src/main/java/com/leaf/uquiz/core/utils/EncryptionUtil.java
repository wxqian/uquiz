package com.leaf.uquiz.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密
 */
public class EncryptionUtil {

    /**
     * 采用加密算法加密字符串数据
     * @param str       需要加密的数据
     * @param algorithm 采用的加密算法，algorithm参数改成MD5、SHA-1 、SHA-256等加密算法也是支持的
     * @return 字节数据
     */

    public static byte[] EncryptionStrBytes(String str, String algorithm) {
        // 加密之后所得字节数组
        byte[] bytes = null;
        try {
            // 获取MD5算法实例 得到一个md5的消息摘要
            MessageDigest md = MessageDigest.getInstance(algorithm);
            //添加要进行计算摘要的信息
            md.update(str.getBytes());
            //得到该摘要
            bytes = md.digest();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("加密算法: " + algorithm + " 不存在: ");
        }
        return null == bytes ? null : bytes;
    }


    /**
     * 把字节数组转化成字符串返回
     * @param bytes
     * @return
     *
     */

    public static String BytesConvertToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte aByte : bytes) {
            String s = Integer.toHexString(0xff & aByte);
            if (s.length() == 1) {
                sb.append("0" + s);
            } else {
                sb.append(s);
            }
        }
        return sb.toString();
    }


    /**
     * 采用加密算法加密字符串数据
     * @param str       需要加密的数据
     * @param algorithm 采用的加密算法
     * @return 字节数据
     */

    public static String EncryptionStr(String str, String algorithm) {
        // 加密之后所得字节数组
        byte[] bytes = EncryptionStrBytes(str, algorithm);
        return BytesConvertToHexString(bytes);
    }
}
