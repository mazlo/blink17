package org.gesis.zl.evaluation.statistics;

import static ch.lambdaj.Lambda.avg;
import static ch.lambdaj.Lambda.max;
import static ch.lambdaj.Lambda.min;

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

import com.csvreader.CsvWriter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

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
		String filename = getStatisticsFilename( properties );

		try
		{
			log.info( "Writing results" );
			printDetails( filename, results );
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try
		{
			log.info( "Writing statistics" );
			printStatistics( "statistics_" + filename, results );
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param toFile
	 * @param results
	 * @throws IOException
	 */
	public static void printStatistics( String toFile, final Multimap<String, Long> results ) throws IOException
	{
		// print summed up values
		CsvWriter csvFile = new CsvWriter( new FileWriter( new File( toFile ) ), ';' );

		// print headers
		String[] headers = new String[] { "query_name", "avg_time", "max_time", "min_time", "total_no" };
		csvFile.writeRecord( headers );
		csvFile.flush();

		for ( String query : results.keySet() )
		{
			Collection<Long> set = results.get( query );

			Number avg = avg( set );
			Number max = max( set );
			Number min = min( set );

			csvFile.writeRecord( new String[] { query, avg.toString(), max.toString(), min.toString(), String.valueOf( set.size() ) } );
			csvFile.flush();
		}

		csvFile.close();
	}

	/**
	 * 
	 * @param results
	 * @throws IOException
	 */
	public static void printDetails( String toFile, final Multimap<String, Long> results ) throws IOException
	{
		// print details
		CsvWriter csvFile = new CsvWriter( new FileWriter( new File( toFile ) ), ';' );

		for ( String query : results.keySet() )
		{
			Collection<Long> set = results.get( query );

			String[] valuesToPrint = getRowOfValues( query, set );

			csvFile.writeRecord( valuesToPrint );
			csvFile.flush();
		}

		csvFile.close();

		return;
	}

	/**
	 * 
	 * @param set
	 * @return
	 */
	private static String[] getRowOfValues( String firstValue, Collection<Long> set )
	{
		Long[] values = set.toArray( new Long[] {} );
		String[] valuesToPrint = new String[values.length + 1];

		valuesToPrint[0] = firstValue;

		for ( int i = 1; i < values.length; i++ )
		{
			valuesToPrint[i] = String.valueOf( values[i - 1] );
		}

		return valuesToPrint;
	}

	/**
	 * @param details
	 * @return
	 */
	public static String getStatisticsFilename( final EvaluationProperties properties )
	{
		String[] str = new String[] { properties.getStatisticsOutputFilename(), String.valueOf( properties.getQueriesTotal() ), String.valueOf( properties.getThreadPoolSize() ), String.valueOf( System.currentTimeMillis() ), ".csv" };
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
