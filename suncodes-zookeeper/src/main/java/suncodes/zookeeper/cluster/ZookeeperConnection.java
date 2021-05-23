package suncodes.zookeeper.cluster;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class ZookeeperConnection {

    private static final String CLUSTER_IP = "192.168.6.110:2182,192.168.6.111:2181,192.168.6.112:2181";

    public static void main(String[] args) throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper connection = new ZooKeeper(CLUSTER_IP, 5000, watchedEvent -> {
            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected)
                System.out.println("连接成功");
            countDownLatch.countDown();
        });
        countDownLatch.await();
        connection.create("/hadoop", new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(connection.getSessionId());
    }
}
