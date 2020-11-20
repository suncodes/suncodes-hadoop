package suncodes;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * 这是一个自定义的类型，
 * 需要重写三个方法
 * 里面可以有一些自己的属性，像javabean一样
 *
 */
public class MyWritableComparable implements WritableComparable<MyWritableComparable> {

    /** 属性 **/
    private String name;
    private Integer age;

    @Override
    public int compareTo(MyWritableComparable o) {
        return age - o.age;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(name);
        dataOutput.writeInt(age);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        name = dataInput.readUTF();
        age = dataInput.readInt();
    }
}
