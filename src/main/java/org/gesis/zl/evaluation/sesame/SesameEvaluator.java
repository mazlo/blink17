package org.gesis.zl.evaluation.sesame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.gesis.zl.evaluation.Evaluator;
import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * @author matthaeus
 * 
 */
public class SesameEvaluator implements Evaluator
{
	private static Logger log = LoggerFactory.getLogger( SesameEvaluator.class );

	private EvaluationProperties properties;
	private String[][] queriesToExecute;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gesis.zl.evaluation.Evaluator#execute()
	 */
	public Multimap<String, Long> execute() throws InterruptedException
	{
		log.info( "Executing ... " );

		// initialize repository
		Repository repo = new HTTPRepository( this.properties.getDbUrl(), this.properties.getDbName() );
		try
		{
			repo.initialize();
		}
		catch ( RepositoryException e1 )
		{
			log.error( "Error initializing repository. Exception as follows:" );
			log.error( e1.getMessage() );

			return HashMultimap.create();
		}

		// prepare threads
		ExecutorService executor = Executors.newFixedThreadPool( this.properties.getThreadPoolSize() );

		List<Future<Long>> listOfWorkers = new ArrayList<Future<Long>>();

		int totalExecutions = this.queriesToExecute.length;
		// int totalExecutions = 10;

		// start so many threads a there are queries
		for ( int i = 0; i < totalExecutions; i++ )
		{
			Callable<Long> queryExecution = new SesameQueryExecutor( repo, this.queriesToExecute[i] );

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
		Multimap<String, Long> results = ArrayListMultimap.create();

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
