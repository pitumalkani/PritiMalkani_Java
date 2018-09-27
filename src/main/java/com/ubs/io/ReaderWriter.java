package com.ubs.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubs.entity.Position;
import com.ubs.entity.Transaction;

/**
 * The Class ReaderWriter.
 */
public class ReaderWriter {

    /** The Constant logger. */
    final static Logger logger = Logger.getLogger( ReaderWriter.class );

    /** The Constant INPUT_POSITIONS_PATH. */
    private final static String INPUT_POSITIONS_PATH = "Input_Positions.txt";

    /** The Constant INPUT_TRANSACTIONS_PATH. */
    private final static String INPUT_TRANSACTIONS_PATH = "Input_Transactions.txt";

    private final static String OUTPUT_FILE = "D:/Expected_EndOfDay_Positions.csv";

    /** The Constant POSITION_DELIMITER. */
    private final static String POSITION_DELIMITER = ",|\\n";

    /** The Constant TRANSACTION_DELIMITER. */
    private final static String TRANSACTION_DELIMITER = "\\Z";

    /**
     * Read position file.
     *
     * @return the list
     */
    public List<Position> readPositionFile() {
        logger.info( "Reading Position file" );
        ArrayList<Position> positionList = new ArrayList<Position>();
        Position p = null;
        Scanner input = skipLines( getScannerObject( INPUT_POSITIONS_PATH, POSITION_DELIMITER ), 1 );
        while ( input.hasNext() ) {
            p = new Position();
            p.setInstrumentName( input.next() );
            p.setAccountNumber( Integer.parseInt( input.next() ) );
            p.setAccountType( Position.AccountType.valueOf( input.next() ) );
            p.setInitialPosition( Double.parseDouble( input.next() ) );
            p.setCurrentPositionValue( p.getInitialPosition() );
            positionList.add( p );
        }
        logger.info( "Reading Position file completed" );
        return positionList;
    }

    /**
     * Read transactions.
     *
     * @return the list
     */
    public List<Transaction> readTransactions() {
        logger.info( "Reading transaction file" );
        List<Transaction> tranactionList = null;
        String transactions = getScannerObject( INPUT_TRANSACTIONS_PATH, TRANSACTION_DELIMITER ).next();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            tranactionList = objectMapper.readValue( transactions, new TypeReference<List<Transaction>>() {
            } );
        } catch ( JsonParseException e ) {
            logger.debug( "Error while parsing json file" + INPUT_TRANSACTIONS_PATH + e.getMessage() );
        } catch ( JsonMappingException e ) {
            logger.debug( "Error while mapping json file" + INPUT_TRANSACTIONS_PATH + e.getMessage() );
        } catch ( IOException e ) {
            logger.debug( "Error in reading file at path" + INPUT_TRANSACTIONS_PATH + e.getMessage() );
        }
        logger.info( "Reading transaction file completed" );
        return tranactionList;
    }

    /**
     * Skip lines.
     *
     * @param s the s
     * @param lineNum the line num
     * @return the scanner
     */
    private Scanner skipLines( Scanner s, int lineNum ) {
        for ( int i = 0; i < lineNum; i++ ) {
            if ( s.hasNextLine() ) {
                s.nextLine();
            }
        }
        return s;
    }

    /**
     * Gets the scanner object.
     *
     * @param fileName the file name
     * @param delimiter the delimiter
     * @return the scanner object
     */
    @SuppressWarnings ( "resource" )
    private Scanner getScannerObject( String fileName, String delimiter ) {
        Scanner scanner = null;
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            File file = new File( classLoader.getResource( fileName ).getFile() );
            scanner = new Scanner( file ).useDelimiter( delimiter );
        } catch ( FileNotFoundException e ) {
            logger.debug( "Error in reading file at path" + INPUT_TRANSACTIONS_PATH + e.getMessage() );
        }
        return scanner;
    }

    public void writeOutput( Map<String, Position> mapOfPositions ) {
        logger.info( "Starting to write output" );
        try {
            StringBuilder output = new StringBuilder( "Instrument,Account,AccountType,Quantity,Delta\n" );
            mapOfPositions.entrySet().forEach( entry -> {
                Position p = entry.getValue();
                output.append( p.toString() );
                output.append( "\n" );
            } );
            Path path = Files.write( Paths.get( OUTPUT_FILE ), output.toString().getBytes() );
            logger.debug( "\nOutput file is at location :: " + path.toAbsolutePath().toString() );
        } catch ( IOException e ) {
            logger.debug( "Error while writing output file" + e.getMessage() );
        }

    }

}
