package dao.Entity;

import dev.morphia.annotations.*;
import dev.morphia.utils.IndexDirection;

import java.sql.Timestamp;
import java.util.Date;

@Entity(value = "baseObj")
public class BaseObj {
    // 创建用户
    public String creator = "system";

    // 创建时间, 默认创建降序索引, 允许后台自动创建索引
    @Indexed(options = @IndexOptions(background = true, expireAfterSeconds = 259200), value = IndexDirection.DESC)
    public Date createTime = new Timestamp(System.currentTimeMillis());

    // 更新用户
    public String updater = "system";

    // 更新时间, 默认创建降序索引, 允许后台自动创建索引
    @Indexed(options = @IndexOptions(background = true, expireAfterSeconds = 259200), value = IndexDirection.ASC)
    public Date updateTime = new Timestamp(System.currentTimeMillis());

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