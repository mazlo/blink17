package org.gesis.zl.evaluation.neo4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
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
public class Neo4jEvaluatorCold extends Evaluator
{
	private static Logger log = LoggerFactory.getLogger( Neo4jEvaluatorCold.class );

	/**
	 * @throws InterruptedException
	 * 
	 */
	@Override
	public Multimap<String, Long> execute() throws InterruptedException
	{
		EvaluationProperties props = getEvaluationProperties();
		String[][] queriesToExecute = getQueries();

		List<Future<Long>> listOfWorkers = new ArrayList<Future<Long>>();

		int totalExecutions = queriesToExecute.length;

		// start so many threads a there are queries
		for ( int i = 0; i < totalExecutions; i++ )
		{
			if ( queriesToExecute[i][1].length() == 0 )
			{
				listOfWorkers.add( Futures.immediateFuture( 0l ) );
				continue;
			}

			this.prepareEvaluation();

			try
			{
				Callable<Long> queryExecution = new Neo4jQueryExecutor( props.getDbUrl() + props.getDbName(), queriesToExecute[i] );

				// execute
				Future<Long> submitedWorker = Futures.immediateFuture( queryExecution.call() );

				// remember
				listOfWorkers.add( submitedWorker );
			}
			catch ( Exception e )
			{
				log.error( e.getMessage() );
				continue;
			}
		}

		return StatisticsHelper.collectResults( listOfWorkers, queriesToExecute );
	}

}
