package com.github.serkanalgl.hydro.raindrop.model;

import org.apache.http.HttpStatus;

/**
 * Created by serkanalgul on 7.07.2018.
 */
public class VerifySignature extends BaseResponse {

    private boolean verified;
    private String verificationId;
    private String timestamp;

    public VerifySignature() {
        super(HttpStatus.SC_OK, "");
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(String verificationId) {
        this.verificationId = verificationId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "VerifySignature{" +
                "verified=" + verified +
                ", verificationId='" + verificationId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", " + super.toString() +
                '}';
    }
}
