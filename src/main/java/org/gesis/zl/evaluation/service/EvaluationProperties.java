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
	@Value( "${evaluate}" )
	private String evaluator;

	@Value( "${server.db.driverClass}" )
	private String dbDriverClass;

	@Value( "${server.db.url}" )
	private String dbUrl;

	@Value( "${server.db.name}" )
	private String dbName;

	@Value( "${server.db.username}" )
	private String dbUsername;

	@Value( "${server.db.password}" )
	private String dbPassword;

	@Value( "${statistics.output.filename}" )
	private String statisticsOutputFilename;

	@Value( "${queries.folder}" )
	private String queriesFolder;

	@Value( "${queries.filetype}" )
	private String queriesFiletype;

	@Value( "${queries.total}" )
	private String queriesTotal;

	@Value( "${queries.probabilities}" )
	private String[] queriesProbabilities;

	@Value( "${queries.available}" )
	private String[] queriesAvailable;

	@Value( "${queries.distribution}" )
	private String queriesDistribution;

	@Value( "${thread.pool.size}" )
	private String threadPoolSize;

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
		return Integer.parseInt( this.threadPoolSize );
	}

	public int getQueriesTotal()
	{
		return Integer.parseInt( this.queriesTotal );
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
