package org.gesis.zl.evaluation.mysql;

import java.util.concurrent.Callable;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author matthaeus
 * 
 */
public class MysqlQueryExecutor implements Callable<Long>
{
	private static Logger log = LoggerFactory.getLogger( MysqlQueryExecutor.class );

	private JdbcTemplate db;

	private String queryKey;
	private String query;

	public MysqlQueryExecutor( DataSource datasource, String[] query )
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
