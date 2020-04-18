/**
 * 
 */
package com.igp.ep.kafka.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author vaibhav
 */
public class KafkaUtils
{
	private static Logger		logger		= LogManager.getLogger(KafkaUtils.class);
	private static Properties	properties	= new Properties();
	static
	{
		try
		{
			properties.load(KafkaUtils.class.getResourceAsStream("/mq.properties"));
		}
		catch (IOException e)
		{
			logger.error("ERROR in loading  mq.properties : ", e);
		}
	}

	public static String getKafkaTopic()
	{
		return properties.getProperty(KafkaConstants.KAFKA_TOPIC);
	}

	public static String getKafkaZookeeperConnect()
	{
		return properties.getProperty(KafkaConstants.KAFKA_ZOOKEEPER_CONNECT);
	}

	public static String getKafkaGroupId()
	{
		return properties.getProperty(KafkaConstants.KAFKA_GROUP_ID);
	}

	public static String getKafkaBootStrapServers()
	{
		return properties.getProperty(KafkaConstants.KAFKA_BOOTSTRAP_SERVERS);
	}

	public static boolean isAutoCommitEnabled()
	{
		return Boolean.valueOf(properties.getProperty(KafkaConstants.KAFKA_AUTOCOMMIT_IS_ENABLED));
	}

	public static String getAutoCommitInterval()
	{
		return properties.getProperty(KafkaConstants.KAFKA_AUTOCOMMIT_INTERVAL);
	}

	public static byte[] serialize(Object obj)
	{
		byte[] bytes = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try
		{
			byteArrayOutputStream = new ByteArrayOutputStream();
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(obj);
			objectOutputStream.flush();
			bytes = byteArrayOutputStream.toByteArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (objectOutputStream != null)
			{
				try
				{
					objectOutputStream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (byteArrayOutputStream != null)
			{
				try
				{
					byteArrayOutputStream.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return bytes;
	}

	public static Object deserialize(byte[] data)
	{
		Object obj = null;
		ByteArrayInputStream in = null;
		ObjectInputStream is = null;
		try
		{
			in = new ByteArrayInputStream(data);
			is = new ObjectInputStream(in);
			obj = is.readObject();
		}
		catch (ClassNotFoundException | IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return obj;
	}
}
