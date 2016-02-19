package org.gesis.zl.evaluation.service.query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
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

	/**
	 * 
	 * @return
	 */
	public static File[] read( final String fromFile )
	{
		File queriesFile = new File( fromFile );

		return null;
	}

	/**
	 * 
	 * @param fromFolder
	 * @param fileType
	 * @param availableQueries
	 * @return
	 */
	public static String[] read( final String fromFolder, final String fileType, final String... availableQueries )
	{
		File queryFolder = new File( fromFolder );

		if ( !queryFolder.exists() )
		{
			log.error( "Folder {} does not exist", fromFolder );
			return new String[] {};
		}

		String[] filenamesList = null;

		if ( availableQueries == null || availableQueries.length == 0 )
		{
			// all files in folder
			filenamesList = queryFolder.list();
		}
		else
		{
			// filter files in folder
			filenamesList = queryFolder.list( new FilenameFilter()
			{
				public boolean accept( final File file, final String filename )
				{
					for ( String filenameAvailable : availableQueries )
					{
						if ( StringUtils.equals( filename, filenameAvailable.concat( fileType ) ) )
						{
							return true;
						}

						continue;
					}

					return false;
				}
			} );
		}

		if ( filenamesList.length == 0 )
		{
			log.warn( "No queries in folder {}", fromFolder );
			return new String[] {};
		}

		return filenamesList;
	}

	/**
	 * 
	 * @param availableQueries
	 * @return
	 */
	public static String[] multiplyNumberOfQueries( final String[] initialQueries, final int totalNoOfQueries )
	{
		if ( totalNoOfQueries <= 1 )
		{
			return initialQueries;
		}

		String[] multipliedQueries = new String[totalNoOfQueries];

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
	public static String[][] mapQueryNameToQuery( final String[] filenamesList )
	{
		String[][] queries = new String[filenamesList.length][2];

		for ( int i = 0; i < filenamesList.length; i++ )
		{
			String queryFilename = filenamesList[i];

			queries[i][0] = queryFilename;

			BufferedReader reader;
			try
			{
				reader = new BufferedReader( new FileReader( queryFilename ) );

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
		{
			return;
		}

		FileWriter writer = new FileWriter( toFile );

		for ( String[] query : shuffledQueries )
		{
			writer.write( query[0] + "=" + query[1] + "\n" );
		}

		writer.flush();
		writer.close();
	}

}
