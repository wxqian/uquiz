package com.leaf.uquiz.core.enums;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2016/10/30
 */
@Description("状态描述")
public enum Status implements Titleable {
    ENABLED("启用"), DISABLED("禁用"), AUDITING("审核中"), DELETED("删除");

    String title;

    Status(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
