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
import com.ubs.util.Constants;

/**
 * The Class ReaderWriter.
 */
public class ReaderWriter {

    /** The Constant logger. */
    final static Logger logger = Logger.getLogger( ReaderWriter.class );

    /**
     * Read position file.
     *
     * @param fileName the file name
     * @param delimiter the delimiter
     * @return the list
     */
    public List<Position> readPositionFile( String fileName, String delimiter ) {
        logger.info( "Reading Position file" );
        ArrayList<Position> positionList = new ArrayList<Position>();
        Position p = null;
        Scanner input = skipLines( getScannerObject( fileName, delimiter ), 1 );
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
     * @param fileName the file name
     * @param delimiter the delimiter
     * @return the list
     * @throws IOException 
     */
    public List<Transaction> readTransactions( String fileName, String delimiter ) throws IOException {
        logger.info( "Reading transaction file" );
        List<Transaction> tranactionList = null;
        String transactions = getScannerObject( fileName, delimiter ).next();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            tranactionList = objectMapper.readValue( transactions, new TypeReference<List<Transaction>>() {
            } );
        } catch ( JsonParseException e ) {
            logger.debug( "Error while parsing json file" + fileName + e.getMessage() );
            throw e;
        } catch ( JsonMappingException e ) {
            logger.debug( "Error while mapping json file" + fileName + e.getMessage() );
            throw e;
        } catch ( IOException e ) {
            logger.debug( "Error in reading file at path" + fileName + e.getMessage() );
            throw e;
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
            logger.debug( "Error in reading file at path" + fileName + e.getMessage() );
        }
        return scanner;
    }

    /**
     * Write output.
     *
     * @param mapOfPositions the map of positions
     */
    public void writeOutput( Map<String, Position> mapOfPositions ) {
        logger.info( "Starting to write output" );
        try {
            StringBuilder output = new StringBuilder( "Instrument,Account,AccountType,Quantity,Delta\n" );
            mapOfPositions.entrySet().forEach( entry -> {
                Position p = entry.getValue();
                output.append( p.toString() );
                output.append( "\n" );
            } );
            Path path = Files.write( Paths.get( Constants.OUTPUT_FILE ), output.toString().getBytes() );
            logger.debug( "\nOutput file is at location :: " + path.toAbsolutePath().toString() );
        } catch ( IOException e ) {
            logger.debug( "Error while writing output file" + e.getMessage() );
        }

    }

}
