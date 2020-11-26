import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;
import suncodes.WordCountJobSubmitter;

import java.io.IOException;

public class MapTest {
    @Test
    public void f() throws IOException {
        Text value = new Text("hi hadoop hadoop");
        new MapDriver<LongWritable, Text, Text, IntWritable>()
                // 自己需要测试的map类
                .withMapper(new WordCountJobSubmitter.WordCountMapper())
                // 输入
                .withInput(new LongWritable(0), value)
                // 输出
                .withOutput(new Text("hi"), new IntWritable(1))
                .withOutput(new Text("hadoop"), new IntWritable(2))
                .runTest();
    }

}
