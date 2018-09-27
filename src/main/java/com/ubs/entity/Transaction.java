package com.ubs.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Instantiates a new transaction.
 */
@Data
public class Transaction {

    /** The transaction id. */
    @JsonProperty ( "TransactionId" )
    private int transactionId;

    /** The instrument name. */
    @JsonProperty ( "Instrument" )
    private String instrumentName;

    /** The transaction type. */
    @JsonProperty ( "TransactionType" )
    private TransactionType transactionType;

    /** The transation quantity. */
    @JsonProperty ( "TransactionQuantity" )
    private int transactionQuantity;

    /**
     * The Enum TransactionType.
     */
    public enum TransactionType {

        /** The b for Buy transaction */
        B,

        /** The s for Sell transaction */
        S
    }
}
