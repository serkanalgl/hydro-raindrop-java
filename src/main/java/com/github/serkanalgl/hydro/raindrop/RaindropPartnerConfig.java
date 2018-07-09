package com.github.serkanalgl.hydro.raindrop;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by serkanalgul on 4.07.2018.
 */
public class RaindropPartnerConfig {

    private Environment environment;
    private String clientId;
    private String clientSecret;
    private String applicationId;

    private RaindropPartnerConfig(Builder builder) {
        this.environment = builder.environment;
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.applicationId = builder.applicationId;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getApplicationId() {
        return applicationId;
    }

    @Override
    public String toString() {
        return "RaindropPartnerConfig{" +
                "environment=" + environment +
                ", clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", applicationId='" + applicationId + '\'' +
                '}';
    }

    public static class Builder {

        private Environment environment = Environment.SANDBOX;
        private String clientId;
        private String clientSecret;

        //optional ( required for client)
        private String applicationId;

        public Builder(String clientId, String clientSecret) {
            this.clientId = clientId;
            this.clientSecret = clientSecret;
        }

        public Builder(String clientId, String clientSecret, Environment environment) {
            this.clientSecret = clientSecret;
            this.environment = environment;
            this.clientId = clientId;
        }

        public Builder setApplicationId(String applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public RaindropPartnerConfig build() {
            if (this.environment == null) throw new IllegalArgumentException("environment can not be null");
            if (StringUtils.isEmpty(this.clientId))
                throw new IllegalArgumentException("clientId can not be null or empty");
            if (StringUtils.isEmpty(this.clientSecret))
                throw new IllegalArgumentException("clientId can not be null or empty");

            return new RaindropPartnerConfig(this);
        }
    }
}
