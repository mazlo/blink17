package org.gesis.zl.evaluation.sesame;

import org.gesis.zl.evaluation.service.EvaluationProperties;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
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
@Ignore
public class SesameConnectionTest
{
	private final static Logger log = LoggerFactory.getLogger( SesameConnectionTest.class );

	@Autowired
	private EvaluationProperties properties;

	private RepositoryConnection conn;

	private final String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX disco: <http://rdf-vocabulary.ddialliance.org/discovery#> PREFIX dcterms: <http://purl.org/dc/terms/> PREFIX skos: <http://www.w3.org/2004/02/skos/core#> SELECT ?studyGroupLabel WHERE { ?studyGroup a disco:StudyGroup ; skos:prefLabel ?studyGroupLabel . }";

	@Before
	public void init()
	{
		Repository repository = new HTTPRepository( this.properties.getDbUrl(), this.properties.getDbName() );
		log.info( "HTTPRepository created" );

		try
		{
			this.conn = repository.getConnection();
			log.info( "Connection created" );
		}
		catch ( RepositoryException e )
		{
			e.printStackTrace();
		}
	}

	@Test
	public void query() throws RepositoryException, MalformedQueryException, QueryEvaluationException
	{
		TupleQuery tupleQuery;

		// prepare
		tupleQuery = this.conn.prepareTupleQuery( QueryLanguage.SPARQL, this.query );
		log.info( "TupleQuery prepared" );

		// execute
		long start = System.currentTimeMillis();

		log.info( "About to execute query ..." );
		TupleQueryResult queryResult = tupleQuery.evaluate();

		long resultMs = System.currentTimeMillis() - start;

		log.info( "query execution took {}ms", resultMs );

		queryResult.close();
	}
}
