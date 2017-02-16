package com.leaf.uquiz.file.convert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.leaf.uquiz.core.common.AbstractEntityConverter;
import com.leaf.uquiz.core.utils.Env;
import com.leaf.uquiz.file.domain.File;
import com.leaf.uquiz.file.domain.Space;
import com.leaf.uquiz.file.dto.FileDto;
import com.leaf.uquiz.file.service.FileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * .
 * <p/>
 *
 * @author <a href="mailto:stormning@163.com">stormning</a>
 * @version V1.0, 2015/8/18.
 */
@Component
public class FileConverter extends AbstractEntityConverter<File, Long, FileDto> {

    private String baseUrl = "/api/file/";

    @Autowired
    private FileService fileService;

    @Override
    public FileDto internalConvert(File file) {
        FileDto fileDto = new FileDto();
        fileDto.setId(file.getId());
        fileDto.setName(file.getName());
        fileDto.setSize(file.getSize());
        fileDto.setSmallUrl(getImgUrl(file, File.ZOOM_SMALL));
        fileDto.setBigUrl(getImgUrl(file, File.ZOOM_BIG));
        return fileDto;
    }

    public String getImgUrl(File file, int zoom) {
        return getImgUrl(file.getId(), zoom);
    }

    public String getImgUrl(Long id, int zoom) {
        return getImgUrl(id, zoom, StringUtils.EMPTY);
    }

    public String getImgUrl(Long fileId, int zoom, String def) {
        if (fileId == null || fileId == 0) {
            return def;
        }
        if (zoom == File.ZOOM_RAW) {
            return Env.url(baseUrl + fileId);
        }
        return Env.url(baseUrl + "z" + zoom + "/" + fileId);
    }

    public FileDto convert(File thumb, String spaceKey) {
        if (thumb == null || thumb.getId() == null || thumb.getId() == 0) {
            Space space = fileService.getSpace(spaceKey);
            FileDto fileDto = new FileDto();
            fileDto.setBigUrl(space.getDefaultImage());
            fileDto.setSmallUrl(space.getDefaultImage());
            return fileDto;
        }
        return convert(thumb);
    }

    public Map<Long, FileDto> mget(Collection<Long> ids, String spaceKey) {
        Map<Long, FileDto> fileDtos = Maps.newHashMap();
        for (Long id : ids) {
            fileDtos.put(id, to(id, spaceKey));
        }
        return fileDtos;
    }

    public FileDto to(Long id, String spaceKey) {
        if (id == null || id == 0) {

            Space space = fileService.getSpace(spaceKey);
            FileDto fileDto = new FileDto();
            fileDto.setBigUrl(space.getDefaultImage());
            fileDto.setSmallUrl(space.getDefaultImage());
            return fileDto;
        }
        return convertWithoutInfo(id);
    }

    private FileDto convertWithoutInfo(Long id) {
        FileDto fileDto = new FileDto();
        fileDto.setId(id);
        fileDto.setSmallUrl(getImgUrl(id, File.ZOOM_SMALL));
        fileDto.setBigUrl(getImgUrl(id, File.ZOOM_BIG));
        return fileDto;
    }

    public List<FileDto> to(List<Long> keys, String spaceKey) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyList();
        }
        List<FileDto> dtos = Lists.newArrayList();
        for (Long key : keys) {
            dtos.add(to(key, spaceKey));
        }
        return dtos;
    }

    @Override
    public FileDto convert(File source) {
        FileDto fileDto = new FileDto();
        fileDto.setId(source.getId());
        fileDto.setName(source.getName());
        fileDto.setSize(source.getSize());
        return fileDto;
    }
}
