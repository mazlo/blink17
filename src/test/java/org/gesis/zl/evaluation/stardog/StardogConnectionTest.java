package org.gesis.zl.evaluation.stardog;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 
 * @author matthaeus
 * 
 */
@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration( locations = { "classpath:context.xml" } )
public class StardogConnectionTest
{
	private final static Logger log = LoggerFactory.getLogger( StardogConnectionTest.class );

	@Autowired
	private EvaluationProperties properties;

	private final String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX disco: <http://rdf-vocabulary.ddialliance.org/discovery#> PREFIX dcterms: <http://purl.org/dc/terms/> PREFIX skos: <http://www.w3.org/2004/02/skos/core#> SELECT ?studyGroupLabel WHERE { ?studyGroup a disco:StudyGroup ; skos:prefLabel ?studyGroupLabel . }";

	private CloseableHttpClient httpClient;

	@Before
	public void init()
	{
		this.httpClient = HttpClientBuilder.create().build();
	}

	@Test
	public void query() throws ClientProtocolException, IOException
	{
		HttpPost request = new HttpPost( this.properties.getDbUrl() + this.properties.getDbName() );

		StringEntity params = new StringEntity( "{ \"query\" : \"" + this.query + "\" }" );
		request.addHeader( "accept", "application/sparql-results+json" );
		request.addHeader( "content-type", "application/json" );
		request.setEntity( params );

		// execute
		log.info( "about to execute query {}..", this.query );
		long start = System.currentTimeMillis();

		CloseableHttpResponse response = this.httpClient.execute( request );

		long resultMs = System.currentTimeMillis() - start;

		response.close();

		System.out.format( "query executed in %dms", resultMs );
	}

}
