package suncodes.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;

import java.util.List;

public class CuratorGetChild {

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

    public void closezk(CuratorFramework client) {
        client.close();
    }

    /**
     * 获取该节点的所有子节点名称
     */
    public void getChildren1(CuratorFramework client) throws Exception {
        // 获取数据
        List<String> strings = client.getChildren()
                .forPath("/node3");
        strings.forEach(System.out::println);
    }

    /**
     * 异步获取该节点的所有子节点名称
     */
    public void getChildren2(CuratorFramework client) throws Exception {
        // 获取数据
        client.getChildren()
                .inBackground((curatorFramework, curatorEvent) -> {
                    curatorEvent.getChildren().forEach(System.out::println);
                    System.out.println("------------");
                })
                .forPath("/node3");
    }
}
