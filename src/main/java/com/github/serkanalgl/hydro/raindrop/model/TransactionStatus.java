package com.github.serkanalgl.hydro.raindrop.model;

import org.apache.http.HttpStatus;

/**
 * Created by serkanalgul on 8.07.2018.
 */
public class TransactionStatus extends BaseResponse {

    private String transactionHash;
    private boolean completed;

    public TransactionStatus() {
        super(HttpStatus.SC_OK, "");
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "TransactionStatus{" +
                "transactionHash='" + transactionHash + '\'' +
                ", completed=" + completed +
                ", " + super.toString() +
                '}';
    }
}
