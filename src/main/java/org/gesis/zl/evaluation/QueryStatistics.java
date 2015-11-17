package org.gesis.zl.evaluation;

/**
 * @author matthaeus
 * 
 */
public class QueryStatistics
{
	private String queryKey;
	private int queryExecutionCounter = 0;
	private long time;

	public String getQueryKey()
	{
		return queryKey;
	}

	public void setQueryKey( String queryKey )
	{
		this.queryKey = queryKey;
	}

	public int getQueryExecutionCounter()
	{
		return queryExecutionCounter;
	}

	public void setQueryExecutionCounter( int queryExecutionCounter )
	{
		this.queryExecutionCounter = queryExecutionCounter;
	}

	public void increaseQueryExecutionCounter()
	{
		this.queryExecutionCounter++;
	}

	public long getTime()
	{
		return time;
	}

	public void setTime( long time )
	{
		this.time = time;
	}

}
