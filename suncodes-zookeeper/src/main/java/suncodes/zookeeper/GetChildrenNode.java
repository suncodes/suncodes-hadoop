package suncodes.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.*;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GetChildrenNode {
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

        List<String> children = zooKeeper.getChildren("/hadoop", false);
        for (String child : children) {
            System.out.println(child);
        }

        zooKeeper.getChildren("/hbase", false, new AsyncCallback.ChildrenCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, List<String> list) {
                list.forEach(System.out::println);
                System.out.println(rc + " " + path + " " + ctx);
            }
        }, "I am context");
        TimeUnit.SECONDS.sleep(3);
        zooKeeper.close();
    }
}
