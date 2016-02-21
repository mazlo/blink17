package org.gesis.zl.evaluation.statistics;

import static ch.lambdaj.Lambda.avg;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import com.google.common.collect.Multimap;

public class StatisticsHelper
{

	/**
	 * @param results
	 */
	public static void writeResults( final String toFile, final Multimap<String, Long> results )
	{
		try
		{
			BufferedWriter statsFile = new BufferedWriter( new FileWriter( new File( toFile ) ) );

			statsFile.write( "query_name;time_in_ms" );
			statsFile.newLine();

			for ( String key : results.keySet() )
			{
				Collection<Long> set = results.get( key );
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
