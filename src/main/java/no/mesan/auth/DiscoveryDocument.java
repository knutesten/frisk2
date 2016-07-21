package no.mesan.auth;

import com.fasterxml.jackson.databind.JsonNode;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Created by knutn on 7/21/2016.
 */
public class DiscoveryDocument {
    private static JsonNode discoveryDocument;
    private final static URI GOOGLE_DISCOVERY_DOCUMENT_URL = URI.create("https://accounts.google.com/.well-known/openid-configuration");

    public DiscoveryDocument() {
        if (discoveryDocument == null) {
            Client client = ClientBuilder.newClient();
            Response response = client.target(GOOGLE_DISCOVERY_DOCUMENT_URL).request().get();
            discoveryDocument = response.readEntity(JsonNode.class);
            System.out.println(discoveryDocument);
        }
    }

    public URI getAuthorizationEndpoint() {
        return URI.create(discoveryDocument.get("authorization_endpoint").asText());
    }
}
