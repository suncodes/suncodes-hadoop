import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class HDFSNoClassPathXmlUtil {
    private static FileSystem fileSystem;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.addResource("file:///F:\\project\\suncodes-hadoop\\suncodes-hdfs\\conf1\\core-site.xml");
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
