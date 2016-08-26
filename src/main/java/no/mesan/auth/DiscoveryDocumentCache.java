package no.mesan.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DiscoveryDocumentCache {
    private static ConcurrentMap<String, Map.Entry<Instant, JsonNode>> cache = new ConcurrentHashMap<>();
    private final String discoveryDocumentUrl;

    public DiscoveryDocumentCache(String discoveryDocumentUrl) {
        this.discoveryDocumentUrl = discoveryDocumentUrl;
    }

    JsonNode getDiscoveryDocument() {
        return get("discoveryDocument", discoveryDocumentUrl);
    }

    JsonNode getJwksKeys() {
        return get("jwks", getDiscoveryDocument().get("jwks_uri").asText()).get("keys");
    }

    private JsonNode get(String key, String url) {
        if (cache.containsKey(key) && cache.get(key).getKey().isAfter(Instant.now())) {
            return cache.get(key).getValue();
        }

        Response response = ClientBuilder.newClient().target(url).request().get();

        CacheControl cacheControl = CacheControl.valueOf(response.getHeaderString("Cache-Control"));
        Instant expiry = Instant.now().plus(cacheControl.getMaxAge(), ChronoUnit.SECONDS);

        JsonNode entity = response.readEntity(JsonNode.class);

        cache.put(key, Maps.immutableEntry(
                expiry,
                entity));

        return entity;
    }
}
