package org.gesis.zl.evaluation.neo4j;

import java.util.concurrent.Callable;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author matthaeus
 * 
 */
public class Neo4jQueryExecutor implements Callable<Long>
{
	private static Logger log = LoggerFactory.getLogger( Neo4jQueryExecutor.class );

	private JdbcTemplate db;

	private String queryKey;
	private String query;

	public Neo4jQueryExecutor( final DataSource datasource, String[] query )
	{
		db = new JdbcTemplate( datasource );

		this.queryKey = query[0];
		this.query = query[1];
	}

	public Long call() throws Exception
	{
		// execute
		log.info( "about to execute query {}..", this.queryKey );
		long start = System.currentTimeMillis();

		db.execute( this.query );

		long resultMs = System.currentTimeMillis() - start;

		return resultMs;
	}

}
