/**
 * 
 */
package com.igp.ep.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Serializable needed for to conversion to byte
 * 
 * @author vaibhav
 */
public class Event implements Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3307998989780624503L;
	/* Event Id is the unique for every event type generated */
	private String				eventId				= null;
	private String[]			productId			= null;
	private int[]				productPosition		= null;
	private int[]				productRank			= null;
	private String[]			cardId				= null;
	// e.g best under 99
	private String				section				= null;
	// e.g page
	private String				page				= null;
	private EventType			eventType			= EventType.IMPRESSION;
	private Date				timestamp			= null;
	private UserType			userType			= UserType.GUEST;
	private Map<String, String>	attributes			= new HashMap<>();

	/** This is mapping for Db column names **/
	public interface Fields
	{
		public String	EVENT_ID		= "eventId";
		public String	PRODUCT_ID		= "productId";
		public String	PRDUCT_POSITION	= "productPosition";
		public String	PRODUCT_RANK	= "productRank";
		public String	SECTION			= "section";
		public String	PAGE			= "page";
		public String	CARD_ID			= "cardId";
		public String	EVENTTYPE		= "eventType";
		public String	TIMESTAMP		= "timestamp";
		public String	USERTYPE		= "userType";
		public String	ATTRIBUTES		= "attributes";
	}

	/**
	 * @return the eventId
	 */
	public String getEventId()
	{
		return eventId;
	}

	/**
	 * @param eventId
	 *            the eventId to set
	 */
	public void setEventId(String eventId)
	{
		this.eventId = eventId;
	}

	/**
	 * @return the productId
	 */
	public String[] getProductId()
	{
		return productId;
	}

	/**
	 * @param productId
	 *            the productId to set
	 */
	public void setProductId(String[] productId)
	{
		this.productId = productId;
	}

	/**
	 * @return the productPosition
	 */
	public int[] getProductPosition()
	{
		return productPosition;
	}

	/**
	 * @param productPosition
	 *            the productPosition to set
	 */
	public void setProductPosition(int[] productPosition)
	{
		this.productPosition = productPosition;
	}

	/**
	 * @return the productRank
	 */
	public int[] getProductRank()
	{
		return productRank;
	}

	/**
	 * @param productRank
	 *            the productRank to set
	 */
	public void setProductRank(int[] productRank)
	{
		this.productRank = productRank;
	}

	/**
	 * @return the section
	 */
	public String getSection()
	{
		return section;
	}

	/**
	 * @param section
	 *            the section to set
	 */
	public void setSection(String section)
	{
		this.section = section;
	}

	/**
	 * @return the page
	 */
	public String getPage()
	{
		return page;
	}

	/**
	 * @param page
	 *            the page to set
	 */
	public void setPage(String page)
	{
		this.page = page;
	}

	/**
	 * @return the cardId
	 */
	public String[] getCardId()
	{
		return cardId;
	}

	/**
	 * @param cardId
	 *            the cardId to set
	 */
	public void setCardId(String[] cardId)
	{
		this.cardId = cardId;
	}

	/**
	 * @return the eventType
	 */
	public EventType getEventType()
	{
		return eventType;
	}

	/**
	 * @param eventType
	 *            the eventType to set
	 */
	public void setEventType(EventType eventType)
	{
		this.eventType = eventType;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp()
	{
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(Date timestamp)
	{
		this.timestamp = timestamp;
	}

	/**
	 * @return the userType
	 */
	public UserType getUserType()
	{
		return userType;
	}

	/**
	 * @param userType
	 *            the userType to set
	 */
	public void setUserType(UserType userType)
	{
		this.userType = userType;
	}

	/**
	 * @return the attributes
	 */
	public Map<String, String> getAttributes()
	{
		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(Map<String, String> attributes)
	{
		this.attributes = attributes;
	}
}
