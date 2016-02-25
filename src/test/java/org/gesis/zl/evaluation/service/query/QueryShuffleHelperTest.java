package org.gesis.zl.evaluation.service.query;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class QueryShuffleHelperTest
{

	private boolean fileExists;

	@Before
	public void init()
	{
		File distributionFile = new File( "queries/equalDistribution.txt" );

		distributionFile.exists();
	}

	@Test
	public void readFromFile_fails()
	{
		String[][] filenamesList = QueryShuffleHelper.readFromFile( null, new File( "queries/does not exist.txt" ), ".sql" );
		assertNotNull( filenamesList );
		assertTrue( filenamesList.length == 0 );
	}

	@Test
	public void readFromFile()
	{
		String[][] filenamesList = QueryShuffleHelper.readFromFile( "queries-mysql", new File( "queries/equalDistribution.txt" ), ".sql" );
		assertNotNull( filenamesList );

		if ( this.fileExists )
		{
			assertTrue( filenamesList.length == 4 );
		}
	}

	@Test
	public void multiplyNumberOfQueries()
	{
		String[] multipliedQueries = QueryShuffleHelper.multiplyNumberOfQueries( new String[] { "dp1.sql", "dp2.sql", "dp3.sql", "dp4.sql" }, 20 );
		assertNotNull( multipliedQueries );
		assertThat( multipliedQueries.length, equalTo( 20 ) );
		
		assertThat( Collections.frequency( Lists.newArrayList( multipliedQueries ), "dp1.sql" ), equalTo( 5 ) );
		assertThat( Collections.frequency( Lists.newArrayList( multipliedQueries ), "dp2.sql" ), equalTo( 5 ) );
		assertThat( Collections.frequency( Lists.newArrayList( multipliedQueries ), "dp3.sql" ), equalTo( 5 ) );
		assertThat( Collections.frequency( Lists.newArrayList( multipliedQueries ), "dp4.sql" ), equalTo( 5 ) );
	}
}
