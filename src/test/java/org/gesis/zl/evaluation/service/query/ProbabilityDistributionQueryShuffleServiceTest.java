package org.gesis.zl.evaluation.service.query;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.gesis.zl.evaluation.service.EvaluationProperties;
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

	@Test
	public void shuffleEmpty()
	{
		String[][] shuffled = this.shuffleService.shuffle( null, 10 );

		assertNotNull( shuffled );
		assertTrue( shuffled.length == 0 );
	}

	@Test
	public void shuffleUnequalLength()
	{
		String[][] shuffled = this.shuffleService.shuffle( new String[] { "queries-mysql/dsv1.sql" }, 10 );

		assertNotNull( shuffled );
		assertTrue( shuffled.length == 0 );
	}

	@Test
	public void shuffle()
	{
		String[][] shuffled = this.shuffleService.shuffle( new String[] { "dsv1.sql", "qd2.sql" }, 10 );

		assertNotNull( shuffled );
		assertTrue( shuffled.length == 10 );
	}

	@Test
	public void shuffleFromFolder()
	{
		String[] queries = QueryShuffleHelper.readFromProperties( "queries-mysql", ".sql", "dp1", "dp2" );
		String[][] shuffled = this.shuffleService.shuffle( queries, 10 );

		assertNotNull( shuffled );
		assertTrue( shuffled.length == 10 );
	}
}
