package org.gesis.zl.evaluation.service.query;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class QueryShuffleHelperTest
{

	@Test
	public void readFromFile_fails()
	{
		String[][] filenamesList = QueryShuffleHelper.read( new File( "queries/does not exist.txt" ), ".sql" );
		assertNotNull( filenamesList );
		assertTrue( filenamesList.length == 0 );
	}

	@Test
	public void readFromFile()
	{
		String[][] filenamesList = QueryShuffleHelper.read( new File( "queries/equalDistribution.txt" ), ".sql" );
		assertNotNull( filenamesList );
		assertTrue( filenamesList.length == 4 );
	}
}
