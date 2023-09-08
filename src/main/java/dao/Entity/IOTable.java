package dao.Entity;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Property;

import java.util.UUID;

/*
 * @Entity 注解表示该类为实体类
 * @Entity(value = "io_table", useDiscriminator = false)
 * value = "io_table" 表示该类对应的集合名为 io_table
 * useDiscriminator = false 表示不使用区分器field
 *
 * @Id 注解表示该字段为主键
 * @Indexes 注解表示该类的索引
 * @Index 表示该类的索引的属性
 * @IndexOptions 表示该类的索引的属性的选项
 * @Field 表示该类的索引的属性的选项的属性
 *
 * 以下备用
//    @Property("cT") // Property注解 表示该字段在数据库中的名字
//    @Indexed(options = @IndexOptions(expireAfterSeconds = 259200), value = DESC)    // expireAfterSeconds 表示过期时间  value = DESC 表示降序索引
//    private Date createTime = new Date(System.currentTimeMillis());
//    @Property("tC")
//    private String tagCode;
 */
@Entity(value = "io_table", useDiscriminator = false)
public class IOTable {
    @Id
    private IdEntity Id;
    @Property("tV")
    private String tagValue;
    @Property("b1")
    private String stringBak;   // 备用字段

    public IOTable() {
        // 随机,仅测试
        var tagCode = UUID.randomUUID().toString();
        this.Id = new IdEntity(tagCode.substring(0,7));
        this.tagValue = tagCode.substring(7, 16);
        this.stringBak = tagCode.substring(16, 22);
    }

    public IOTable(String tagCode, String tagValue, String stringBak) {
        this.Id = new IdEntity(tagCode);
        this.tagValue = tagValue;
        this.stringBak = stringBak;
    }

    public IOTable(String tagCode, String tagValue) {
        this.Id = new IdEntity(tagCode);
        this.tagValue = tagValue;
    }

    public IOTable(String tagCode) {
        this.Id = new IdEntity(tagCode);
    }

    public IdEntity getId() {
        return Id;
    }

    public void setId(IdEntity id) {
        Id = id;
    }

    public String getTagValue() {
        return tagValue;
    }

    public String getStringBak() {
        return stringBak;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public void setStringBak(String stringBak) {
        this.stringBak = stringBak;
    }

    public String toString() {
        return "IOTable{" +
                "Id=" + Id +
                ", tagValue='" + tagValue + '\'' +
                ", stringBak='" + stringBak + '\'' +
                '}';
    }
}
