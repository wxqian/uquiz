package com.leaf.uquiz.teacher.service;

import com.leaf.uquiz.core.enums.Status;
import com.leaf.uquiz.core.exception.MyException;
import com.leaf.uquiz.core.utils.SessionUtils;
import com.leaf.uquiz.teacher.domain.Course;
import com.leaf.uquiz.teacher.domain.CourseContent;
import com.leaf.uquiz.teacher.domain.Teacher;
import com.leaf.uquiz.teacher.repository.CourseContentRepository;
import com.leaf.uquiz.teacher.repository.CourseReadRepository;
import com.leaf.uquiz.teacher.repository.CourseRepository;
import com.leaf.uquiz.teacher.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/22
 */
@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseContentRepository courseContentRepository;

    @Autowired
    private CourseReadRepository courseReadRepository;

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
        Assert.notNull(teacher, "用户不能为空");
        Teacher user = getCurrentTeacher();
        teacher.setId(user.getId());
        teacher.setNickName(encodeNickName(teacher.getNickName()));
        teacherRepository.save(teacher);
    }

    /**
     * 查询教师列表
     *
     * @param pageable
     * @return
     */
    public Page<Course> listCourse(Pageable pageable) {
        Teacher teacher = getCurrentTeacher();
        Page<Course> courses = courseRepository.findTeacherCourse(teacher.getId(), Status.ENABLED, pageable);
        if (courses.hasNext()) {
            for (Course course : courses.getContent()) {
                getCourseDetail(course);
            }
        }
        return courses;
    }

    /**
     * 获取课程内容列表和阅读次数等信息
     *
     * @param course
     * @return
     */
    private Course getCourseDetail(Course course) {
        int count = courseReadRepository.getReadCount(course.getId());
        course.setCount(count);
        List<CourseContent> contents = courseContentRepository.getCourseContents(course.getId(), Status.ENABLED);
        course.setContents(contents);
        return course;
    }

    private Teacher getCurrentTeacher() {
        Teacher user = (Teacher) SessionUtils.getSession().getAttribute("user");
        if (user == null) {
            throw new MyException(10000, "请登录后操作");
        }
        return user;
    }

    /**
     * 获取课程详情,如果id等于0,则是新建课程
     *
     * @param id
     * @return
     */
    public Course detailCourse(long id) {
        Course course;
        if (id == 0) {
            Teacher teacher = getCurrentTeacher();
            course = new Course(teacher.getId());
            return courseRepository.save(course);
        }
        course = courseRepository.findOne(id);
        return getCourseDetail(course);
    }

    /**
     * 获取登录教师信息
     *
     * @return
     */
    public Teacher teacherInfo() {
        return getCurrentTeacher();
    }

    /**
     * 更改课程内容
     *
     * @param course
     */
    public void modifyCourse(Course course) {
        Assert.notNull(course, "课程不能为空");
        course.setTeacherId(getCurrentTeacher().getId());
        courseRepository.save(course);
    }

    /**
     * 添加课程详情,sort若为-1,则append,否则在sort之前添加
     *
     * @param content
     * @param sort
     */
    public void addContent(CourseContent content, int sort) {

    }
}
