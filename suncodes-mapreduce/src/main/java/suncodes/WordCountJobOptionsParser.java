package suncodes;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.math3.util.MathUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.filecache.DistributedCache;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//import org.apache.hadoop.mapreduce.filecache.DistributedCache;

public class WordCountJobOptionsParser {

    private static Logger logger = LoggerFactory.getLogger(WordCountJobOptionsParser.class);
    /**
     * KEYIN：输入kv数据对中key的数据类型
     * VALUEIN：输入kv数据对中value的数据类型
     * KEYOUT：输出kv数据对中key的数据类型
     * VALUEOUT：输出kv数据对中value的数据类型
     */
    public static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        /**
         * map方法是提供给map task进程来调用的，map task进程是每读取一行文本来调用一次我们自定义的map方法
         * map task在调用map方法时，传递的参数：
         * 一行的起始偏移量LongWritable作为key
         * 一行的文本内容Text作为value
         */
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            logger.info("进入map方法了");
            //拿到一行文本内容，转换成String 类型
            String line = value.toString();
            //将这行文本切分成单词
            String[] words = line.split(" ");

            //输出<单词，1>
            for (String word : words) {
                context.write(new Text(word), new IntWritable(1));
            }
            System.out.println("map方法已经结束了");
        }
    }


    /**
     * KEYIN：对应mapper阶段输出的key类型
     * VALUEIN：对应mapper阶段输出的value类型
     * KEYOUT：reduce处理完之后输出的结果kv对中key的类型
     * VALUEOUT：reduce处理完之后输出的结果kv对中value的类型
     */
    public static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

        /**
         * reduce方法提供给reduce task进程来调用
         * <p>
         * reduce task会将shuffle阶段分发过来的大量kv数据对进行聚合，聚合的机制是相同key的kv对聚合为一组
         * 然后reduce task对每一组聚合kv调用一次我们自定义的reduce方法
         * 比如：<hello,1><hello,1><hello,1><tom,1><tom,1><tom,1>
         * hello组会调用一次reduce方法进行处理，tom组也会调用一次reduce方法进行处理
         * 调用时传递的参数：
         * key：一组kv中的key
         * values：一组kv中所有value的迭代器
         */
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            logger.info("进入reduce方法了");
            //定义一个计数器
            int count = 0;
            //通过value这个迭代器，遍历这一组kv中所有的value，进行累加
            for (IntWritable value : values) {
                count += value.get();
            }

            //输出这个单词的统计结果
            context.write(key, new IntWritable(count));
            System.out.println("reduce方法已经结束了");
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        logger.info(">>>>>>>>>>>>");

        Configuration conf = new Configuration();
        System.setProperty("HADOOP_USER_NAME", "root");

        FileSystem fileSystem = FileSystem.get(conf);
        Path output = new Path("hdfs://192.168.6.110:9000/user/scz/mapreduce/output/");
        if (fileSystem.exists(output)) {
            fileSystem.delete(output, true);
        }

        Job wordCountJob = Job.getInstance(conf);

        //此时的输入数据路径为args[1] ，原因在使用方式中解释
//        String input = args[1];
        //解析命令行参数
        CommandLine commandLine = new GenericOptionsParser(conf, args).getCommandLine();
        String[] tmpArgs = commandLine.getArgs();
        for (String tmpArg : tmpArgs) {
            System.out.println(tmpArg);
        }
        System.out.println(MathUtils.equals(1.0, 2.0));
        //重要：指定本job所在的jar包
        wordCountJob.setJarByClass(WordCountJobOptionsParser.class);

        //设置wordCountJob所用的mapper逻辑类为哪个类
        wordCountJob.setMapperClass(WordCountMapper.class);
        //设置wordCountJob所用的reducer逻辑类为哪个类
        wordCountJob.setReducerClass(WordCountReducer.class);

        //设置map阶段输出的kv数据类型
        wordCountJob.setMapOutputKeyClass(Text.class);
        wordCountJob.setMapOutputValueClass(IntWritable.class);

        //设置最终输出的kv数据类型
        wordCountJob.setOutputKeyClass(Text.class);
        wordCountJob.setOutputValueClass(IntWritable.class);

        //设置要处理的文本数据所存放的路径
        FileInputFormat.setInputPaths(wordCountJob,
                "hdfs://192.168.6.110:9000/user/scz/mapreduce/input/");
        FileOutputFormat.setOutputPath(wordCountJob, output);

        //提交job给hadoop集群
        wordCountJob.waitForCompletion(true);
    }
}