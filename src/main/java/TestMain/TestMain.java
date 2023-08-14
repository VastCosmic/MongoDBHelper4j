package TestMain;

import dao.MongoDBHelper;
import dao.Stu;
import Log.Log;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        MongoDBHelper db = new MongoDBHelper("localhost", 27017, "demo");

        // 创建测试用Stu类型数据
        Stu stu = new Stu();
        TestPak.saveNewTest(stu);
        sleep(1000);
        List<Stu> result = TestPak.findTest();
        sleep(1000);
        TestPak.saveUpdateTest(result.get(0));
    }

    public static class TestPak{
        private static final MongoDBHelper db = new MongoDBHelper("localhost", 27017, "demo");

        public static void saveNewTest(Stu stu){
            // 随机年龄与性别
            int age = new Random().nextInt(100);
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
            db.saveEntity(stu);
            Log.info("Save successfully.");
        }

        public static List<Stu> findTest(){
            Log.info("Sort by name.");
            var result = db.findEntityWithSort(Stu.class, "name", 1);
            for (var obj : result) {
                Log.info("Name：" + obj.getName() + " Age：" + obj.getAge() + " Gender：" + obj.getGender());
            }

            Log.info("Sort by age.");
            result = db.findEntityWithSort(Stu.class, "age", 1);
            for (var obj : result) {
                Log.info("Name：" + obj.getName() + " Age：" + obj.getAge() + " Gender：" + obj.getGender());
            }

            return result;
        }

        public static void saveUpdateTest(Stu stu){
            stu.setName(stu.getName()+"_update");
            stu.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            stu.setUpdater("admin");

            db.saveEntity(stu);
            Log.info("Update successfully.");
        }

        public static void deleteTest(Stu stu){
            db.deleteEntity(stu);
            Log.info("Delete successfully.");
        }
    }
}
