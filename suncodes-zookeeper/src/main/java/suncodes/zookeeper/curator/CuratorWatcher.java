package suncodes.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorWatcher {

    private static final String IP = "192.168.6.110:2182,192.168.6.111:2181,192.168.6.112:2181";

    public CuratorFramework createZK() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString(IP)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();
        return client;
    }

    public void closeZK(CuratorFramework client) {
        client.close();
    }


    public void watcher1(CuratorFramework client) throws Exception {
        // 监视某个节点的数据变化
        // arg1:连接对象
        // arg2:监视的节点路径
        final NodeCache nodeCache = new NodeCache(client, "/watcher1");
        // 启动监视器对象
        nodeCache.start();
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            // 节点变化时回调的方法
            @Override
            public void nodeChanged() throws Exception {
                System.out.println(nodeCache.getCurrentData().getPath());
                System.out.println(new String(nodeCache.getCurrentData().getData()));
            }
        });
        Thread.sleep(100000);
        System.out.println("结束");
        //关闭监视器对象
        nodeCache.close();
    }

    public void watcher1NewAPI(CuratorFramework client) throws Exception {
        // 监视某个节点的数据变化
        // arg1:连接对象
        // arg2:监视的节点路径
        CuratorCache curatorCache = CuratorCache.build(client, "/watcher1");
        curatorCache.start();
        curatorCache.listenable().addListener(new CuratorCacheListener() {
            /**
             * @param type the type of event
             * @param oldData the old data or null
             * @param childData the new data or null
             */
            @Override
            public void event(Type type, ChildData oldData, ChildData childData) {
                System.out.println(childData.getPath());
                System.out.println(new String(childData.getData()));
            }
        });
        Thread.sleep(100000);
        System.out.println("结束");
        //关闭监视器对象
        curatorCache.close();
    }

    public void watcher2(CuratorFramework client) throws Exception {
        // 监视子节点的变化
        // arg1:连接对象
        // arg2:监视的节点路径
        // arg3:事件中是否可以获取节点的数据
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/watcher1", true);
        // 启动监听
        pathChildrenCache.start();
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            // 当子节点方法变化时回调的方法
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent)
                    throws Exception {
                // 节点的事件类型
                System.out.println(pathChildrenCacheEvent.getType());
                // 节点的路径
                System.out.println(pathChildrenCacheEvent.getData().getPath());
                // 节点数据
                System.out.println(new String(pathChildrenCacheEvent.getData().getData()));
            }
        });
        Thread.sleep(100000);
        System.out.println("结束");
        // 关闭监听
        pathChildrenCache.close();
    }

    public void watcher2NewAPI(CuratorFramework client) throws Exception {
        // 监视子节点的变化
        // arg1:连接对象
        // arg2:监视的节点路径
        // arg3:事件中是否可以获取节点的数据
        CuratorCache curatorCache = CuratorCache.build(client, "/watcher1", CuratorCache.Options.SINGLE_NODE_CACHE);
        curatorCache.start();
        curatorCache.listenable().addListener(new CuratorCacheListener() {
            @Override
            public void event(Type type, ChildData oldData, ChildData childData) {
                System.out.println(type);
                System.out.println(childData.getPath());
                System.out.println(new String(childData.getData()));
            }
        });

        Thread.sleep(100000);
        System.out.println("结束");
        // 关闭监听
        curatorCache.close();
    }
}