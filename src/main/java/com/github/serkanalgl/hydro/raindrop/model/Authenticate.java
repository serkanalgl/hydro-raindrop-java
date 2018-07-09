package com.github.serkanalgl.hydro.raindrop.model;

import org.apache.http.HttpStatus;

/**
 * Created by serkanalgul on 4.07.2018.
 */
public class Authenticate extends BaseResponse {

    private boolean authenticated;
    private String authenticationId;
    private String timestamp;

    public Authenticate() {
        super(HttpStatus.SC_OK, "");
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getAuthenticationId() {
        return authenticationId;
    }

    public void setAuthenticationId(String authenticationId) {
        this.authenticationId = authenticationId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Authenticate{" +
                "authenticated=" + authenticated +
                ", authenticationId='" + authenticationId + '\'' +
                ", timestamp=" + timestamp +
                ", " + super.toString() +
                '}';
    }
}
