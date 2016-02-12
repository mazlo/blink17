package org.gesis.zl.evaluation.service.query;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author matthaeus
 * 
 */
public class ProbabilityDistributionQueryShuffleService implements QueryShuffleService
{
	private static final Logger log = LoggerFactory.getLogger( ProbabilityDistributionQueryShuffleService.class );

	private Properties properties;

	private File[] queriesFileList;

	/**
	 * 
	 * @param properties
	 */
	public ProbabilityDistributionQueryShuffleService( final Properties properties )
	{
		this.properties = properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gesis.zl.evaluation.service.query.QueryShuffleService#shuffle(int)
	 */
	public String[][] shuffle( final int totalNumberOfQueries )
	{
		if ( this.queriesFileList == null || this.queriesFileList.length == 0 )
		{
			log.error( "Empty query file list" );
			return new String[][] {};
		}

		if ( this.properties == null )
		{
			log.error( "No properties found" );
			return new String[][] {};
		}

		String probabilitiesKey = this.properties.getProperty( "queries.probabilities" );

		//
		if ( noProbabilitiesFound( probabilitiesKey ) )
		{
			log.error( "No probabilities found for property key '{}'", probabilitiesKey );
			return new String[][] {};
		}

		String[] probabilityValues = probabilitiesKey.split( "," );

		//
		if ( this.queriesFileList.length != probabilityValues.length )
		{
			log.error( "Number of queries and probability values is not equal. Queries {}, probabilities {}", this.queriesFileList.length, probabilityValues.length );
			return new String[][] {};
		}

		File[] totalQueriesFileList = new File[totalNumberOfQueries];

		int index = 0;
		while ( index < totalNumberOfQueries )
		{
			double p = Math.random();
			double cumulativeProbability = 0.0;

			for ( int i = 0; i < probabilityValues.length; i++ )
			{
				cumulativeProbability += Double.valueOf( probabilityValues[i] );

				if ( p <= cumulativeProbability )
				{
					totalQueriesFileList[index++] = this.queriesFileList[i];
					break;
				}
			}
		}

		return QueryShuffleHelper.mapQueryNameToQuery( totalQueriesFileList );
	}

	/**
	 * 
	 * @param keyValue
	 * @return
	 */
	private boolean noProbabilitiesFound( final String keyValue )
	{
		if ( StringUtils.isEmpty( keyValue ) )
		{
			return true;
		}

		if ( keyValue.split( "," ).length == 0 )
		{
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gesis.zl.evaluation.service.query.QueryShuffleService#setProperties
	 * (java.util.Properties)
	 */
	public void setProperties( final Properties properties )
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
	public void setQueries( final File[] queries )
	{
		this.queriesFileList = queries;
	}

}
