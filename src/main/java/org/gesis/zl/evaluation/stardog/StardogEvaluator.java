package org.gesis.zl.evaluation.stardog;

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

/**
 * @author matthaeus
 * 
 */
public class StardogEvaluator extends Evaluator
{
	private static Logger log = LoggerFactory.getLogger( StardogEvaluator.class );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gesis.zl.evaluation.Evaluator#execute()
	 */
	@Override
	public Multimap<String, Long> execute() throws InterruptedException
	{
		EvaluationProperties props = getEvaluationProperties();
		String[][] queriesToExecute = getQueries();

		// prepare threads
		ExecutorService executor = Executors.newFixedThreadPool( props.getThreadPoolSize() );

		List<Future<Long>> listOfWorkers = new ArrayList<Future<Long>>();

		int totalExecutions = queriesToExecute.length;

		// start so many threads a there are queries
		for ( int i = 0; i < totalExecutions; i++ )
		{
			if ( queriesToExecute[i][1].length() == 0 )
			{
				continue;
			}

			Callable<Long> queryExecution = new StardogQueryExecutor( props.getDbUrl() + props.getDbName(), queriesToExecute[i] );

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

		return StatisticsHelper.collectResults( listOfWorkers, queriesToExecute );
	}

}
