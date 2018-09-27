package com.ubs.main;

import org.apache.log4j.Logger;

import com.ubs.io.ReaderWriter;
import com.ubs.process.ProcessPositionCalculation;

public class Main {

    /** The Constant logger. */
    final static Logger logger = Logger.getLogger( Main.class );

    public static void main( String[] args ) {
        logger.info( "Starting...." );
        ReaderWriter reader = new ReaderWriter();
        ProcessPositionCalculation process = new ProcessPositionCalculation();
        reader.writeOutput( process.calculatePositions( reader.readPositionFile(), reader.readTransactions() ) );
        logger.info( "Everything is completed..." );
    }

}
