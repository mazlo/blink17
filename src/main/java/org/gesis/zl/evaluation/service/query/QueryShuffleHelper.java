package org.gesis.zl.evaluation.service.query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author matthaeus
 * 
 */
public class QueryShuffleHelper
{

	private static final Logger log = LoggerFactory.getLogger( QueryShuffleHelper.class );

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gesis.zl.evaluation.service.query.QueryShuffleService#read(java.lang
	 * .String, java.lang.String[])
	 */
	public static File[] read( final String fromFolder, final String[] availableQueries )
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

		return filenamesList;
	}

	/**
	 * 
	 * @param availableQueries
	 * @return
	 */
	public static File[] multiplyNumberOfQueries( final File[] initialQueries, final int totalNoOfQueries )
	{
		if ( totalNoOfQueries <= 1 )
			return initialQueries;

		File[] multipliedQueries = new File[totalNoOfQueries];

		int index = 0;
		for ( int i = 0; i < totalNoOfQueries; i++ )
		{
			for ( int j = 0; j < initialQueries.length && index < totalNoOfQueries; j++ )
			{
				multipliedQueries[index++] = initialQueries[j];
			}
		}

		return multipliedQueries;
	}

	/**
	 * 
	 * @param filenamesList
	 * @return
	 */
	public static String[][] mapQueryNameToQuery( File[] filenamesList )
	{
		String[][] queries = new String[filenamesList.length][2];

		for ( int i = 0; i < filenamesList.length; i++ )
		{
			File queryFile = filenamesList[i];

			queries[i][0] = queryFile.getName();

			BufferedReader reader;
			try
			{
				reader = new BufferedReader( new FileReader( queryFile ) );

				String query = reader.readLine();
				queries[i][1] = query.trim();
			}
			catch ( FileNotFoundException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch ( IOException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return queries;
	}

	/**
	 * 
	 * @param shuffledQueries
	 * @param toFile
	 * @throws IOException
	 */
	public static void writeShuffledQueries( final String[][] shuffledQueries, final String toFile ) throws IOException
	{
		if ( shuffledQueries.length == 0 )
			return;

		FileWriter writer = new FileWriter( toFile );

		for ( String[] query : shuffledQueries )
		{
			writer.write( query[0] + "=" + query[1] + "\n" );
		}

		writer.flush();
		writer.close();
	}

}
