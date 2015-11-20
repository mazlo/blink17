package org.gesis.zl.evaluation.sesame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.gesis.zl.evaluation.service.QueryHelper;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

/**
 * @author matthaeus
 * 
 */
public class SesameEvaluator
{
	private static Logger log = LoggerFactory.getLogger( SesameEvaluator.class );

	// beans
	private QueryHelper queryHelper;
	private EvaluationProperties properties;

	// simple properties
	private SetMultimap<String, Long> results = HashMultimap.create();

	public SesameEvaluator() throws RepositoryException, InterruptedException, ExecutionException
	{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( "classpath:context.xml" );

		queryHelper = context.getBean( QueryHelper.class );
		properties = context.getBean( EvaluationProperties.class );

		execute();

		context.close();
	}

	private void execute() throws RepositoryException, InterruptedException
	{
		// initialize repository
		Repository repo = new HTTPRepository( properties.getServerUrl(), properties.getServerDbName() );
		repo.initialize();

		// prepare threads
		ExecutorService executor = Executors.newFixedThreadPool( properties.getThreadPoolSize() );

		List<Future<Long>> listOfWorkers = new ArrayList<Future<Long>>();

		String[][] queriesToExecute = queryHelper.shuffleQueriesToExecute( properties.getQueryQueueSize() );

		int totalExecutions = queriesToExecute.length;
		// int totalExecutions = 10;

		// start so many threads a there are queries
		for ( int i = 0; i < totalExecutions; i++ )
		{
			Callable<Long> queryExecution = new SesameQueryExecutor( repo, queriesToExecute[i] );

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

		queryHelper.writeResults( getStatisticsFilename(), results );

		log.info( "Finished" );
	}

	/**
	 * @return
	 */
	private String getStatisticsFilename()
	{
		String[] str = new String[] { properties.getStatisticsOutputFilename(), String.valueOf( properties.getQueryQueueSize() ), String.valueOf( properties.getThreadPoolSize() ), String.valueOf( System.currentTimeMillis() ) };
		return StringUtils.join( str, "_" );
	}
	public static void main( String[] args ) throws RepositoryException, InterruptedException, ExecutionException
	{
		new SesameEvaluator();
	}
}
