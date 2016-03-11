package org.gesis.zl.evaluation.stardog;

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
public class StardogQueryExecutor implements Callable<Long>
{
	private static Logger log = LoggerFactory.getLogger( StardogQueryExecutor.class );

	private final CloseableHttpClient httpClient;

	private final String url;

	private final String queryKey;
	private final String query;

	public StardogQueryExecutor( final String url, final String[] query )
	{
		this.httpClient = HttpClientBuilder.create().build();
		this.url = url;

		this.queryKey = query[0];
		this.query = query[1];
	}

	@Override
	public Long call() throws Exception
	{
		HttpPost request = new HttpPost( this.url );

		StringEntity params = new StringEntity( "{ \"query\" : \"" + this.query + "\" }" );
		request.addHeader( "accept", "application/sparql-results+json" );
		request.addHeader( "content-type", "application/json" );
		request.setEntity( params );

		// execute
		log.info( "about to execute query {}..", this.queryKey );
		long start = System.currentTimeMillis();

		CloseableHttpResponse response = this.httpClient.execute( request );

		long resultMs = System.currentTimeMillis() - start;

		response.close();

		return resultMs;
	}

}
