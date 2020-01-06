package com.cheetah.bootstrap.mdc.channel;

import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * 启用 spring cloud stream binders，使用CacheChannel 接口作为 channel 定义
 */
@EnableBinding(CacheChannel.class)
public class BootstrapChannelConfig {
}
