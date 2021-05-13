package suncodes.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.retry.RetryOneTime;

public class CuratorDelete {

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
     * 删除节点，不带版本号
     */
    public void delete1(CuratorFramework client) throws Exception {
        client.delete().forPath("/hadoop");
    }

    /**
     * 删除节点，带版本号
     */
    public void delete2(CuratorFramework client) throws Exception {
        client.delete()
                // 版本
                .withVersion(0)
                .forPath("/node2");
    }

    /**
     * 递归删除（node3下还有子节点，也能删除）
     */
    public void delete3(CuratorFramework client) throws Exception {
        client.delete()
                // 递归删除
                .deletingChildrenIfNeeded()
                .withVersion(-1)
                .forPath("/node3");
    }

    /**
     * 异步删除
     */
    public void delete4(CuratorFramework client) throws Exception {
        // 删除节点
        client.delete()
                .withVersion(-1)
                // 异步
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework curatorFramework, CuratorEvent curatorEvent)
                            throws Exception {
                        if (curatorEvent.getType() == CuratorEventType.DELETE) {
                            System.out.println(curatorEvent.getPath() + "    " + curatorEvent.getType());
                        }
                    }
                })
                .forPath("/node3");
    }
}
