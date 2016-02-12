package org.gesis.zl.evaluation.service.query;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ProbabilityDistributionQueryShuffleServiceTest
{

	private QueryShuffleService shuffleService;

	private Properties properties;

	@Before
	public void init() throws FileNotFoundException, IOException
	{
		this.properties = new Properties();
		this.properties.load( new FileReader( new File( "src/test/resources/application.properties" ) ) );

		this.shuffleService = new ProbabilityDistributionQueryShuffleService( this.properties );
	}

	@Test
	public void shuffleEmpty()
	{
		String[][] shuffled = this.shuffleService.shuffle( 10 );

		assertNotNull( shuffled );
		assertTrue( shuffled.length == 0 );
	}

	@Test
	public void shuffleUnequalLength()
	{
		this.shuffleService.setQueries( new File[] { new File( "queries-mysql/dsv1.sql" ) } );
		String[][] shuffled = this.shuffleService.shuffle( 10 );

		assertNotNull( shuffled );
		assertTrue( shuffled.length == 0 );
	}

	@Test
	public void shuffle()
	{
		this.shuffleService.setQueries( new File[] { new File( "queries-mysql/dsv1.sql" ), new File( "queries-mysql/qd2.sql" ) } );
		String[][] shuffled = this.shuffleService.shuffle( 10 );

		assertNotNull( shuffled );
		assertTrue( shuffled.length == 10 );
	}

	@Test
	public void shuffleFromFolder()
	{
		File[] queries = QueryShuffleHelper.read( "queries-mysql", "dp1.sql", "dp2.sql" );
		this.shuffleService.setQueries( queries );

		String[][] shuffled = this.shuffleService.shuffle( 10 );

		assertNotNull( shuffled );
		assertTrue( shuffled.length == 10 );
	}
}
