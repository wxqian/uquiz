package com.leaf.uquiz.teacher.domain;

import com.leaf.uquiz.core.common.LeafPolaris;
import com.leaf.uquiz.core.enums.Status;
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
public class Teacher implements Serializable {

    @Id
    @GenericGenerator(name = "LeafPolaris", strategy = LeafPolaris.Type)
    @GeneratedValue(generator = "LeafPolaris")
    private long id;

    @Column(length = 50)
    private String openId;

    @Column(length = 150, nullable = false)
    private String headImg;

    @Column(length = 100, nullable = false)
    private String nickName;

    @Column(length = 100)
    private String name;

    @Column(length = 50)
    private String course;

    @Column(length = 100)
    private String university;

    @Column(length = 50)
    private String level;

    @Column(length = 50)
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
