package com.github.serkanalgl.hydro.raindrop;

import com.github.serkanalgl.hydro.HydroOAuth2Exception;
import com.github.serkanalgl.hydro.raindrop.model.BaseResponse;
import com.github.serkanalgl.hydro.raindrop.model.VerifySignature;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by serkanalgul on 4.07.2018.
 */
final class RaindropClientImpl extends RaindropPartnerBase implements RaindropClient {

    private static final Logger logger = LoggerFactory.getLogger(RaindropClientImpl.class);

    private final static String RESOURCE_REGISTER_USER = "/application/client";
    private final static String RESOURCE_DELETE_USER = "/application/client";
    private final static String RESOURCE_VERIFY_SIGNATURE = "/verify_signature";

    private SecureRandom secureRandom = null;

    public RaindropClientImpl(RaindropPartnerConfig config) {
        super(config);

        if (StringUtils.isEmpty(config.getApplicationId())) {
            throw new IllegalArgumentException("applicationId is mandatory for raindrop client");
        }

        try {
            this.secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
        }

    }


    @Override
    public BaseResponse registerUser(String hydroId) throws RaindropException {

        try {

            JSONObject jsonBody = new JSONObject()
                    .put("hydro_id", hydroId)
                    .put("application_id", config.getApplicationId());

            HttpResponse<JsonNode> jsonResponse = Unirest.post(getEndpoint(RESOURCE_REGISTER_USER))
                    .header("Authorization", "Bearer " + getOAuth2Token())
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asJson();

            if (jsonResponse.getStatus() == HttpStatus.SC_CREATED) {
                return new BaseResponse(HttpStatus.SC_OK, "the user registered successfully");
            }

            return getErrorResponse(jsonResponse, BaseResponse.class);

        } catch (UnirestException e) {
            throw new RaindropException("hydro api call failed", e);
        } catch (HydroOAuth2Exception e) {
            throw new RaindropException("authorization is failed: " + e.getMessage(), e);
        }


    }

    @Override
    public BaseResponse deleteUser(String hydroId) throws RaindropException {

        try {

            HttpResponse<JsonNode> jsonResponse = Unirest.delete(getEndpoint(RESOURCE_DELETE_USER))
                    .header("Authorization", "Bearer " + getOAuth2Token())
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("hydro_id", hydroId)
                    .queryString("application_id", config.getApplicationId())
                    .asJson();

            if (jsonResponse.getStatus() == HttpStatus.SC_NO_CONTENT) {
                return new BaseResponse(HttpStatus.SC_OK, "the user deleted successfully");
            }

            return getErrorResponse(jsonResponse, BaseResponse.class);

        } catch (UnirestException e) {
            throw new RaindropException("hydro api call failed", e);
        } catch (HydroOAuth2Exception e) {
            throw new RaindropException("authorization is failed: " + e.getMessage(), e);
        }
    }

    @Override
    public VerifySignature verifySignature(String hydroId, Integer message) throws RaindropException {

        try {

            HttpResponse<JsonNode> jsonResponse = Unirest.get(getEndpoint(RESOURCE_VERIFY_SIGNATURE))
                    .header("Authorization", "Bearer " + getOAuth2Token())
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("hydro_id", hydroId)
                    .queryString("message", message)
                    .queryString("application_id", config.getApplicationId())
                    .asJson();

            try {

                JSONObject verifyJson = jsonResponse.getBody().getObject();

                VerifySignature verifySignature = new VerifySignature();
                verifySignature.setVerified(jsonResponse.getStatus() == HttpStatus.SC_OK);
                verifySignature.setVerificationId(verifyJson.getString("verification_id"));
                verifySignature.setTimestamp(verifyJson.getString("timestamp"));
                return verifySignature;

            } catch (JSONException e) {
                throw new RaindropException("Invalid response from server", e);
            }

        } catch (UnirestException e) {
            throw new RaindropException("hydro api call failed", e);
        } catch (HydroOAuth2Exception e) {
            throw new RaindropException("authorization is failed: " + e.getMessage(), e);
        }
    }

    @Override
    public Integer generateMessage() {
        if (this.secureRandom != null) {
            return secureRandom.nextInt(900000) + 100000;
        }
        throw new RaindropException("SecureRandom algorithm could not be found!");
    }
}
