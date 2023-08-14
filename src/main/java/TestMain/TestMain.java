package TestMain;

import dao.MongoDBHelper;
import dao.Stu;
import Log.Log;
import dev.morphia.query.FindOptions;

import java.sql.Timestamp;
import java.util.Random;

import static java.lang.Thread.sleep;

public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        MongoDBHelper db = new MongoDBHelper("localhost", 27017, "demo");

        // 创建测试用Stu类型数据
        // 随机年龄与性别
        int age = new Random().nextInt(100);
        Stu stu = new Stu();
        // 随机生成人名字符串作为姓名，第一个字符大写
        StringBuilder name = new StringBuilder();
        name.append((char) (new Random().nextInt(26) + 'A'));
        for (int i = 0; i < 5; i++) {
            name.append((char) (new Random().nextInt(26) + 'a'));
        }
        stu.setName(name.toString());
        stu.setAge(age);
        if (age % 2 == 1)
            stu.setGender("man");
        else
            stu.setGender("female");
        stu.creator = "Admin";
        stu = db.saveEntity(stu);
        Log.info("Save successfully.");

        // 打印所有Stu类型数据
        Log.info("Sort by name.");
        var result = db.findEntityWithSort(Stu.class, "name", 1);
        for (var obj : result) {
            Log.info("Name：" + obj.getName() + " Age：" + obj.getAge() + " Gender：" + obj.getGender());
        }

        Log.info("Sort by age.");
        var result2 = db.findEntityWithSort(Stu.class, "age", 1);
        for (var obj : result2) {
            Log.info("Name：" + obj.getName() + " Age：" + obj.getAge() + " Gender：" + obj.getGender());
        }

//        sleep(10000);
//        stu.setName("CCC_updated");
//        stu.setUpdateTime(new Timestamp(System.currentTimeMillis()));
//        stu.setUpdater("admin");
//
//        db.saveEntity(stu);
//        Log.info("Update successfully.");
//
//        sleep(10000);
//        db.deleteEntity(stu);
//        Log.info("Delete successfully.");
    }
}
