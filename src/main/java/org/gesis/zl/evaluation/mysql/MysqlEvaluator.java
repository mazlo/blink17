package org.gesis.zl.evaluation.mysql;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.dbcp.BasicDataSource;
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
public class MysqlEvaluator extends Evaluator
{

	private static Logger log = LoggerFactory.getLogger( MysqlEvaluator.class );

	/* (non-Javadoc)
	 * @see org.gesis.zl.evaluation.mysql.Evaluator#execute()
	 */
	@Override
	public Multimap<String, Long> execute() throws InterruptedException
	{
		log.info( "Executing ..." );

		EvaluationProperties props = getEvaluationProperties();
		String[][] queriesToExecute = getQueries();

		BasicDataSource datasource = new BasicDataSource();
		datasource.setDriverClassName( props.getDbDriverClass() );
		datasource.setUrl( props.getDbUrl() + props.getDbName() );
		datasource.setUsername( props.getDbUsername() );
		datasource.setPassword( props.getDbPassword() );

		// prepare threads
		ExecutorService executor = Executors.newFixedThreadPool( props.getThreadPoolSize() );

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

			Callable<Long> queryExecution = new MysqlQueryExecutor( datasource, queriesToExecute[i] );

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
