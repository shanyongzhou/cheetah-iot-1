package com.cheetah.common.core.service;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 使用 zookeeper 作为规则数据源
 *
 * @author cheetah.zsy
 */
@Component
public class SentinelZookeeperRuleSource {
    public static final String SENTINEL_RULE_ROOT_PATH = "/sentinel_rule_config/";
    @Value("${spring.application.name}")
    private String appName;

    @Value("${sentinel.zookeeper.address}")
    private String zookeeperAddress;

    @PostConstruct
    public void registerSentinelDataSource() {
        final String path = SENTINEL_RULE_ROOT_PATH + appName;
        ReadableDataSource<String, List<FlowRule>> readableDataSource = new ZookeeperDataSource<>(zookeeperAddress,
                                                                                                  path,
                                                                                                  source -> JSON.parseObject(
                                                                                                          source,
                                                                                                          new TypeReference<List<FlowRule>>() {
                                                                                                          }));
        FlowRuleManager.register2Property(readableDataSource.getProperty());
    }
}
