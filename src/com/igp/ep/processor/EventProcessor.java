/**
 * 
 */
package com.igp.ep.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.google.gson.Gson;
import com.igp.common.util.ExecutorProcessor;
import com.igp.common.util.ExecutorProcessorParams;
import com.igp.ep.bo.Event;
import com.igp.ep.bo.Event.Fields;
import com.igp.ep.cassandra.utils.CassandraUtils;
import com.igp.ep.kafka.utils.KafkaUtils;

/**
 * @author vaibhav
 */
public class EventProcessor extends ExecutorProcessor<List<Event>>
{
	private static Logger					logger				= LogManager.getLogger(EventProcessor.class);
	private volatile static EventProcessor	instance			= null;
	private static String					topic				= "";
	/** Kafka **/
	KafkaConsumer<String, byte[]>			consumer			= null;
	/** Casssandra **/
	private Cluster							cluster				= null;
	private Session							session				= null;
	private PreparedStatement				preparedStatement	= null;

	public static EventProcessor getInstance()
	{
		if (instance == null)
		{
			synchronized (EventProcessor.class)
			{
				if (instance == null)
				{
					// default initialization with 1 producer and 1 consumer
					ExecutorProcessorParams executorProcessorParams = new ExecutorProcessorParams(
							new LinkedBlockingQueue<>(), 10, EventProcessor.class.getName());
					instance = new EventProcessor(executorProcessorParams);
				}
			}
		}
		return instance;
	}

	private EventProcessor(ExecutorProcessorParams executorProcessorParams)
	{
		super(executorProcessorParams);
		initKafka();
		initCassandra();
	}

	private void initKafka()
	{
		Properties properties = new Properties();
		properties.put("bootstrap.servers", KafkaUtils.getKafkaBootStrapServers());
		properties.put("group.id", KafkaUtils.getKafkaGroupId());
		properties.put("enable.auto.commit", KafkaUtils.isAutoCommitEnabled() + "");
		properties.put("auto.commit.interval.ms", KafkaUtils.getAutoCommitInterval());
		properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		properties.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");
		consumer = new KafkaConsumer<>(properties);
		topic = KafkaUtils.getKafkaTopic();
	}

	private void initCassandra()
	{
		cluster = Cluster.builder().addContactPoint(CassandraUtils.getCassandraContactPoint()).build();
		Metadata metadata = cluster.getMetadata();
		session = cluster.connect(CassandraUtils.getNewCassandraKeyspace());
		logger.debug("Cassandra Initialized. Properties Loaded for :" + metadata.getClusterName());
		preparedStatement = session.prepare("INSERT INTO EventCollection (" + Fields.EVENT_ID + "," + Fields.PRODUCT_ID
				+ "," + Fields.PRDUCT_POSITION + "," + Fields.PRODUCT_RANK + "," + Fields.SECTION + "," + Fields.PAGE
				+ "," + Fields.CARD_ID + "," + Fields.EVENTTYPE + "," + Fields.TIMESTAMP + "," + Fields.USERTYPE + ","
				+ Fields.ATTRIBUTES + ") VALUES (?,?,?,?,?,?,?,?,?,?,?);");
	}

	@Override
	protected List<List<Event>> produceObject() throws Exception
	{
		List<List<Event>> eventsList = new ArrayList<>();
		List<Event> events = new ArrayList<>();
		eventsList.add(events);
		consumer.subscribe(Arrays.asList(topic));
		ConsumerRecords<String, byte[]> consumerRecords = consumer.poll(1000);
		for (ConsumerRecord<String, byte[]> consumerRecord : consumerRecords)
		{
			events.add((Event) KafkaUtils.deserialize(consumerRecord.value()));
		}
		logger.debug("Recieved  Total No of Events : " + events.size());
		return eventsList;
	}

	@Override
	protected boolean consumeObject(List<Event> events) throws Exception
	{
		try
		{
			BoundStatement boundStatement = new BoundStatement(preparedStatement);
			BatchStatement batchStatement = new BatchStatement();
			for (Event event : events)
			{
				for (int i = 0; i < event.getProductId().length; i++)
				{
					batchStatement.add(boundStatement.bind(event.getEventId(), event.getProductId()[i],
							event.getProductPosition()[i], event.getProductRank()[i], event.getSection(),
							event.getPage(), event.getCardId()[i], event.getEventType().name(), event.getTimestamp(),
							event.getUserType().name(), new Gson().toJson(event.getAttributes())));
				}
			}
			session.execute(batchStatement);
			logger.debug(" Total No of Events Data Dumped into Cassandra DB : " + events.size());
		}
		catch (Exception e)
		{
			logger.error("Unable to insert data into Cassandra DB: ", e);
		}
		return true;
	}

	@Override
	protected void handleCancelled(List<List<Event>> eventList) throws Exception
	{
		logger.error("Handle Cancelled , not required");
	}

	@Override
	protected void handleFailed(List<Event> eventList, Throwable throwable) throws Exception
	{
		logger.error("Handle Failed , not required");
	}
}
