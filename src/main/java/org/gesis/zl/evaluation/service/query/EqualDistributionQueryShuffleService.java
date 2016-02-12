package org.gesis.zl.evaluation.service.query;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
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
	 * @see
	 * org.gesis.zl.evaluation.service.query.QueryShuffleService#read(java.lang
	 * .String, java.lang.String[])
	 */
	public File[] read( final String fromFolder, final String[] availableQueries )
	{
		File queryFolder = new File( fromFolder );

		if ( !queryFolder.exists() )
		{
			log.error( "Folder {} does not exist", fromFolder );
			return new File[] {};
		}

		File[] filenamesList = null;

		if ( availableQueries == null || availableQueries.length == 0 )
		{
			filenamesList = queryFolder.listFiles();
		}
		else
		{
			filenamesList = queryFolder.listFiles( new FileFilter()
			{
				public boolean accept( File currentFile )
				{
					for ( String filename : availableQueries )
					{
						if ( StringUtils.equals( currentFile.getName(), filename ) )
							return true;

						continue;
					}

					return false;
				}
			} );
		}

		if ( filenamesList.length == 0 )
		{
			log.warn( "No queries in folder {}", fromFolder );
			return new File[] {};
		}

		this.queriesFileList = filenamesList;

		return filenamesList;
	}

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
