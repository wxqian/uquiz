package com.leaf.uquiz.file.service;


import com.leaf.uquiz.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/14.
 */
public interface FileRepository extends JpaRepository<File, Long> {
    @Query("FROM File WHERE name = ?1")
    List<File> getFileListByName(String name);
}
