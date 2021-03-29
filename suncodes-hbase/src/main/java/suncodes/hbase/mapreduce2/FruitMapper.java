package suncodes.hbase.mapreduce2;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

/**
 * ImmutableBytesWritable：rowKey
 * Result：一行数据对应的结果
 */
public class FruitMapper extends TableMapper<ImmutableBytesWritable, Put> {
    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
        Put put = new Put(key.get());
        List<Cell> cells = value.listCells();
        for (Cell cell : cells) {
            // 判断 当前列是否为 name 列
            if ("name".equals(Bytes.toString(CellUtil.cloneQualifier(cell)))) {
                // put 赋值
                put.add(cell);
            }
        }
        context.write(key, put);
    }
}
