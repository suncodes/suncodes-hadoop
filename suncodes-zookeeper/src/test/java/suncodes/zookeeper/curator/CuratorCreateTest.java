package suncodes.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorCreateTest {

    private CuratorFramework client;

    @Before
    public void createzk() {
        client = new CuratorCreate().createzk();
    }

    @After
    public void closezk() {
        new CuratorCreate().closezk(client);
    }


    @Test
    public void create1() throws Exception {
        new CuratorCreate().create1(client);
    }

    @Test
    public void create2() throws Exception {
        new CuratorCreate().create2(client);
    }

    @Test
    public void create3() throws Exception {
        new CuratorCreate().create3(client);
    }

    @Test
    public void create4() throws Exception {
        new CuratorCreate().create4(client);
    }
}