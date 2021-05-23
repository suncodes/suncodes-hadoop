package suncodes.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CuratorExistsTest {

    private CuratorFramework client;

    @Before
    public void createzk() {
        client = new CuratorExists().createzk();
    }

    @After
    public void closeZk() {
        new CuratorExists().closeZk(client);
    }

    @Test
    public void exists1() throws Exception {
        new CuratorExists().exists1(client);
    }

    @Test
    public void exists2() throws Exception {
        new CuratorExists().exists2(client);
    }
}