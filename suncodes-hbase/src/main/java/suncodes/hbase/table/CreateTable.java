package suncodes.hbase.table;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * firewall-cmd --zone=public --add-port=16000/tcp --permanent
 * firewall-cmd --reload
 */
public class CreateTable {
    public static void main(String[] args) throws IOException {
        // 创建配置
        Configuration configuration = HBaseConfiguration.create();
        // HBase地址
        configuration.set("hbase.zookeeper.quorum", "192.168.6.110");
        // 端口，默认为2181，如果和默认相同，可省略
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        // 创建连接
        Connection connection = ConnectionFactory.createConnection(configuration);
        // 创建管理器
        Admin hBaseAdmin = connection.getAdmin();
        // 指定列族
        List<ColumnFamilyDescriptor> list = new ArrayList<>();
        list.add(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("F")).build());
        TableDescriptor tableDescriptor = TableDescriptorBuilder
                // 指定表名
                .newBuilder(TableName.valueOf("JavaHBase"))
                .setColumnFamilies(list)
                .build();
        hBaseAdmin.createTable(tableDescriptor);
        hBaseAdmin.close();
    }
}
