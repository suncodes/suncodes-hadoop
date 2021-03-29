package suncodes.hbase.mapreduce2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class FruitDriver implements Tool {

    private Configuration configuration = null;

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        // HBase地址
        configuration.set("hbase.zookeeper.quorum", "192.168.6.110");
        // 端口，默认为2181，如果和默认相同，可省略
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
        ToolRunner.run(configuration, new FruitDriver(), args);
    }
    @Override
    public int run(String[] strings) throws Exception {
        Job job = Job.getInstance(configuration);
        job.setJarByClass(FruitDriver.class);
        job.setMapperClass(FruitMapper.class);
        job.setReducerClass(FruitReducer.class);

        // 输入表
        TableMapReduceUtil.initTableMapperJob("Fruit", new Scan(),
                FruitMapper.class, ImmutableBytesWritable.class, Put.class, job);
        // 输出表
        TableMapReduceUtil.initTableReducerJob("Fruit2", FruitReducer.class, job);
        return 0;
    }

    @Override
    public void setConf(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Configuration getConf() {
        return configuration;
    }
}
