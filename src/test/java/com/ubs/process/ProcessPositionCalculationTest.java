package com.ubs.process;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
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
import com.ubs.io.ReaderWriter;
import com.ubs.util.Constants;

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

    private ReaderWriter readWrite;

    /**
     * Setup.
     * @throws IOException 
     */
    @Before
    public void setup() throws IOException {
        readWrite = new ReaderWriter();
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
        positionList = new ArrayList<Position>();
        positionList=readWrite.readPositionFile( "Test_Input_Positions.txt", Constants.POSITION_DELIMITER );
    }

    /**
     * Sets the up transactions.
     * 
     * @throws IOException
     */
    private void setUpTransactions() throws IOException {
        transactionList = new ArrayList<Transaction>();
        transactionList=readWrite.readTransactions( "Test_Input_Transactions.txt", Constants.TRANSACTION_DELIMITER );
    }

    /**
     * Test calculate positions.
     */
    @Test
    public void testCalculatePositions() {
        Map<String, Position> mapOfPositions = processPositionCalculation.calculatePositions( positionList, transactionList );
        assertEquals( 8, mapOfPositions.size() );
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
