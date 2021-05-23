package suncodes.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CuratorWatcherTest {

    private CuratorFramework client;

    @Before
    public void createZK() {
        client = new CuratorWatcher().createZK();
    }

    @After
    public void closeZK() {
        new CuratorWatcher().closeZK(client);
    }

    @Test
    public void watcher1() throws Exception {
        new CuratorWatcher().watcher1(client);
    }

    @Test
    public void watcher1NewAPI() throws Exception {
        new CuratorWatcher().watcher1NewAPI(client);
    }

    @Test
    public void watcher2() throws Exception {
        new CuratorWatcher().watcher2(client);
    }

    @Test
    public void watcher2NewAPI() throws Exception {
        new CuratorWatcher().watcher2NewAPI(client);
    }
}