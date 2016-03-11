package org.gesis.zl.evaluation.service.query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

/**
 * 
 * @author matthaeus
 * 
 */
public class QueryShuffleHelper
{

	private static final Logger log = LoggerFactory.getLogger( QueryShuffleHelper.class );

	/**
	 * Expects the <i>fromFile</i> to contain a list of queries on a per line
	 * basis.
	 * 
	 * @return
	 */
	public static String[][] readFromFile( final String inFolder, final File fromFile, final String fileType )
	{
		log.info( "Reading queries specified in file '{}'", fromFile.getPath() );

		List<String> queryFilenamesList = Lists.newArrayList();

		BufferedReader reader;
		try
		{
			reader = new BufferedReader( new FileReader( fromFile ) );

			String queryFilename = null;
			int index = 0;
			while ( StringUtils.isNotEmpty( queryFilename = reader.readLine() ) )
			{
				queryFilenamesList.add( queryFilename.concat( fileType ) );
				log.debug( "bucket[{}] -> {}", index++, queryFilename );
			}

			reader.close();
		}
		catch ( FileNotFoundException e )
		{
			log.error( "'{}' does not exist", fromFile.getPath() );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}

		log.info( "Found {} queries", queryFilenamesList.size() );

		return mapQueryNameToQuery( inFolder, queryFilenamesList.toArray( new String[] {} ) );
	}

	/**
	 * 
	 * @param fromFolder
	 * @param fileType
	 * @param availableQueries
	 * @return
	 */
	public static String[] readFromProperties( final String fromFolder, final String fileType, final String... availableQueries )
	{
		log.info( "Reading queries according to property file" );

		File queryFolder = new File( fromFolder );

		if ( !queryFolder.exists() )
		{
			log.error( "Folder {} does not exist", fromFolder );
			return new String[] {};
		}

		log.debug( "Folder holding queries is '{}'", queryFolder.getPath() );

		String[] filenamesList = null;

		if ( availableQueries == null || availableQueries.length == 0 )
		{
			log.debug( "Using all queries in folder (property 'queries.available' not set)" );

			// all files in folder
			filenamesList = queryFolder.list();

			log.debug( "Found {} queries in query folder", filenamesList.length );
		}
		else
		{
			log.debug( "Filtering queries according to property (property 'queries.avaibable' = {})", availableQueries.length );

			// filter files in folder
			filenamesList = queryFolder.list( new FilenameFilter()
			{
				@Override
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
			log.warn( "No queries in folder '{}'", fromFolder );
			return new String[] {};
		}

		log.info( "Found {} queries", filenamesList.length );

		return filenamesList;
	}

	/**
	 * 
	 * @param availableQueries
	 * @return
	 */
	public static String[] multiplyNumberOfQueries( final String[] initialQueries, final int totalNoOfQueries )
	{
		log.info( "Multiplying queries" );

		if ( totalNoOfQueries <= 1 )
		{
			return initialQueries;
		}

		log.info( "initial {} -> total {}", initialQueries.length, totalNoOfQueries );

		String[] multipliedQueries = new String[totalNoOfQueries];

		int index = 0;
		for ( int i = 0; i < totalNoOfQueries; i++ )
		{
			for ( int j = 0; j < initialQueries.length && index < totalNoOfQueries; j++ )
			{
				multipliedQueries[index++] = initialQueries[j];
				log.debug( "bucket[{}] -> {}", index, initialQueries[j] );
			}
		}

		return multipliedQueries;
	}

	/**
	 * 
	 * @param filenamesList
	 * @return
	 */
	public static String[][] mapQueryNameToQuery( final String inFolder, final String[] filenamesList )
	{
		log.info( "Mapping actual queries to query names" );

		String[][] queries = new String[filenamesList.length][2];

		for ( int i = 0; i < filenamesList.length; i++ )
		{
			String queryFilename = filenamesList[i];

			queries[i][0] = queryFilename;

			BufferedReader reader;
			try
			{
				reader = new BufferedReader( new FileReader( inFolder + "/" + queryFilename ) );

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
	public static void writeToFile( final String[][] shuffledQueries, final String toFile )
	{
		if ( shuffledQueries.length == 0 )
		{
			return;
		}

		File file = new File( toFile );
		FileWriter writer = null;

		try
		{
			if ( !file.exists() )
			{
				log.info( "Creating file in '{}', because file does not exist", toFile );
				file.createNewFile();
			}

			writer = new FileWriter( file );

			for ( String[] query : shuffledQueries )
			{
				writer.write( query[0].substring( 0, query[0].indexOf( "." ) ) + "\n" );
			}

			writer.flush();
			writer.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();

			if ( writer != null )
			{
				try
				{
					writer.close();
				}
				catch ( IOException e1 )
				{
					e1.printStackTrace();
				}
			}
		}
	}

}
