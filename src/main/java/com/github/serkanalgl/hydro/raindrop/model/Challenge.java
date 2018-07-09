package com.github.serkanalgl.hydro.raindrop.model;

import org.apache.http.HttpStatus;

import java.math.BigInteger;

/**
 * Created by serkanalgul on 4.07.2018.
 */
public class Challenge extends BaseResponse {

    private BigInteger amount;
    private BigInteger challenge;
    private BigInteger partnerId;
    private String transactionHash;

    public Challenge() {
        super(HttpStatus.SC_OK, "");
    }

    public Challenge(int status, String message) {
        super(status, message);
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public BigInteger getChallenge() {
        return challenge;
    }

    public void setChallenge(BigInteger challenge) {
        this.challenge = challenge;
    }

    public BigInteger getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(BigInteger partnerId) {
        this.partnerId = partnerId;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "amount=" + amount +
                ", challenge='" + challenge + '\'' +
                ", partnerId=" + partnerId +
                ", transactionHash='" + transactionHash + '\'' +
                ", " + super.toString() +
                '}';
    }


}
