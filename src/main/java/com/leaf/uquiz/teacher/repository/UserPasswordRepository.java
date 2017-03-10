package com.leaf.uquiz.teacher.repository;

import com.leaf.uquiz.teacher.domain.UserPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/3/9
 */
public interface UserPasswordRepository extends JpaRepository<UserPassword, Long> {

    @Query(value = "from UserPassword where teacherId = ?1 and password =?2 ")
    UserPassword findByTeacherPassword(long teacherId, String password);
}
