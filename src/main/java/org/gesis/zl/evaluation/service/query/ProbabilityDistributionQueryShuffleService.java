package org.gesis.zl.evaluation.service.query;

import java.io.File;
import java.util.Properties;

/**
 * 
 * @author matthaeus
 * 
 */
public class ProbabilityDistributionQueryShuffleService implements QueryShuffleService
{

	private Properties properties;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gesis.zl.evaluation.service.query.QueryShuffleService#read(java.lang
	 * .String, java.lang.String[])
	 */
	public File[] read( String fromFolder, String[] availableQueries )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gesis.zl.evaluation.service.query.QueryShuffleService#shuffle(int)
	 */
	public String[][] shuffle( int totalNumberOfQueries )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gesis.zl.evaluation.service.query.QueryShuffleService#setProperties
	 * (java.util.Properties)
	 */
	public void setProperties( Properties properties )
	{
		this.properties = properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gesis.zl.evaluation.service.query.QueryShuffleService#setQueries(
	 * java.io.File[])
	 */
	public void setQueries( File[] queries )
	{
		// TODO Auto-generated method stub

	}

}
