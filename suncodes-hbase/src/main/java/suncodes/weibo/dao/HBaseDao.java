package suncodes.weibo.dao;

import org.apache.commons.collections.CollectionUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import suncodes.weibo.constants.Constants;
import suncodes.weibo.util.HBaseUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 1、发布微博
 * 2、删除微博
 * 3、关注用户
 * 4、取关用户
 * 5、获取用户微博详情
 * 6、获取用户的初始化页面
 */
public class HBaseDao {

    /**
     * 发布微博
     * @param uid 用户id
     * @param content 微博内容
     * @throws IOException
     */
    public static void publishWeiBo(String uid, String content) throws IOException {
        Connection connection = HBaseUtil.getConnection();
        // 微博内容表
        Table contentTable = connection.getTable(TableName.valueOf(Constants.CONTENT_TABLE));
        // rowKey
        long l = System.currentTimeMillis();
        String contentTableRowKey = uid + "_" + l;
        Put contentPut = new Put(Bytes.toBytes(contentTableRowKey));
        // 赋值
        contentPut.addColumn(Bytes.toBytes(Constants.CONTENT_TABLE_CF),
                Bytes.toBytes("content"), Bytes.toBytes(content));
        // 插入操作
        contentTable.put(contentPut);

        // 微博收件箱表
        // 1、获取用户关系表对象
        Table relationTable = connection.getTable(TableName.valueOf(Constants.RELATION_TABLE));
        // 2、获取当前发布微博人的 fans
        Get get = new Get(Bytes.toBytes(uid));
        get.addFamily(Bytes.toBytes(Constants.RELATION_TABLE_CF2));
        Result result = relationTable.get(get);
        // 3、创建一个集合，用于存放 微博收件箱表的 put 对象
        ArrayList<Put> inboxList = new ArrayList<>();
        List<Cell> cells = result.listCells();
        if (CollectionUtils.isNotEmpty(cells)) {
            for (Cell cell : cells) {
                Put put = new Put(CellUtil.cloneQualifier(cell));
                // 列为 发布微博人的 uid，value为对应的微博内容表的rowkey
                put.addColumn(Bytes.toBytes(Constants.INBOX_TABLE_CF),
                        Bytes.toBytes(uid), Bytes.toBytes(contentTableRowKey));
                inboxList.add(put);
            }
        }
        if (CollectionUtils.isNotEmpty(inboxList)) {
            Table inboxTable = connection.getTable(TableName.valueOf(Constants.INBOX_TABLE));
            inboxTable.put(inboxList);
            inboxTable.close();
        }
        relationTable.close();
        contentTable.close();
    }

    /**
     * 关注用户
     * @param uid
     * @param attends
     * @throws IOException
     */
    public static void addAttends(String uid, String... attends) throws IOException {
        if (attends.length <= 0) {
            return;
        }
        Connection connection = HBaseUtil.getConnection();
        Table relationTable = connection.getTable(TableName.valueOf(Constants.RELATION_TABLE));
        List<Put> relList = new ArrayList<>();
        Put put = new Put(Bytes.toBytes(uid));
        for (String attend : attends) {
            // 插入 关注列族
            put.addColumn(Bytes.toBytes(Constants.RELATION_TABLE_CF1),
                    Bytes.toBytes(attend), Bytes.toBytes(attend));
            // 粉丝列族
            Put put1 = new Put(Bytes.toBytes(attend));
            put1.addColumn(Bytes.toBytes(Constants.RELATION_TABLE_CF2),
                    Bytes.toBytes(uid), Bytes.toBytes(uid));
            relList.add(put1);
        }
        relList.add(put);
        relationTable.put(relList);

        Table inboxTable = connection.getTable(TableName.valueOf(Constants.INBOX_TABLE));
        List<Put> inboxPutList = new ArrayList<>();

        // 微博内容表
        // 取出自己关注的人的微博，把 id 放到 value中
        // 通过时间戳，控制版本
        Table contentTable = connection.getTable(TableName.valueOf(Constants.CONTENT_TABLE));
        for (String attend : attends) {
            Scan scan = new Scan();
            RowFilter rowFilter = new RowFilter(CompareOperator.EQUAL, new SubstringComparator(attend + "_"));
            scan.setFilter(rowFilter);
            ResultScanner scanner = contentTable.getScanner(scan);
            List<String> rowKeyList = new ArrayList<>();
            for (Result result : scanner) {
                rowKeyList.add(Bytes.toString(result.getRow()));
            }
            if (rowKeyList.size() > 3) {
                rowKeyList = rowKeyList.subList(rowKeyList.size() - 3, rowKeyList.size());
            }
            long l = System.currentTimeMillis();
            for (String s : rowKeyList) {
                Put inboxPut = new Put(Bytes.toBytes(uid));
                // TODO 此处必须加上时间戳，不然通过 list 插入数据的时候，数据会被覆盖，则最终只剩一条数据
                // 或者 一条一条的 put 数据 inboxTable.put(inboxPut);
                inboxPut.addColumn(Bytes.toBytes(Constants.INBOX_TABLE_CF),
                        Bytes.toBytes(attend), l++, Bytes.toBytes(s));
                inboxPutList.add(inboxPut);
            }
        }
        inboxTable.put(inboxPutList);
        contentTable.close();
        inboxTable.close();
        relationTable.close();
    }

    public static void deleteAttends(String uid, String... attends) throws IOException {
        if (attends.length <= 0) {
            return;
        }
        Connection connection = HBaseUtil.getConnection();
        Table relationTable = connection.getTable(TableName.valueOf(Constants.RELATION_TABLE));
        List<Delete> relList = new ArrayList<>();
        Delete delete = new Delete(Bytes.toBytes(uid));
        for (String attend : attends) {
            delete.addColumns(Bytes.toBytes(Constants.RELATION_TABLE_CF1),
                    Bytes.toBytes(attend));

            Delete delete1 = new Delete(Bytes.toBytes(attend));
            delete1.addColumns(Bytes.toBytes(Constants.RELATION_TABLE_CF2),
                    Bytes.toBytes(uid));
            relList.add(delete1);
        }
        relList.add(delete);
        relationTable.delete(relList);

        Table inboxTable = connection.getTable(TableName.valueOf(Constants.INBOX_TABLE));
        Delete inboxDelete = new Delete(Bytes.toBytes(uid));

        for (String attend : attends) {
            // 删除多个版本
            inboxDelete.addColumns(Bytes.toBytes(Constants.INBOX_TABLE_CF), Bytes.toBytes(attend));
        }
        inboxTable.delete(inboxDelete);
        inboxTable.close();
        relationTable.close();
    }

    public static void getInitPageData(String uid) throws IOException {
        Connection connection = HBaseUtil.getConnection();
        Table inboxTable = connection.getTable(TableName.valueOf(Constants.INBOX_TABLE));
        // 微博内容表
        Table contentTable = connection.getTable(TableName.valueOf(Constants.CONTENT_TABLE));
        Get get = new Get(Bytes.toBytes(uid));
        get.readAllVersions();
        Result result = inboxTable.get(get);
        List<Cell> cells = result.listCells();

        List<String> contentList = new ArrayList<>();
        for (Cell cell : cells) {
            Result result1 = contentTable.get(new Get(CellUtil.cloneValue(cell)));
            for (Cell listCell : result1.listCells()) {
                if (CellUtil.matchingQualifier(listCell, Bytes.toBytes("content"))) {
                    byte[] bytes = CellUtil.cloneValue(listCell);
                    contentList.add(Bytes.toString(bytes));
                }
            }
        }
        for (String content : contentList) {
            System.out.println(content);
        }
        contentTable.close();
        inboxTable.close();
    }

    public static void getAllWeiBoByUid(String uid) throws IOException {
        Connection connection = HBaseUtil.getConnection();
        // 微博内容表
        Table contentTable = connection.getTable(TableName.valueOf(Constants.CONTENT_TABLE));
        Scan scan = new Scan();
        RowFilter rowFilter = new RowFilter(CompareOperator.EQUAL, new SubstringComparator(uid + "_"));
        scan.setFilter(rowFilter);
        ResultScanner scanner = contentTable.getScanner(scan);
        for (Result result : scanner) {
            for (Cell listCell : result.listCells()) {
                System.out.println(Bytes.toString(CellUtil.cloneValue(listCell)));
            }
        }
        contentTable.close();
    }
}
