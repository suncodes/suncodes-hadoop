package suncodes.hbase.table;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;

public class CreateNamespace {
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
        // 创建命名空间
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create("suncodes").build();
        hBaseAdmin.createNamespace(namespaceDescriptor);
        hBaseAdmin.close();
    }
}
