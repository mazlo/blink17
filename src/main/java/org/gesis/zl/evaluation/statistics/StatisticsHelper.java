package org.gesis.zl.evaluation.statistics;

import static ch.lambdaj.Lambda.avg;
import static ch.lambdaj.Lambda.max;
import static ch.lambdaj.Lambda.min;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.opencsv.CSVWriter;

/**
 * 
 * @author matthaeus
 * 
 */
public class StatisticsHelper
{
	private static final Logger log = LoggerFactory.getLogger( StatisticsHelper.class );

	/**
	 * 
	 * @param results
	 */
	public static void writeResults( final Multimap<String, Long> results, final EvaluationProperties properties )
	{
		writeResults( getStatisticsFilename( properties, false ), results, false );
		writeResults( getStatisticsFilename( properties, true ), results, true );
	}

	/**
	 * @param results
	 */
	public static void writeResults( final String toFile, final Multimap<String, Long> results, boolean details )
	{
		try
		{
			// print details
			if ( details )
			{
				CSVWriter csvFile = new CSVWriter( new FileWriter( new File( toFile ) ), ';' );

				// print headers
				String[] headers = results.keySet().toArray( new String[] {} );
				csvFile.writeNext( headers );
				csvFile.flush();

				for ( String query : headers )
				{
					Collection<Long> set = results.get( query );
					csvFile.writeNext( set.toArray( new String[] {} ) );
					csvFile.flush();
				}

				csvFile.close();

				return;
			}

			// summed up values
			BufferedWriter statsFile = new BufferedWriter( new FileWriter( new File( toFile ) ) );

			statsFile.write( "query_name;avg_time;max_time;min_time;total_no" );
			statsFile.newLine();

			for ( String key : results.keySet() )
			{
				Collection<Long> set = results.get( key );
				Number avg = avg( set );
				Number max = max( set );
				Number min = min( set );
				statsFile.write( key + ";" + avg + ";" + max + ";" + min + ";" + set.size() );
				statsFile.newLine();
			}

			statsFile.close();
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.info( "Writing results" );
	}

	/**
	 * @param details
	 * @return
	 */
	public static String getStatisticsFilename( final EvaluationProperties properties, boolean details )
	{
		String[] str = new String[] { properties.getStatisticsOutputFilename(), details ? "details" : "", String.valueOf( properties.getQueriesTotal() ), String.valueOf( properties.getThreadPoolSize() ), String.valueOf( System.currentTimeMillis() ) };
		return StringUtils.join( str, "_" );
	}

	/**
	 * 
	 * @param listOfWorkers
	 * @param totalExecutions
	 * @return
	 * @throws InterruptedException
	 */
	public static Multimap<String, Long> collectResults( final List<Future<Long>> listOfWorkers, final String[][] queriesToExecute ) throws InterruptedException
	{
		log.info( "Collecting results" );

		Multimap<String, Long> results = ArrayListMultimap.create();

		for ( int i = 0; i < listOfWorkers.size(); i++ )
		{
			Future<Long> executedTask = listOfWorkers.get( i );
			Long ms;
			try
			{
				ms = executedTask.get();
				results.put( queriesToExecute[i][0], ms );
			}
			catch ( ExecutionException e )
			{
				log.error( "Failed to get value from one Future (worker {})", i );
				e.printStackTrace();
			}
		}

		if ( queriesToExecute.length == 0 )
		{
			log.warn( "No executions (threads) due to empty query list" );
		}

		return results;
	}

}
