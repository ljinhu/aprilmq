package com.jimbolix.april.mq.api;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @ClassName Message
 * @Author liruihui
 * @date 2020.04.18 18:12
 */
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = -8743238130178650385L;

    private String messageId;

    /**
     * 消息的exchange
     */
    private String topic;

    private String routingKey ="";

    private Map<String,Object> attributes = new HashMap<String, Object>();

    /**
     * 延迟消息的参数
     */
    private int delayMills;

    private String messageType = MessageType.confirm;

    public Message() {
    }

    public Message(String messageId, String topic, String routingKey, Map<String, Object> attributes, int delayMills, String messageType) {
        this.messageId = messageId;
        this.topic = topic;
        this.routingKey = routingKey;
        this.attributes = attributes;
        this.delayMills = delayMills;
        this.messageType = messageType;
    }
}
