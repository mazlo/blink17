package org.gesis.zl.evaluation.virtuoso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.gesis.zl.evaluation.Evaluator;
import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.gesis.zl.evaluation.statistics.StatisticsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.Futures;

/**
 * @author matthaeus
 * 
 */
public class VirtuosoEvaluator implements Evaluator
{
	private static Logger log = LoggerFactory.getLogger( VirtuosoEvaluator.class );

	private EvaluationProperties properties;
	private String[][] queriesToExecute;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gesis.zl.evaluation.Evaluator#execute()
	 */
	@Override
	public Multimap<String, Long> execute() throws InterruptedException
	{
		// prepare threads
		ExecutorService executor = Executors.newFixedThreadPool( this.properties.getThreadPoolSize() );

		List<Future<Long>> listOfWorkers = new ArrayList<Future<Long>>();

		int totalExecutions = this.queriesToExecute.length;

		// start so many threads a there are queries
		for ( int i = 0; i < totalExecutions; i++ )
		{
			if ( this.queriesToExecute[i][1].length() == 0 )
			{
				listOfWorkers.add( Futures.immediateFuture( 0l ) );
				continue;
			}

			Callable<Long> queryExecution = new VirtuosoQueryExecutor( this.properties.getDbUrl() + this.properties.getDbName(), this.queriesToExecute[i] );

			// execute
			Future<Long> submitedWorker = executor.submit( queryExecution );

			// remember
			listOfWorkers.add( submitedWorker );
		}

		// finish them all
		executor.shutdown();

		while ( !executor.isTerminated() )
		{
			// wait to terminate
		}

		return StatisticsHelper.collectResults( listOfWorkers, this.queriesToExecute );
	}

	@Override
	public void setEvaluationProperties( final EvaluationProperties properties )
	{
		this.properties = properties;
	}

	@Override
	public void setQueries( final String[][] queriesToExecute )
	{
		this.queriesToExecute = queriesToExecute;
	}
}
