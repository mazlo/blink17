package org.gesis.zl.evaluation.service.query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author matthaeus
 * 
 */
public class QueryShuffleHelper
{

	/**
	 * 
	 * @param availableQueries
	 * @return
	 */
	public File[] multiplyNumberOfQueries( final File[] initialQueries, final int totalNoOfQueries )
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
