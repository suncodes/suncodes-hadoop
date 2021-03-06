import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import suncodes.zookeeper.CreateNode;
import suncodes.zookeeper.ZKWatcherGetData;

import java.io.*;

/**
 * getData
 * getData(String path, boolean b, Stat stat)
 * getData(String path, Watcher w, Stat stat)
 * NodeDeleted：节点删除
 * NodeDataChange：节点内容发生变化
 */
public class ZKWatcherGetDataTest {
    private ZooKeeper zooKeeper;

    @Before
    public void f() throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZKWatcherGetData().getZK();
        if (zooKeeper.exists("/watcher2", false) == null) {
            zooKeeper.create("/watcher2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    @After
    public void f1() throws InterruptedException {
        zooKeeper.close();
    }

    @Test
    public void f2() throws KeeperException, InterruptedException {
        // 因为监听节点内容的时候，节点必须存在，所以就没有了创建节点事件
        new ZKWatcherGetData().watcherGetData1(zooKeeper);
    }

    @Test
    public void f3() throws KeeperException, InterruptedException {
        new ZKWatcherGetData().watcherGetData2(zooKeeper);
    }

    @Test
    public void f4() throws KeeperException, InterruptedException {
        new ZKWatcherGetData().watcherGetData3(zooKeeper);
    }

    @Test
    public void f5() throws KeeperException, InterruptedException {
        new ZKWatcherGetData().watcherGetData4(zooKeeper);
    }
}
