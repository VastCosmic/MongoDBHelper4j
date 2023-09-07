package dao.Entity;

import dev.morphia.annotations.*;

import java.util.Date;
import java.util.UUID;

import static dev.morphia.utils.IndexDirection.DESC;

/*
 * @Entity 注解表示该类为实体类
 * @Entity(value = "io_table") 表示该类对应的集合名为 io_table
 * @Id 注解表示该字段为主键
 * @Indexes 注解表示该类的索引
 * @Index 表示该类的索引的属性
 * @IndexOptions 表示该类的索引的属性的选项
 * @Field 表示该类的索引的属性的选项的属性
 */
@Entity(value = "io_table")
//@Indexes(@Index(fields = @Field(value = "createTime", type = DESC)))
public class IOTable {
    @Id
    private String Id;
    @Property("cT") // Property注解 表示该字段在数据库中的名字
    @Indexed(options = @IndexOptions(expireAfterSeconds = 259200), value = DESC)
    private Date createTime = new Date(System.currentTimeMillis());
    @Property("tC")
    private String tagCode;
    @Property("tV")
    private String tagValue;
    @Property("b1")
    private String stringBak;   // 备用字段

    public IOTable() {
        this.Id = UUID.randomUUID().toString();
        // 随机
        this.tagCode = Id.substring(0, 7);
        this.tagValue = Id.substring(7, 16);
        this.stringBak = Id.substring(16, 22);
    }

    public IOTable(String tagCode, String tagValue) {
        this.Id = UUID.randomUUID().toString();
        this.tagCode = tagCode;
        this.tagValue = tagValue;
    }

    public IOTable(String tagCode, String tagValue, String stringBak) {
        this.Id = UUID.randomUUID().toString();
        this.tagCode = tagCode;
        this.tagValue = tagValue;
        this.stringBak = stringBak;
    }

    public String getId() {
        return Id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getTagCode() {
        return tagCode;
    }

    public String getTagValue() {
        return tagValue;
    }

    public String getStringBak() {
        return stringBak;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public void setStringBak(String stringBak) {
        this.stringBak = stringBak;
    }
}
