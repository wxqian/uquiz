package com.leaf.uquiz.teacher.controller;

import com.leaf.uquiz.teacher.domain.Course;
import com.leaf.uquiz.teacher.service.TeacherService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/3/4
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    private TeacherService teacherService;

    @RequestMapping(value = "/viewCourse/{id}", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "查看课程详情")})
    @ApiOperation(value = "查看课程详情", notes = "查看课程详情")
    public Course viewCourse(@ApiParam(name = "id", value = "课程id") @PathVariable("id") long id) {
        return teacherService.viewCourse(id);
    }
}
