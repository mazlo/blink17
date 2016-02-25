package org.gesis.zl.evaluation;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.gesis.zl.evaluation.service.query.QueryShuffleHelper;
import org.gesis.zl.evaluation.service.query.QueryShuffleService;
import org.gesis.zl.evaluation.statistics.StatisticsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

/**
 * 
 * @author matthaeus
 * 
 */
public class EvaluationExecuter
{

	private static Logger log = LoggerFactory.getLogger( EvaluationExecuter.class );

	private Evaluator evaluator;
	private EvaluationProperties properties;
	private QueryShuffleService queryShuffleService;

	private final ClassPathXmlApplicationContext context;

	private Multimap<String, Long> results;

	/**
	 * 
	 * @param args
	 */
	public static void main( final String[] args )
	{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( "classpath:context.xml" );

		EvaluationExecuter executer = new EvaluationExecuter( context );

		try
		{
			executer.runEvaluation();
			executer.writeResults();
		}
		catch ( InterruptedException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		context.close();
	}

	/**
	 * 
	 * @param context
	 */
	public EvaluationExecuter( final ClassPathXmlApplicationContext context )
	{
		this.context = context;

		loadBeans();
		loadEvaluator();
		loadQueries();
	}

	/**
	 * @throws InterruptedException
	 * 
	 */
	public void runEvaluation() throws InterruptedException
	{
		this.results = this.evaluator.execute();
	}

	/**
	 * 
	 */
	public void writeResults()
	{
		StatisticsHelper.writeResults( this.results, this.properties );
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	private void loadEvaluator()
	{
		String evaluator = this.properties.getEvaluator();

		if ( !Sets.newHashSet( new String[] { "mysql", "sesame", "neo4j" } ).contains( evaluator ) )
		{
			log.error( "Unknown evaluator given: '{}'. Please change to one of {'mysql', 'neo4j', 'sesame'}", evaluator );
			System.exit( 1 );
		}

		try
		{
			@SuppressWarnings( "unchecked" )
			Class<Evaluator> evaluatorClass = (Class<Evaluator>) Class.forName( "org.gesis.zl.evaluation." + evaluator + "." + WordUtils.capitalize( evaluator ) + "Evaluator" );

			this.evaluator = evaluatorClass.newInstance();
			this.evaluator.setEvaluationProperties( this.properties );
		}
		catch ( ClassNotFoundException e )
		{
			e.printStackTrace();
		}
		catch ( InstantiationException e )
		{
			e.printStackTrace();
		}
		catch ( IllegalAccessException e )
		{
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void loadBeans()
	{
		// set properties
		this.properties = this.context.getBean( EvaluationProperties.class );

		// set query shuffle service
		String queryDistribution = this.properties.getQueriesDistribution();

		if ( StringUtils.isEmpty( queryDistribution ) )
		{ // default is equal distribution
			this.queryShuffleService = this.context.getBean( "equalDistribution", QueryShuffleService.class );
		}
		else
		{ //
			this.queryShuffleService = this.context.getBean( queryDistribution, QueryShuffleService.class );
		}

	}

	/**
	 * 
	 */
	private void loadQueries()
	{
		String expectedDistributionFilepath = "queries/" + this.properties.getQueriesDistribution() + ".txt";

		File distributionFile = new File( expectedDistributionFilepath );

		String[][] queriesToExecute = null;

		if ( distributionFile.exists() )
		{
			log.info( "Using distribution file found in '{}' for evaluation", expectedDistributionFilepath );

			// load queries from file. We expect them to be already distributed
			queriesToExecute = QueryShuffleHelper.readFromFile( this.properties.getQueriesFolder(), distributionFile, this.properties.getQueriesFiletype() );
		}
		else
		{
			log.info( "No distribution file found in '{}', creating my own distribution", expectedDistributionFilepath );

			String[] queries = QueryShuffleHelper.readFromProperties( this.properties.getQueriesFolder(), this.properties.getQueriesFiletype(), this.properties.getQueriesAvailable() );

			// create distribution with the specified properties
			queriesToExecute = this.queryShuffleService.shuffle( queries, this.properties.getQueriesTotal() );

			// save for later usage
			QueryShuffleHelper.writeToFile( queriesToExecute, expectedDistributionFilepath );
		}

		this.evaluator.setQueries( queriesToExecute );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.gesis.zl.evaluation.mysql.Evaluator#debugProperties()
	 */
	public void debugProperties( final EvaluationProperties properties )
	{
		if ( properties != null )
		{
			log.debug( "Properties set:" );
			log.debug( "Database url: '{}'", properties.getDbUrl() );
			log.debug( "Database name: '{}'", properties.getDbName() );
			log.debug( "Queries folder: '{}'", properties.getQueriesFolder() );
			log.debug( "Queries filetype: '{}'", properties.getQueriesFiletype() );
			log.debug( "Queries distribution: '{}'", properties.getQueriesDistribution() );
			log.debug( "Queries total: '{}'", properties.getQueriesTotal() );
			log.debug( "Queries available: '{}'", properties.getQueriesAvailable() );
			log.debug( "Queries probabilities: '{}'", properties.getQueriesProbabilities() );
			log.debug( "Thread pool size: '{}'", properties.getThreadPoolSize() );
		}
	}
}
