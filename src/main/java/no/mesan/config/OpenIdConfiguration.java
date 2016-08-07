package no.mesan.config;

import java.net.URL;

public class OpenIdConfiguration {
    private String discoveryDocumentUrl;
    private String clientId;
    private String clientSecret;
    private String redirectUri;

    public String getDiscoveryDocumentUrl() {
        return discoveryDocumentUrl;
    }

    public void setDiscoveryDocumentUrl(String discoveryDocumentUrl) {
        this.discoveryDocumentUrl = discoveryDocumentUrl;
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

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
