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

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Sets;

/**
 * 
 * @author matthaeus
 * 
 */
public class EvaluationExecuter
{

	private static Logger log = LoggerFactory.getLogger( EvaluationExecuter.class );

	private final Evaluator evaluator;
	private EvaluationProperties properties;
	private QueryShuffleService queryShuffleService;

	private final ClassPathXmlApplicationContext context;

	private ListMultimap<String, Long> results;

	/**
	 * 
	 * @param specificEvaluator2
	 * @param context
	 */
	public EvaluationExecuter( final Evaluator specificEvaluator, final ClassPathXmlApplicationContext context )
	{
		this.evaluator = specificEvaluator;
		this.context = context;

		loadBeans();
		loadQueries();
	}

	/**
	 * @throws InterruptedException
	 * 
	 */
	public void execute( final Evaluator evaluator ) throws InterruptedException
	{
		this.results = evaluator.execute();
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
	 */
	public static void main( final String[] args )
	{
		Evaluator specificEvaluator = loadEvaluator( args );

		if ( specificEvaluator == null )
		{
			log.error( "Evaluator is null, cannot proceed." );
			System.exit( 2 );
		}

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( "classpath:context.xml" );

		EvaluationExecuter executer = new EvaluationExecuter( specificEvaluator, context );

		try
		{
			executer.execute( specificEvaluator );
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
	 * @param args
	 * @return
	 */
	private static Evaluator loadEvaluator( final String[] args )
	{
		if ( args == null || args.length == 0 )
		{
			log.info( "No explicite evaluator given. Please specify as first argument one of {'mysql', 'neo4j', 'sesame'}" );
			System.exit( 1 );
		}

		String evaluator = args[0];

		if ( !Sets.newHashSet( new String[] { "mysql", "sesame", "neo4j" } ).contains( evaluator ) )
		{
			log.error( "Unknown evaluator given: '{}'. Please change to one of {'mysql', 'neo4j', 'sesame'}", evaluator );
			System.exit( 1 );
		}

		Evaluator evaluatorInstance = null;

		try
		{
			@SuppressWarnings( "unchecked" )
			Class<Evaluator> evaluatorClass = (Class<Evaluator>) Class.forName( WordUtils.capitalize( evaluator ) + "Evaluator" );

			evaluatorInstance = evaluatorClass.newInstance();
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

		return evaluatorInstance;
	}

	/**
	 * 
	 */
	public void loadBeans()
	{
		// set properties
		EvaluationProperties properties = this.context.getBean( EvaluationProperties.class );
		this.evaluator.setEvaluationProperties( properties );

		// set query shuffle service
		String queryDistribution = properties.getQueriesDistribution();

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
	public void loadQueries()
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
