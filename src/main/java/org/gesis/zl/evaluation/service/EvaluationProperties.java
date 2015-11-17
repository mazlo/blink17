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

	@Value( "#{applicationProperties['queries.folder']}" )
	private String queriesFolder;

	@Value( "#{applicationProperties['server.url']}" )
	private String serverUrl;

	@Value( "#{applicationProperties['server.db.name']}" )
	private String serverDbName;

	@Value( "#{applicationProperties['statistics.output.filename']}" )
	private String statisticsOutputFilename;

	@Value( "#{applicationProperties['thread.pool.size']}" )
	private int threadPoolSize;

	@Value( "#{applicationProperties['query.queue.size']}" )
	private int queryQueueSize;

	public String getQueriesFolder()
	{
		return queriesFolder;
	}

	public String getServerUrl()
	{
		return serverUrl;
	}

	public String getServerDbName()
	{
		return serverDbName;
	}

	public String getStatisticsOutputFilename()
	{
		return statisticsOutputFilename;
	}

	public int getThreadPoolSize()
	{
		return threadPoolSize;
	}

	public int getQueryQueueSize()
	{
		return queryQueueSize;
	}

}
