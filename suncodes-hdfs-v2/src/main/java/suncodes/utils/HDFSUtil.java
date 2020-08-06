package suncodes.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 需要的功能：
 * 1、获取某个路径下的文件列表
 * 2、读取某个文件
 * 3、创建目录
 * 4、删除目录
 * 5、创建空文件
 * 6、上传文件
 * 7、如果文件不存在，创建并写入
 * 8、追加文件
 * 9、覆盖文件
 * 10、下载文件
 */
public class HDFSUtil {

    private static FileSystem FILE_SYSTEM;

    private static Properties getProperties() {
        Properties properties = new Properties();
        InputStream resourceAsStream = HDFSUtil.class.getClassLoader()
                .getResourceAsStream("hdfs.properties");
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return properties;
    }

    static {
        Configuration configuration = new Configuration();
        Properties properties = getProperties();
        if (properties != null) {
            for (Map.Entry<Object, Object> objectObjectEntry : properties.entrySet()) {
                configuration.set(objectObjectEntry.getKey().toString(),
                        objectObjectEntry.getValue().toString());
            }
        }
        //通过这种方式设置java客户端身份
        System.setProperty("HADOOP_USER_NAME", "root");
        try {
            FILE_SYSTEM = FileSystem.get(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 感觉HDFS没有必要写工具类，因为方法全都有，在FileSystem中
     * @return
     */
    public static FileSystem getFileSystem() {
        return FILE_SYSTEM;
    }

    public static FileStatus getFileStatus(String hdfsPath) throws IOException {
        if (StringUtils.isBlank(hdfsPath)) {
            throw new RuntimeException("文件路径不合法");
        }
        List<String> ls = new ArrayList<>();
        return FILE_SYSTEM.getFileStatus(new Path(hdfsPath));
    }

    public static List<String> getFileOrDirList(String hdfsPath) throws IOException {
        if (StringUtils.isBlank(hdfsPath)) {
            throw new RuntimeException("文件路径不合法");
        }
        List<String> ls = new ArrayList<>();
        FileStatus[] fileStatuses = FILE_SYSTEM.listStatus(new Path(hdfsPath));
        for (FileStatus fileStatus : fileStatuses) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("组：").append(fileStatus.getGroup()).append("---");
            stringBuilder.append("owner：").append(fileStatus.getOwner()).append("---");
            if (fileStatus.isDirectory()) {
                stringBuilder.append("目录：");
            }
            if (fileStatus.isFile()) {
                stringBuilder.append("文件");
            }
            stringBuilder.append(fileStatus.getPath());
            ls.add(stringBuilder.toString());
        }
        return ls;
    }
}
