package com.jimbolix.april.mq.api;

import com.jimbolix.april.mq.exception.MessageRunTimeException;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description 建造者模式创建
 * 构建方法一定要私有化
 * @ClassName MessageBuilder
 * @Author liruihui
 * @date 2020.04.18 18:23
 */
public class MessageBuilder {
    private String messageId;
    private String topic;
    private String routingKey = "";
    private Map<String, Object> attributes = new HashMap<String, Object>();
    private int delayMills;
    private String messageType = MessageType.confirm;

    private MessageBuilder() {
    }

    public static MessageBuilder create() {
        return new MessageBuilder();
    }

    public MessageBuilder withMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public MessageBuilder withTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public MessageBuilder withRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    public MessageBuilder withDelayMills(int delayMills) {
        this.delayMills = delayMills;
        return this;
    }

    public MessageBuilder withMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public MessageBuilder withAttributes(Map<String, Object> attributes) {
        this.attributes.putAll(attributes);
        return this;
    }

    public MessageBuilder withAttribute(String key, Object value) {
        this.attributes.put(key, value);
        return this;
    }

    public Message build() {
        if(StringUtils.isEmpty(this.messageId)){
            this.messageId = UUID.randomUUID().toString();
        }

        if(StringUtils.isEmpty(topic)){
            throw new MessageRunTimeException("topic is null");
        }
        return new Message(messageId, topic, routingKey, attributes, delayMills, messageType);
    }
}
