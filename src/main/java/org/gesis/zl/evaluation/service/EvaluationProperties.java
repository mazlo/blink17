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

	@Value( "#{applicationProperties['server.db.url']}" )
	private String serverUrl;

	@Value( "#{applicationProperties['server.db.name']}" )
	private String serverDbName;

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

	public String getServerUrl()
	{
		return this.serverUrl;
	}

	public String getServerDbName()
	{
		return this.serverDbName;
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
