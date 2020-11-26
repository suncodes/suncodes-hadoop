import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.junit.Test;
import suncodes.WordCountJobSubmitter;

import java.io.IOException;

public class MapReduceTest {
    @Test
    public void f() throws IOException {
        Text value = new Text("hi hadoop hadoop");
        new MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable>()
                .withMapper(new WordCountJobSubmitter.WordCountMapper())
                .withReducer(new WordCountJobSubmitter.WordCountReducer())
                .withInput(new LongWritable(0), value)
                .withOutput(new Text("hadoop"), new IntWritable(2))
                .withOutput(new Text("hi"), new IntWritable(1))
                .runTest();

    }
}
