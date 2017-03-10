package com.leaf.uquiz.teacher.domain;

import com.leaf.uquiz.core.common.LeafPolaris;
import com.leaf.uquiz.core.enums.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/22
 */
@Entity
@Table(name = "t_teacher")
@ApiModel(value = "教师信息", discriminator = "Teacher")
public class Teacher implements Serializable {

    private static final long serialVersionUID = -836926940936280786L;
    @Id
    @GenericGenerator(name = "LeafPolaris", strategy = LeafPolaris.Type)
    @GeneratedValue(generator = "LeafPolaris")
    @ApiModelProperty("id")
    private long id;

    @Column(length = 50)
    @ApiModelProperty("openId")
    private String openId;

    @Column(length = 150, nullable = false)
    @ApiModelProperty("用户头像url")
    private String headImg = "http://m.studypointshare.com/images/timg.jpg";

    @Column(length = 100, nullable = false)
    @ApiModelProperty("用户昵称")
    private String nickName;

    @Column(length = 100)
    @ApiModelProperty("用户姓名")
    private String name;

    @Column(length = 50)
    @ApiModelProperty("所授课程")
    private String course;

    @Column(length = 100)
    @ApiModelProperty("任职学校")
    private String university;

    @Column(length = 50)
    @ApiModelProperty("职称")
    private String level;

    @Column(length = 50)
    @ApiModelProperty("联系电话")
    private String phone;

    @Column
    private Date createTime = new Date();

    @Column
    private Status status = Status.ENABLED;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Teacher() {
    }

    public Teacher(String openId, String nickName, String headImg) {
        this.openId = openId;
        this.nickName = nickName;
        this.headImg = headImg;
    }
}
