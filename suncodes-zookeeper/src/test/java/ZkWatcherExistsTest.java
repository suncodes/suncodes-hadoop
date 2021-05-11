import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import suncodes.zookeeper.ZKWatcherExists;

import java.io.*;

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
