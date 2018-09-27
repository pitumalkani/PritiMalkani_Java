package com.ubs.entity;

import lombok.Data;

/**
 * Instantiates a new position.
 */

/**
 * Instantiates a new position.
 */
@Data
public class Position {

    /** The initial position. */
    private double initialPosition;

    /** The instrument name. */
    private String instrumentName;

    /** The account number. */
    private int accountNumber;

    /** The account type. */
    private AccountType accountType;

    /** The current positio value. */
    private double currentPositionValue;

    /** The delta. */
    private double delta;

    /**
     * The Enum AccountType.
     */
    public enum AccountType {

        /** The i. */
        I,

        /** The e. */
        E
    }

    @Override
    public String toString() {
        return instrumentName + "," + accountNumber + ", " + accountType + "," + currentPositionValue + "," + delta;
    }
}
