package dao;

import Log.Log;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import dao.Entity.IOTable;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.DiscriminatorFunction;
import dev.morphia.mapping.Mapper;
import dev.morphia.mapping.MapperOptions;
import dev.morphia.mapping.NamingStrategy;
import dev.morphia.mapping.codec.pojo.EntityModelBuilder;
import dev.morphia.query.FindOptions;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/*
 * MongoDBHelper
 *
 * 该类基于Morphia框架（2.3.6）
 * https://morphia.dev/morphia/2.3/configuration.html
 *
        <dependency>
            <groupId>org.mongodb</groupId>
            <artifactId>mongodb-driver-sync</artifactId>
            <version>4.10.2</version>
        </dependency>
        <dependency>
            <groupId>dev.morphia.morphia</groupId>
            <artifactId>morphia-core</artifactId>
            <version>2.3.6</version>
        </dependency>
 *
 */
public class MongoDBHelper {
    private final Datastore datastore;
    private final MongoDatabase database;
    private final MongoClient mongoClient;
    public MongoDBHelper(String host, int port, String dbName, String mapPackage) {
        mongoClient = MongoClients.create("mongodb://" + host + ":" + port);
        datastore = Morphia.createDatastore(mongoClient, dbName);
        datastore.getMapper().mapPackage(mapPackage);
        datastore.ensureIndexes();
        database = mongoClient.getDatabase(dbName);
    }
    public MongoDBHelper(String host, int port, String dbName) {
        this(host, port, dbName, "dao.Entity");
    }


    public <T> CompletableFuture<List<T>> saveEntityAsync(List<T> entityList, int batchSize, ExecutorService executor) {
        long entityListSize = entityList.size();
        long batchNum = entityListSize / batchSize;
        long lastBatchSize = entityListSize % batchSize;

        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < batchNum; i++) {
            List<T> batch = new ArrayList<>(batchSize);
            for (int j = 0; j < batchSize; j++) {
                batch.add(entityList.get((int) (i * batchSize + j)));
            }
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                datastore.save(batch);
            }, executor);
            futures.add(future);
        }

        if (lastBatchSize != 0) {
            List<T> lastBatch = new ArrayList<>((int) lastBatchSize);
            for (int i = (int) (entityListSize - lastBatchSize); i < entityListSize; i++) {
                lastBatch.add(entityList.get(i));
            }
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                datastore.save(lastBatch);
            }, executor);
            futures.add(future);
        }

        // 等待所有任务完成后返回结果
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return allOf.thenApply(result -> {
            Log.trace("Tasks have completed.");
            return entityList;
        });
    }




    public Datastore getdatastore() {
        return datastore;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    // 保存实体对象, 如果已存在, 则更新
    public <T> T saveEntity(T entity) {
        datastore.save(entity);
        return entity;
    }

    public <T> List<T> saveEntity(List<T> entityList) {
        datastore.save(entityList);
        return entityList;
    }

    public <T> List<T> saveEntity(List<T> entityList, int batchSize) {
        long entityListSize = entityList.size();
        List<T> batch = new ArrayList<>(batchSize);
        for (T entity : entityList) {
            batch.add(entity);
            if (batch.size() == batchSize) {
                datastore.save(batch);
                batch.clear();
            }
        }
        if (!batch.isEmpty()) {
            datastore.save(batch);
            batch.clear();
        }

        return entityList;
    }

    public <T> List<T> saveEntity(List<T> entityList, int batchSize, int threadSize) {
        long entityListSize = entityList.size();
        long batchNum = entityListSize / batchSize;
        long lastBatchSize = entityListSize % batchSize;

        ExecutorService executor = Executors.newFixedThreadPool(threadSize);

        for (int i = 0; i < batchNum; i++) {
            List<T> batch = new ArrayList<>(batchSize);
            for (int j = 0; j < batchSize; j++) {
                batch.add(entityList.get((int) (i * batchSize + j)));
            }
            executor.execute(() -> {
                datastore.save(batch);
            });
        }

        if (lastBatchSize != 0) {
            List<T> lastBatch = new ArrayList<>((int) lastBatchSize);
            for (int i = (int) (entityListSize - lastBatchSize); i < entityListSize; i++) {
                lastBatch.add(entityList.get(i));
            }
            executor.execute(() -> {
                datastore.save(lastBatch);
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            return entityList;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 删除实体对象
    public <T> DeleteResult deleteEntity(T entity) {
        return datastore.delete(entity);
    }

    public <T> List<DeleteResult> deleteEntity(List<T> entityList) {
        List<DeleteResult> deleteResultList = new ArrayList<>();
        for (T entity : entityList) {
            deleteResultList.add(datastore.delete(entity));
        }
        return deleteResultList;
    }

    /*
     * 查找实体对象
     * @param clazz 实体类的类对象
     * @param findOptions 查询条件
     * @return List<T> 实体对象列表
     *
     * batchSize 方法来设置每个批次返回的文档数量。
     * 示例：new FindOptions().batchSize(100); 将每次返回100个文档。
     *
     * limit 方法来设置查询的限制数量。
     * 示例：new FindOptions().limit(10); 将只返回10个文档。
     *
     * skip 方法来设置查询的跳过数量。
     * 示例：new FindOptions().skip(10); 将跳过前10个文档。
     *
     * maxAwaitTime 方法来设置查询的最大等待时间。
     * 示例：new FindOptions().maxAwaitTime(5, TimeUnit.SECONDS); 将在等待5秒后停止查询。
     *
     * maxTime 方法来设置查询的最大执行时间。
     * 示例：new FindOptions().maxTime(10, TimeUnit.SECONDS); 将在执行10秒后停止查询。
     *
     * min和max 方法来设置查询的索引范围。
     * 示例：new FindOptions().min(new Document(“age”, 18)).max(new Document(“age”, 30)); 将只匹配年龄在18到30之间的文档。
     *
     * sort 方法来设置查询的排序规则。这可以影响查询返回的文档顺序。
     * 示例：new FindOptions().sort(new Document(“name”, 1)); 将按照name字段的升序排序。
     *
     * projection 方法来设置查询的投影规则。这可以影响查询返回的文档内容。
     * 示例：new FindOptions().projection().include(“name”).exclude(“age”); 将只返回文档中的name字段，而不返回age字段。
     */

    // 查找
    public <T> List<T> findEntity(Class<T> clazz) {
        try (var find = datastore.find(clazz).iterator()) {
            return find.toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 分页查找
    public <T> List<T> findEntityByPage(Class<T> clazz, int page, int pageSize) {
        return findEntityByPage(clazz, page, pageSize, 0, null);
    }

    // 分页查找,按照默认“cT”排序（创建时间），注意没有此属性时会报错！
    public <T> List<T> findEntityByPage(Class<T> clazz, int page, int pageSize, int sort) {
        return findEntityByPage(clazz, page, pageSize, sort, "cT");
    }

    // 分页查找,按照指定fieldName排序
    public <T> List<T> findEntityByPage(Class<T> clazz, int page, int pageSize, int sort, String fieldName) {
        // sort: 1 升序, -1 降序
        if (sort != -1 && sort != 1) {
            return null;
        }
        try {
            FindOptions findOptions = new FindOptions().skip(page * pageSize).limit(pageSize);
            if (fieldName != null) {
                findOptions.sort(new Document(fieldName, sort));
            }

            try (var find = datastore.find(clazz).iterator(findOptions)) {
                return find.toList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> findEntity(Class<T> clazz, FindOptions findOptions) {
        try (var find = datastore.find(clazz).iterator(findOptions)) {
            return find.toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 分批次查找
    public <T> List<T> findEntityWithBatchSize(Class<T> clazz, int batchSize) {
        try (var find = datastore.find(clazz).iterator(new FindOptions().batchSize(batchSize))) {
            return find.toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 查找并排序
    public <T> List<T> findEntityWithSort(Class<T> clazz, String fieldName, int sort) {
        // sort: 1 升序, -1 降序
        if (sort != -1 && sort != 1) {
            return null;
        }
        try (var find = datastore.find(clazz).iterator(new FindOptions().sort(new Document(fieldName, sort)))) {
            return find.toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 过滤查找
    public <T> List<T> findEntityWithFilters(Class<T> clazz, Map<String, Object> queryMap) {
        Document document = new Document();
        for (String key : queryMap.keySet()) {
            document.append(key, queryMap.get(key));
        }
        try (var find = datastore.find(clazz, document).iterator()) {
            return find.toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> findEntityWithFilters(Class<T> clazz, Map<String, Object> queryMap, FindOptions findOptions) {
        Document document = new Document();
        for (String key : queryMap.keySet()) {
            document.append(key, queryMap.get(key));
        }
        try (var find = datastore.find(clazz, document).iterator(findOptions)) {
            return find.toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 分批次过滤查找
    public <T> List<T> findEntityWithFiltersByBatchSize(Class<T> clazz, Map<String, Object> queryMap, int batchSize) {
        Document document = new Document();
        for (String key : queryMap.keySet()) {
            document.append(key, queryMap.get(key));
        }
        try (var find = datastore.find(clazz, document).iterator(new FindOptions().batchSize(batchSize))) {
            return find.toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 过滤查找并排序
    public <T> List<T> findEntityWithFiltersAndSort(Class<T> clazz, Map<String, Object> queryMap, String fieldName, int sort) {
        // sort: 1 升序, -1 降序
        if (sort != -1 && sort != 1) {
            return null;
        }
        Document document = new Document();
        for (String key : queryMap.keySet()) {
            document.append(key, queryMap.get(key));
        }
        try (var find = datastore.find(clazz, document).iterator(new FindOptions().sort(new Document(fieldName, sort)))) {
            return find.toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /****************************
     *以下为不引用Morphia框架的原生操作
     ****************************/

    /*
     *向collection中插入一个document
     */
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

    /*
     * 从collection中查找匹配参数的第一个document
     * @param key 字段名
     * @param value 字段值
     * @return BasicDBObject 匹配的document
     */
    public BasicDBObject findOne(String collection, String key, Object value) {
        BasicDBObject document = new BasicDBObject();
        document.append(key, value);
        return database.getCollection(collection, BasicDBObject.class).find(document).first();
    }

    /*
     * 从collection中查找匹配参数的第一个document
     * @param map 字段名和字段值的键值对
     * @return BasicDBObject 匹配的document
     */
    public BasicDBObject findOne(String collection, Map<String, Object> map) {
        BasicDBObject document = new BasicDBObject();
        for (String key : map.keySet()) {
            document.append(key, map.get(key));
        }
        return database.getCollection(collection, BasicDBObject.class).find(document).first();
    }

    /*
     * 从collection中查找匹配参数的所有document
     * @param key 字段名
     * @param value 字段值
     * @return List<BasicDBObject> 匹配的document列表
     */
    public List<BasicDBObject> findAll(String collection, String key, Object value) {
        BasicDBObject document = new BasicDBObject();
        document.append(key, value);
        return database.getCollection(collection, BasicDBObject.class).find(document).into(new java.util.ArrayList<>());
    }

    /*
     * 从collection中查找匹配参数的所有document
     * @param map 字段名和字段值的键值对
     * @return List<BasicDBObject> 匹配的document列表
     */
    public List<BasicDBObject> findAll(String collection, Map<String, Object> map) {
        BasicDBObject document = new BasicDBObject();
        for (String key : map.keySet()) {
            document.append(key, map.get(key));
        }
        return database.getCollection(collection, BasicDBObject.class).find(document).into(new java.util.ArrayList<>());
    }

    public void close() {
        mongoClient.close();
        Log.info("MongoDB client closed.");
    }
}
