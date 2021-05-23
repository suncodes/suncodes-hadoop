package suncodes.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CuratorDeleteTest {

    private CuratorFramework client;

    @Before
    public void createzk() {
        client = new CuratorDelete().createzk();
    }

    @After
    public void closezk() {
        new CuratorDelete().closezk(client);
    }

    @Test
    public void delete1() throws Exception {
        new CuratorDelete().delete1(client);
    }

    @Test
    public void delete2() throws Exception {
        new CuratorDelete().delete2(client);
    }

    @Test
    public void delete3() throws Exception {
        new CuratorDelete().delete3(client);
    }

    @Test
    public void delete4() throws Exception {
        new CuratorDelete().delete4(client);
    }
}