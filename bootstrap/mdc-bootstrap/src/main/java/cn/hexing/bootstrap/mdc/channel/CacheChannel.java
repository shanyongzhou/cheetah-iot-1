package com.cheetah.bootstrap.mdc.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * 定义用于缓存处理的 stream channel
 */
public interface CacheChannel {
    /**
     * 输出 cache message 的 channel name
     */
    String UPDATE_OUTPUT = "cache_update_output";

    @Output(UPDATE_OUTPUT)
    MessageChannel updateOutput();
}
