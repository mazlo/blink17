package org.gesis.zl.evaluation.mysql;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.gesis.zl.evaluation.service.QueryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

/**
 * @author matthaeus
 * 
 */
public class MysqlEvaluator
{

	private static Logger log = LoggerFactory.getLogger( MysqlEvaluator.class );

	// beans
	private DataSource datasource;
	private QueryHelper queryHelper;
	private EvaluationProperties properties;

	// simple properties
	private ListMultimap<String, Long> results = ArrayListMultimap.create();

	public MysqlEvaluator() throws InterruptedException
	{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( "classpath:context.xml" );

		datasource = context.getBean( "dataSourceMysql", DataSource.class );
		queryHelper = context.getBean( QueryHelper.class );
		properties = context.getBean( EvaluationProperties.class );

		execute();

		context.close();
	}

	private void execute() throws InterruptedException
	{
		// prepare threads
		ExecutorService executor = Executors.newFixedThreadPool( properties.getThreadPoolSize() );

		List<Future<Long>> listOfWorkers = new ArrayList<Future<Long>>();

		String[][] queriesToExecute = queryHelper.shuffleQueriesToExecute( properties.getQueryQueueSize() );

		int totalExecutions = queriesToExecute.length;
		// int totalExecutions = 10;

		// start so many threads a there are queries
		for ( int i = 0; i < totalExecutions; i++ )
		{
			Callable<Long> queryExecution = new MysqlQueryExecutor( this.datasource, queriesToExecute[i] );

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

		// collect results
		log.info( "collect results" );
		for ( int i = 0; i < totalExecutions; i++ )
		{
			Future<Long> executedTask = listOfWorkers.get( i );
			Long ms;
			try
			{
				ms = executedTask.get();
				results.put( queriesToExecute[i][0], ms );
			}
			catch ( ExecutionException e )
			{
				System.err.println( "Failed to get value from one Future" );
				e.printStackTrace();
			}
		}

		if ( totalExecutions == 0 )
			log.warn( "No executions (threads) due to empty query list" );

		queryHelper.writeResults( properties.getStatisticsOutputFilename() + "_" + System.currentTimeMillis(), results );

		log.info( "Finished" );
	}

	public static void main( String[] args ) throws InterruptedException
	{
		new MysqlEvaluator();
	}
}
