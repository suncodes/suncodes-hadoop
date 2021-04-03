package suncodes.weibo.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 1、创建命名空间
 * 2、判断表是否存在
 * 3、创建表
 */
public class HBaseUtil {

    private static Connection connection = null;

    static {
        // 创建配置
        Configuration configuration = HBaseConfiguration.create();
        // HBase地址
        configuration.set("hbase.zookeeper.quorum", "192.168.6.110");
        // 端口，默认为2181，如果和默认相同，可省略
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        // 创建连接
        try {
            connection = ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void createNamespace(String namespace) throws IOException {
        Admin admin = connection.getAdmin();
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(namespace).build();
        admin.createNamespace(namespaceDescriptor);
        admin.close();
    }

    public static boolean isExistsTable(String tableName) throws IOException {
        // 创建管理器
        Admin hBaseAdmin = connection.getAdmin();
        // 表是否存在
        boolean tableExists = hBaseAdmin.tableExists(TableName.valueOf(tableName));
        // 退出之前 关闭管理器
        hBaseAdmin.close();
        return tableExists;
    }

    public static void createTable(String tableName, int versions, String... cfs) throws IOException {
        // 创建管理器
        Admin hBaseAdmin = connection.getAdmin();
        // 指定列族
        List<ColumnFamilyDescriptor> list = new ArrayList<>();
        for (String cf : cfs) {
            ColumnFamilyDescriptor familyDescriptor = ColumnFamilyDescriptorBuilder
                    .newBuilder(Bytes.toBytes(cf))
                    .setMaxVersions(versions)
                    .build();
            list.add(familyDescriptor);
        }
        TableDescriptor tableDescriptor = TableDescriptorBuilder
                // 指定表名
                .newBuilder(TableName.valueOf(tableName))
                .setColumnFamilies(list)
                .build();
        hBaseAdmin.createTable(tableDescriptor);
        hBaseAdmin.close();
    }

    public static void truncate(String tableName) throws IOException {
        // 创建管理器
        Admin hBaseAdmin = connection.getAdmin();
        hBaseAdmin.disableTable(TableName.valueOf(tableName));
        hBaseAdmin.truncateTable(TableName.valueOf(tableName), true);
        hBaseAdmin.close();
    }
}
