package com.cheetah.common.core.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 定义用于处理缓存消息的 Stream channel
 * @author cheetah.zsy
 */
public interface CacheChannel {
    String UPDATE_INPUT = "cache_update_input";

    @Input(UPDATE_INPUT)
    SubscribableChannel updateInput();
}
