package org.gesis.zl.evaluation.statistics;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class StatisticsHelperTest
{

	Multimap<String, Long> results = ArrayListMultimap.create();

	@Before
	public void init()
	{
		results.put( "q1", 30l );
		results.put( "q1", 15l );
		results.put( "q1", 40l );
		results.put( "q1", 20l );
		results.put( "q1", 400l );
		results.put( "q1", 5l );
		results.put( "q2", 30l );
		results.put( "q2", 30l );
		results.put( "q2", 3l );
		results.put( "q2", 30l );
		results.put( "q2", 20l );
	}

	@Test
	public void printDetails() throws IOException
	{
		StatisticsHelper.printDetails( "target/results_test.csv", results );
	}

	@Test
	public void printStatistics() throws IOException
	{
		StatisticsHelper.printStatistics( "target/statistics_test.csv", results );
	}
}
