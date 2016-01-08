package org.gesis.zl.evaluation.neo4j;

import java.util.concurrent.Callable;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author matthaeus
 * 
 */
public class Neo4jQueryExecutor implements Callable<Long>
{
	private static Logger log = LoggerFactory.getLogger( Neo4jQueryExecutor.class );

	private CloseableHttpClient httpClient;

	private String url;

	private String queryKey;
	private String query;

	public Neo4jQueryExecutor( final String url, String[] query )
	{
		this.httpClient = HttpClientBuilder.create().build();
		this.url = url;

		this.queryKey = query[0];
		this.query = query[1];
	}

	public Long call() throws Exception
	{
		HttpPost request = new HttpPost( url );
		
		StringEntity params = new StringEntity( "{ \"query\" : \"" + this.query + "\" }" );
		request.addHeader( "accept", "application/json" );
		request.addHeader( "content-type", "application/json" );
		request.setEntity( params );

		// execute
		log.info( "about to execute query {}..", this.queryKey );
		long start = System.currentTimeMillis();

		CloseableHttpResponse response = httpClient.execute( request );
		
		long resultMs = System.currentTimeMillis() - start;

		response.close();

		return resultMs;
	}

}
