package com.jimbolix.april.mq.api;

/**
 * @Description 消息类型
 * @ClassName MessageType
 * @Author liruihui
 * @date 2020.04.18 18:16
 */
public interface MessageType {

    /**
     * 迅速消息，不需要保证消息的可靠性也不需要确认
     */
    String rapid = "0";
    /**
     * 确认消息，不需要保证消息的可靠性但需要确认
     */
     String confirm = "1";

    /**
     * 可靠性消息，保证消息100%投递成功
     */
    String reliability = "2";
}
