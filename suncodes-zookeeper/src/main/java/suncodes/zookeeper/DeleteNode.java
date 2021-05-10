package suncodes.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.*;
import java.util.concurrent.TimeUnit;

public class DeleteNode {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.6.110", 2181, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("连接成功");
                System.out.println(watchedEvent.getState());
                System.out.println(watchedEvent.getType());
            }
        });
        zooKeeper.delete("/node1", 1);
        zooKeeper.delete("node2", -1, new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx) {
                System.out.println(rc + " " + path + " " + ctx);
            }
        }, "I am context");
        TimeUnit.SECONDS.sleep(1);
        zooKeeper.close();
    }
}
