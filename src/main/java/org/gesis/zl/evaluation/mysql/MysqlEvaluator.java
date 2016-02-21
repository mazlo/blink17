package org.gesis.zl.evaluation.mysql;

import java.io.File;
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
import org.gesis.zl.evaluation.service.query.QueryShuffleHelper;
import org.gesis.zl.evaluation.service.query.QueryShuffleService;
import org.gesis.zl.evaluation.statistics.StatisticsHelper;
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
	private EvaluationProperties properties;
	private QueryShuffleService queryShuffleService;

	// simple properties
	private final ListMultimap<String, Long> results = ArrayListMultimap.create();

	private String[][] queriesToExecute;

	public MysqlEvaluator() throws InterruptedException
	{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( "classpath:context.xml" );

		loadBeans( context );
		loadQueries();

		execute();

		context.close();
	}

	/**
	 * 
	 * @param context
	 */
	private void loadBeans( final ClassPathXmlApplicationContext context )
	{
		this.datasource = context.getBean( "dataSourceMysql", DataSource.class );
		this.properties = context.getBean( EvaluationProperties.class );

		getQueryShuffleServiceBean( context );
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

	/**
	 * 
	 * @return
	 */
	private void loadQueries()
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
			String[] queries = QueryShuffleHelper.readFromProperties( this.properties.getQueriesFolder(), this.properties.getQueriesFiletype(), this.properties.getQueriesAvailable() );

			// create distribution with the specified properties
			this.queriesToExecute = this.queryShuffleService.shuffle( queries, this.properties.getQueriesTotal() );

			// save for later usage
			QueryShuffleHelper.writeToFile( this.queriesToExecute, expectedDistributionFilepath );
		}
	}

	private void execute() throws InterruptedException
	{
		// prepare threads
		ExecutorService executor = Executors.newFixedThreadPool( this.properties.getThreadPoolSize() );

		List<Future<Long>> listOfWorkers = new ArrayList<Future<Long>>();

		int totalExecutions = this.queriesToExecute.length;
		// int totalExecutions = 10;

		// start so many threads a there are queries
		for ( int i = 0; i < totalExecutions; i++ )
		{
			Callable<Long> queryExecution = new MysqlQueryExecutor( this.datasource, this.queriesToExecute[i] );

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

		if ( totalExecutions == 0 )
		{
			log.warn( "No executions (threads) due to empty query list" );
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

	public EvaluationProperties getProperties()
	{
		return this.properties;
	}

	public static void main( final String[] args ) throws InterruptedException
	{
		new MysqlEvaluator();
	}
}
