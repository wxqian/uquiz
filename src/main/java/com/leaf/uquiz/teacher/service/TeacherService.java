package com.leaf.uquiz.teacher.service;

import com.leaf.uquiz.core.cache.StringCache;
import com.leaf.uquiz.core.enums.Status;
import com.leaf.uquiz.core.exception.MyException;
import com.leaf.uquiz.core.utils.EncryptionUtil;
import com.leaf.uquiz.core.utils.RequestUtils;
import com.leaf.uquiz.core.utils.SessionUtils;
import com.leaf.uquiz.teacher.domain.*;
import com.leaf.uquiz.teacher.dto.TeacherRegisterDto;
import com.leaf.uquiz.teacher.repository.*;
import com.leaf.uquiz.weixin.message.resp.Resp;
import com.leaf.uquiz.weixin.message.resp.TextResp;
import com.leaf.uquiz.weixin.service.WeixinService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
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

    @Autowired
    private UserPasswordRepository userPasswordRepository;

    @Autowired
    private StringCache stringCache;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String ALGORITHM_MD5 = "MD5";

    /**
     * 根据openId 查找用户
     *
     * @param openId
     * @return
     */
    public Teacher findTeacherByOpenId(String openId) {
        Assert.hasLength(openId, "openId不能为空");
        Teacher teacher = teacherRepository.findByOpenId(openId);
        if (teacher != null) {
            teacher.setNickName(decodeNickName(teacher.getNickName()));
        }
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
        if (teacher.getId() != user.getId()) {
            throw new RuntimeException("请登陆后修改");
        }
        logger.info("nickName:{}", decodeNickName(encodeNickName(teacher.getNickName())));
        teacher.setId(user.getId());
        teacher.setNickName(encodeNickName(teacher.getNickName()));
        teacherRepository.save(teacher);
        teacher.setNickName(decodeNickName(teacher.getNickName()));
        SessionUtils.getSession().setAttribute("teacher", teacher);
    }

    /**
     * 查询教师列表
     *
     * @param pageable
     * @param type     view | use 所有 | 发布
     * @return
     */
    public Page<Course> listCourse(Pageable pageable, String type) {
        Teacher teacher = getCurrentTeacher();
        Page<Course> courses;
        if (StringUtils.equals("view", type)) {
            courses = courseRepository.findAllTeacherCourse(teacher.getId(), Status.ENABLED, Status.AUDITING, pageable);
        } else {
            courses = courseRepository.findTeacherCourse(teacher.getId(), Status.ENABLED, pageable);
        }
        List<Course> contents = courses.getContent();
        logger.info("content-size:{}", courses.getSize());
        if (!CollectionUtils.isEmpty(contents)) {
            for (Course course : contents) {
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
        long teacherId = course.getTeacherId();
        Teacher teacher = teacherRepository.findOne(teacherId);
        if (null == teacher) {
            throw new MyException("无效的课程");
        }
        course.setTeacherHeadImg(teacher.getHeadImg());
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
    @Transactional
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
        getCurrentTeacher();
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
        //二维码有效期300sec
        stringCache.set(ticket, SessionUtils.getSession().getId(), 300);
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
        String user = stringCache.get(ticket);
        String content = "您通过扫码登录了uquiz";
        if (user != null) {
            if (teacher != null) {
                messagingTemplate.convertAndSendToUser(user, "/queue/notifications", fromUserName);
            } else {
                messagingTemplate.convertAndSendToUser(user, "/queue/notifications", false);
            }
        } else {
            content = "二维码已过期";
        }

        TextResp resp = new TextResp();
        resp.ToUserName = fromUserName;
        resp.FromUserName = toUserName;
        resp.CreateTime = System.currentTimeMillis();
        resp.Content = content;
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

    /**
     * 同学登录查看课程详情
     *
     * @param id
     * @return
     */
    public Course viewCourse(long id) {
        Assert.isTrue(id > 0, "无效的课程id");
        Course course = courseRepository.findOne(id);
        Assert.notNull(course, "无效的课程id");
        course = getCourseDetail(course);
        addCourseRead(course);
        return course;
    }

    /**
     * 添加课程阅读记录
     *
     * @param course
     */
    private void addCourseRead(Course course) {
        CourseRead courseRead = new CourseRead();
        courseRead.setCourseId(course.getId());
        courseRead.setOpenId((String) SessionUtils.getSession().getAttribute("openId"));
        courseRead.setIp(RequestUtils.getIP());
        courseRead.setReadTime(new Date());
        courseReadRepository.save(courseRead);
    }

    /**
     * 删除课程
     *
     * @param id
     */
    public void delCourse(Long id) {
        Assert.notNull(id, "无效的课程id");
        Course course = courseRepository.findOne(id);
        Assert.notNull(course, "无效的课程id");
        Teacher user = getCurrentTeacher();
        if (user.getId() != course.getTeacherId()) {
            throw new RuntimeException("请登陆后操作");
        }
        course.setStatus(Status.DELETED);
        courseRepository.save(course);
    }

    /**
     * 发布课程
     *
     * @param id
     */
    public void publishCourse(Long id) {
        Assert.notNull(id, "无效的课程id");
        Course course = courseRepository.findOne(id);
        Assert.notNull(course, "无效的课程id");
        Teacher user = getCurrentTeacher();
        if (user.getId() != course.getTeacherId()) {
            throw new RuntimeException("请登陆后操作");
        }
        course.setStatus(Status.ENABLED);
        courseRepository.save(course);
    }

    /**
     * 批量删除课程内容
     *
     * @param ids
     */
    public void delContents(List<Long> ids) {
        Assert.notEmpty(ids, "内容id不能为空");
        getCurrentTeacher();
        List<CourseContent> contents = courseContentRepository.findAll(ids);
        Assert.notEmpty(contents, "无效的内容id");
        for (CourseContent content : contents) {
            content.setStatus(Status.DELETED);
        }
        courseContentRepository.save(contents);
    }

    /**
     * 批量删除课程
     *
     * @param ids
     */
    public void delCourses(List<Long> ids) {
        Assert.notEmpty(ids, "无效的课程id");
        List<Course> courses = courseRepository.findAll(ids);
        Assert.notEmpty(courses, "无效的课程id");
        getCurrentTeacher();
        for (Course course : courses) {
            course.setStatus(Status.DELETED);
        }
        courseRepository.save(courses);
    }

    /**
     * 判断当前用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return SessionUtils.getSession().getAttribute("teacher") != null;
    }

    /**
     * 判断用户注册名是否存在
     *
     * @param loginName
     * @return
     */
    public boolean isUserExists(String loginName) {
        Teacher teacher = teacherRepository.findByName(loginName);
        return teacher != null;
    }

    /**
     * 注册新用户
     *
     * @param registerDto
     */
    @Transactional
    public void register(TeacherRegisterDto registerDto) {
        Assert.notNull(registerDto, "参数不能为空");
        Assert.hasLength(registerDto.getName(), "用户名不能为空");
        Assert.hasLength(registerDto.getPassword(), "用户密码不能为空");
        Assert.hasLength(registerDto.getKaptcha(), "验证码不能为空");
        if (isUserExists(registerDto.getName())) {
            throw new MyException("该用户名已被注册");
        }
        if (!StringUtils.equalsIgnoreCase(registerDto.getKaptcha(), SessionUtils.getKaptcha())) {
            throw new MyException("验证码出错");
        }
        Teacher teacher = new Teacher();
        String name = registerDto.getName();
        teacher.setNickName(name);
        teacher.setName(name);
        String openId = (String) SessionUtils.getSession().getAttribute("openId");
        teacher.setOpenId(openId);
        teacher = teacherRepository.save(teacher);
        UserPassword userPassword = new UserPassword(teacher.getId(), EncryptionUtil.EncryptionStr(registerDto.getPassword(), ALGORITHM_MD5));
        userPasswordRepository.save(userPassword);
        SessionUtils.getSession().setAttribute("teacher", teacher);
    }

    /**
     * 用户名密码登录
     *
     * @param loginName
     * @param password
     */
    public void userLogin(String loginName, String password) {
        Assert.hasLength(loginName, "用户名不能为空");
        Assert.hasLength(password, "密码不能为空");
        Teacher teacher = teacherRepository.findByName(loginName);
        if (teacher == null) {
            throw new MyException("用户名或密码错误");
        }
        password = EncryptionUtil.EncryptionStr(password, ALGORITHM_MD5);
        UserPassword userPassword = userPasswordRepository.findByTeacherPassword(teacher.getId(), password);
        if (userPassword == null) {
            throw new MyException("用户名或密码错误");
        }
        SessionUtils.getSession().setAttribute("teacher", teacher);
    }

    /**
     * 更新用户密码
     *
     * @param teacherId
     * @param oldPassword
     * @param newPassword
     */
    public void modifyPwd(long teacherId, String oldPassword, String newPassword) {
        Assert.isTrue(teacherId > 0, "无效的教师id");
        Assert.hasLength(oldPassword, "旧密码不能为空");
        Assert.hasLength(newPassword, "新密码不能为空");
        UserPassword userPassword = userPasswordRepository.findByTeacherPassword(teacherId, EncryptionUtil.EncryptionStr(oldPassword, ALGORITHM_MD5));
        if (userPassword == null) {
            throw new MyException("用户名或密码错误");
        }
        userPassword = new UserPassword(teacherId, EncryptionUtil.EncryptionStr(newPassword, ALGORITHM_MD5));
        userPasswordRepository.save(userPassword);
    }

}
