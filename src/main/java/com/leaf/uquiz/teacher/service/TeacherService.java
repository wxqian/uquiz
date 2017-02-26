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
import com.leaf.uquiz.weixin.message.resp.Resp;
import com.leaf.uquiz.weixin.message.resp.TextResp;
import com.leaf.uquiz.weixin.service.WeixinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Autowired
    private WeixinService weixinService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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
        Teacher user = (Teacher) SessionUtils.getSession().getAttribute("teacher");
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
     * 添加课程详情,id若为0,则append,否则在sort之前添加,如果id不存在,则append
     *
     * @param content
     * @param id      课程id
     * @return CourseContent
     */
    public CourseContent addContent(CourseContent content, long id) {
        Assert.notNull(content, "内容不能为空");
        Assert.isTrue(content.getCourseId() > 0, "内容必须指定课程");
        CourseContent courseContent = courseContentRepository.findOne(id);
        if (courseContent == null) {
            content = appendCourseContent(content);
        } else {
            content = insertBeforeContent(content, courseContent);
        }
        return content;
    }

    /**
     * insert before content
     *
     * @param content
     * @param courseContent
     * @return
     */
    private CourseContent insertBeforeContent(CourseContent content, CourseContent courseContent) {
        content.setSort(courseContent.getSort());
        courseContentRepository.insertSort(courseContent.getCourseId(), courseContent.getSort());
        return courseContentRepository.save(content);
    }

    /**
     * append content
     *
     * @param content
     * @return courseContent
     */
    private CourseContent appendCourseContent(CourseContent content) {
        int sort = courseContentRepository.getSort(content.getCourseId());
        content.setSort(sort + 1);
        return courseContentRepository.save(content);
    }

    /**
     * 删除课程内容
     *
     * @param contentId
     */
    public void delContent(long contentId) {
        Assert.isTrue(contentId > 0, "内容id不能为空");
        CourseContent content = courseContentRepository.findOne(contentId);
        Assert.notNull(content, "无效的内容id");
        content.setStatus(Status.DELETED);
        courseContentRepository.save(content);
    }

    /**
     * 获取用户扫码登录的二维码,有效时间5分钟
     *
     * @return
     */
    public String scanView() {
        String ticket = weixinService.scanView();
        SessionUtils.getSession().setAttribute("user", ticket);
        return ticket;
    }

    /**
     * @param fromUserName
     * @param toUserName
     * @param ticket
     * @return
     */
    public Resp scanLogin(String fromUserName, String toUserName, String ticket) {
        Teacher teacher = weixinService.loginTeacher(fromUserName);
        if (teacher != null) {
            messagingTemplate.convertAndSendToUser(ticket, "/queue/notifications", fromUserName);
        } else {
            throw new RuntimeException("some error occurs");
        }
        TextResp resp = new TextResp();
        resp.ToUserName = fromUserName;
        resp.FromUserName = toUserName;
        resp.CreateTime = System.currentTimeMillis();
        resp.Content = "您通过扫码登录了uquiz";
        return resp;
    }

    /**
     * 根据openId 登录
     *
     * @param openId
     */
    public void login(String openId) {
        Teacher teacher = weixinService.loginTeacher(openId);
        SessionUtils.getSession().setAttribute("teacher", teacher);
    }
}
