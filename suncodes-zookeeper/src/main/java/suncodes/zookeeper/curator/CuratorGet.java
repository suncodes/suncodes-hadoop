package suncodes.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.data.Stat;

public class CuratorGet {

    public CuratorFramework createzk() {
        RetryPolicy retryPolicy = new RetryOneTime(1000);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("192.168.1.7:2181,192.168.1.7:2182,192.168.1.7:2183")
                .sessionTimeoutMs(5000000)
                .retryPolicy(retryPolicy)
                .namespace("settwo")
                .build();
        client.start();
        return client;
    }

    public void closezk(CuratorFramework client) {
        client.close();
    }

    /**
     * 获取节点数据
     */
    public void get1(CuratorFramework client) throws Exception {
        // 获取数据
        byte[] bytes = client.getData()
                .forPath("/node2");
        System.out.println(new String((bytes)));
    }

    /**
     * 获取节点数据，并获取节点属性
     */
    public void get2(CuratorFramework client) throws Exception {
        Stat stat = new Stat();
        // 获取数据
        byte[] bytes = client.getData()
                .storingStatIn(stat)
                .forPath("/node2");
        ;
        System.out.println(new String((bytes)));
        System.out.println(stat.getVersion());
        System.out.println(stat.getCzxid());
    }

    /**
     * 异步获取数据
     */
    public void get3(CuratorFramework client) throws Exception {
        // 获取数据
        client.getData().inBackground((CuratorFramework curatorFramework, CuratorEvent curatorEvent) -> {
            System.out.println(curatorEvent.getPath() + "  " + curatorEvent.getType());
        }).forPath("/node2");
    }

}
