package dao;

import java.util.Date;

public class BaseObj {
    // 创建用户
    public String creator = "system";
    // 创建时间
    public Date createTime = new Date();
    // 更新用户
    public String updater = "system";
    // 更新时间
    public Date updateTime = new Date();

    public String getCreator() {
        return creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getUpdater() {
        return updater;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}