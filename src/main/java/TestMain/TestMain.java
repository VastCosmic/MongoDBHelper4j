package TestMain;

import Log.Log;
import dao.MongoDBHelper;
import dao.Stu;

import java.sql.Timestamp;
import java.util.*;

import static TestMain.TestMain.TestPak.largeDataSaveTest;

public class TestMain {
    public static void main(String[] args) {
        MongoDBHelper db = new MongoDBHelper("localhost", 27017, "demo");

        largeDataSaveTest();
//        Log.info("Start finding data.");
//        var result = db.findEntity(Stu.class);
//        Log.info("Finding data OK.");
//        // 打印
//        for(var obj : result){
//            //Log.info("Name：" + obj.getName() + " Age：" + obj.getAge() + " Gender：" + obj.getGender());
//        }

//        // 创建测试用Stu类型数据
//        Stu stu = new Stu();
//        TestPak.saveNewTest(stu);
//        sleep(1000);
//        List<Stu> result = TestPak.findTest();
//        sleep(1000);
//        TestPak.saveUpdateTest(result.get(0));
//        sleep(1000);
//        findEntityWithFiltersTest(stu);
    }

    public static class TestPak {
        private static final MongoDBHelper db = new MongoDBHelper("localhost", 27017, "demo");

        public static void largeDataSaveTest(){
            int batchSize = 100;
            int totalRecords = 10000000;
            List<Stu> stuList = new ArrayList<>();
            for (int i = 0; i < totalRecords; i++) {
                Stu student = new Stu();
                int age = new Random().nextInt(100);
                StringBuilder name = new StringBuilder();
                name.append((char) (new Random().nextInt(26) + 'A'));
                for (int j = 0; j < 10; j++) {
                    name.append((char) (new Random().nextInt(26) + 'a'));
                }
                String gender = new Random().nextInt(2) == 0 ? "man" : "female";
                student.setGender(gender);
                student.setName(name.toString());
                student.setAge(age);
                stuList.add(student);
            }
            Log.info("Start saving data.");
            db.saveEntity(stuList, batchSize,100);    // 5ms
            Log.info("Saving data OK.");
        }

        public static void saveNewTest(Stu stu) {
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

        public static List<Stu> findTest() {
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

        public static void saveUpdateTest(Stu stu) {
            stu.setName(stu.getName() + "_update");
            stu.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            stu.setUpdater("admin");

            db.saveEntity(stu);
            Log.info("Update successfully.");
        }

        public static void deleteTest(Stu stu) {
            db.deleteEntity(stu);
            Log.info("Delete successfully.");
        }

        public static void findEntityWithFiltersTest(Stu stu) {
            Map<String, Object> map = new HashMap<>();
            map.put("updater", "system");
            map.put("gender", "man");
            var result = db.findEntityWithFilters(Stu.class, map);
            for (var obj : result) {
                Log.info("Name：" + obj.getName() + " Age：" + obj.getAge() + " Gender：" + obj.getGender());
            }
        }
    }
}
