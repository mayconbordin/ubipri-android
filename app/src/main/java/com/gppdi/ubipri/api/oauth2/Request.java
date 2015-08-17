package com.gppdi.ubipri.api.oauth2;

import com.google.gson.annotations.SerializedName;

/**
 * @author mayconbordin
 */
public class Request {
    @SerializedName("client_id")
    private String clientId;

    @SerializedName("client_secret")
    private String clientSecret;
    private String email;
    private String password;

    @SerializedName("grant_type")
    private String grantType;

    public Request() {
    }

    private Request(String clientId, String clientSecret, String grantType) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.grantType = grantType;
    }

    private Request(String clientId, String clientSecret, String email, String password, String grantType) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.email = email;
        this.password = password;
        this.grantType = grantType;
    }

    public static Request createPasswordGrant(String clientId, String clientSecret, String email, String password)
    {
        return new Request(clientId, clientSecret, email, password, "password");
    }

    public static Request createRefreshTokenGrant(String clientId, String clientSecret, String email, String password)
    {
        return new Request(clientId, clientSecret, email, password, "refresh_token");
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
