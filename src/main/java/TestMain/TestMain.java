package TestMain;

import Log.Log;
import dao.Entity.IOTable;
import dao.MongoDBHelper;
import dao.Entity.Stu;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static TestMain.TestMain.TestPak.*;
import static java.lang.Thread.sleep;


public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        MongoDBHelper db = new MongoDBHelper("localhost", 27017, "demo","dao.Entity");

        //ioTableSaveTest();
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                ioTableSaveTest();
//            }
//        }, 0, 1000);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        int i = 0;
        while (true) {
            i++;
            ioTableSaveTest(executor);
            Log.info("第" + i + "次");
            sleep(1000);
            if (i==200){
                executor.shutdown();
                try {
                    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


//        Log.info("Start finding data.");
//        //var result = db.findEntityByPageByTime(Stu.class, 1, 10, -1);
//        var result = db.findEntityByPage(Stu.class, 900000, 100);
//        //var result = db.findEntityWithBatchSize(Stu.class,100);
//        Log.info("Finding data OK.");
//        // 打印找到的个数
//        Log.info("Found " + result.size() + " records.");


////        // 删除
////        Log.info("Start deleting data.");
////        db.deleteEntity(result);
////        Log.info("Deleting data OK.");
//
        // 打印
//        for(var obj : result){
//            Log.info("Name：" + obj.getName() + " Age：" + obj.getAge() + " Gender：" + obj.getGender());
//            Log.info("Creator：" + obj.getCreator() + " CreateTime：" + obj.getCreateTime() + " Updater：" + obj.getUpdater() + " UpdateTime：" + obj.getUpdateTime());
//        }

        // 新建一个线程，每小时打印一次数据库中的数据到文件中
//        Thread thread = new Thread(
//                () -> {
//                    try {
//                        Timer timerPrint = new Timer();
//                        timerPrint.schedule(new TimerTask() {
//                            @Override
//                            public void run() {
//                                Log.info("Start finding data.");
//                                var result = db.findEntity(Stu.class);
//                                Log.info("Finding data OK.");
//                                // 打印找到的第一个的时间
//                                Log.info("Found " + result.size() + " records.");
//                                if (!result.isEmpty()) {
//                                    // 打印找到的第一个的时间
//                                    Log.info("First record's time: " + result.get(0).getUpdateTime());
//                                    // 打印所有找到的数据
////                                    for (Stu v : result) {
////                                        Log.trace("Name：" + v.getName() + " Age：" + v.getAge() + " Gender：" + v.getGender());
////                                        Log.trace("Creator：" + v.getCreator() + " CreateTime：" + v.getCreateTime() + " Updater：" + v.getUpdater() + " UpdateTime：" + v.getUpdateTime());
////                                    }
//                                }
//                            }
//                        }, 60000, 1000 * 60 * 60);
//                    } catch (Exception e) {
//                        Log.error(String.valueOf(e));
//                    }
//                }
//        );
//        thread.start();


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
        private static final MongoDBHelper db = new MongoDBHelper("localhost", 27017, "demo","dao.Entity");

        public static boolean ioTableSaveTest() {
            int batchSize = 100;
            int totalRecords = 20000;
            List<IOTable> ioTableList = new ArrayList<>();
            for(int i = 0; i < totalRecords; i++) {
                IOTable ioTable = new IOTable();
                ioTableList.add(ioTable);
            }
            Log.info("Start saving data.");
            db.saveEntity(ioTableList, batchSize,4);
            Log.info("Saving data OK.");
            // 清理内存
            ioTableList.clear();
            System.gc();
            return true;
        }

        public static boolean ioTableSaveTest(ExecutorService executor) {
            int batchSize = 100;
            int totalRecords = 20000;
            List<IOTable> ioTableList = new ArrayList<>();
            for (int i = 0; i < totalRecords; i++) {
                IOTable ioTable = new IOTable();
                ioTableList.add(ioTable);
            }
            Log.info("Start saving data.");
            db.saveEntityForIoTable(ioTableList, batchSize, executor);
            return true;
        }

        public static void largeDataSaveTest() {
            int batchSize = 100;
            int totalRecords = 20000;
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
            db.saveEntity(stuList, batchSize, 8);    // 5ms
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
