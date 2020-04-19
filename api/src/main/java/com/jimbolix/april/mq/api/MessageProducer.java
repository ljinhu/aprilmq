package com.jimbolix.april.mq.api;

import com.jimbolix.april.mq.exception.MessageRunTimeException;

import java.util.List;

/**
 * @Description 消息生产者
 * @ClassName MessageProducer
 * @Author liruihui
 * @date 2020.04.18 18:42
 */
public interface MessageProducer {

    void send(Message message)throws MessageRunTimeException;

    /**
     *
     * @param message
     * @param callBack 回调业务
     * @throws MessageRunTimeException
     */
    void send(Message message,SendCallBack callBack)throws MessageRunTimeException;

    void send(List<Message> messages)throws MessageRunTimeException;
}
