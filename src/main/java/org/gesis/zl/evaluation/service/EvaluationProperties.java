package org.gesis.zl.evaluation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author matthaeus
 * 
 */
@Service
public class EvaluationProperties
{
	@Value( "#{applicationProperties['evaluate']}" )
	private String evaluator;

	@Value( "#{applicationProperties['server.db.driverClass']}" )
	private String dbDriverClass;

	@Value( "#{applicationProperties['server.db.url']}" )
	private String dbUrl;

	@Value( "#{applicationProperties['server.db.name']}" )
	private String dbName;

	@Value( "#{applicationProperties['server.db.username']}" )
	private String dbUsername;

	@Value( "#{applicationProperties['server.db.password']}" )
	private String dbPassword;

	@Value( "#{applicationProperties['statistics.output.filename']}" )
	private String statisticsOutputFilename;

	@Value( "#{applicationProperties['queries.folder']}" )
	private String queriesFolder;

	@Value( "#{applicationProperties['queries.filetype']}" )
	private String queriesFiletype;

	@Value( "#{applicationProperties['queries.total']}" )
	private int queriesTotal;

	@Value( "#{applicationProperties['queries.probabilities']}" )
	private String[] queriesProbabilities;

	@Value( "#{applicationProperties['queries.available']}" )
	private String[] queriesAvailable;

	@Value( "#{applicationProperties['queries.distribution']}" )
	private String queriesDistribution;

	@Value( "#{applicationProperties['thread.pool.size']}" )
	private int threadPoolSize;

	/* getter / setter */

	public String getEvaluator()
	{
		return this.evaluator;
	}

	public String getQueriesFolder()
	{
		return this.queriesFolder;
	}

	public String getDbDriverClass()
	{
		return this.dbDriverClass;
	}

	public String getDbUrl()
	{
		return this.dbUrl;
	}

	public String getDbName()
	{
		return this.dbName;
	}

	public String getDbUsername()
	{
		return this.dbUsername;
	}

	public String getDbPassword()
	{
		return this.dbPassword;
	}

	public String getStatisticsOutputFilename()
	{
		return this.statisticsOutputFilename;
	}

	public int getThreadPoolSize()
	{
		return this.threadPoolSize;
	}

	public int getQueriesTotal()
	{
		return this.queriesTotal;
	}

	public String getQueriesFiletype()
	{
		return this.queriesFiletype;
	}

	public String[] getQueriesProbabilities()
	{
		return this.queriesProbabilities;
	}

	public String[] getQueriesAvailable()
	{
		return this.queriesAvailable;
	}

	public String getQueriesDistribution()
	{
		return this.queriesDistribution;
	}

}
