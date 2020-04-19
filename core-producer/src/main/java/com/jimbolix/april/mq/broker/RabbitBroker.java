package com.jimbolix.april.mq.broker;

import com.jimbolix.april.mq.api.Message;

/**
 * @Description
 * @ClassName RabbitBroker
 * @Author liruihui
 * @date 2020.04.18 19:54
 */
public interface RabbitBroker {

    void rapidSend(Message message);

    void confirmSend(Message message);

    void reliabilitySend(Message message);

}
