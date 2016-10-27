package org.gesis.zl.evaluation;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.gesis.zl.evaluation.service.EvaluationProperties;

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
	public void prepareEvaluation()
	{
		// restart db in case we evaluate using "cold" database

		if ( !StringUtils.equals( "cold", this.evaluationProperties.getEvaluationStyle() ) )
		{
			return;
		}

		String script = this.evaluationProperties.getDbInitdScript();

		try
		{
			// run init script and wait until it is finished
			Process p = Runtime.getRuntime().exec( script );
			p.waitFor();
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch ( InterruptedException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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