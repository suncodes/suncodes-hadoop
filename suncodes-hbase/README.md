### DDL（表相关操作）

#### 1、判断表是否存在

```java

package suncodes.hbase.table;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;

/**
 * 判断表是否存在
 */
public class ExistsTable {
    public static void main(String[] args) throws IOException {
        System.out.println(isExistsTable("Student"));
    }

    public static boolean isExistsTable(String tableName) throws IOException {
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
        // 表是否存在
        boolean tableExists = hBaseAdmin.tableExists(TableName.valueOf(tableName));
        // 退出之前 关闭管理器
        hBaseAdmin.close();
        return tableExists;
    }
}

```

运行程序，报错：

> 2021-03-27 23:21:40,994 INFO [org.apache.hadoop.hbase.client.RpcRetryingCallerImpl] - Call exception, tries=7, retries=16, started=49012 ms ago, cancelled=false, msg=Call to hadoop01/192.168.6.110:16023 failed on connection exception: org.apache.hbase.thirdparty.io.netty.channel.ConnectTimeoutException: connection timed out: hadoop01/192.168.6.110:16023, details=row 'Student' on table 'hbase:meta' at region=hbase:meta,,1.1588230740, hostname=hadoop01,16023,1616422959384, seqNum=-1, see https://s.apache.org/timeout

解决：

- 开启防火墙：
```shell script
# backup masters
firewall-cmd --zone=public --add-port=16002/tcp --permanent
# dead region servers(这个不应该开启)
firewall-cmd --zone=public --add-port=16020/tcp --permanent
# live region servers: 3
firewall-cmd --zone=public --add-port=16022/tcp --permanent
firewall-cmd --zone=public --add-port=16023/tcp --permanent
firewall-cmd --zone=public --add-port=16024/tcp --permanent
firewall-cmd --reload

```
`附`：查看表情况
```shell script
hbase hbck -details web_profiling_user
```

#### 2、创建表

```java
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

```

注：需要开启防火墙，16000端口

注：创建表时，可自动创建命名空间

#### 3、创建命名空间

```java
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

```

附：查询 HBase 命名空间
> hbase(main):005:0> list_namespace  
  NAMESPACE                                                                                                                                        
  default                                                                                                                                          
  hbase                                                                                                                                            
  suncodes                                                                                                                                         
  3 row(s)
  Took 0.0372 seconds

注：创建表时，可自动创建命名空间

#### 4、删除表

```java
package suncodes.hbase.table;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class DeleteTable {
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
        // 删除表之前必须禁用表
        hBaseAdmin.disableTable(TableName.valueOf("JavaHBase"));
        hBaseAdmin.deleteTable(TableName.valueOf("JavaHBase"));
        hBaseAdmin.close();
    }
}

```

### DML（数据相关操作）

对数据进行操作，则需要 connect.getTable

#### 1、插入数据

```java
package suncodes.hbase.data;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class PutData {
    public static void main(String[] args) throws IOException {
        // 创建配置
        Configuration configuration = HBaseConfiguration.create();
        // HBase地址
        configuration.set("hbase.zookeeper.quorum", "192.168.6.110");
        // 端口，默认为2181，如果和默认相同，可省略
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        // 创建连接
        Connection connection = ConnectionFactory.createConnection(configuration);
        // 获取表
        Table table = connection.getTable(TableName.valueOf("Student"));
        String rowKey = "0002";
        String family = "StuInfo";
        String column = "SSS";
        String value = "ka";

        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(family), Bytes.toBytes(column), Bytes.toBytes(value));
        table.put(put);
        table.close();
    }
}

```

#### 2、查数据（get）

```java
package suncodes.hbase.data;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

public class GetData {
    public static void main(String[] args) throws IOException {
        // 创建配置
        Configuration configuration = HBaseConfiguration.create();
        // HBase地址
        configuration.set("hbase.zookeeper.quorum", "192.168.6.110");
        // 端口，默认为2181，如果和默认相同，可省略
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        // 创建连接
        Connection connection = ConnectionFactory.createConnection(configuration);
        // 获取表
        Table table = connection.getTable(TableName.valueOf("Student"));
        String rowKey = "0002";
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes("StuInfo"), Bytes.toBytes("SSS"));
        Result result = table.get(get);
        List<Cell> cells = result.listCells();
        for (Cell cell : cells) {
            System.out.println("ROWKEY: " + Bytes.toString(CellUtil.cloneRow(cell)));
            System.out.println("FAMILY: " + Bytes.toString(CellUtil.cloneFamily(cell)));
            System.out.println("COLUMN: " + Bytes.toString(CellUtil.cloneQualifier(cell)));
            System.out.println("VALUE : " + Bytes.toString(CellUtil.cloneValue(cell)));
        }
    }
}

```

#### 3、查数据（scan）

```java
package suncodes.hbase.data;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

public class ScanData {
    public static void main(String[] args) throws IOException {
        // 创建配置
        Configuration configuration = HBaseConfiguration.create();
        // HBase地址
        configuration.set("hbase.zookeeper.quorum", "192.168.6.110");
        // 端口，默认为2181，如果和默认相同，可省略
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        // 创建连接
        Connection connection = ConnectionFactory.createConnection(configuration);
        // 获取表
        Table table = connection.getTable(TableName.valueOf("Student"));
        Scan scan = new Scan();
        // 获取所有版本数据
        scan.readAllVersions();
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            List<Cell> cells = result.listCells();
            for (Cell cell : cells) {
                System.out.println("=====================================");
                System.out.println("ROWKEY: " + Bytes.toString(CellUtil.cloneRow(cell)));
                System.out.println("FAMILY: " + Bytes.toString(CellUtil.cloneFamily(cell)));
                System.out.println("COLUMN: " + Bytes.toString(CellUtil.cloneQualifier(cell)));
                System.out.println("VALUE : " + Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }
    }
}

```

#### 4、删除数据

```java
package suncodes.hbase.data;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class DeleteData {
    public static void main(String[] args) throws IOException {
        // 创建配置
        Configuration configuration = HBaseConfiguration.create();
        // HBase地址
        configuration.set("hbase.zookeeper.quorum", "192.168.6.110");
        // 端口，默认为2181，如果和默认相同，可省略
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        // 创建连接
        Connection connection = ConnectionFactory.createConnection(configuration);
        // 获取表
        Table table = connection.getTable(TableName.valueOf("Student"));
        Delete delete = new Delete(Bytes.toBytes("0002"));
        delete.addColumn(Bytes.toBytes("StuInfo"), Bytes.toBytes("SSS"));
        table.delete(delete);
        table.close();
    }
}

```

### 微博涉及的表

```shell script
create_namespace 'weibo';
create 'weibo:content','info';
create 'weibo:relation','attends','fans';
create 'weibo:inbox',{COLUMN=>'info',VERSIONS=>3};
```