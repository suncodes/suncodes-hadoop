package suncodes.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorGetChildTest {

    private CuratorFramework client;

    @Before
    public void createzk() {
        client = new CuratorGetChild().createzk();
    }

    @After
    public void closezk() {
        new CuratorGetChild().closezk(client);
    }

    @Test
    public void getChildren1() throws Exception {
        new CuratorGetChild().getChildren1(client);
    }

    @Test
    public void getChildren2() throws Exception {
        new CuratorGetChild().getChildren2(client);
    }
}