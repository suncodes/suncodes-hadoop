package suncodes.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GetNode {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = new ZooKeeper("192.168.6.110:2181", 100000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                // 异步连接
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("连接成功");
                    System.out.println(watchedEvent.getState());
                    System.out.println(watchedEvent.getType());
                    countDownLatch.countDown();
                }
            }
        });
        countDownLatch.await();
        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/hadoop", false, stat);
        System.out.println(new String(data));
        System.out.println(stat);

        zooKeeper.getData("/hbase", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] bytes, Stat stat) {
                // 判空
                System.out.println(rc + " " + path
                        + " " + ctx + " " + new String(bytes) + " " +
                        stat.getCzxid());
            }
        }, "I am context");
        TimeUnit.SECONDS.sleep(3);
        zooKeeper.close();
    }
}
