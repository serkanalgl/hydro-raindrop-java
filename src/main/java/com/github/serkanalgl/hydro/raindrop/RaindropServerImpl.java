package com.github.serkanalgl.hydro.raindrop;

import com.github.serkanalgl.hydro.HydroOAuth2Exception;
import com.github.serkanalgl.hydro.raindrop.model.Authenticate;
import com.github.serkanalgl.hydro.raindrop.model.Challenge;
import com.github.serkanalgl.hydro.raindrop.model.Whitelist;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by serkanalgul on 4.07.2018.
 */
final class RaindropServerImpl extends RaindropPartnerBase implements RaindropServer {

    private final static Logger logger = LoggerFactory.getLogger(RaindropServerImpl.class);

    private final static String RESOURCE_WHITELIST = "/whitelist";
    private final static String RESOURCE_CHALLENGE = "/challenge";
    private final static String RESOURCE_AUTHENTICATE = "/authenticate";

    public RaindropServerImpl(RaindropPartnerConfig config) {
        super(config);
    }

    @Override
    public Whitelist whitelist(String address) throws RaindropException {

        try {

            JSONObject jsonBody = new JSONObject().put("address", address);

            HttpResponse<JsonNode> jsonResponse = Unirest.post(getEndpoint(RESOURCE_WHITELIST))
                    .header("Authorization", "Bearer " + getOAuth2Token())
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asJson();

            if (jsonResponse.getStatus() == HttpStatus.SC_OK) {

                try {

                    Whitelist whitelist = new Whitelist();
                    whitelist.setHydroAddressId(jsonResponse.getBody().getObject().getString("hydro_address_id"));
                    whitelist.setTransactionHash(jsonResponse.getBody().getObject().getString("transaction_hash"));
                    return whitelist;

                } catch (JSONException e) {
                    throw new RaindropException("Invalid response from server", e);
                }

            }

            return getErrorResponse(jsonResponse, Whitelist.class);

        } catch (UnirestException e) {
            throw new RaindropException("hydro api call failed", e);
        } catch (HydroOAuth2Exception e) {
            throw new RaindropException("authorization is failed: " + e.getMessage(), e);
        }

    }

    @Override
    public Challenge challenge(String hydroAddressId) throws RaindropException {

        try {

            JSONObject jsonBody = new JSONObject().put("hydro_address_id", hydroAddressId);

            HttpResponse<JsonNode> jsonResponse = Unirest.post(getEndpoint(RESOURCE_CHALLENGE))
                    .header("Authorization", "Bearer " + getOAuth2Token())
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .body(jsonBody)
                    .asJson();

            if (jsonResponse.getStatus() == HttpStatus.SC_OK) {

                try {
                    JSONObject challengeJson = jsonResponse.getBody().getObject();

                    Challenge challenge = new Challenge();
                    challenge.setAmount(challengeJson.getBigInteger("amount"));
                    challenge.setChallenge(challengeJson.getBigInteger("challenge"));
                    challenge.setPartnerId(challengeJson.getBigInteger("partner_id"));
                    challenge.setTransactionHash(challengeJson.getString("transaction_hash"));

                    return challenge;

                } catch (JSONException e) {
                    throw new RaindropException("Invalid response from server", e);
                }

            }

            return getErrorResponse(jsonResponse, Challenge.class);

        } catch (UnirestException e) {
            throw new RaindropException("hydro api call failed", e);
        } catch (HydroOAuth2Exception e) {
            throw new RaindropException("authorization is failed: " + e.getMessage(), e);
        }

    }

    @Override
    public Authenticate authenticate(String hydroAddressId) throws RaindropException {

        try {

            HttpResponse<JsonNode> jsonResponse = Unirest.get(getEndpoint(RESOURCE_AUTHENTICATE))
                    .header("Authorization", "Bearer " + getOAuth2Token())
                    .header("accept", "application/json")
                    .header("Content-Type", "application/json")
                    .queryString("hydro_address_id", hydroAddressId)
                    .asJson();


            if (jsonResponse.getStatus() == HttpStatus.SC_OK || jsonResponse.getStatus() == HttpStatus.SC_UNAUTHORIZED) {
                return getAuthenticateResponse(jsonResponse);
            }

            return getErrorResponse(jsonResponse, Authenticate.class);

        } catch (UnirestException e) {
            throw new RaindropException("hydro api call failed", e);
        } catch (HydroOAuth2Exception e) {
            throw new RaindropException("authorization is failed: " + e.getMessage(), e);
        }
    }

    private Authenticate getAuthenticateResponse(HttpResponse<JsonNode> jsonResponse) {
        try {

            JSONObject authenticateJson = jsonResponse.getBody().getObject();

            Authenticate authenticate = new Authenticate();
            authenticate.setStatus(jsonResponse.getStatus());
            authenticate.setAuthenticated(authenticateJson.getBoolean("authenticated"));
            authenticate.setAuthenticationId(authenticateJson.getString("authentication_id"));
            authenticate.setTimestamp(authenticateJson.getString("timestamp"));
            return authenticate;

        } catch (JSONException e) {
            throw new RaindropException("Invalid response from server", e);
        }
    }

}
