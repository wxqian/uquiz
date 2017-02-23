package com.leaf.uquiz.teacher.service;

import com.leaf.uquiz.teacher.domain.Teacher;
import com.leaf.uquiz.teacher.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/22
 */
@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    /**
     * 根据openId 查找用户
     *
     * @param openId
     * @return
     */
    public Teacher findTeacherByOpenId(String openId) {
        Assert.hasLength(openId, "openId不能为空");
        Teacher teacher = teacherRepository.findByOpenId(openId);
        teacher.setNickName(decodeNickName(teacher.getNickName()));
        return teacher;
    }

    /**
     * 新建用户
     *
     * @param openId
     * @param nickName
     * @param headImg
     * @return
     */
    public Teacher createTeacher(String openId, String nickName, String headImg) {
        Assert.hasLength(openId, "openId不能为空");
        Assert.hasLength(nickName, "nickName不能为空");
        Assert.hasLength(headImg, "headImg不能为空");
        Teacher teacher = new Teacher(openId, encodeNickName(nickName), headImg);
        teacher = teacherRepository.save(teacher);
        return teacher;
    }


    /**
     * 微信昵称支持emoji,4个字符串的utf-8,编码
     *
     * @param nickName
     * @return
     */
    private String encodeNickName(String nickName) {
        try {
            return URLEncoder.encode(nickName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 微信昵称支持emoji,4个字符串的utf-8,解码
     *
     * @param nickName
     * @return
     */
    private String decodeNickName(String nickName) {
        try {
            return URLDecoder.decode(nickName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更改教师信息
     *
     * @param teacher
     */
    public void modifyTeacher(Teacher teacher) {

    }
}
