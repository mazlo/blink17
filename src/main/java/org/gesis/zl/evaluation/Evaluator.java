package org.gesis.zl.evaluation;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Multimap;

/**
 * Global interface to implement a specific Evaluator. An Evaluator needs some
 * properties (setEvaluationProperties()-method) and queries
 * (setQueries()-method) before it I will be executed (execute()-method).
 * 
 * @author matthaeus
 */
public abstract class Evaluator
{
	private static Logger log = LoggerFactory.getLogger( Evaluator.class );

	private EvaluationProperties evaluationProperties;
	private String[][] queriesToExecute;

	/**
	 * 
	 * @throws InterruptedException
	 */
	public abstract Multimap<String, Long> execute() throws InterruptedException;

	/**
	 * 
	 */
	public int prepareEvaluation()
	{
		// restart db in case we evaluate using "cold" database

		if ( !StringUtils.equals( "cold", this.evaluationProperties.getEvaluationStyle() ) )
		{
			log.info( "Queries will be executed all against a running database." );
			return 0;
		}

		log.info( "Preparing to restart the database." );

		String script = this.evaluationProperties.getDbInitdScript();
		int returnValue = 0;

		try
		{
			// run init script and wait until it is finished
			ProcessBuilder pb = new ProcessBuilder( script.split( " " ) );
			Process p = pb.start();

			p.waitFor();

			log.info( "Restart performed." );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		catch ( InterruptedException e )
		{
			e.printStackTrace();
		}

		return returnValue;
	}

	public EvaluationProperties getEvaluationProperties()
	{
		return this.evaluationProperties;
	}

	public void setEvaluationProperties( final EvaluationProperties properties )
	{
		this.evaluationProperties = properties;
	}

	public String[][] getQueries()
	{
		return this.queriesToExecute;
	}

	public void setQueries( final String[][] queriesToExecute )
	{
		this.queriesToExecute = queriesToExecute;
	}

}