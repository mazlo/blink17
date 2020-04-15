package org.gesis.zl.evaluation.helper;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


/**
 * 
 * @author matthaeus
 * 
 */
public class ComputeDistribution
{
	/* ADJUST THIS */
	private int totalNumberOfQueries = 390;
	private int columnsLength = 7;

	public static void main( String[] args ) throws IOException
	{
		ComputeDistribution computeDistribution = new ComputeDistribution();

		String[] useCases = new String[] { "uc1", "uc2", "uc3", "uc4" };

		for ( String useCase : useCases )
		{
			computeDistribution.read( useCase );
		}
	}

	/**
	 * @throws IOException
	 * 
	 */
	public void read( String useCase ) throws IOException
	{
		List<CSVRecord> records = getRecords( useCase );

		String[] queries = new String[records.size() - 1];

		// collect queries
		for ( CSVRecord csvRecord : records )
		{
			if ( csvRecord.getRecordNumber() == 1 )
				continue;

			queries[(int) csvRecord.getRecordNumber() - 2] = csvRecord.get( 0 );
		}

		// columns
		for ( int i = 1; i < columnsLength; i++ )
		{
			String[] probabilityValues = new String[records.size() - 1];
			String columnName = "";

			for ( CSVRecord csvRecord : records )
			{
				if ( csvRecord.getRecordNumber() == 1 )
				{
					columnName = csvRecord.get( i );
					continue;
				}

				probabilityValues[(int) csvRecord.getRecordNumber() - 2] = csvRecord.get( i );
			}

			printDistributionFileFinal( totalNumberOfQueries, useCase, columnName, queries, probabilityValues );
		}

	}

	/**
	 * 
	 * @param useCase
	 * @return
	 * @throws IOException
	 */
	private List<CSVRecord> getRecords( String useCase ) throws IOException
	{
		String filename = "distributions/dist_" + useCase + ".csv";

		CSVParser parser = CSVParser.parse( new File( filename ), Charset.forName( "utf8" ), CSVFormat.newFormat( ';' ) );

		List<CSVRecord> records = parser.getRecords();
		return records;
	}

	/**
	 * @param totalNumberOfQueries
	 * @param queries
	 * @param probabilityValues
	 * @return
	 */
	public String[] computeDistributions( int totalNumberOfQueries, String[] queries, String[] probabilityValues )
	{
		String[] totalQueriesFileList = new String[totalNumberOfQueries];
		int index = 0;

		while ( index < totalNumberOfQueries )
		{
			double p = Math.random();
			double cumulativeProbability = 0.0;

			for ( int i = 0; i < probabilityValues.length; i++ )
			{
				// cumulate until the value is bigger than the randomly chosen p
				// value. this works, because the value of p is equally
				// distributed
				cumulativeProbability += Double.valueOf( probabilityValues[i] );

				if ( p <= cumulativeProbability )
				{
					totalQueriesFileList[index++] = queries[i];
					break;
				}
			}
		}

		return totalQueriesFileList;
	}

	/**
	 * @param probabilityValues
	 * @param queries
	 * @param totalNumberOfQueries
	 * @param useCase
	 * @param columnName
	 * 
	 */
	private void printDistributionFileFinal( int totalNumberOfQueries, String useCase, String columnName, String[] queries, String[] probabilityValues )
	{
		String[] distributionOverall = computeDistributions( totalNumberOfQueries, queries, probabilityValues );

		try
		{
			PrintWriter writer = new PrintWriter( "distributions/each/dist_" + useCase + "_" + ( 100 - Integer.valueOf( columnName ) ) + "_" + columnName + ".txt" );

			for ( String query : distributionOverall )
			{
				writer.println( query );
			}

			writer.flush();
			writer.close();
		}
		catch ( IOException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
