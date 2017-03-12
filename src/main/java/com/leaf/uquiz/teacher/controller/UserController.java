package com.leaf.uquiz.teacher.controller;

import com.leaf.uquiz.core.cache.StringCache;
import com.leaf.uquiz.core.utils.SessionUtils;
import com.leaf.uquiz.teacher.domain.Course;
import com.leaf.uquiz.teacher.service.TeacherService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/3/4
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private TeacherService teacherService;

    @RequestMapping(value = "/viewCourse/{id}", method = RequestMethod.GET)
    @ApiResponses({@ApiResponse(code = 200, message = "查看课程详情")})
    @ApiOperation(value = "查看课程详情", notes = "查看课程详情")
    public Course viewCourse(@ApiParam(name = "id", value = "课程id") @PathVariable("id") long id) {
        return teacherService.viewCourse(id);
    }

    @Autowired
    private StringCache stringCache;

    @RequestMapping(value = "/ticket", method = RequestMethod.GET)
    public String ticket() {
        String uuid = UUID.randomUUID().toString();
        stringCache.set("uuid", uuid);
        stringCache.set(uuid, SessionUtils.getSession().getId());
        logger.info("sessionId:{}", SessionUtils.getSession().getId());
        return uuid;
    }

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SimpUserRegistry userRegistry;

    @RequestMapping(value = "/sendTime", method = RequestMethod.GET)
    public void sendTime(Principal principal) {
        logger.info("sessionId:{}", SessionUtils.getSession().getId());
        if(principal != null){
            logger.info("principal:{}", principal.getName());
        }
        String uuid = stringCache.get("uuid");
        logger.info("sessionID:{}", stringCache.get(uuid) + "");
        messagingTemplate.convertAndSendToUser(stringCache.get(uuid), "/queue/notifications", System.currentTimeMillis());
        userRegistry.getUsers().stream()
                .map(u -> u.getName())
                .forEach(logger::info);
    }
}
