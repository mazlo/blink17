package org.gesis.zl.evaluation.sesame;

import java.util.concurrent.Callable;

import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author matthaeus
 * 
 */
public class SesameQueryExecutor implements Callable<Long>
{
	private static Logger log = LoggerFactory.getLogger( SesameQueryExecutor.class );

	private RepositoryConnection conn;

	private String queryKey;
	private String query;

	public SesameQueryExecutor( Repository repo, String[] query )
	{
		this.queryKey = query[0];
		this.query = query[1];

		try
		{
			this.conn = repo.getConnection();
		}
		catch ( RepositoryException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Long call() throws Exception
	{
		TupleQuery tupleQuery;

		// prepare
		tupleQuery = conn.prepareTupleQuery( QueryLanguage.SPARQL, this.query );

		// execute
		log.info( "about to execute query {}..", this.queryKey );
		long start = System.currentTimeMillis();
		TupleQueryResult queryResult = tupleQuery.evaluate();

		// evaluate
		queryResult.close();

		long resultMs = System.currentTimeMillis() - start;

		return resultMs;
	}

}
