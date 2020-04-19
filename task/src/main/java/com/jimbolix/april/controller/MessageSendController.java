package com.jimbolix.april.controller;

import com.jimbolix.april.mq.api.Message;
import com.jimbolix.april.mq.api.MessageType;
import com.jimbolix.april.mq.broker.impl.ProducerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Description 消息发送测试
 * @ClassName MessageSendController
 * @Author liruihui
 * @date 2020.04.19 17:39
 */
@RestController
@RequestMapping(value = "/message")
public class MessageSendController {
    @Autowired
    private ProducerClient producerClient;
    @RequestMapping("/send")
    public Map<String,Object> sendMessage(){
        Map<String,Object> result = new HashMap<>(1);
        for (int i = 0;i<5;i++){
            Message message = new Message();
            message.setMessageId(UUID.randomUUID().toString());
            message.setMessageType(MessageType.reliability);
            message.setTopic("spring_topic_exchange");
            message.setRoutingKey("spring.test");
            Map<String,Object> map = new HashMap<>();
            map.put("id","i");
            map.put("name","张"+i);
            map.put("age",i);
            message.setAttributes(map);
            producerClient.send(message);

        }
        result.put("code",200);
        return  result;
    }
}
