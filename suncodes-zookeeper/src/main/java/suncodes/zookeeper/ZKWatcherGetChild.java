package suncodes.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.*;
import java.util.concurrent.CountDownLatch;

public class ZKWatcherGetChild {
    public ZooKeeper getZK() throws IOException, InterruptedException {
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
        return zooKeeper;
    }

    public void watcherGetChild1(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        // arg1:节点的路径
        // arg2:使用连接对象中的watcher
        zooKeeper.getChildren("/watcher3", true, null);
        Thread.sleep(50000);
        System.out.println("结束");
    }

    public void watcherGetChild2(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        // arg1:节点的路径
        // arg2:自定义watcher对象
        zooKeeper.getChildren("/watcher3", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("自定义watcher对象");
                System.out.println("path: " + watchedEvent.getPath());
                System.out.println("eventType: " + watchedEvent.getType());
            }
        }, null);
        Thread.sleep(50000);
        System.out.println("结束");
    }

    public void watcherGetChild3(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        // watcher 一次性
        // arg1:节点的路径
        // arg2:自定义watcher对象
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("自定义watcher对象");
                System.out.println("path: " + watchedEvent.getPath());
                System.out.println("eventType: " + watchedEvent.getType());
                // ???????
                try {
                    zooKeeper.getChildren("/watcher3", this);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        zooKeeper.getChildren("/watcher3", watcher, null);
        Thread.sleep(50000);
        System.out.println("结束");
    }

    public void watcherGetChild4(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        // 注册多个监听器
        // arg1:节点的路径
        // arg2:自定义watcher对象
        zooKeeper.getChildren("/watcher3", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    System.out.println("1");
                    System.out.println("path=" + event.getPath());
                    System.out.println("eventType=" + event.getType());
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        zooKeeper.getChildren("/watcher3", this, null);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, null);
        zooKeeper.getChildren("/watcher3", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                try {
                    System.out.println("2");
                    System.out.println("path=" + event.getPath());
                    System.out.println("eventType=" + event.getType());
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        zooKeeper.getChildren("/watcher3", this, null);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, null);
        Thread.sleep(50000);
        System.out.println("结束");
    }
}
