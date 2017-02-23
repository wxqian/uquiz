package com.leaf.uquiz.teacher.repository;

import com.leaf.uquiz.teacher.domain.CourseRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/23
 */
public interface CourseReadRepository extends JpaRepository<CourseRead, Long> {
    @Query(value = "select count(1) from t_course_read t where t.course_id = ?1", nativeQuery = true)
    int getReadCount(long courseId);
}
