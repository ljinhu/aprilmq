package com.jimbolix.april.mq.api;

/**
 * @Description 消息发送后的处理
 * @ClassName SendCallBack
 * @Author liruihui
 * @date 2020.04.18 18:42
 */
public interface SendCallBack {

    void onSuccess();

    void obFailure();

}
