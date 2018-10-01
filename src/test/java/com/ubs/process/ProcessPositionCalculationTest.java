package com.ubs.process;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.ubs.entity.Position;
import com.ubs.entity.Transaction;
import com.ubs.entity.Transaction.TransactionType;

/**
 * The Class ProcessPositionCalculationTest.
 */
@RunWith ( MockitoJUnitRunner.class )
public class ProcessPositionCalculationTest {

    /** The process position calculation. */
    @InjectMocks
    ProcessPositionCalculation processPositionCalculation;

    /** The position list. */
    private List<Position> positionList;

    /** The transaction list. */
    private List<Transaction> transactionList;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        positionList = new ArrayList<Position>();
        transactionList = new ArrayList<Transaction>();
        setUpPositions();
        setUpTransactions();
    }

    /**
     * Tear down.
     */
    @After
    public void tearDown() {
        positionList = null;
        transactionList = null;
    }

    /**
     * Sets the up positions.
     */
    private void setUpPositions() {
        Position p1 = new Position();
        p1.setAccountNumber( 101 );
        p1.setAccountType( Position.AccountType.E );
        p1.setInitialPosition( 100000d );
        p1.setCurrentPositionValue( 100000d );
        p1.setInstrumentName( "IBM" );

        Position p2 = new Position();
        p2.setAccountNumber( 103 );
        p2.setAccountType( Position.AccountType.I );
        p2.setInitialPosition( 100000d );
        p2.setCurrentPositionValue( -100000d );
        p2.setInstrumentName( "MSFT" );
        positionList.add( p1 );
        positionList.add( p2 );
    }

    /**
     * Sets the up transactions.
     */
    private void setUpTransactions() {
        Transaction t1 = new Transaction();
        t1.setInstrumentName( "IBM" );
        t1.setTransactionId( 1 );
        t1.setTransactionType( TransactionType.B );
        t1.setTransactionQuantity( 1000 );
        Transaction t2 = new Transaction();
        t2.setInstrumentName( "MSFT" );
        t2.setTransactionId( 1 );
        t2.setTransactionType( TransactionType.S );
        t2.setTransactionQuantity( 50 );
        transactionList.add( t1 );
        transactionList.add( t2 );
    }

    /**
     * Test calculate positions.
     */
    @Test
    public void testCalculatePositions() {
        Map<String, Position> mapOfPositions = processPositionCalculation.calculatePositions( positionList, transactionList );
        assertEquals( 2, mapOfPositions.size() );
    }

    /**
     * Test calculate positions for null.
     */
    @Test
    public void testCalculatePositionsForNull() {
        Map<String, Position> mapOfPositions = processPositionCalculation.calculatePositions( null, null );
        assertEquals( 0, mapOfPositions.size() );
    }
}
