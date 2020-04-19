package com.jimbolix.aprilmq.convert;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * 	$RabbitMessageConverter
 * @author Alienware
 *
 */
public class RabbitMessageConverter implements MessageConverter {

	private GenericMessageConverter delegate;
	
//	private final String delaultExprie = String.valueOf(24 * 60 * 60 * 1000);
	
	public RabbitMessageConverter(GenericMessageConverter genericMessageConverter) {
		Preconditions.checkNotNull(genericMessageConverter);
		this.delegate = genericMessageConverter;
	}
	
	@Override
	public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
//		messageProperties.setExpiration(delaultExprie);
		com.jimbolix.april.mq.api.Message message = (com.jimbolix.april.mq.api.Message) object;
		messageProperties.setDelay(message.getDelayMills());
		return this.delegate.toMessage(object, messageProperties);
	}

	@Override
	public Object fromMessage(Message message) throws MessageConversionException {
		com.jimbolix.april.mq.api.Message msg = (com.jimbolix.april.mq.api.Message) this.delegate.fromMessage(message);
		return msg;
	}

}
