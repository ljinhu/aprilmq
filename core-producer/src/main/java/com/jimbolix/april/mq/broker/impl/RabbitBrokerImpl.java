package com.jimbolix.april.mq.broker.impl;

import com.jimbolix.april.mq.api.Message;
import com.jimbolix.april.mq.api.MessageType;
import com.jimbolix.april.mq.broker.AsyncBaseQueue;
import com.jimbolix.april.mq.broker.RabbitBroker;
import com.jimbolix.april.mq.broker.RabbitTemplateContainer;
import com.jimbolix.april.mq.constant.BrokerMessageConst;
import com.jimbolix.april.mq.constant.BrokerMessageStatus;
import com.jimbolix.april.mq.entity.BrokerMessage;
import com.jimbolix.april.mq.service.MessageStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description
 * @ClassName RabbitBrokerImpl
 * @Author liruihui
 * @date 2020.04.18 19:55
 */
@Slf4j
@Component
public class RabbitBrokerImpl implements RabbitBroker {

    @Autowired
    private RabbitTemplateContainer rabbitTemplateContainer;

    @Autowired
    private MessageStoreService messageStoreService;
    /**
     * 发送快速消息，无确认和可靠性要求
     *
     * @param message
     */
    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageType.rapid);
        senKernel(message);
    }

    /**
     * 发送确认消息，无可靠性要求
     *
     * @param message
     */
    @Override
    public void confirmSend(Message message) {
            message.setMessageType(MessageType.confirm);
            senKernel(message);
    }

    /**
     * 发送可靠性消息
     *
     * @param message
     */
    @Override
    public void reliabilitySend(Message message) {
            message.setMessageType(MessageType.reliability);
            BrokerMessage bkMessage = messageStoreService.selectByMessageId(message.getMessageId());

        //发送可靠性消息前，消息数据入库
            if(null == bkMessage){
                Date now = new Date();
                BrokerMessage brokerMessage = new BrokerMessage();
                brokerMessage.setMessageId(message.getMessageId());
                brokerMessage.setStatus(BrokerMessageStatus.SENDING.getCode());
                //tryCount 在最开始发送的时候不需要进行设置
                brokerMessage.setNextRetry(DateUtils.addMinutes(now, BrokerMessageConst.TIMEOUT));
                brokerMessage.setCreateTime(now);
                brokerMessage.setUpdateTime(now);
                brokerMessage.setMessage(message);
                messageStoreService.insert(brokerMessage);
            }
            senKernel(message);
    }

    private void senKernel(Message message) {
        AsyncBaseQueue.submit(() -> {
            String topic = message.getTopic();
            String routingKey = message.getRoutingKey();
            CorrelationData correlationData =
                    new CorrelationData(String.format("%s#%s#%s",
                            message.getMessageId(),
                            System.currentTimeMillis(),
                            message.getMessageType()));
            RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
            rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
            log.info("#com.jimbolix.april.mq.broker.impl.RabbitBrokerImpl.senKernel#,send message,messageId{}",
                    message.getMessageId());

        });
    }
}
