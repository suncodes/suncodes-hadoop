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
