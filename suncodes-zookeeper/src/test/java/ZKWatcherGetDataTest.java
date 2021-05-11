import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import suncodes.zookeeper.ZKWatcherGetData;

import java.io.*;

public class ZKWatcherGetDataTest {
    private ZooKeeper zooKeeper;

    @Before
    public void f() throws IOException, InterruptedException {
        zooKeeper = new ZKWatcherGetData().getZK();
    }

    @After
    public void f1() throws InterruptedException {
        zooKeeper.close();
    }

    @Test
    public void f2() throws KeeperException, InterruptedException {
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
