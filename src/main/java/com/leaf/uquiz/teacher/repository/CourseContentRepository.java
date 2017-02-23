package com.leaf.uquiz.teacher.repository;

import com.leaf.uquiz.core.enums.Status;
import com.leaf.uquiz.teacher.domain.CourseContent;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
