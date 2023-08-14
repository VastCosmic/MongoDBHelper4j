package TestMain;

import dao.BaseObj;
import dao.MongoDBHelper;
import dao.Stu;
import Log.Log;

import java.sql.Timestamp;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Stu stu = new Stu();
        stu.setName("CCC");
        stu.setAge(12);
        stu.setGender("man");
        stu.setTimestamp(new Timestamp(System.currentTimeMillis()));
//        stu.creater = "John";
//        stu.createTime = new Date();
//        stu.updater = "John_updater";
//        stu.updateTime = new Date();
        System.out.println(stu.createTime.toString());

        Stu stu2 = new Stu();
        stu2.setName("CAA");
        stu2.setAge(33);
        stu2.setGender("female");
        stu2.setTimestamp(new Timestamp(System.currentTimeMillis()));
        stu2.creater = "Bob";
        stu2.createTime = new Date();
        stu2.updater = "Bob_updater";
        stu2.updateTime = new Date();

        Stu stu3 = new Stu();
        stu3.setName("CBB");
        stu3.setAge(44);
        stu3.setGender("man");
        stu3.setTimestamp(new Timestamp(System.currentTimeMillis()));
        stu3.creater = "Candy";
        stu3.createTime = new Date();
        stu3.updater = "Candy_updater";
        stu3.updateTime = new Date();

        MongoDBHelper db1 = new MongoDBHelper("127.0.0.1", 27017, "demo");
        db1.save("u", stu);
//        db1.save("u", stu2);
//        db1.save("u", stu3);
        Log.info("Save successfully.");


        var result = db1.deleteAll("u", "name", "CCC");
        Log.info(result.toString());
//
//        List<Map<String, Object>> documents = new ArrayList<>();
//        Map<String, Object> document1 = new HashMap<>();
//        document1.put("name", "John");
//        document1.put("age", 30);
//        documents.add(document1);
//
//        Map<String, Object> document2 = new HashMap<>();
//        document2.put("name", "Jane");
//        document2.put("age", 25);
//        documents.add(document2);
//
//        MongoDBHelper mongoDBHelper = new MongoDBHelper("localhost", 27017, "demo");
//        mongoDBHelper.insertMany("users", documents);
    }
}
