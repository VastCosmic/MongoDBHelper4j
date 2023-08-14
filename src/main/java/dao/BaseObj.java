package dao;

import java.util.Date;
import java.util.UUID;

public class BaseObj {
    // Id
    public String Id = UUID.randomUUID().toString();
    // 创建用户
    public String creater = "system";
    // 创建时间
    public Date createTime = new Date();
    // 更新用户
    public String updater = "system";

    // 更新时间
    public Date updateTime = new Date();

//
//    public String getId() {
//        return Id;
//    }
//
//    public String getCreater() {
//        return creater;
//    }
//
//    public void setCreater(String creater) {
//        this.creater = creater;
//    }
//
//    public Date getCreateTime() {
//        return this.createTime;
//    }
//
//    public void setCreateTime(Date createTime) {
//        this.createTime = createTime;
//    }
//
//    public String getUpdater() {
//        return this.updater;
//    }
//
//    public void setUpdater(String updater) {
//        this.updater = updater;
//    }
//
//    public Date getUpdateTime() {
//        return this.updateTime;
//    }
//
//    public void setUpdateTime(Date updateTime) {
//        this.updateTime= updateTime;
//    }
}