package org.gesis.zl.evaluation;

import org.gesis.zl.evaluation.service.EvaluationProperties;

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
	public abstract void execute() throws InterruptedException;

	public abstract void setEvaluationProperties( EvaluationProperties properties );

	public abstract void setQueries( String[][] queriesToExecute );

}