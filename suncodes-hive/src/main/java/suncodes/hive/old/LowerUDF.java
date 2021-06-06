package suncodes.hive.old;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * A User-defined function (UDF) for use with Hive.
 * <p>
 * New UDF classes need to inherit from this UDF class (or from {@link
 * org.apache.hadoop.hive.ql.udf.generic.GenericUDF GenericUDF} which provides more flexibility at
 * the cost of more complexity).
 */
public final class LowerUDF extends UDF {
    public Text evaluate(final Text s) {
        if (s == null) { return null; }
        return new Text(s.toString().toLowerCase());
    }
}