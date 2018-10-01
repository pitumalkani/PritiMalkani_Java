package com.ubs.process;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ubs.entity.Position;
import com.ubs.entity.Position.AccountType;
import com.ubs.entity.Transaction;
import com.ubs.entity.Transaction.TransactionType;

/**
 * The Class ProcessPositionCalculation.
 */
public class ProcessPositionCalculation {

    /** The Constant logger. */
    final static Logger logger = Logger.getLogger( ProcessPositionCalculation.class );

    /**
     * Calculate positions.
     *
     * @param positionList the position list
     * @param transactionList the transaction list
     */
    public Map<String, Position> calculatePositions( List<Position> positionList, List<Transaction> transactionList ) {
        logger.debug( "Starting to calculate positions" );
        Map<String, Position> mapOfPositions = new LinkedHashMap<>();

        if ( transactionList != null ) 
            for ( Transaction t : transactionList ) {
                for ( Position p : positionList ) {
                    if ( p.getInstrumentName().equalsIgnoreCase( t.getInstrumentName() ) ) {
                        if ( TransactionType.B.equals( t.getTransactionType() ) ) {

                            if ( AccountType.E.equals( p.getAccountType() ) ) {
                                handleAddition( mapOfPositions, p, t );
                            }
                            if ( AccountType.I.equals( p.getAccountType() ) ) {
                                handleSubtraction( mapOfPositions, p, t );
                            }
                        }

                        if ( TransactionType.S.equals( t.getTransactionType() ) ) {
                            if ( AccountType.E.equals( p.getAccountType() ) ) {
                                handleSubtraction( mapOfPositions, p, t );
                            }
                            if ( AccountType.I.equals( p.getAccountType() ) ) {
                                handleAddition( mapOfPositions, p, t );
                            }
                        }

                    }
                }
            }
        
        logger.debug( "Calculation of positions completed" );
        return mapOfPositions;
    }

    /**
     * Handle addition.
     *
     * @param mapOfPositions the map of positions
     * @param p the p
     * @param t the t
     * @return the map
     */
    private Map<String, Position> handleAddition( Map<String, Position> mapOfPositions, Position p, Transaction t ) {
        if ( !mapOfPositions.containsKey( p.getInstrumentName() + p.getAccountType() ) ) {
            p.setCurrentPositionValue( p.getInitialPosition() + t.getTransactionQuantity() );
            p.setDelta( p.getCurrentPositionValue() - p.getInitialPosition() );
            mapOfPositions.put( p.getInstrumentName() + p.getAccountType(), p );

        } else {
            Position tempPosition = mapOfPositions.get( p.getInstrumentName() + p.getAccountType() );
            p.setCurrentPositionValue( tempPosition.getCurrentPositionValue() + t.getTransactionQuantity() );
            p.setDelta( p.getCurrentPositionValue() - p.getInitialPosition() );
            mapOfPositions.replace( p.getInstrumentName() + p.getAccountType(), p );
        }
        return mapOfPositions;
    }

    /**
     * Handle subtraction.
     *
     * @param mapOfPositions the map of positions
     * @param p the p
     * @param t the t
     * @return the map
     */
    private Map<String, Position> handleSubtraction( Map<String, Position> mapOfPositions, Position p, Transaction t ) {
        if ( !mapOfPositions.containsKey( p.getInstrumentName() + p.getAccountType() ) ) {
            p.setCurrentPositionValue( p.getInitialPosition() - t.getTransactionQuantity() );
            p.setDelta( p.getCurrentPositionValue() - p.getInitialPosition() );
            mapOfPositions.put( p.getInstrumentName() + p.getAccountType(), p );
        } else {
            Position tempPosition = mapOfPositions.get( p.getInstrumentName() + p.getAccountType() );
            p.setCurrentPositionValue( tempPosition.getCurrentPositionValue() - t.getTransactionQuantity() );
            p.setDelta( p.getCurrentPositionValue() - p.getInitialPosition() );
            mapOfPositions.replace( p.getInstrumentName() + p.getAccountType(), p );
        }
        return mapOfPositions;
    }
}
