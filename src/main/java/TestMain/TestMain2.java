package TestMain;

import dao.Entity.IOTable;
import dao.MongoDBHelper;
import Log.Log;

import java.util.Scanner;

public class TestMain2 {
    public static void main(String[] args) {

        MongoDBHelper db = new MongoDBHelper("localhost", 27017, "demo","dao.Entity");
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("请输入页码和页大小");
            System.out.print("页码：");
            int page = scanner.nextInt();
            System.out.print("页大小：");
            int pagesize = scanner.nextInt();
            var result = db.findEntityByPage(IOTable.class,page,pagesize,-1);
            //var result = db.findEntity(IOTable.class);
            //打印
            for(var obj : result){
                Log.debug("cT：" + obj.getCreateTime());
                Log.debug("tC：" + obj.getTagCode() + " tV：" + obj.getTagValue() + " b1：" + obj.getStringBak());
            }
            if (result.isEmpty()){
                Log.debug("没有数据");
            }
        }
    }
}
