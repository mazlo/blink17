package org.gesis.zl.evaluation.statistics;

import static ch.lambdaj.Lambda.avg;
import static ch.lambdaj.Lambda.maxFrom;
import static ch.lambdaj.Lambda.minFrom;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.gesis.zl.evaluation.service.EvaluationProperties;

import com.google.common.collect.Multimap;

/**
 * 
 * @author matthaeus
 * 
 */
public class StatisticsHelper
{

	/**
	 * 
	 * @param results
	 */
	public static void writeResults( final Multimap<String, Long> results, final EvaluationProperties properties )
	{
		writeResults( getStatisticsFilename( properties ), results );
	}

	/**
	 * @param results
	 */
	public static void writeResults( final String toFile, final Multimap<String, Long> results )
	{
		try
		{
			BufferedWriter statsFile = new BufferedWriter( new FileWriter( new File( toFile ) ) );

			statsFile.write( "query_name;avg_time;max_time;min_time;total_no" );
			statsFile.newLine();

			for ( String key : results.keySet() )
			{
				Collection<Long> set = results.get( key );
				Number avg = avg( set );
				Number max = maxFrom( set );
				Number min = minFrom( set );
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
	}

	/**
	 * @return
	 */
	public static String getStatisticsFilename( final EvaluationProperties properties )
	{
		String[] str = new String[] { properties.getEvaluator(), properties.getStatisticsOutputFilename(), String.valueOf( properties.getQueriesTotal() ), String.valueOf( properties.getThreadPoolSize() ), String.valueOf( System.currentTimeMillis() ) };
		return StringUtils.join( str, "_" );
	}

}
