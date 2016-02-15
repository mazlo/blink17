package org.gesis.zl.evaluation.service.query;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author matthaeus
 * 
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = { "classpath:context.xml" } )
public class EqualDistributionQueryShuffleServiceTest
{

	@Autowired
	@Qualifier( "equalDistribution" )
	private QueryShuffleService shuffleService;

	@Before
	public void init()
	{
		this.shuffleService = new EqualDistributionQueryShuffleService();
	}

	@Test
	public void shuffleEmpty()
	{
		String[][] shuffled = this.shuffleService.shuffle( 10 );
		assertNotNull( shuffled );
		assertEquals( 0, shuffled.length );
	}

	@Test
	public void shuffle()
	{
		this.shuffleService.setQueries( new File[] { new File( "queries-mysql/dsv1.sql" ) } );

		String[][] shuffled = this.shuffleService.shuffle( 10 );
		assertNotNull( shuffled );
		assertEquals( 10, shuffled.length );

		for ( String[] pair : shuffled )
		{
			assertNotNull( pair );
			assertEquals( pair[0], "dsv1.sql" );
		}
	}
}
