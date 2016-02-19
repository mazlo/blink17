package org.gesis.zl.evaluation.service.query;

import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author matthaeus
 * 
 */
@Service( "probabilityDistribution" )
public class ProbabilityDistributionQueryShuffleService implements QueryShuffleService
{
	private static final Logger log = LoggerFactory.getLogger( ProbabilityDistributionQueryShuffleService.class );

	@Autowired
	private EvaluationProperties properties;

	private String[] queryFilenamesList;

	/**
	 * required due to spring instantiation
	 */
	public ProbabilityDistributionQueryShuffleService()
	{

	}

	/**
	 * 
	 * @param properties
	 */
	public ProbabilityDistributionQueryShuffleService( final EvaluationProperties properties )
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
		if ( this.queryFilenamesList == null || this.queryFilenamesList.length == 0 )
		{
			log.error( "No queries so shuffle, empty query file list" );
			return new String[][] {};
		}

		if ( this.properties == null )
		{
			log.error( "No properties found" );
			return new String[][] {};
		}

		String[] probabilityValues = this.properties.getQueriesProbabilities();

		//
		if ( noProbabilitiesFound( probabilityValues ) )
		{
			log.error( "No query probabilities found, please specify some" );
			return new String[][] {};
		}

		//
		if ( this.queryFilenamesList.length != probabilityValues.length )
		{
			log.error( "Number of queries and probability values is not equal. Queries {}, probabilities {}", this.queryFilenamesList.length, probabilityValues.length );
			return new String[][] {};
		}

		String[] totalQueriesFileList = new String[totalNumberOfQueries];

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
					totalQueriesFileList[index++] = this.queryFilenamesList[i];
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
	private boolean noProbabilitiesFound( final String[] keyValue )
	{
		if ( keyValue == null )
		{
			return true;
		}

		if ( keyValue.length == 0 )
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
	public void setProperties( final EvaluationProperties properties )
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
	public void setQueries( final String[] queries )
	{
		this.queryFilenamesList = queries;
	}

}
