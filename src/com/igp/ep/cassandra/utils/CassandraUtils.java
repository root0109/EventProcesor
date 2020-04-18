/**
 * 
 */
package com.igp.ep.cassandra.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author vaibhav
 */
public class CassandraUtils
{
	private static Logger		logger		= LogManager.getLogger(CassandraUtils.class);
	private static Properties	properties	= new Properties();
	static
	{
		try
		{
			properties.load(CassandraUtils.class.getResourceAsStream("/db.properties"));
		}
		catch (IOException e)
		{
			logger.error("ERROR in loading  db.properties : ", e);
		}
	}

	public static String getCassandraContactPoint()
	{
		return properties.getProperty(CassandraConstants.CASSANDRA_CONTACT_POINT);
	}

	public static String getNewCassandraKeyspace()
	{
		return properties.getProperty(CassandraConstants.CASSANDRA_KEYSPACE);
	}
}
