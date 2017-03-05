package com.leaf.uquiz.teacher.domain;

import com.leaf.uquiz.core.common.LeafPolaris;
import com.leaf.uquiz.core.enums.ContentType;
import com.leaf.uquiz.core.enums.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/22
 */
@Entity
@Table(name = "t_course")
@ApiModel
public class Course implements Serializable {

    @Id
    @GenericGenerator(name = "LeafPolaris", strategy = LeafPolaris.Type)
    @GeneratedValue(generator = "LeafPolaris")
    @ApiModelProperty("id")
    private Long id;

    @Column
    @ApiModelProperty("教师Id")
    private long teacherId;

    @Column
    @ApiModelProperty("创建时间")
    private Date createTime = new Date();

    @Column
    @ApiModelProperty("标题")
    private String title;

    @Column
    @ApiModelProperty("status")
    private Status status = Status.AUDITING;

    @Transient
    @ApiModelProperty(readOnly = true)
    private List<CourseContent> contents;

    @Transient
    @ApiModelProperty(readOnly = true)
    private CourseContent firstImageContent;

    @Transient
    @ApiModelProperty(readOnly = true)
    private int count;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CourseContent> getContents() {
        return contents;
    }

    public void setContents(List<CourseContent> contents) {
        this.contents = contents;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public CourseContent getFirstImageContent() {

        CourseContent content = null;
        if (contents != null) {
            for (CourseContent courseContent : contents) {
                if (ContentType.IMAGE.equals(courseContent.getContentType())) {
                    content = courseContent;
                    break;
                }
            }
        }
        return content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Course() {
    }

    public Course(long teacherId) {
        this.teacherId = teacherId;
    }
}
