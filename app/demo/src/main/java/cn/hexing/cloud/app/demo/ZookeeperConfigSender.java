package com.cheetah.cloud.app.demo;

import com.cheetah.common.core.service.SentinelZookeeperRuleSource;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * 使用zookeeper client 直接新增/修改限流规则
 *
 * @author cheetah.zsy
 */
public class ZookeeperConfigSender {
    public static void main(String[] args) throws Exception {
        final String remoteAddress = "localhost:2181";
        final String path = SentinelZookeeperRuleSource.SENTINEL_RULE_ROOT_PATH + "demo";
        final String rule = "[\n"
                + "  {\n"
                + "    \"resource\": \"refresh_test\",\n"
                + "    \"controlBehavior\": 0,\n"
                + "    \"count\": 2.0,\n"
                + "    \"grade\": 1,\n"
                + "    \"limitApp\": \"default\",\n"
                + "    \"strategy\": 0\n"
                + "  }\n"
                + "]";

        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(remoteAddress,
                                                                      new ExponentialBackoffRetry(1000, 3));
        zkClient.start();

        Stat stat = zkClient.checkExists().forPath(path);
        if (stat == null) {
            zkClient.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, null);
        }
        zkClient.setData().forPath(path, rule.getBytes());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        zkClient.close();
    }
}
