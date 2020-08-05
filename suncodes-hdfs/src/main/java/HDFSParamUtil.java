import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HDFSParamUtil {
    private static final String HDFS_PATH = "hdfs://192.168.6.110:9000";
    private static final String HDFS_USER = "root";
    private static FileSystem fileSystem;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.set("fs.default.name", HDFS_PATH);
            //通过这种方式设置java客户端身份
            System.setProperty("HADOOP_USER_NAME", "root");
            // FIXME
//            configuration.set("fs.default.username", HDFS_USER);
            fileSystem = FileSystem.get(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询给定路径中文件/目录的状态
     *
     * @param path 目录路径
     * @return 文件信息的数组
     */
    public FileStatus[] listFiles(String path) throws Exception {
        return fileSystem.listStatus(new Path(path));
    }
}
