package dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.result.DeleteResult;
import dev.morphia.Datastore;

import dev.morphia.Morphia;
import dev.morphia.query.FindOptions;
import dev.morphia.query.Meta;
import dev.morphia.query.Sort;
import org.bson.Document;

import java.util.List;

public class MongoDBHelper {
    private final Datastore datastore;

    public MongoDBHelper(String host, int port, String dbName) {
        MongoClient mongoClient = MongoClients.create("mongodb://" + host + ":" + port);
        datastore = Morphia.createDatastore(mongoClient, dbName);
    }

    // 保存实体对象, 如果已存在, 则更新
    public <T> T saveEntity(T entity) {
        return datastore.save(entity);
    }

    // 删除实体对象
    public <T> DeleteResult deleteEntity(T entity) {
        return datastore.delete(entity);
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
    public <T> List<T> findEntity(Class<T> clazz) {
        try (var find = datastore.find(clazz).iterator()) {
            return find.toList();
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

    public <T> List<T> findEntityWithBatchSize(Class<T> clazz, int batchSize) {
        try (var find = datastore.find(clazz).iterator(new FindOptions().batchSize(batchSize))) {
            return find.toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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
}
