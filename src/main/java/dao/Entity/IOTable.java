package dao.Entity;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;

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
//@Indexes(@Index(fields = @Field(value = "cT", type = DESC)))
public class IOTable {
    // id
    @Id
    private String Id;
    // createTime
    @Indexed(options = @IndexOptions(expireAfterSeconds = 259200), value = DESC)
    private Date cT = new Date(System.currentTimeMillis());
    // tagCode
    private String tC;
    // tagValue
    private String tV;
    // 备用
    private String b1;

    public IOTable() {
        this.Id = UUID.randomUUID().toString();
        // 随机
        this.tC = Id.substring(0, 7);
        this.tV = Id.substring(7, 16);
        this.b1 = Id.substring(16, 22);
    }

    public IOTable(String tC, String tV) {
        this.Id = UUID.randomUUID().toString();
        this.tC = tC;
        this.tV = tV;
    }

    public IOTable(String tC, String tV, String b1) {
        this.Id = UUID.randomUUID().toString();
        this.tC = tC;
        this.tV = tV;
        this.b1 = b1;
    }

    public String getId() {
        return Id;
    }

    public Date getcT() {
        return cT;
    }

    public String gettC() {
        return tC;
    }

    public String gettV() {
        return tV;
    }

    public String getB1() {
        return b1;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setcT(Date cT) {
        this.cT = cT;
    }

    public void settC(String tC) {
        this.tC = tC;
    }

    public void settV(String tV) {
        this.tV = tV;
    }

    public void setB1(String b1) {
        this.b1 = b1;
    }
}
