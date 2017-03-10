package com.leaf.uquiz.teacher.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/3/9
 */
@ApiModel(value = "教师注册信息",discriminator = "TeacherRegisterDto")
public class TeacherRegisterDto {

    @ApiModelProperty("注册码")
    private String name;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("验证码")
    private String kaptcha;

    public String getKaptcha() {
        return kaptcha;
    }

    public void setKaptcha(String kaptcha) {
        this.kaptcha = kaptcha;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
