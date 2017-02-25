package com.leaf.uquiz.teacher.repository;

import com.leaf.uquiz.core.enums.Status;
import com.leaf.uquiz.teacher.domain.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/23
 */
public interface CourseContentRepository extends JpaRepository<CourseContent, Long> {

    @Query(value = "from CourseContent where courseId = ?1 and status =?2 order by sort asc")
    List<CourseContent> getCourseContents(Long courseId, Status status);

    @Query(value = "select ifnull(max(sort),0) from t_course_content where course_id = ?1", nativeQuery = true)
    int getSort(long courseId);

    @Modifying
    @Query("update CourseContent t set t.sort = t.sort + 1 where t.courseId = ?1 and t.sort >= ?2")
    void insertSort(long courseId, int sort);
}
