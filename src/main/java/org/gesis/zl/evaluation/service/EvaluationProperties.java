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

	public String getQueriesFolder()
	{
		return queriesFolder;
	}

	public void setQueriesFolder( String queriesFolder )
	{
		this.queriesFolder = queriesFolder;
	}

	public String getServerUrl()
	{
		return serverUrl;
	}

	public void setServerUrl( String serverUrl )
	{
		this.serverUrl = serverUrl;
	}

	public String getServerDbName()
	{
		return serverDbName;
	}

	public void setServerDbName( String serverDbName )
	{
		this.serverDbName = serverDbName;
	}

}
