package org.gesis.zl.evaluation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

			waitFor( p );

			log.info( "Restart performed. Waiting 10sec for database to recover from restart.." );

			Thread.sleep( 10000l );
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		catch ( InterruptedException e )
		{
			log.error( e.getMessage() );
		}

		return returnValue;
	}

	/**
	 * 
	 * @param p
	 */
	private void waitFor( final Process p )
	{
		InputStream inputStream = p.getInputStream();

		BufferedReader reader = new BufferedReader( new InputStreamReader( inputStream ) );

		try
		{
			while ( reader.readLine() != null )
			{
				//
				Thread.sleep( 1000l );
			}
		}
		catch ( IOException e )
		{
			log.error( e.getMessage() );
		}
		catch ( InterruptedException e )
		{
			log.error( e.getMessage() );
		}

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