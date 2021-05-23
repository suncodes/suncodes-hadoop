package suncodes.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorSetTest {

    private CuratorFramework client;

    @Before
    public void createzk() {
        client = new CuratorSet().createzk();
    }

    @After
    public void closezk() {
        new CuratorSet().closezk(client);
    }

    @Test
    public void set1() throws Exception {
        new CuratorSet().set1(client);
    }

    @Test
    public void set2() throws Exception {
        new CuratorSet().set2(client);
    }

    @Test
    public void set3() throws Exception {
        new CuratorSet().set3(client);
    }
}