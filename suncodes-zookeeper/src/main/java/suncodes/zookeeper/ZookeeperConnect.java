package suncodes.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.*;
import java.util.concurrent.CountDownLatch;

public class ZookeeperConnect {
    public static void main(String[] args) throws IOException, InterruptedException {
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
        System.out.println(zooKeeper.getSessionId());
        zooKeeper.close();
    }
}
