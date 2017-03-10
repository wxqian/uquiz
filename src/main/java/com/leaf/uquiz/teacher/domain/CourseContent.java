package com.leaf.uquiz.teacher.domain;

import com.leaf.uquiz.core.common.LeafPolaris;
import com.leaf.uquiz.core.enums.ContentType;
import com.leaf.uquiz.core.enums.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/22
 */
@Entity
@Table(name = "t_course_content")
@ApiModel(value = "课程详情", discriminator = "CourseContent")
public class CourseContent implements Serializable {

    private static final long serialVersionUID = 2039613778308089071L;
    @Id
    @GenericGenerator(name = "LeafPolaris", strategy = LeafPolaris.Type)
    @GeneratedValue(generator = "LeafPolaris")
    @ApiModelProperty("id")
    private Long id;

    @Column
    @ApiModelProperty("课程id")
    private long courseId;

    @Column
    @ApiModelProperty("内容类型")
    private ContentType contentType;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "text")
    @ApiModelProperty("内容详细")
    private String content;

    @Column
    @ApiModelProperty("状态")
    private Status status = Status.ENABLED;

    @Column
    @ApiModelProperty("排序")
    private int sort;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
