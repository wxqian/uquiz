package com.leaf.uquiz.teacher.domain;

import com.leaf.uquiz.core.common.LeafPolaris;
import com.leaf.uquiz.core.enums.PasswordType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/3/9
 */
@Entity
@Table(name = "t_user_password")
public class UserPassword implements Serializable {
    private static final long serialVersionUID = 7673585884639248690L;

    @Id
    @GenericGenerator(name = "LeafPolaris", strategy = LeafPolaris.Type)
    @GeneratedValue(generator = "LeafPolaris")
    private Long id;

    @Column
    private long teacherId;

    @Column
    private String password;

    @Column
    private PasswordType passwordType = PasswordType.LOGIN;

    @Column
    private Date createDate = new Date();

    @Column
    private Date updateDate;

    public UserPassword() {
    }

    public UserPassword(long teacherId, String password) {
        this.teacherId = teacherId;
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public PasswordType getPasswordType() {
        return passwordType;
    }

    public void setPasswordType(PasswordType passwordType) {
        this.passwordType = passwordType;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
