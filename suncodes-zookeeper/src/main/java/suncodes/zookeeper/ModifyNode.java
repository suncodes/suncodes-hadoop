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

public class ModifyNode {
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
        // arg1:节点的路径
        // arg2:修改的数据
        // arg3:数据的版本号 -1 代表版本号不参与更新
        Stat stat = zooKeeper.setData("/node6", "java node6".getBytes(), -1);
        System.out.println(stat);

        // VERSION 对应的就是 set [-v version]
        zooKeeper.setData("/node6", "node11".getBytes(), -1, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                System.out.println(rc + " " + path + " " + stat +  " " + ctx);
            }
        }, "I am context");
        TimeUnit.SECONDS.sleep(3);
        zooKeeper.close();
    }
}
