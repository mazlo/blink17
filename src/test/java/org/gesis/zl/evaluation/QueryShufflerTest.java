package org.gesis.zl.evaluation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

public class QueryShufflerTest
{

	@Test
	public void getQueries()
	{
		File[] queries = QueryShuffler.getQueries( "queries-mysql" );
		assertNotNull( queries );
		assertEquals( 36, queries.length );
	}

	@Test
	public void multiplyNumberOfQueries()
	{
		File[] queries = QueryShuffler.getQueries( "queries-mysql" );
		File[] multipliedQueries = QueryShuffler.multiplyNumberOfQueries( queries, 3 );
		assertNotNull( multipliedQueries );
		assertEquals( 108, multipliedQueries.length );

		for ( File file : multipliedQueries )
		{
			assertNotNull( file );
		}
	}

	@Test
	public void readAndShuffle()
	{
		String[][] readAndShuffledQueries = QueryShuffler.readAndShuffle( "queries-mysql", 3 );
		assertNotNull( readAndShuffledQueries );
		assertEquals( 108, readAndShuffledQueries.length );
	}

	@Test
	@Ignore
	public void write() throws IOException
	{
		String[][] shuffledQueries = QueryShuffler.readAndShuffle( "queries-mysql", 25 );
		QueryShuffler.writeShuffledQueries( shuffledQueries, "src/main/resources/queries-mysql.properties" );
	}
}
