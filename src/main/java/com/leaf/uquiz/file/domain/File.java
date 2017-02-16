package com.leaf.uquiz.file.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Sets;
import com.leaf.uquiz.core.common.LeafPolaris;
import com.leaf.uquiz.core.enums.Status;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "t_file")
@JsonIgnoreProperties(ignoreUnknown = true)
public class File implements Serializable {
    private static final long serialVersionUID = -2962288521851035537L;

    public static final int ZOOM_RAW = 0;
    public static final int ZOOM_BIG = 1;
    public static final int ZOOM_SMALL = 2;

    public static final Set<String> IMAGE_POSTFIX = Sets.newHashSet("gif", "bmp", "jpg", "jpeg", "png", "tif");
    public static final Set<String> DOC_POSTFIX = Sets.newHashSet("doc", "docx", "rtf", "xls", "xlsx", "ppt", "pptx", "pdf");
    public static final Set<String> TEXT_POSTFIX = Sets.newHashSet("txt", "html", "htm", "log", "xml", "ini", "java", "c", "cpp", "js", "css", "m", "h");
    public static final Set<String> VIDEO_POSTFIX = Sets.newHashSet("mpg", "mpeg", "mpe", "avi", "mov", "asf", "mp4", "wmv", "flv", "3gp", "rm", "rmvb");
    public static final Set<String> AUDIO_POSTFIX = Sets.newHashSet("mp3", "wav", "wma", "ogg", "ape", "flac", "aif", "pcm", "aac", "amr");
    public static final Set<String> ZIP_POSTFIX = Sets.newHashSet("zip", "rar", "7z", "jar", "gz", "tgz", "bz2", "tar", "iso");

    @Id
    @GenericGenerator(name = "polaris", strategy = LeafPolaris.Type)
    @GeneratedValue(generator = "polaris")
    private Long id;

    @Column(name = "space_id", precision = 3, nullable = false)
    @Index(name = "idx_file_space")
    private int spaceId;

    @Column(length = 64, nullable = false)
    private String owner;

    @Column(length = 256, nullable = false)
    //"文件名"
    private String name;

    @Column(name = "size", nullable = false)
    //"大小"
    private int size;

    @Column(name = "duration", precision = 5, nullable = false)
    //"持续时间,单位秒,仅供音频视频用"
    private int duration;

    @Column(name = "width", precision = 5, nullable = false)
    //"宽度,仅供图片视频用"
    private int width;

    @Column(name = "height", precision = 5, nullable = false)
    //"高度,仅供图片视频用"
    private int height;

    @Column(length = 256)
    //"存储或远程地址"
    private String url;

    @Column(name = "user_id", nullable = false)
    //"用户编号"
    private long userId;

    @Column(name = "expire_at", nullable = false)
    //"过期时间"
    private long expireAt;

    @Column(name = "update_at", nullable = false)
    //"更新时间戳"
    private long updateAt;

    @Column(precision = 3, nullable = false)
    //"状态"
    private Status status = Status.ENABLED;

    @Transient
    public String ext;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(long expireAt) {
        this.expireAt = expireAt;
    }

    public long getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(long updateAt) {
        this.updateAt = updateAt;
    }


    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getExt() {
        if (ext == null) {
            ext = FilenameUtils.getExtension(name);
        }
        return ext;
    }

    public String getSimpleName() {
        return StringUtils.substringBeforeLast(name, ".");
    }

    public boolean isEnabled() {
        return status == Status.ENABLED;
    }

    public boolean isImage() {
        return IMAGE_POSTFIX.contains(getExt());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
