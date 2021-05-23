package suncodes.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorOp;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * 多个创建操作在同一个事务
 * 要么都创建成功，要么均失败
 */
public class CuratorTransaction {

    private static final String IP = "192.168.6.110:2182,192.168.6.111:2181,192.168.6.112:2181";

    public CuratorFramework createZK() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString(IP)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        return client;
    }

    public void closeZK(CuratorFramework client) {
        client.close();
    }

    public void tra1(CuratorFramework client) throws Exception {
        // 开启事务
        client.inTransaction()
                .create().forPath("/node1", "node1".getBytes())
                .and()
                .create().forPath("/node2", "node2".getBytes())
                .and()
                //事务提交
                .commit();
    }

    public void tra1NewAPI(CuratorFramework client) throws Exception {
        // 开启事务
        CuratorOp curatorOp = client.transactionOp().create().forPath("/node1", "node1".getBytes());
        CuratorOp curatorOp2 = client.transactionOp().create().forPath("/node2", "node2".getBytes());
        client.transaction().forOperations(curatorOp, curatorOp2);
    }
}