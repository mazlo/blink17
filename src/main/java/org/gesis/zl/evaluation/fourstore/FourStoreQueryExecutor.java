package org.gesis.zl.evaluation.fourstore;

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
public class FourStoreQueryExecutor implements Callable<Long>
{
	private static Logger log = LoggerFactory.getLogger( FourStoreQueryExecutor.class );

	private CloseableHttpClient httpClient;

	private String url;

	private String queryKey;
	private String query;

	public FourStoreQueryExecutor( final String url, String[] query )
	{
		this.httpClient = HttpClientBuilder.create().build();
		this.url = url;

		this.queryKey = query[0];
		this.query = query[1];
	}

	public Long call() throws Exception
	{
		HttpPost request = new HttpPost( url );
		
		StringEntity params = new StringEntity( "query=" + this.query + "&soft-limit=10000" );
		request.addHeader( "accept", "application/sparql-results+json" );
		request.addHeader( "content-type", "application/x-www-form-urlencoded" );
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
