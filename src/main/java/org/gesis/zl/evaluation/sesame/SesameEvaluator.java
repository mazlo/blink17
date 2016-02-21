package org.gesis.zl.evaluation.sesame;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.gesis.zl.evaluation.Evaluator;
import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.gesis.zl.evaluation.service.query.QueryShuffleHelper;
import org.gesis.zl.evaluation.service.query.QueryShuffleService;
import org.gesis.zl.evaluation.statistics.StatisticsHelper;
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
public class SesameEvaluator implements Evaluator
{
	private static Logger log = LoggerFactory.getLogger( SesameEvaluator.class );

	// beans
	private EvaluationProperties properties;
	private QueryShuffleService queryShuffleService;

	// simple properties
	private final SetMultimap<String, Long> results = HashMultimap.create();

	private String[][] queriesToExecute;

	public SesameEvaluator() throws RepositoryException, InterruptedException, ExecutionException
	{
		log.info( "Started sesame evaluator" );

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( "classpath:context.xml" );

		loadBeans( context );
		loadQueries();

		execute();

		context.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.gesis.zl.evaluation.mysql.Evaluator#loadBeans(org.springframework
	 * .context.support.ClassPathXmlApplicationContext)
	 */
	public void loadBeans( final ClassPathXmlApplicationContext context )
	{
		this.properties = context.getBean( EvaluationProperties.class );

		getQueryShuffleServiceBean( context );

		debugProperties();
	}

	/**
	 * 
	 * @param context
	 */
	private void getQueryShuffleServiceBean( final ClassPathXmlApplicationContext context )
	{
		String queryDistribution = this.properties.getQueriesDistribution();

		if ( StringUtils.isEmpty( queryDistribution ) )
		{ // default is equal distribution
			this.queryShuffleService = context.getBean( "equalDistribution", QueryShuffleService.class );
		}
		else
		{ //
			this.queryShuffleService = context.getBean( queryDistribution, QueryShuffleService.class );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gesis.zl.evaluation.mysql.Evaluator#loadQueries()
	 */
	public void loadQueries()
	{
		String expectedDistributionFilepath = "queries/" + this.properties.getQueriesDistribution() + ".txt";

		File distributionFile = new File( expectedDistributionFilepath );

		if ( distributionFile.exists() )
		{
			log.info( "Using distribution file found in '{}' for evaluation", expectedDistributionFilepath );

			// load queries from file. We expect them to be already distributed
			this.queriesToExecute = QueryShuffleHelper.readFromFile( this.properties.getQueriesFolder(), distributionFile, this.properties.getQueriesFiletype() );
		}
		else
		{
			log.info( "No distribution file found in '{}', creating my own distribution", expectedDistributionFilepath );

			String[] queries = QueryShuffleHelper.readFromProperties( this.properties.getQueriesFolder(), this.properties.getQueriesFiletype(), this.properties.getQueriesAvailable() );

			// create distribution with the specified properties
			this.queriesToExecute = this.queryShuffleService.shuffle( queries, this.properties.getQueriesTotal() );

			// save for later usage
			QueryShuffleHelper.writeToFile( this.queriesToExecute, expectedDistributionFilepath );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gesis.zl.evaluation.mysql.Evaluator#debugProperties()
	 */
	public void debugProperties()
	{
		if ( this.properties != null )
		{
			log.debug( "Properties set:" );
			log.debug( "Database url: '{}'", this.properties.getDbUrl() );
			log.debug( "Database name: '{}'", this.properties.getDbName() );
			log.debug( "Queries folder: '{}'", this.properties.getQueriesFolder() );
			log.debug( "Queries filetype: '{}'", this.properties.getQueriesFiletype() );
			log.debug( "Queries distribution: '{}'", this.properties.getQueriesDistribution() );
			log.debug( "Queries total: '{}'", this.properties.getQueriesTotal() );
			log.debug( "Queries available: '{}'", this.properties.getQueriesAvailable() );
			log.debug( "Queries probabilities: '{}'", this.properties.getQueriesProbabilities() );
			log.debug( "Thread pool size: '{}'", this.properties.getThreadPoolSize() );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gesis.zl.evaluation.Evaluator#execute()
	 */
	public void execute() throws InterruptedException
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

			return;
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
		for ( int i = 0; i < totalExecutions; i++ )
		{
			Future<Long> executedTask = listOfWorkers.get( i );
			Long ms;
			try
			{
				ms = executedTask.get();
				this.results.put( this.queriesToExecute[i][0], ms );
			}
			catch ( ExecutionException e )
			{
				System.err.println( "Failed to get value from one Future" );
				e.printStackTrace();
			}
		}

		StatisticsHelper.writeResults( getStatisticsFilename(), this.results );

		log.info( "Finished" );
	}

	/**
	 * @return
	 */
	private String getStatisticsFilename()
	{
		String[] str = new String[] { this.properties.getStatisticsOutputFilename(), String.valueOf( this.properties.getQueriesTotal() ), String.valueOf( this.properties.getThreadPoolSize() ), String.valueOf( System.currentTimeMillis() ) };
		return StringUtils.join( str, "_" );
	}
	public static void main( final String[] args ) throws RepositoryException, InterruptedException, ExecutionException
	{
		new SesameEvaluator();
	}
}
