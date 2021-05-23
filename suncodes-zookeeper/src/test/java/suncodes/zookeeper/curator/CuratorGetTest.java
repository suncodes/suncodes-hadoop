package suncodes.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CuratorGetTest {

    private CuratorFramework client;

    @Before
    public void createzk() {
        client =  new CuratorGet().createzk();
    }

    @After
    public void closezk() {
        new CuratorGet().closezk(client);
    }

    @Test
    public void get1() throws Exception {
        new CuratorGet().get1(client);
    }

    @Test
    public void get2() throws Exception {
        new CuratorGet().get2(client);
    }

    @Test
    public void get3() throws Exception {
        new CuratorGet().get3(client);
    }
}