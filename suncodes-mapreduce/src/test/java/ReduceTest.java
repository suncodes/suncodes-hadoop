import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;
import suncodes.WordCountJobSubmitter;

import java.io.IOException;
import java.util.Arrays;

public class ReduceTest {
    @Test
    public void f() throws IOException {
        new ReduceDriver<Text, IntWritable, Text, IntWritable>()
                .withReducer(new WordCountJobSubmitter.WordCountReducer())
                .withInput(new Text("hadoop"),
                        Arrays.asList(new IntWritable(1),
                                new IntWritable(1)))
                .withInput(new Text("hi"), Arrays.asList(new IntWritable(1)))
                .withOutput(new Text("hadoop"), new IntWritable(2))
                .withOutput(new Text("hi"), new IntWritable(1))
                .runTest();
    }
}
