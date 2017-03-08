package com.leaf.uquiz.teacher.repository;

import com.leaf.uquiz.core.enums.Status;
import com.leaf.uquiz.teacher.domain.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/23
 */
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query(value = "from Course where teacherId = ?1 and status = ?2 order by createTime desc, id desc",
            countQuery = "select count(1) from Course where teacherId = ?1 and status <= ?2 ")
    Page<Course> findTeacherCourse(long teacherId, Status status, Pageable pageable);

    @Query(value = "from Course where teacherId = ?1 and (status = ?2 or status = ?3) order by createTime desc ,id desc",
            countQuery = "select count(1) from Course where teacherId = ?1 and (status = ?2 or status = ?3)")
    Page<Course> findAllTeacherCourse(long teacherId, Status s1, Status s2, Pageable pageable);
}
