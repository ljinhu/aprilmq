package com.jimbolix.april.mq.api;

/**
 * @Description 消费者监听
 * @ClassName MessageListener
 * @Author liruihui
 * @date 2020.04.18 18:47
 */
public interface MessageListener {

    void onMessage(Message message);
}
