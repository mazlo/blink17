package org.gesis.zl.evaluation.service.query;

import org.gesis.zl.evaluation.service.EvaluationProperties;

/**
 * 
 */
public interface QueryShuffleService
{

	/**
	 * Returns a 2-dimensional array, where the 2nd dimension is an array of the
	 * size two. It contains the query name as key and the query itself as
	 * value. For example,<br>
	 * <br>
	 * [ [ "Q1", "SELECT * FROM ..." ], [ "Q2", "SELECT * FROM ..." ], ... ]
	 * 
	 * @return
	 */
	public String[][] shuffle( final String[] queries, final int totalNumberOfQueries );

	/**
	 * 
	 * @param properties
	 */
	public void setProperties( final EvaluationProperties properties );

	/**
	 * Returns the name of this shuffle service instance.
	 * 
	 * @return
	 */
	public String getName();

}