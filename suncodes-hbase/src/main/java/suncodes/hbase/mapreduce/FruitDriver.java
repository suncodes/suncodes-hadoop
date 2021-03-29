package suncodes.hbase.mapreduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
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
        int run = ToolRunner.run(configuration, new FruitDriver(), args);
        System.exit(run);
    }

    @Override
    public int run(String[] strings) throws Exception {
        // 1、获取 Job 对象
        Job job = Job.getInstance(configuration);
        // 2、设置驱动类路径
        job.setJarByClass(FruitDriver.class);
        // 3、设置 Mapper 和 Reducer KV 类型
        job.setMapperClass(FruitMapper.class);
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(FruitReducer.class);
        TableMapReduceUtil.initTableReducerJob("Fruit", FruitReducer.class, job);
        FileInputFormat.setInputPaths(job,
                "hdfs://192.168.6.110:9000/user/scz/mapreduce/input/");
        boolean completion = job.waitForCompletion(true);
        return completion ? 0 : 1;
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
