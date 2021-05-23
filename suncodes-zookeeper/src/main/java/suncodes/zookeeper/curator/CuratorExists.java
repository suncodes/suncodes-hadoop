package suncodes.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.RetryOneTime;
import org.apache.zookeeper.data.Stat;

public class CuratorExists {

    private static final String CLUSTER_IP = "192.168.6.110:2182,192.168.6.111:2181,192.168.6.112:2181";

    public CuratorFramework createzk() {
        RetryPolicy retryPolicy = new RetryOneTime(1000);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(CLUSTER_IP)
                .sessionTimeoutMs(5000000)
                .retryPolicy(retryPolicy)
                .namespace("settwo")
                .build();
        client.start();
        return client;
    }

    public void closeZk(CuratorFramework client) {
        client.close();
    }

    public void exists1(CuratorFramework client) throws Exception {
        // 判断节点是否存在
        Stat stat = client.checkExists()
                // 节点路径
                .forPath("/node2");
        if (stat == null) {
            System.out.println("节点不存在");
        } else {
            System.out.println(stat.getVersion());
        }
    }


    public void exists2(CuratorFramework client) throws Exception {
        // 异步方式判断节点是否存在
        client.checkExists()
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent)
                            throws Exception {
                        // 节点路径
                        System.out.println(curatorEvent.getPath());
                        // 事件类型
                        System.out.println(curatorEvent.getType());
                        System.out.println(curatorEvent.getStat());
                    }
                })
                .forPath("/node2");
        Thread.sleep(5000);
        System.out.println("结束");
    }
}
