package com.ubs.main;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.ubs.io.ReaderWriter;
import com.ubs.process.ProcessPositionCalculation;
import com.ubs.util.Constants;

public class Main {

    /** The Constant logger. */
    final static Logger logger = Logger.getLogger( Main.class );

    public static void main( String[] args ) throws IOException {
        logger.info( "Starting...." );
        ReaderWriter reader = new ReaderWriter();
        ProcessPositionCalculation process = new ProcessPositionCalculation();
        reader.writeOutput( process.calculatePositions( reader.readPositionFile( Constants.INPUT_POSITIONS_PATH, Constants.POSITION_DELIMITER ),
                                                        reader.readTransactions( Constants.INPUT_TRANSACTIONS_PATH, Constants.TRANSACTION_DELIMITER ) ) );
        logger.info( "Everything is completed..." );
    }

}
