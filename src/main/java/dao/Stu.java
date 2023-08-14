package dao;

import java.sql.Timestamp;

public class Stu extends BaseObj {
    private String name = "Candy";
    private int age = 11;
    private String gender = "female";
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public Timestamp getTimestamp() {
        return timestamp;
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

    public void setTimestamp(Timestamp _timestamp) {
        this.timestamp = _timestamp;
    }
}