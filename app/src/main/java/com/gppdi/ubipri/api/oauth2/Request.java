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
    private String username;
    private String password;

    @SerializedName("grant_type")
    private String grantType;

    @SerializedName("refresh_token")
    private String refreshToken;

    public static class Builder {
        private Request request;

        public Builder() {
            request = new Request();
        }

        public Builder(String grantType) {
            this();
            request.grantType = grantType;
        }

        public Builder client(String clientId, String clientSecret) {
            request.clientId = clientId;
            request.clientSecret = clientSecret;
            return this;
        }

        public Builder user(String username, String password) {
            request.username = username;
            request.password = password;
            request.grantType = "password";
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            request.refreshToken = refreshToken;
            request.grantType = "refresh_token";
            return this;
        }

        public Builder grantType(String grantType) {
            request.grantType = grantType;
            return this;
        }

        public Request build() {
            return request;
        }

    }

    private Request() { }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
