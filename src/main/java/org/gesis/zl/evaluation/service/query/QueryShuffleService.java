package org.gesis.zl.evaluation.service.query;

import java.io.File;

import org.gesis.zl.evaluation.service.EvaluationProperties;

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
	public void setProperties( final EvaluationProperties properties );

	/**
	 * 
	 * @param queries
	 */
	public void setQueries( final File[] queries );

}