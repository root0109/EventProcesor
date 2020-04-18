/**
 * 
 */
package com.igp.ep.processor;

import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import com.igp.ep.bo.Event;
import com.igp.ep.bo.EventType;
import com.igp.ep.bo.UserType;
import com.igp.ep.kafka.utils.KafkaUtils;

/**
 * @author vaibhav
 */
public class EventAdder
{
	public static volatile EventAdder		instance		= null;
	private static String					topic			= "";
	private static Producer<String, byte[]>	kafkaProducer	= null;

	public static EventAdder getInstance()
	{
		if (instance == null)
		{
			synchronized (EventProcessor.class)
			{
				if (instance == null)
				{
					instance = new EventAdder();
				}
			}
		}
		return instance;
	}

	private EventAdder()
	{
		initialize();
	}

	private void initialize()
	{
		topic = KafkaUtils.getKafkaTopic();
		Properties properties = new Properties();
		properties.put("bootstrap.servers", KafkaUtils.getKafkaBootStrapServers());
		properties.put("acks", "all");
		properties.put("retries", 0);
		properties.put("batch.size", 1000);
		properties.put("linger.ms", 1);
		properties.put("buffer.memory", 33554432);
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
		kafkaProducer = new KafkaProducer<String, byte[]>(properties);
	}

	public void send(Event event)
	{
		kafkaProducer.send(new ProducerRecord<String, byte[]>(topic, event.getEventId(), KafkaUtils.serialize(event)));
	}

	/**
	 * DO NOT USE. This is for testing purpose only .
	 */
	public static void main(String[] args)
	{
		EventAdder eventProducer = new EventAdder();
		eventProducer.initialize();
		Thread thread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				int i = 1;
				while (i != 32000)
				{
					Event event = new Event();
					event.setEventId(UUID.randomUUID().toString());
					event.setProductId(new String[] { "asdasdasdasd" });
					event.setProductPosition(new int[] { i });
					event.setProductRank(new int[] { 2 * i });
					event.setCardId(new String[] { UUID.randomUUID().toString() });
					event.setPage("Home Page");
					event.setSection("best under 999");
					event.setEventType(EventType.CLICK);
					event.setTimestamp(new Date());
					event.setUserType(UserType.IGP_USER);
					event.setAttributes(new HashMap<>());
					kafkaProducer
							.send(new ProducerRecord<String, byte[]>(topic, "test" + i, KafkaUtils.serialize(event)));
					System.out.println(i++);
				}

			}
		});
		thread.start();

	}
}
