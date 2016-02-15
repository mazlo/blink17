package org.gesis.zl.evaluation.service.query;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = { "classpath:context.xml" } )
public class ProbabilityDistributionQueryShuffleServiceTest
{

	@Autowired
	@Qualifier( "probabilityDistribution" )
	private QueryShuffleService shuffleService;

	@Autowired
	private EvaluationProperties properties;

	@Before
	public void init() throws FileNotFoundException, IOException
	{
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
