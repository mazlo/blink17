package org.gesis.zl.evaluation;

import org.gesis.zl.evaluation.service.EvaluationProperties;

import com.google.common.collect.Multimap;

/**
 * Global interface to implement a specific Evaluator. An Evaluator needs some
 * properties (setEvaluationProperties()-method) and queries
 * (setQueries()-method) before it I will be executed (execute()-method).
 * 
 * @author matthaeus
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