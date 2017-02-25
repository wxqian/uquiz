package com.leaf.uquiz.teacher.controller;

import com.leaf.uquiz.teacher.domain.Course;
import com.leaf.uquiz.teacher.domain.CourseContent;
import com.leaf.uquiz.teacher.domain.Teacher;
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
    public Page<Course> listCourse(@ApiParam(name = "pageable", value = "分页信息,传参方式：?page=0&size=50") @PageableDefault(page = 0, size = 5) Pageable pageable) {
        return teacherService.listCourse(pageable);
    }

    @RequestMapping(value = "/course/{id}", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "获取课程详情成功")})
    @ApiOperation(value = "获取课程详情", notes = "获取课程详情")
    public Course detailCourse(@ApiParam(name = "id", value = "课程id") @PathVariable("id") @RequestParam(name = "id", defaultValue = "0") long id) {
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
}
