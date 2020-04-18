/**
 * 
 */
package com.igp.ep.servlets;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.igp.ep.bo.Event;
import com.igp.ep.bo.EventType;
import com.igp.ep.bo.UserType;
import com.igp.ep.processor.EventAdder;

/**
 * @author vaibhav
 */
public class EventCaptureServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1158909557595547335L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		/*
		 * Product position , rank & id will have equal no of entries
		 */
		Event event = new Event();
		event.setEventId(UUID.randomUUID().toString());
		event.setProductId(request.getParameterValues("productId"));
		event.setProductPosition(
				Arrays.stream(request.getParameterValues("productPosition")).mapToInt(Integer::parseInt).toArray());

		String[] productRank = request.getParameterValues("productRank");
		event.setPage(request.getParameter("page"));

		if (productRank != null & productRank.length > 0)
			event.setProductRank(Arrays.stream(productRank).mapToInt(Integer::parseInt).toArray());
		// card id will be null for impressions it will be receieved incase of
		// clicks only
		String[] cardId = request.getParameterValues("cardId");
		if (cardId != null && cardId.length > 0)
			event.setCardId(cardId);
		String section = request.getParameter("section");
		if (section != null)
			event.setSection(section);
		event.setEventType(EventType.valueOf(getString(request, "eventType", EventType.IMPRESSION.name())));
		event.setTimestamp(new Date());
		event.setUserType(UserType.valueOf(getString(request, "userType", UserType.GUEST.name())));

		String[] keys = request.getParameterValues("attributeKey");
		String[] values = request.getParameterValues("attributeValue");
		Map<String, String> attributes = new HashMap<>();
		// attribute key and value size should be same
		for (int i = 0; i < keys.length; i++)
		{
			attributes.put(keys[i], values[i]);
		}

		event.setAttributes(attributes);
		// send this event object to kafka producer
		EventAdder.getInstance().send(event);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

	/**
	 * @param key
	 * @param size
	 * @return String
	 */
	public String getString(HttpServletRequest request, String key, String defaultValue)
	{
		String value = request.getParameter(key);
		if (value == null)
		{
			return defaultValue;
		}
		return value;
	}

}
