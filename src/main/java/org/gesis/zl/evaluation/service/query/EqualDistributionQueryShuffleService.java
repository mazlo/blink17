package org.gesis.zl.evaluation.service.query;

import java.util.Arrays;
import java.util.Collections;

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
@Service( "equalDistribution" )
public class EqualDistributionQueryShuffleService implements QueryShuffleService
{
	private static final Logger log = LoggerFactory.getLogger( EqualDistributionQueryShuffleService.class );

	@Autowired
	private EvaluationProperties properties;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gesis.zl.evaluation.service.query.QueryShuffleService#shuffle()
	 */
	@Override
	public String[][] shuffle( final String[] queryFilenamesList, final int multiplier )
	{
		if ( queryFilenamesList == null || queryFilenamesList.length == 0 )
		{
			log.warn( "No queries to shuffle, empty query file list" );
			return new String[][] {};
		}

		String[] totalQueryFilenamesList = QueryShuffleHelper.multiplyNumberOfQueries( queryFilenamesList, multiplier );

		log.info( "Shuffling queries" );

		Collections.shuffle( Arrays.asList( totalQueryFilenamesList ) );

		return QueryShuffleHelper.mapQueryNameToQuery( this.properties.getQueriesFolder(), totalQueryFilenamesList );
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
		return "Equal Distribution Shuffle Service";
	}

}
