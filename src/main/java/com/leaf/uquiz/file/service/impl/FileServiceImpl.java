package com.leaf.uquiz.file.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.leaf.uquiz.core.utils.IOUtils;
import com.leaf.uquiz.file.domain.File;
import com.leaf.uquiz.file.domain.Space;
import com.leaf.uquiz.file.domain.SpaceType;
import com.leaf.uquiz.file.domain.Zoom;
import com.leaf.uquiz.file.service.FileRepository;
import com.leaf.uquiz.file.service.FileService;
import com.leaf.uquiz.file.service.Storage;
import com.leaf.uquiz.file.service.VFile;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

/**
 * The Class FileServiceImpl.
 *
 * @author <a href="mailto:stormning@163.com">ningzhou</a>
 * @since 2014-8-20
 */
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    private FileRepository fileRepository;

    private Storage storage;

    private List<Space> spaces = Lists.newArrayList();

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void setSpaces(List<Space> spaces) {
        this.spaces = spaces;
    }

    public void setFileRepository(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    @Cacheable(value = "getFile", keyGenerator = "wiselyKeyGenerator")
    public File getFile(long id) {
        return fileRepository.findOne(id);
    }

//    @Override
//    public Map<Long, File> mgetFile(Collection<Long> ids) {
//        return fileRepository.mget(ids);
//    }

    @Override
    @Transactional
    public File saveFile(File file) {
        file.setName(StringUtils.defaultString(file.getName()));
        file.setOwner(StringUtils.defaultString(file.getOwner()));
        file.setUpdateAt(System.currentTimeMillis());
        return fileRepository.save(file);
    }

    @Override
    @Transactional
    public File saveInputStreamFile(File file, InputStream inputStream) {
        VFile vf = getVFile(file);
        vf.saveFrom(inputStream);
        fillFileInfo(file, vf);
        return file;
    }

    @Override
    @Transactional
    public File saveDiskFile(File file, java.io.File diskFile) {
        if (StringUtils.isBlank(file.getName())) {
            file.setName(diskFile.getName());
        }
        VFile vf = getVFile(file);
        vf.copyFrom(diskFile);
        fillFileInfo(file, vf);
        return file;
    }

    @Transactional
    private void fillFileInfo(File file, VFile vf) {
        file.setSize(vf.getSize());
        boolean change = false;
        if (StringUtils.endsWithIgnoreCase(file.getExt(), "amr")) {
            file.setDuration((int) Math.ceil(FileUtils.getAmrDuration(vf.getNativeFile()) / 1000.0f));
            change = true;
        }
        if (file.isImage()) {
            try {
                ImageTransform imageTransform = getImageTransform();
                imageTransform.load(vf.getNativeFile().getAbsolutePath());
                file.setWidth(imageTransform.getSize().getWidth());
                file.setHeight(imageTransform.getSize().getHeight());
                change = true;
            } catch (Exception e) {
                LOGGER.warn("get image width and height fail:", e);
            }
        }
        if (change || file.getId() == null) {
            fileRepository.save(file);
        }
    }

    @Override
    @Async
    public void saveUrlFile(final long id, final String url) {
        InputStream is = IOUtils.load(url);
        File file = getFile(id);
        saveInputStreamFile(file, is);
    }

    @Override
    public VFile getVFile(File file) {
        if (file.getId() == null) {
            file = saveFile(file);
        }
        Space space = getSpace(file.getSpaceId());
        return storage.getVFile(String.valueOf(space.getId()), String.valueOf(file.getId()));
    }

    @Override
    public Space getSpace(String spaceKey) {
        for (Space space : spaces) {
            if (StringUtils.equals(spaceKey, space.getKey())) {
                return space;
            }
        }
        return returnWhenNull();
    }

    @Override
    public Space getSpace(int spaceId) {
        for (Space space : spaces) {
            if (space.getId() == spaceId) {
                return space;
            }
        }
        return returnWhenNull();
    }

    private Space returnWhenNull() {
//        throw new AppException("Space not exist!", ExConstants.ENTITY_NOT_FOUND);
        return null;
    }

    private static ImageTransform getImageTransform() {
        return new AwtImageTransform();
    }

    public Space addImageSpace(String key, Zoom... zooms) {
        int id = spaces.size() + 1;
        Space space = new Space();
        space.setId(id);
        space.setType(SpaceType.IMAGE);
        space.setZooms(Lists.newArrayList(zooms));
        space.setKey(key);
        spaces.add(space);
        return space;
    }

    public Space addVideoSpace(String key, String... exts) {
        int id = spaces.size() + 1;
        Space space = new Space();
        space.setId(id);
        space.setType(SpaceType.VIDEO);
        space.setKey(key);
        space.setAllowExts(Sets.newHashSet(exts));
        spaces.add(space);
        return space;
    }
}
