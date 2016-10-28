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
import org.springframework.context.ApplicationContext;
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

	private final ApplicationContext context;

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
	public EvaluationExecuter( final ApplicationContext context )
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
	public Evaluator loadEvaluator()
	{
		String evaluator = this.properties.getEvaluator();
		String coldStarter = this.properties.getEvaluationStyle();

		if ( !Sets.newHashSet( new String[] { "mysql", "sesame", "neo4j", "stardog", "virtuoso" } ).contains( evaluator ) )
		{
			log.error( "Unknown evaluator given: '{}'. Please change to one of {'mysql', 'neo4j', 'sesame', 'stardog', 'virtuoso'}", evaluator );
			System.exit( 1 );
		}

		try
		{
			String className = "org.gesis.zl.evaluation." + evaluator + "." + WordUtils.capitalize( evaluator ) + "Evaluator";

			// cold starters class names are expected to end with "..Cold.java"
			if ( StringUtils.equals( "cold", coldStarter ) )
			{
				className += "Cold";
			}

			@SuppressWarnings( "unchecked" )
			Class<Evaluator> evaluatorClass = (Class<Evaluator>) Class.forName( className );

			this.evaluator = evaluatorClass.newInstance();
			this.evaluator.setEvaluationProperties( this.properties );

			log.info( "{} instantiated successfully", this.evaluator.getClass().getName() );

			debugProperties();

			return this.evaluator;
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

		return null;
	}

	/**
	 * 
	 */
	public void loadBeans()
	{
		// set properties
		this.properties = this.context.getBean( EvaluationProperties.class );

		// set query shuffle service
		String queryDistribution = this.properties.getQueriesDistribution();

		if ( StringUtils.isEmpty( queryDistribution ) )
		{ // default is equal distribution
			this.queryShuffleService = this.context.getBean( "equalDistribution", QueryShuffleService.class );
			log.warn( "Using default shuffle service '{}', because no explicite distribution given in property 'queries.distributions'", this.queryShuffleService.getName() );
		}
		else
		{ //
			this.queryShuffleService = this.context.getBean( queryDistribution, QueryShuffleService.class );
			log.info( "Using shuffle service '{}'", this.queryShuffleService.getName() );
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
			queriesToExecute = this.queryShuffleService.shuffle( queries, this.properties.getQueriesMultiplier() );

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
	public void debugProperties()
	{
		if ( this.properties == null )
		{
			return;
		}

		log.info( "==== Properties BEGIN ====" );
		log.info( "Evaluation of: {}", this.properties.getEvaluator() );
		log.info( "Evaluation style: {}", this.properties.getEvaluationStyle() );
		log.info( "Database url: '{}'", this.properties.getDbUrl() );
		log.info( "Database name: '{}'", this.properties.getDbName() );
		log.info( "Queries folder: '{}'", this.properties.getQueriesFolder() );
		log.info( "Queries filetype: '{}'", this.properties.getQueriesFiletype() );
		log.info( "Queries distribution: '{}'", this.properties.getQueriesDistribution() );
		log.info( "Queries total: '{}'", this.properties.getQueriesTotal() );
		log.info( "Queries available: '{}'", this.properties.getQueriesAvailable() );
		log.info( "Queries probabilities: '{}'", this.properties.getQueriesProbabilities() );
		log.info( "Thread pool size: '{}'", this.properties.getThreadPoolSize() );
		log.info( "==== Properties END ====" );
	}
}
