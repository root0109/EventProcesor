/**
 * 
 */
package com.igp.ep.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.igp.ep.processor.EventProcessor;

/**
 * @author vaibhav
 */
public class InitServlet extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 286581719764784626L;

	@Override
	public void init() throws ServletException
	{
		super.init();
		EventProcessor.getInstance().start();
	}
}
