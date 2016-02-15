package org.gesis.zl.evaluation.service.query;

import java.io.File;
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

	private File[] queriesFileList;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gesis.zl.evaluation.service.query.QueryShuffleService#shuffle()
	 */
	public String[][] shuffle( final int totalNumberOfQueries )
	{
		if ( this.queriesFileList == null || this.queriesFileList.length == 0 )
		{
			log.info( "No queries to shuffle, empty query file list" );
			return new String[][] {};
		}

		this.queriesFileList = QueryShuffleHelper.multiplyNumberOfQueries( this.queriesFileList, totalNumberOfQueries );

		Collections.shuffle( Arrays.asList( this.queriesFileList ) );

		return QueryShuffleHelper.mapQueryNameToQuery( this.queriesFileList );
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
	public void setQueries( final File[] queries )
	{
		this.queriesFileList = queries;
	}

}
