package suncodes.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.*;

public class ModifyNode {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.6.110", 2181, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("连接成功");
                System.out.println(watchedEvent.getState());
                System.out.println(watchedEvent.getType());
            }
        });
        // arg1:节点的路径
        // arg2:修改的数据
        // arg3:数据的版本号 -1 代表版本号不参与更新
        Stat stat = zooKeeper.setData("/node1", "java node1".getBytes(), -1);
        System.out.println(stat);

        zooKeeper.setData("/node1", "node11".getBytes(), 1, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                // 讲道理，要判空
                System.out.println(rc + " " + path + " " + stat.getVersion() +  " " + ctx);
            }
        }, "I am context");
        zooKeeper.close();
    }
}
