package com.leaf.uquiz.core.enums;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/2/22
 */
@Description("课程内容类型")
public enum ContentType implements Titleable {
    TEXT("文字"), IMAGE("图片"), VIDEO("音频");

    String title;

    ContentType(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
