package com.jimbolix.april.mq.broker;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.jimbolix.april.mq.api.Message;
import com.jimbolix.april.mq.api.MessageType;
import com.jimbolix.april.mq.exception.MessageRunTimeException;
import com.jimbolix.april.mq.service.MessageStoreService;
import com.jimbolix.aprilmq.convert.GenericMessageConverter;
import com.jimbolix.aprilmq.convert.RabbitMessageConverter;
import com.jimbolix.aprilmq.serializer.Serializer;
import com.jimbolix.aprilmq.serializer.SerializerFactory;
import com.jimbolix.aprilmq.serializer.impl.JacksonSerializerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description RabbitTemplate池化封装
 * @ClassName RabbitTemplateContainer
 * @Author liruihui
 * @date 2020.04.18 20:34
 */
@Slf4j
@Component
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private MessageStoreService messageStoreService;

    private SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;

    private Map<String/*topic*/, RabbitTemplate> rabbitTemplateMap = Maps.newConcurrentMap();

    private Splitter splitter = Splitter.on("#");

    public RabbitTemplate getTemplate(Message message)throws MessageRunTimeException{
        Preconditions.checkNotNull(message);
        String topic = message.getTopic();
        Preconditions.checkNotNull(topic);
        RabbitTemplate rabbitTemplate = rabbitTemplateMap.get(topic);
        if(null != rabbitTemplate){
            return rabbitTemplate;
        }
        log.info("#com.jimbolix.april.mq.broker.RabbitTemplateContainer.getTemplate#,result is null,now create new one");
        RabbitTemplate rabbitTemp = new RabbitTemplate(connectionFactory);
        rabbitTemp.setExchange(topic);
        rabbitTemp.setRoutingKey(message.getRoutingKey());
        rabbitTemp.setRetryTemplate(new RetryTemplate());
        //消息序列化
        Serializer serializer = serializerFactory.create();
        GenericMessageConverter gmc = new GenericMessageConverter(serializer);
        RabbitMessageConverter rmc = new RabbitMessageConverter(gmc);
        rabbitTemp.setMessageConverter(rmc);
        //消息确认
        String messageType = message.getMessageType();
        if(!StringUtils.equals(messageType,MessageType.rapid)){
            log.info("=============设置消息确认============");
            rabbitTemp.setConfirmCallback(this);
        }
        rabbitTemplateMap.putIfAbsent(topic,rabbitTemp);
        return rabbitTemplateMap.get(topic);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info("recived confirm ，result is：{}",ack);
        // 	具体的消息应答
        List<String> strings = splitter.splitToList(correlationData.getId());
        String messageId = strings.get(0);
        long sendTime = Long.parseLong(strings.get(1));
        String messageType = strings.get(2);
        if(ack) {
            //	当Broker 返回ACK成功时, 就是更新一下日志表里对应的消息发送状态为 SEND_OK

            // 	如果当前消息类型为reliant 我们就去数据库查找并进行更新
            if(MessageType.reliability.endsWith(messageType)) {
                this.messageStoreService.succuess(messageId);
            }
            log.info("send message is OK, confirm messageId: {}, sendTime: {}", messageId, sendTime);
        } else {
            log.error("send message is Fail, confirm messageId: {}, sendTime: {}", messageId, sendTime);

        }
    }
}
