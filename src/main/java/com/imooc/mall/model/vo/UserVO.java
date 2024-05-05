package com.imooc.mall.model.vo;

import java.util.Date;

public class UserVO {
    private Date createTime;
    private String username;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
