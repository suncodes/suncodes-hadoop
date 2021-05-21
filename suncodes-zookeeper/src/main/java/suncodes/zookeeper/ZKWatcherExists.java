package suncodes.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.*;
import java.util.concurrent.CountDownLatch;

public class ZKWatcherExists {

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

    public void watcherExists1(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        // arg1:节点的路径
        // arg2:使用连接对象中的watcher
        zooKeeper.exists("/watcher1", true);
        Thread.sleep(50000);
        System.out.println("结束");
    }

    public void watcherExists2(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        // arg1:节点的路径
        // arg2:自定义watcher对象
        zooKeeper.exists("/watcher1", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("自定义watcher对象");
                System.out.println("path: " + watchedEvent.getPath());
                System.out.println("eventType: " + watchedEvent.getType());
            }
        });
        Thread.sleep(50000);
        System.out.println("结束");
    }

    public void watcherExists3(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        // watcher 一次性
        // arg1:节点的路径
        // arg2:自定义watcher对象
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("自定义watcher对象");
                System.out.println("path: " + watchedEvent.getPath());
                System.out.println("eventType: " + watchedEvent.getType());
                // 通过递归，可以在监听器响应事件后，再次注册监听器，从而解决一次性的问题
                try {
                    zooKeeper.exists("/watcher1", this);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        zooKeeper.exists("/watcher1", watcher);
        // 虽然注册了两次，但是注册的是同一个监听器，可忽略
        zooKeeper.exists("/watcher1", watcher);
        // 如果睡眠时间太长，则连接就直接断开了
        Thread.sleep(50000);
        System.out.println("结束");
    }

    public void watcherExists4(ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        // 注册多个监听器，一次性的
        // arg1:节点的路径
        // arg2:自定义watcher对象
        zooKeeper.exists("/watcher1", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("自定义watcher对象1");
                System.out.println("path: " + watchedEvent.getPath());
                System.out.println("eventType: " + watchedEvent.getType());
            }
        });
        zooKeeper.exists("/watcher1", new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("自定义watcher对象2");
                System.out.println("path: " + watchedEvent.getPath());
                System.out.println("eventType: " + watchedEvent.getType());
            }
        });
        Thread.sleep(50000);
        System.out.println("结束");
    }
}
