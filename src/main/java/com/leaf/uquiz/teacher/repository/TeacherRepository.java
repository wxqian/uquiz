package com.leaf.uquiz.teacher.repository;

import com.leaf.uquiz.teacher.domain.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/22
 */
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    Teacher findByOpenId(String openId);
    Teacher findByName(String name);
}
