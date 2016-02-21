package org.gesis.zl.evaluation;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author matthaeus
 * 
 */
public interface Evaluator
{

	/**
	 * 
	 * @param context
	 */
	public abstract void loadBeans( ClassPathXmlApplicationContext context );

	/**
	 * 
	 * @return
	 */
	public abstract void loadQueries();

	/**
	 * 
	 */
	public abstract void debugProperties();

	/**
	 * 
	 * @throws InterruptedException
	 */
	public abstract void execute() throws InterruptedException;

}