package com.ubs.io;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubs.entity.Position;
import com.ubs.entity.Transaction;
import com.ubs.util.Constants;

/**
 * The Class ReaderWriterTest.
 */
@RunWith ( MockitoJUnitRunner.class )
public class ReaderWriterTest {

    /** The read writer. */
    @InjectMocks
    private ReaderWriter readWriter;

    /** The class loader. */
    @Mock
    private ClassLoader classLoader;

    /** The mapper. */
    @Spy
    private ObjectMapper mapper;

    /**
     * Test read transactions.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test
    public void testReadTransactions() throws IOException {
        List<Transaction> listOfTransactions = readWriter.readTransactions( Constants.INPUT_TRANSACTIONS_PATH, Constants.TRANSACTION_DELIMITER );
        assertNotNull( listOfTransactions );
    }

    /**
     * Test read position file.
     */
    @Test
    public void testReadPositionFile() {
        List<Position> listOfPositions = readWriter.readPositionFile( Constants.INPUT_POSITIONS_PATH, Constants.POSITION_DELIMITER );
        assertNotNull( listOfPositions );
    }

    /**
     * Test read transactions for null.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test ( expected = JsonParseException.class )
    public void testReadTransactionsForNull() throws IOException {
        readWriter.readTransactions( Constants.INPUT_POSITIONS_PATH, Constants.TRANSACTION_DELIMITER );
    }

    /**
     * Test write output.
     */
    @Test
    public void testWriteOutput() {
        Map<String, Position> mapOfPositions = new HashMap<>();
        mapOfPositions.put( "Position1", new Position() );
        readWriter.writeOutput( mapOfPositions );
    }

}
