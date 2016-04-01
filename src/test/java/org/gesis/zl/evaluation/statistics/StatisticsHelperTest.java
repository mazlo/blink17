package org.gesis.zl.evaluation.statistics;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class StatisticsHelperTest
{

	@Before
	public void init()
	{

	}

	@Test
	public void printDetails() throws IOException
	{
		Multimap<String, Long> results = ArrayListMultimap.create();

		results.put( "q1", 30l );
		results.put( "q1", 15l );
		results.put( "q1", 40l );
		results.put( "q1", 5l );
		results.put( "q2", 30l );
		results.put( "q2", 30l );
		results.put( "q2", 20l );

		StatisticsHelper.printDetails( "target/results_test.csv", results );
	}

	@Test
	public void printStatistics() throws IOException
	{
		Multimap<String, Long> results = ArrayListMultimap.create();

		results.put( "q1", 30l );
		results.put( "q1", 15l );
		results.put( "q1", 40l );
		results.put( "q1", 5l );
		results.put( "q2", 30l );
		results.put( "q2", 30l );
		results.put( "q2", 20l );

		StatisticsHelper.printStatistics( "target/statistics_test.csv", results );
	}
}
