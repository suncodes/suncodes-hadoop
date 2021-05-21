package suncodes.zookeeper;

import org.apache.zookeeper.*;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * zookeeper 连接 监听 测试
 */
public class ZKConnectionWatcher {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper("192.168.6.110:2181", 5000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                // 对于监听连接状态的 事件类型为 None
                if (watchedEvent.getType() == Event.EventType.None) {
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        System.out.println("连接成功");
                        countDownLatch.countDown();
                    }
                    if (watchedEvent.getState() == Event.KeeperState.Disconnected) {
                        System.out.println("断开连接");
                    }
                    if (watchedEvent.getState() == Event.KeeperState.Expired) {
                        System.out.println("会话超时");
                    }
                    if (watchedEvent.getState() == Event.KeeperState.AuthFailed) {
                        System.out.println("认证失败");
                    }
                }
            }
        });

        countDownLatch.await();
        System.out.println(zooKeeper.getSessionId());

        String node1 = zooKeeper.create("/node1", "node1".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(node1);

        zooKeeper.addAuthInfo("digest", "scz:scz".getBytes());
        byte[] data = zooKeeper.getData("/node1", false, null);
        System.out.println(new String(data));
        TimeUnit.SECONDS.sleep(5);

        System.out.println("===========================");

        String node2 = zooKeeper.create("/node2", "node22".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        System.out.println(node2);

        zooKeeper.addAuthInfo("digest", "scz1:scz1".getBytes());
        byte[] data2 = zooKeeper.getData("/node2", false, null);
        System.out.println(new String(data2));
        TimeUnit.SECONDS.sleep(5);

        zooKeeper.close();
        System.out.println("结束");
    }
}
