package org.gesis.zl.evaluation.service.query;

import java.io.File;
import java.util.Properties;

/**
 * 
 */
public interface QueryShuffleService
{

	/**
	 * 
	 * @return
	 */
	public String[][] shuffle( final int totalNumberOfQueries );

	/**
	 * 
	 * @param properties
	 */
	public void setProperties( final Properties properties );

	/**
	 * 
	 * @param queries
	 */
	public void setQueries( final File[] queries );

}