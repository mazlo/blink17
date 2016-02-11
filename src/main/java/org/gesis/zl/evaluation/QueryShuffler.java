package org.gesis.zl.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryShuffler
{
	private static final Logger log = LoggerFactory.getLogger( QueryShuffler.class );

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

	/**
	 * 
	 * @param fromFolder
	 * @return
	 */
	public static String[][] readAndShuffle( final String fromFolder, final int multiplyNumberOfQueries )
	{
		File[] filenamesList = null;

		filenamesList = getQueries( fromFolder );
		filenamesList = multiplyNumberOfQueries( filenamesList, multiplyNumberOfQueries );

		Collections.shuffle( Arrays.asList( filenamesList ) );

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
	 * @param fromFolder
	 * @return
	 */
	public static File[] getQueries( final String fromFolder )
	{
		File queryFolder = new File( fromFolder );

		if ( !queryFolder.exists() )
		{
			log.error( "Folder {} does not exist", fromFolder );
			return new File[] {};
		}

		File[] filenamesList = queryFolder.listFiles();

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
	public static File[] multiplyNumberOfQueries( final File[] initialQueries, final int multiplier )
	{
		if ( multiplier <= 1 )
			return initialQueries;

		int arrayLength = multiplier * initialQueries.length;

		File[] multipliedQueries = new File[arrayLength];

		int index = 0;
		for ( int i = 0; i < multiplier; i++ )
		{
			for ( int j = 0; j < initialQueries.length; j++ )
			{
				multipliedQueries[index++] = initialQueries[j];
			}
		}

		return multipliedQueries;
	}

}
