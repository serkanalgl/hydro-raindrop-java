package com.github.serkanalgl.hydro.raindrop.model;

import org.apache.http.HttpStatus;

/**
 * Created by serkanalgul on 4.07.2018.
 */
public class Whitelist extends BaseResponse {

    private String hydroAddressId;
    private String transactionHash;

    public Whitelist() {
        super(HttpStatus.SC_OK, "");
    }

    public String getHydroAddressId() {
        return hydroAddressId;
    }

    public void setHydroAddressId(String hydroAddressId) {
        this.hydroAddressId = hydroAddressId;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    @Override
    public String toString() {
        return "Whitelist{" +
                "hydroAddressId='" + hydroAddressId + '\'' +
                ", transactionHash='" + transactionHash + '\'' +
                ", " + super.toString() +
                '}';
    }
}
