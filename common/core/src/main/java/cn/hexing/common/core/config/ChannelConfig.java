package com.cheetah.common.core.config;

import com.cheetah.common.core.channel.CacheChannel;
import com.cheetah.common.core.service.CacheMessageHandler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * 用于绑定 stream channel和相关 listener 的配置类
 */
@EnableBinding(CacheChannel.class)
public class ChannelConfig {
    private final CacheMessageHandler cacheService;

    public ChannelConfig(CacheMessageHandler cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * 绑定CacheChannel 的 UPDATE_INPUT 输入源
     * @param cacheMessage
     */
    @StreamListener(CacheChannel.UPDATE_INPUT)
    public void handleCacheMessage(com.cheetah.common.core.entity.message.CacheMessage cacheMessage){
        this.cacheService.handleCacheMessage(cacheMessage);
    }
}
