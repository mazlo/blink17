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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gesis.zl.evaluation.service.query.QueryShuffleService#shuffle(int)
	 */
	@Override
	public String[][] shuffle( final String[] queryFilenamesList, final int totalNumberOfQueries )
	{
		log.info( "Shuffling queries" );

		if ( queryFilenamesList == null || queryFilenamesList.length == 0 )
		{
			log.error( "No queries to shuffle, empty query file list" );
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

		log.debug( "Found {} probabilities", probabilityValues.length );

		//
		if ( queryFilenamesList.length != probabilityValues.length )
		{
			log.error( "Number of queries and probability values is not equal. Queries {}, probabilities {}", queryFilenamesList.length, probabilityValues.length );
			return new String[][] {};
		}

		String[] totalQueriesFileList = new String[totalNumberOfQueries];

		// the probability algorithm

		int index = 0;
		while ( index < totalNumberOfQueries )
		{
			double p = Math.random();
			double cumulativeProbability = 0.0;

			for ( int i = 0; i < probabilityValues.length; i++ )
			{
				// cumulate until the value is bigger than the randomly chosen p
				// value. this works, because the value of p is equally distributed
				cumulativeProbability += Double.valueOf( probabilityValues[i] );

				if ( p <= cumulativeProbability )
				{
					totalQueriesFileList[index++] = queryFilenamesList[i];
					break;
				}
			}
		}

		return QueryShuffleHelper.mapQueryNameToQuery( this.properties.getQueriesFolder(), totalQueriesFileList );
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
	@Override
	public void setProperties( final EvaluationProperties properties )
	{
		this.properties = properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gesis.zl.evaluation.service.query.QueryShuffleService#getName()
	 */
	@Override
	public String getName()
	{
		return "Probability Distribution Shuffle Service";
	}

}
