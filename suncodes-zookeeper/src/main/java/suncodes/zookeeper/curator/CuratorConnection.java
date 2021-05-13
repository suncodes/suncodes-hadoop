package suncodes.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryOneTime;

public class CuratorConnection {
    public static void main(String[] args) {
        /**
         * 设置重连策略
         * 三秒后重连一次，只重连一次
         * RetryPolicy retryPolicy = new RetryOneTime(3000);
         * 每三秒重连一次，重连三次
         * RetryPolicy retryPolicy = new RetryNTimes(3,3000);
         * 每三秒重连一次，总等待时间超过个10秒后停止重连
         * RetryPolicy retryPolicy = new RetryUntilElapsed(1000,3000);
         *
         * 这个策略的重试间隔会越来越长
         * RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3)
         * 计算解析重连间隔时间：
         *      baseSleepTImeMs * Math.max(1,random.nextInt(1 << (retryCount + 1)))
         *      baseSleepTimeMs` = `1000` 例子中的值
         *      maxRetries` = `3` 例子中的值
         */
        //三秒后重连一次，只重连一次
        RetryPolicy retryPolicy = new RetryOneTime(1000);

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("192.168.1.7:2181,192.168.1.7:2182,192.168.1.7:2183")
                // 会话超时
                .sessionTimeoutMs(5000)
                // 重试机制，这里是超时后1000毫秒重试一次
                .retryPolicy(retryPolicy)
                // 名称空间，在操作节点的时候，会以这个为父节点
                .namespace("create")
                .build();
        client.start();
        System.out.println(client.getState());
        client.close();
    }
}
