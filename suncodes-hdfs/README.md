# suncodes-hdfs

## hadoop环境：

NameNode：192.168.6.110

DataNode：192.168.6.111

DataNode：192.168.6.112

hadoop 版本 3.2.1

hadoop安装位置：

/usr/local/hadoop/hadoop-3.2.1



## hadoop Java API：

- 注意：连接的端口是9000

### 1、简单demo

```XML
POM:
<properties>
    <hadoop.version>3.2.1</hadoop.version>
</properties>

<dependencies>
    <!-- https://mvnrepository.com/artifact/org.apache.hadoop/hadoop-client -->
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-client</artifactId>
        <version>${hadoop.version}</version>
    </dependency>
</dependencies>
```

```JAVA
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class HDFSVarUtil {
    private static final String HDFS_PATH = "hdfs://192.168.6.110:9000";
    private static final String HDFS_USER = "root";
    private static FileSystem fileSystem;

    static {
        try {
            Configuration configuration = new Configuration();
            fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, HDFS_USER);
        } catch (IOException | InterruptedException | URISyntaxException e) {
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
```



### 2、有关连接hadoop的配置方式（不止5种）
- 注意：工程里面其实已经重复了

（1）变量方式

核心代码：
```JAVA
fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, HDFS_USER);
```
```JAVA
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class HDFSVarUtil {
    private static final String HDFS_PATH = "hdfs://192.168.6.110:9000";
    private static final String HDFS_USER = "root";
    private static FileSystem fileSystem;

    static {
        try {
            Configuration configuration = new Configuration();
            fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, HDFS_USER);
        } catch (IOException | InterruptedException | URISyntaxException e) {
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
```

（2）参数方式

核心代码：

```JAVA
Configuration configuration = new Configuration();
configuration.set("fs.default.name", HDFS_PATH);
//通过这种方式设置java客户端身份
System.setProperty("HADOOP_USER_NAME", "root");
```

```JAVA
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
```

（3）properties配置文件方式

核心代码；

hdfs.properties
```XML
fs.default.name=hdfs://192.168.6.110:9000
```

```JAVA
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
```

（4）classpath目录下，xml配置

方式1：resources目录下建立文件：core-site.xml

方式2：工程根目录下，在conf目录下，建立文件：core-site.xml

两种方式都会把配置文件，放到classpath中

```JAVA
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class HDFSClassPathXmlUtil {
    private static FileSystem fileSystem;

    static {
        try {
            Configuration configuration = new Configuration();
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
```

（5）非classpath目录下，xml配置

工程根目录，在conf1目录下，建立文件：core-site.xml

核心代码：
```
configuration.addResource("file:///F:\\project\\suncodes-hadoop\\suncodes-hdfs\\conf1\\core-site.xml");
```
另外一种方式：使用maven的build，resources功能，把xml文件加载到classpath中。

```JAVA
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
```



3、java怎样加载hadoop配置文件

在Hadoop源码中，通过搜索classpath路径，来直接找到和读取配置的xml文件，使得你的配置在启动时生效。

在Hadoop源码Configuration类中，先后加载了core-default.xml 和 core-site.xml
类HdfsConfiguration继承了Configuration类，因此，hdfs-site.xml中的配置如果在core-site.xml 中配置了，那么将覆盖掉这个配置。
同理，yarn-site.xml也是这样的。

HDFS：hadoop-env.sh --> core-default.xml --> core-site.xml --> hdfs-default.xml --> hdfs-site.xml

Mapred：hadoop-env.sh --> core-default.xml --> core-site.xml --> mapred.default.xml --> mapred.site.xml


