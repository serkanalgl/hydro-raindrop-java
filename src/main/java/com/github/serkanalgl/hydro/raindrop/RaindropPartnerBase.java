package com.github.serkanalgl.hydro.raindrop;

import com.github.serkanalgl.hydro.HydroOAuth2;
import com.github.serkanalgl.hydro.HydroOAuth2Builder;
import com.github.serkanalgl.hydro.HydroOAuth2Exception;
import com.github.serkanalgl.hydro.raindrop.model.BaseResponse;
import com.github.serkanalgl.hydro.raindrop.model.TransactionStatus;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by serkanalgul on 4.07.2018.
 */
class RaindropPartnerBase {

    protected final static String RESOURCE_TRANSACTION_STATUS = "/transaction";

    protected RaindropPartnerConfig config;
    protected HydroOAuth2 hydroOAuth2;

    public RaindropPartnerBase(RaindropPartnerConfig config) {

        if (config == null) throw new IllegalArgumentException("RaindropPartnerConfig parameter can not be 'null'");

        this.config = config;
        this.hydroOAuth2 = new HydroOAuth2Builder()
                .setClientId(config.getClientId())
                .setClientSecret(config.getClientSecret())
                .environment(com.github.serkanalgl.hydro.Environment.valueOf(config.getEnvironment().name()))
                .build();
    }

    public String getOAuth2Token() {
        return hydroOAuth2.getToken().getAccessToken();
    }

    public String getEndpoint(String resourceUrl) {

        if (config.getEnvironment().equals(com.github.serkanalgl.hydro.raindrop.Environment.SANDBOX)) {
            return String.format("https://sandbox.hydrogenplatform.com/hydro/v1%s", resourceUrl);
        } else {
            return String.format("https://api.hydrogenplatform.com/hydro/v1%s", resourceUrl);
        }

    }


    public <T extends BaseResponse> T getErrorResponse(HttpResponse<JsonNode> response, Class<T> typeOfClass) {

        try {

            JSONObject json = response.getBody().getObject();

            T instance = typeOfClass.newInstance();
            instance.setStatus(json.getInt("status"));
            instance.setMessage(json.getString("message"));

            return instance;

        } catch (Exception e) {
            throw new RaindropException("Invalid response from server", e);
        }

    }

    public TransactionStatus transactionStatus(String transactionHash) {

        try {

            HttpResponse<JsonNode> jsonResponse = Unirest.get(getEndpoint(RESOURCE_TRANSACTION_STATUS))
                    .header("Authorization", "Bearer " + getOAuth2Token())
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("transaction_hash", transactionHash)
                    .queryString("application_id", config.getApplicationId())
                    .asJson();

            if (jsonResponse.getStatus() == HttpStatus.SC_OK) {

                try {

                    JSONObject verifyJson = jsonResponse.getBody().getObject();

                    TransactionStatus transactionStatus = new TransactionStatus();
                    transactionStatus.setTransactionHash(verifyJson.getString("transaction_hash"));
                    transactionStatus.setCompleted(verifyJson.getBoolean("completed"));
                    return transactionStatus;

                } catch (JSONException e) {
                    throw new RaindropException("Invalid response from server", e);
                }

            }

            return getErrorResponse(jsonResponse, TransactionStatus.class);

        } catch (UnirestException e) {
            throw new RaindropException("hydro api call failed", e);
        } catch (HydroOAuth2Exception e) {
            throw new RaindropException("authorization is failed: " + e.getMessage(), e);
        }

    }
}
