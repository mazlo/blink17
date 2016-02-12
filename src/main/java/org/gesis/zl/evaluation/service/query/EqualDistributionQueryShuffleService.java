package org.gesis.zl.evaluation.service.query;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author matthaeus
 * 
 */
public class EqualDistributionQueryShuffleService implements QueryShuffleService
{
	private static final Logger log = LoggerFactory.getLogger( EqualDistributionQueryShuffleService.class );

	private Properties properties;

	private File[] queriesFileList;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gesis.zl.evaluation.service.query.QueryShuffleService#shuffle()
	 */
	public String[][] shuffle( final int totalNumberOfQueries )
	{
		if ( this.queriesFileList == null || this.queriesFileList.length == 0 )
			return new String[][] {};

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
	public void setProperties( Properties properties )
	{
		this.properties = properties;
	}

}
