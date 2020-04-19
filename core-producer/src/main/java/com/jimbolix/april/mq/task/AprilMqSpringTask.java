package com.jimbolix.april.mq.task;

import com.jimbolix.april.mq.broker.RabbitBroker;
import com.jimbolix.april.mq.constant.BrokerMessageStatus;
import com.jimbolix.april.mq.entity.BrokerMessage;
import com.jimbolix.april.mq.service.MessageStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description 可靠性消息投递的补偿任务
 * 如果没有es-job则生效
 * @ClassName AprilMqSpringTask
 * @Author liruihui
 * @date 2020.04.19 15:38
 */
@ConditionalOnExpression("!${april.enable.esjob}")
@Component
@EnableScheduling
@Slf4j
public class AprilMqSpringTask {
    @Autowired
    private MessageStoreService messageStoreService;

    @Autowired
    private RabbitBroker rabbitBroker;

    private static final int MAX_RETRY_COUNT = 3;
    @Scheduled(cron = "0/10 * * * * ?")
    private void configureTasks() {
        List<BrokerMessage> list = messageStoreService.fetchTimeOutMessage4Retry(BrokerMessageStatus.SENDING);
        log.info("--------@@@@@ 定时任务抓取数据集合, 数量：	{} 	@@@@@@-----------" , list.size());
        list.forEach( brokerMessage -> {

            String messageId = brokerMessage.getMessageId();
            if(brokerMessage.getTryCount() >= MAX_RETRY_COUNT) {
                this.messageStoreService.failure(messageId);
                log.warn(" -----消息设置为最终失败，消息ID: {} -------", messageId);
            } else {
                //	每次重发的时候要更新一下try count字段
                this.messageStoreService.updateTryCount(messageId);
                // 	重发消息
                this.rabbitBroker.reliabilitySend(brokerMessage.getMessage());
            }

        });
    }

}
