import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Properties;

public class HDFSPropertiesUtil {
    private static FileSystem fileSystem;

    static {
        try {
            Configuration configuration = new Configuration();
            Properties properties = new Properties();
            properties.load(HDFSPropertiesUtil.class.getClassLoader().getResourceAsStream("hdfs.properties"));
            for (Map.Entry<Object, Object> objectObjectEntry : properties.entrySet()) {
                configuration.set(objectObjectEntry.getKey().toString(), objectObjectEntry.getValue().toString());
            }
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
