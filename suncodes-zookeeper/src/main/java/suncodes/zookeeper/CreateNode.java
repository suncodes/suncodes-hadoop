package suncodes.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CreateNode {
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.6.110", 2181, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("连接成功");
                System.out.println(watchedEvent.getState());
                System.out.println(watchedEvent.getType());
            }
        });

        // 创建节点
        // OPEN_ACL_UNSAFE : 完全开放的ACL，任何连接的客户端都可以操作该属性znode
        // CREATOR_ALL_ACL : 只有创建者才有ACL权限
        // READ_ACL_UNSAFE ：只能读取ACL
        String node1 = zooKeeper.create("/node1", "node1".getBytes(), ZooDefs.Ids.READ_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(node1);

        // 指定对应 IP 有 cdrwa 权限
        List<ACL> aclList = new ArrayList<>();
        Id id = new Id("ip", "192.168.6.111");
        aclList.add(new ACL(ZooDefs.Perms.ALL, id));
        String node2 = zooKeeper.create("/node2", "node2".getBytes(), aclList, CreateMode.PERSISTENT);
        System.out.println(node2);

        // auth 方式
        zooKeeper.addAuthInfo("digest", "scz:scz".getBytes());
        String node3 = zooKeeper.create("/node3", "node3".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.PERSISTENT);
        System.out.println(node3);

        zooKeeper.addAuthInfo("digest", "scz:scz".getBytes());
        List<ACL> aclList2 = new ArrayList<>();
        Id id2 = new Id("auth", "scz");
        aclList2.add(new ACL(ZooDefs.Perms.ALL, id2));
        String node4 = zooKeeper.create("/node4", "node4".getBytes(), aclList2, CreateMode.PERSISTENT);
        System.out.println(node4);

        // digest
        List<ACL> aclList3 = new ArrayList<>();
        Id id3 = new Id("digest", "itcast:qUFSHxJjItUW/93UHFXFVGlvryY=");
        aclList3.add(new ACL(ZooDefs.Perms.ALL, id3));
        String node5 = zooKeeper.create("/node5", "node5".getBytes(), aclList3, CreateMode.PERSISTENT);
        System.out.println(node5);

        // 异步
        zooKeeper.create("/node6", "node6".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT,
                new AsyncCallback.StringCallback() {
                    /**
                     * @param i 状态，0 则为成功，以下的所有示例都是如此
                     * @param s 路径
                     * @param o 上下文参数
                     * @param s1 路径
                     */
                    @Override
                    public void processResult(int i, String s, Object o, String s1) {
                        System.out.println(i + "" + s + "" + s1 + "" + o);
                    }
                }, "I am context");
        TimeUnit.SECONDS.sleep(1);
        System.out.println("结束");
        zooKeeper.close();
    }
}
