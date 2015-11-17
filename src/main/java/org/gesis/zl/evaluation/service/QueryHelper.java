package org.gesis.zl.evaluation.service;

import static ch.lambdaj.Lambda.avg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.SetMultimap;

/**
 * @author matthaeus
 * 
 */
@Service
public class QueryHelper
{

	@Autowired
	private EvaluationProperties properties;

	/**
	 * @return
	 */
	public String[][] shuffleQueriesToExecute( int queue_length )
	{
		String[][] availableQueryList = this.getQueries();
		String[][] queryQueue = new String[queue_length][2];

		if ( availableQueryList.length == 0 )
			return new String[][] {};

		Random random = new Random();

		for ( int i = 0; i < queue_length; i++ )
		{
			int nextQuery = random.nextInt( availableQueryList.length );
			queryQueue[i][0] = availableQueryList[nextQuery][0];
			queryQueue[i][1] = availableQueryList[nextQuery][1];
		}

		return queryQueue;
	}

	/**
	 * @return
	 */
	public String[][] getQueries()
	{
		File queriesFolder = new File( properties.getQueriesFolder() );

		if ( !queriesFolder.exists() )
			return new String[][] {};

		File[] files = queriesFolder.listFiles();

		if ( files.length == 0 )
			return new String[][] {};

		String[][] queries = new String[files.length][2];

		for ( int i = 0; i < files.length; i++ )
		{
			File queryFile = files[i];

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
	 * @param results
	 */
	public void writeResults( String outputFile, SetMultimap<String, Long> results )
	{
		try
		{
			BufferedWriter statsFile = new BufferedWriter( new FileWriter( new File( outputFile ) ) );

			statsFile.write( "query_name;time_in_ms" );
			statsFile.newLine();

			for ( String key : results.keySet() )
			{
				Set<Long> set = results.get( key );
				Number sum = avg( set );
				statsFile.write( key + ";" + sum + ";" + set.size() );
				statsFile.newLine();
			}

			statsFile.close();
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
