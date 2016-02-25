package org.gesis.zl.evaluation.mysql;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.dbcp.BasicDataSource;
import org.gesis.zl.evaluation.Evaluator;
import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * @author matthaeus
 * 
 */
public class MysqlEvaluator implements Evaluator
{

	private static Logger log = LoggerFactory.getLogger( MysqlEvaluator.class );

	private EvaluationProperties properties;
	private String[][] queriesToExecute;

	/* (non-Javadoc)
	 * @see org.gesis.zl.evaluation.mysql.Evaluator#execute()
	 */
	public ListMultimap<String, Long> execute() throws InterruptedException
	{
		log.info( "Executing ..." );

		BasicDataSource datasource = new BasicDataSource();
		datasource.setDriverClassName( this.properties.getDbDriverClass() );
		datasource.setUrl( this.properties.getDbUrl() + this.properties.getDbName() );
		datasource.setUsername( this.properties.getDbUsername() );
		datasource.setPassword( this.properties.getDbPassword() );

		// prepare threads
		ExecutorService executor = Executors.newFixedThreadPool( this.properties.getThreadPoolSize() );

		List<Future<Long>> listOfWorkers = new ArrayList<Future<Long>>();

		int totalExecutions = this.queriesToExecute.length;
		// int totalExecutions = 10;

		// start so many threads a there are queries
		for ( int i = 0; i < totalExecutions; i++ )
		{
			Callable<Long> queryExecution = new MysqlQueryExecutor( datasource, this.queriesToExecute[i] );

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

		log.info( "Collect results" );

		// collect results
		ListMultimap<String, Long> results = ArrayListMultimap.create();

		for ( int i = 0; i < totalExecutions; i++ )
		{
			Future<Long> executedTask = listOfWorkers.get( i );
			Long ms;
			try
			{
				ms = executedTask.get();
				results.put( this.queriesToExecute[i][0], ms );
			}
			catch ( ExecutionException e )
			{
				System.err.println( "Failed to get value from one Future" );
				e.printStackTrace();
			}
		}

		if ( totalExecutions == 0 )
		{
			log.warn( "No executions (threads) due to empty query list" );
		}

		log.info( "Finished" );

		return results;
	}

	public void setEvaluationProperties( final EvaluationProperties properties )
	{
		this.properties = properties;
	}

	public void setQueries( final String[][] queriesToExecute )
	{
		this.queriesToExecute = queriesToExecute;
	}

}
