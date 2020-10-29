package suncodes;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WordCountJobSubmitter {

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
            //拿到一行文本内容，转换成String 类型
            String line = value.toString();
            //将这行文本切分成单词
            String[] words = line.split(" ");

            //输出<单词，1>
            for (String word : words) {
                context.write(new Text(word), new IntWritable(1));
            }
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
            //定义一个计数器
            int count = 0;
            //通过value这个迭代器，遍历这一组kv中所有的value，进行累加
            for (IntWritable value : values) {
                count += value.get();
            }

            //输出这个单词的统计结果
            context.write(key, new IntWritable(count));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job wordCountJob = Job.getInstance(conf);

        //重要：指定本job所在的jar包
        wordCountJob.setJarByClass(WordCountJobSubmitter.class);

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
        FileInputFormat.setInputPaths(wordCountJob, "hdfs://192.168.77.70:9000/wordcount/srcdata/");
        FileOutputFormat.setOutputPath(wordCountJob, new Path("hdfs://192.168.77.70:9000/wordcount/output/"));

        //提交job给hadoop集群
        wordCountJob.waitForCompletion(true);
    }
}