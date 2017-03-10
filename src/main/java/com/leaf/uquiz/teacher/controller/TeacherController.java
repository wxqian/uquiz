package com.leaf.uquiz.teacher.controller;

import com.leaf.uquiz.core.utils.AmrToMp3;
import com.leaf.uquiz.file.dto.FileDto;
import com.leaf.uquiz.teacher.domain.Course;
import com.leaf.uquiz.teacher.domain.CourseContent;
import com.leaf.uquiz.teacher.domain.Teacher;
import com.leaf.uquiz.teacher.dto.TeacherRegisterDto;
import com.leaf.uquiz.teacher.service.TeacherService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/22
 */
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "获取教师信息成功")})
    @ApiOperation(value = "获取教师信息", notes = "获取教师信息")
    public Teacher teacherInfo() {
        return teacherService.teacherInfo();
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "更改教师信息成功")})
    @ApiOperation(value = "更改教师信息", notes = "更改教师信息")
    public void modify(@RequestBody Teacher teacher) {
        teacherService.modifyTeacher(teacher);
    }

    @RequestMapping(value = "/listCourse", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "获取课程信息成功")})
    @ApiOperation(value = "获取教师课程信息", notes = "获取教师课程信息")
    public Page<Course> listCourse(@ApiParam(name = "pageable", value = "分页信息,传参方式：?page=0&size=50") @PageableDefault(page = 0, size = 5) Pageable pageable,
                                   @ApiParam(name = "type", value = "查看所有课程或查看已发布课程,view|use") @RequestParam(name = "type", defaultValue = "view") String type) {
        return teacherService.listCourse(pageable, type);
    }

    @RequestMapping(value = "/course/{id}", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "获取课程详情成功")})
    @ApiOperation(value = "获取课程详情", notes = "获取课程详情")
    public Course detailCourse(@ApiParam(name = "id", value = "课程id") @PathVariable("id") long id) {
        return teacherService.detailCourse(id);
    }

    @RequestMapping(value = "/couse/edit", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "更改课程成功")})
    @ApiOperation(value = "更改课程", notes = "更改课程")
    public void modifyCourse(@RequestBody Course course) {
        teacherService.modifyCourse(course);
    }

    @RequestMapping(value = "/addContent", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "添加课程内容成功")})
    @ApiOperation(value = "添加课程内容", notes = "添加课程内容")
    public CourseContent addContent(@RequestBody CourseContent content, @ApiParam(name = "id", value = "不传代表append,传值代表插在值的前面") @RequestParam(name = "id", defaultValue = "0") long id) {
        return teacherService.addContent(content, id);
    }

    @RequestMapping(value = "/content/del/{id}", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "删除课程内容成功")})
    @ApiOperation(value = "删除课程内容", notes = "删除课程内容")
    public void delContent(@ApiParam("id") @PathVariable("id") long contentId) {
        teacherService.delContent(contentId);
    }

    @RequestMapping(value = "/contents/del", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "批量删除课程内容成功")})
    @ApiOperation(value = "批量删除课程内容", notes = "批量删除课程内容")
    public void delConents(@ApiParam(name = "id", value = "用户领奖列表id") @RequestParam("id") List<Long> ids) {
        teacherService.delContents(ids);
    }

    @RequestMapping(value = "/scanView", method = RequestMethod.GET)
    @ApiOperation(value = "返回二维码ticket", notes = "返回二维码ticket,图片地址是:https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET,TICKET为返回的ticket")
    @ApiResponses({@ApiResponse(code = 200, message = "获取扫码登录二维码")})
    public String scanView() {
        return teacherService.scanView();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "扫码登录成功")})
    @ApiOperation(value = "扫码登陆返回openId登录", notes = "扫码登陆后返回openId登录")
    public void login(@ApiParam("openId") @RequestParam("openId") String openId) {
        teacherService.login(openId);
    }

    @RequestMapping(value = "/delCourse/{id}", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "删除课程成功")})
    @ApiOperation(value = "删除课程", notes = "删除课程")
    public void delCourse(@ApiParam("id") @PathVariable("id") Long id) {
        teacherService.delCourse(id);
    }

    @RequestMapping(value = "/delCourses", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "批量删除课程成功")})
    @ApiOperation(value = "批量删除课程", notes = "批量删除课程")
    public void delCourses(@ApiParam(name = "id", value = "用户领奖列表id") @RequestParam("id") List<Long> ids) {
        teacherService.delCourses(ids);
    }

    @RequestMapping(value = "/publish/{id}", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "发布课程成功")})
    @ApiOperation(value = "发布课程", notes = "发布课程")
    public void publishCourse(@ApiParam("id") @PathVariable("id") Long id) {
        teacherService.publishCourse(id);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "注册用户成功")})
    @ApiOperation(value = "用户注册", notes = "用户注册")
    public void register(@RequestBody TeacherRegisterDto registerDto) {
        teacherService.register(registerDto);
    }

    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "用户登录")})
    @ApiOperation(value = "用户登录", notes = "用户登录")
    public void login(@ApiParam("name") @RequestParam("name") String loginName,
                      @ApiParam("password") @RequestParam("password") String password) {
        teacherService.userLogin(loginName, password);
    }

    @RequestMapping(value = "/isLogin", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "判断用户是否登录")})
    @ApiOperation(value = "判断用户是否登录", notes = "判断用户是否登录")
    public boolean isLogin() {
        return teacherService.isLogin();
    }

    @RequestMapping(value = "/exists/{name}", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "判断用户注册名是否存在")})
    @ApiOperation(value = "判断用户注册名是否存在", notes = "判断用户注册名是否存在")
    public boolean isUserExists(@ApiParam(name = "name", value = "注册名") @PathVariable("name") String loginName) {
        return teacherService.isUserExists(loginName);
    }

    @RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "更改用户密码")})
    @ApiOperation(value = "更改用户密码", notes = "更改用户密码")
    public void modifyPassword(@ApiParam(name = "teacherId", value = "教师id") @RequestParam("teacherId") long teacherId,
                               @ApiParam(name = "oldPassword", value = "原密码") @RequestParam("oldPassword") String oldPassword,
                               @ApiParam(name = "newPassword", value = "新密码") @RequestParam("newPassword") String newPassword) {
        teacherService.modifyPwd(teacherId, oldPassword, newPassword);
    }

    @RequestMapping(value = "/testAmr2Mp3", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "测试amr to mp3")})
    @ApiOperation(value = "测试amr to mp3", notes = "测试amr to mp3")
    public void testAmrToMp3(@ApiParam("fromPath") @RequestParam("fromPath") String fromPath,
                             @ApiParam("toPath") @RequestParam("toPath") String toPath) {
        AmrToMp3.convert(fromPath, toPath);
    }

    @RequestMapping(value = "/testAmr2Mp3_2", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "测试amr to mp3")})
    @ApiOperation(value = "测试amr to mp3", notes = "测试amr to mp3")
    public FileDto testAmrToMp3_2(@ApiParam("fromPath") @RequestParam("fromPath") String fromPath,
                                  @ApiParam("toPath") @RequestParam("toPath") String toPath) {
        return AmrToMp3.convert2(fromPath, toPath);
    }

}
