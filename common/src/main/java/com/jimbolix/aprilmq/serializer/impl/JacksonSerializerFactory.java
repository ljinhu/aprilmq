package com.jimbolix.aprilmq.serializer.impl;


import com.jimbolix.april.mq.api.Message;
import com.jimbolix.aprilmq.serializer.Serializer;
import com.jimbolix.aprilmq.serializer.SerializerFactory;
import org.springframework.stereotype.Component;

@Component
public class JacksonSerializerFactory implements SerializerFactory {

	public static final SerializerFactory INSTANCE = new JacksonSerializerFactory();
	
	@Override
	public Serializer create() {
		return JacksonSerializer.createParametricType(Message.class);
	}

}
