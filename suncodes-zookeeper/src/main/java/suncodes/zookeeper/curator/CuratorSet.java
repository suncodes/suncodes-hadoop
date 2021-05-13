package suncodes.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.retry.RetryOneTime;

public class CuratorSet {

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
     * 不使用版本号
     * -1代表不使用版本号
     */
    public void set1(CuratorFramework client) throws Exception {
        // 修改节点
        client.setData()
                // 版本   -1代表不使用版本号
                .withVersion(-1)
                .forPath("/hadoop", "hadoop1".getBytes());
    }

    /**
     * 使用版本号更新
     */
    public void set2(CuratorFramework client) throws Exception {
        // 修改节点
        client.setData()
                .withVersion(1)
                .forPath("/hadoop", "hadoop".getBytes());
    }

    /**
     * 异步更新
     */
    public void set3(CuratorFramework client) throws Exception {
        // 修改节点
        client.setData()
                .withVersion(-1)
                // 异步
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent)
                            throws Exception {
                        if (curatorEvent.getType() == CuratorEventType.SET_DATA) {
                            System.out.println(curatorEvent.getPath() + "    " + curatorEvent.getType());
                        }
                    }
                })
                .forPath("/hadoop", "hadoop3".getBytes());
    }
}
