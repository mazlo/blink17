package org.gesis.zl.evaluation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.gesis.zl.evaluation.mysql.MysqlEvaluator;
import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.junit.Test;

public class SpringContextLoaderTest
{

	@Test
	public void load() throws InterruptedException
	{
		MysqlEvaluator evaluator = new MysqlEvaluator();
		EvaluationProperties properties = evaluator.getProperties();

		assertNotNull( properties.getQueriesAvailable() );
		assertTrue( properties.getQueriesAvailable().length == 2 );
	}
}
