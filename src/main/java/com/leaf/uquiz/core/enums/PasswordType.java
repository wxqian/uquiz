package com.leaf.uquiz.core.enums;

/**
 * @author <a href="mailto:qianwx@asiainfo.com">qianwx</a>
 * @version 1.0.0
 * @date 2017/3/9
 */
@Description("密码类型")
public enum PasswordType implements Titleable {
    LOGIN("登陆密码");

    String title;

    PasswordType(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
