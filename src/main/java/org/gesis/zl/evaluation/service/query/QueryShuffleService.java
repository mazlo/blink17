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
	 * @param fromFolder
	 * @param availableQueries
	 * @return
	 */
	public File[] read( final String fromFolder, final String[] availableQueries );
	
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

}