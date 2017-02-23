package com.leaf.uquiz.teacher.controller;

import com.leaf.uquiz.core.utils.SessionUtils;
import com.leaf.uquiz.teacher.domain.Teacher;
import com.leaf.uquiz.teacher.service.TeacherService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        return (Teacher) SessionUtils.getSession().getAttribute("user");
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    @ApiResponses({@ApiResponse(code = 200, message = "更改教师信息成功")})
    @ApiOperation(value = "", notes = "")
    public void modify(@RequestBody Teacher teacher) {
        teacherService.modifyTeacher(teacher);
    }
}
