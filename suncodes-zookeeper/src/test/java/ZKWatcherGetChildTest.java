import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import suncodes.zookeeper.ZKWatcherGetChild;

import java.io.IOException;

/**
 * getChildren
 * getChildren(String path, boolean b)
 * getChildren(String path, Watcher w)
 * NodeChildrenChanged：子节点发生变化（子节点增删）
 * NodeDeleted：节点删除
 */
public class ZKWatcherGetChildTest {

    private ZooKeeper zooKeeper;

    @Before
    public void f() throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZKWatcherGetChild().getZK();
        if (zooKeeper.exists("/watcher3", false) == null) {
            zooKeeper.create("/watcher3", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    @After
    public void f1() throws InterruptedException {
        zooKeeper.close();
    }

    @Test
    public void f2() throws KeeperException, InterruptedException {
        // 因为监听节点内容的时候，节点必须存在，所以就没有了创建节点事件
        // create /watcher3/1 "1"
        new ZKWatcherGetChild().watcherGetChild1(zooKeeper);
    }

    @Test
    public void f3() throws KeeperException, InterruptedException {
        // set /watcher3/1 "1"
        new ZKWatcherGetChild().watcherGetChild2(zooKeeper);
    }

    @Test
    public void f4() throws KeeperException, InterruptedException {
        new ZKWatcherGetChild().watcherGetChild3(zooKeeper);
    }

    @Test
    public void f5() throws KeeperException, InterruptedException {
        new ZKWatcherGetChild().watcherGetChild4(zooKeeper);
    }
}
