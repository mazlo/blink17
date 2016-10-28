package org.gesis.zl.evaluation;

import static org.junit.Assert.assertNotNull;

import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = { "classpath:context.xml" } )
public class EvaluatorTest implements ApplicationContextAware
{

	@Autowired
	private EvaluationProperties properties;

	private ApplicationContext context;

	@Before
	public void init()
	{
		assertNotNull( this.context );
	}

	@Test
	public void prepareEvaluation()
	{
		EvaluationExecuter executor = new EvaluationExecuter( this.context );

		Evaluator evaluator = executor.loadEvaluator();

		evaluator.prepareEvaluation();
	}

	@Override
	public void setApplicationContext( final ApplicationContext applicationContext ) throws BeansException
	{
		this.context = applicationContext;
	}
}
