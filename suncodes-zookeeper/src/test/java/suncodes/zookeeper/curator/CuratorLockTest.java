package suncodes.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CuratorLockTest {

    private CuratorFramework client;

    @Before
    public void createZK() {
        client = new CuratorLock().createZK();
    }

    @After
    public void closeZK() {
        new CuratorLock().closeZK(client);
    }

    @Test
    public void lock1() throws Exception {
        new CuratorLock().lock1(client);
    }

    @Test
    public void lock2() throws Exception {
        new CuratorLock().lock2(client);
    }

    @Test
    public void lock3() throws Exception {
        new CuratorLock().lock3(client);
    }
}