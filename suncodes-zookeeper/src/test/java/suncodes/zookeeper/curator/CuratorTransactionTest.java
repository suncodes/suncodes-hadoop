package suncodes.zookeeper.curator;

import org.apache.curator.framework.CuratorFramework;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CuratorTransactionTest {

    private CuratorFramework client;

    @Before
    public void createZK() {
        client = new CuratorTransaction().createZK();
    }

    @After
    public void closeZK() {
        new CuratorTransaction().closeZK(client);
    }

    @Test
    public void tra1() throws Exception {
        new CuratorTransaction().tra1(client);
    }

    @Test
    public void tra1NewAPI() throws Exception {
        new CuratorTransaction().tra1NewAPI(client);
    }
}