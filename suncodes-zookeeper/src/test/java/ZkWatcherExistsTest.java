import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import suncodes.zookeeper.ZKWatcherExists;

import java.io.*;

/**
 * exists(String path, boolean b)
 * exists(String path, Watcher w)
 * NodeCreated：节点创建
 * NodeDeleted：节点删除
 * NodeDataChanged：节点内容
 */
public class ZkWatcherExistsTest {

    private ZooKeeper zooKeeper;

    @Before
    public void f() throws IOException, InterruptedException {
        zooKeeper = new ZKWatcherExists().getZK();
    }

    @After
    public void f1() throws InterruptedException {
        zooKeeper.close();
    }

    @Test
    public void f2() throws KeeperException, InterruptedException {
        // 运行后，手动在linux下创建节点，查看监听变化
        new ZKWatcherExists().watcherExists1(zooKeeper);
    }

    @Test
    public void f3() throws KeeperException, InterruptedException {
        new ZKWatcherExists().watcherExists2(zooKeeper);
    }

    @Test
    public void f4() throws KeeperException, InterruptedException {
        new ZKWatcherExists().watcherExists3(zooKeeper);
    }

    @Test
    public void f5() throws KeeperException, InterruptedException {
        new ZKWatcherExists().watcherExists4(zooKeeper);
    }
}
