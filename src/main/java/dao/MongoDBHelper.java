package dao;

import Log.Log;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

public class MongoDBHelper {
    private final MongoClient mongoClient;
    private MongoDatabase database = null;
    //private MongoCollection<BasicDBObject> collection;

    public MongoDBHelper(String host, int port, String dbName) {
        mongoClient = new MongoClient(host, port);
        database = mongoClient.getDatabase(dbName);
        //collection = database.getCollection(collName, BasicDBObject.class);
    }

    /*
     * 将对象存入数据库
     * @param obj 要存入的对象, 应当继承自BaseObj
     * @return boolean 是否存入成功
     */
    public boolean save(String collection, Object obj) {
        BasicDBObject document = new BasicDBObject();
        try {
            Class<?> clazz = obj.getClass();

            // 获取类的所有属性
            // 获取类的父类的共有属性，并添加到fields中
            Field[] fields = Stream.concat(
                            Arrays.stream(clazz.getDeclaredFields()),
                            Arrays.stream(clazz.getFields()))
                    .toArray(Field[]::new);

            for (Field field : fields) {
                // 设置可访问性
                field.setAccessible(true);
                String name = field.getName();
                Object value = null;
                try {
                    // 通过反射获取字段值
                    value = field.get(obj);
                } catch (IllegalAccessException e) {
                    Log.error("Save error." + e);
                    return false;
                }
                // 将字段名和值放入文档中
                document.append(name, value);
            }

            database.getCollection(collection, BasicDBObject.class).insertOne(document);
        } catch (Exception e) {
            Log.error("Save error, check your format." + e);
            return false;
        }
        return true;
    }

    public boolean insertOne(String collection, Map<String, Object> map) {
        BasicDBObject document = new BasicDBObject();
        for (String key : map.keySet()) {
            document.append(key, map.get(key));
        }
        database.getCollection(collection, BasicDBObject.class).insertOne(document);
        return true;
    }

    public boolean insertMany(String collection, List<Map<String, Object>> list) {
        List<BasicDBObject> documents = new java.util.ArrayList<>(List.of());
        for (Map<String, Object> map : list) {
            BasicDBObject document = new BasicDBObject();
            for (String key : map.keySet()) {
                document.append(key, map.get(key));
            }
            documents.add(document);
        }
        database.getCollection(collection, BasicDBObject.class).insertMany(documents);
        return true;
    }

    /*
     **从collection中删除匹配参数的第一个document
     */
    public DeleteResult deleteOne(String collection, String key, Object value) {
        BasicDBObject document = new BasicDBObject();
        document.append(key, value);
        return database.getCollection(collection, BasicDBObject.class).deleteOne(document);
    }

    public DeleteResult deleteOne(String collection, Map<String, Object> map) {
        BasicDBObject document = new BasicDBObject();
        for (String key : map.keySet()) {
            document.append(key, map.get(key));
        }
        return database.getCollection(collection, BasicDBObject.class).deleteOne(document);
    }

    /*
     **从collection中删除匹配参数的所有document
     */
    public DeleteResult deleteAll(String collection, String key, Object value) {
        BasicDBObject document = new BasicDBObject();
        document.append(key, value);
        return database.getCollection(collection, BasicDBObject.class).deleteMany(document);
    }

    public DeleteResult deleteAll(String collection, Map<String, Object> map) {
        BasicDBObject document = new BasicDBObject();
        for (String key : map.keySet()) {
            document.append(key, map.get(key));
        }
        return database.getCollection(collection, BasicDBObject.class).deleteMany(document);
    }

    public void close() {
        mongoClient.close();
        Log.info("MongoDB client closed.");
    }

    //    public boolean setCurrentCollection(String collName) throws NullPointerException {
//        try {
//            collection = database.getCollection(collName, BasicDBObject.class);
//            return true;
//        } catch (NullPointerException e) {
//            Log.error("Database may not be initialed." + e);
//            return false;
//        }
//    }

//    public MongoCollection<BasicDBObject> getCurrentCollection() {
//        return collection;
//    }
}