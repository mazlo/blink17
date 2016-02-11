package org.gesis.zl.evaluation;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class QueryShuffler
{
	private static final Logger log = LoggerFactory.getLogger( QueryShuffler.class );

	public static String[] shuffle( final String fromFolder, final int totalNumberOfQueries )
	{
		File queryFolder = new File( fromFolder );

		if ( !queryFolder.exists() )
		{
			log.error( "Folder {} does not exist", fromFolder );
			return new String[] {};
		}

		String[] filenamesList = queryFolder.list();

		if ( filenamesList.length == 0 )
		{
			log.warn( "No queries in folder {}", fromFolder );
			return new String[] {};
		}

		List<String> queriesList = Lists.newArrayList( filenamesList );

		Collections.shuffle( queriesList );

		return queriesList.toArray( new String[] {} );
	}
}
