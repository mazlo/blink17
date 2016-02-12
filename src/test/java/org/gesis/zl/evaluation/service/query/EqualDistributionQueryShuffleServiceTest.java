package org.gesis.zl.evaluation.service.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author matthaeus
 * 
 */
public class EqualDistributionQueryShuffleServiceTest
{

	private QueryShuffleService shuffleService;

	@Before
	public void init()
	{
		shuffleService = new EqualDistributionQueryShuffleService();
	}

	@Test
	public void shuffleEmpty()
	{
		String[][] shuffled = shuffleService.shuffle( 10 );
		assertNotNull( shuffled );
		assertEquals( 0, shuffled.length );
	}

	@Test
	public void shuffle()
	{
		shuffleService.setQueries( new File[] { new File( "queries-mysql/dsv1.sql" ) } );

		String[][] shuffled = shuffleService.shuffle( 10 );
		assertNotNull( shuffled );
		assertEquals( 10, shuffled.length );

		for ( String[] pair : shuffled )
		{
			assertNotNull( pair );
			assertEquals( pair[0], "dsv1.sql" );
		}
	}
}
