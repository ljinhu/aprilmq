package com.jimbolix.april.mq.broker.impl;

import com.google.common.base.Preconditions;
import com.jimbolix.april.mq.api.Message;
import com.jimbolix.april.mq.api.MessageProducer;
import com.jimbolix.april.mq.api.MessageType;
import com.jimbolix.april.mq.api.SendCallBack;
import com.jimbolix.april.mq.broker.RabbitBroker;
import com.jimbolix.april.mq.exception.MessageRunTimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @ClassName ProducerClient
 * @Author liruihui
 * @date 2020.04.18 19:16
 */
@Component
public class ProducerClient implements MessageProducer {
    @Autowired
    private RabbitBroker rabbitBroker;
    @Override
    public void send(Message message) throws MessageRunTimeException {
        //根据不同的消息类型进行发送
        Preconditions.checkNotNull(message.getMessageType());
        String messageType = message.getMessageType();
        switch (messageType){
            case MessageType.rapid:
                rabbitBroker.rapidSend(message);
                break;
            case MessageType.confirm:
                rabbitBroker.confirmSend(message);
                break;
            case MessageType.reliability:
                rabbitBroker.reliabilitySend(message);
                break;
        }
    }

    @Override
    public void send(Message message, SendCallBack callBack) throws MessageRunTimeException {

    }

    @Override
    public void send(List<Message> messages) throws MessageRunTimeException {

    }
}
