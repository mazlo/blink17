package org.gesis.zl.evaluation;

import org.gesis.zl.evaluation.service.EvaluationProperties;

import com.google.common.collect.Multimap;

/**
 * 
 * @author matthaeus
 * 
 */
public interface Evaluator
{

	/**
	 * 
	 * @throws InterruptedException
	 */
	public abstract Multimap<String, Long> execute() throws InterruptedException;

	public abstract void setEvaluationProperties( EvaluationProperties properties );

	public abstract void setQueries( String[][] queriesToExecute );

}