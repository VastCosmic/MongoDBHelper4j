package dao;

import dev.morphia.annotations.*;

/*
 * @Entity 注解表示该类为实体类
 * @Entity(value = "stu") 表示该类对应的表名为stu
 * @Id 注解表示该字段为主键
 */
@Entity(value = "stu")
public class Stu extends BaseObj {
    /*
     *以下为实体类的属性
     */
    @Id
    public String Id;
    private String name;
    private int age;
    private String gender;

    /*
     * 以下为getter和setter方法
     */
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public void setAge(int _age) {
        this.age = _age;
    }

    public void setGender(String _gender) {
        this.gender = _gender;
    }

}