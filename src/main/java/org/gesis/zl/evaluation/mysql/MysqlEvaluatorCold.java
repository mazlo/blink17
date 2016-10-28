package org.gesis.zl.evaluation.mysql;

import java.util.ArrayList;
import java.util.List;
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
 * 
 * @author matthaeus
 * 
 */
public class MysqlEvaluatorCold extends Evaluator
{
	private static Logger log = LoggerFactory.getLogger( MysqlQueryExecutor.class );

	@Override
	public Multimap<String, Long> execute() throws InterruptedException
	{
		log.info( "Executing ..." );

		EvaluationProperties props = getEvaluationProperties();
		String[][] queriesToExecute = getQueries();

		List<Future<Long>> listOfWorkers = new ArrayList<Future<Long>>();

		int totalExecutions = queriesToExecute.length;

		// start so many threads as there are queries
		for ( int i = 0; i < totalExecutions; i++ )
		{
			if ( queriesToExecute[i][1].length() == 0 )
			{
				listOfWorkers.add( Futures.immediateFuture( 0l ) );
				continue;
			}

			this.prepareEvaluation();

			// since database has been restarted, reopen connection
			BasicDataSource datasource = new BasicDataSource();
			datasource.setDriverClassName( props.getDbDriverClass() );
			datasource.setUrl( props.getDbUrl() + props.getDbName() );
			datasource.setUsername( props.getDbUsername() );
			datasource.setPassword( props.getDbPassword() );

			MysqlQueryExecutor queryExecution = new MysqlQueryExecutor( datasource, queriesToExecute[i] );

			// execute
			Future<Long> submitedWorker = Futures.immediateFuture( queryExecution.call() );

			// remember
			listOfWorkers.add( submitedWorker );
		}

		return StatisticsHelper.collectResults( listOfWorkers, queriesToExecute );
	}

}
