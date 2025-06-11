package com.dom.service;

import com.dom.config.CakeConfig;
import com.dom.model.CakeEntity;
import com.dom.model.dto.CakeDto;
import com.dom.repository.CakeRepository;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

@Singleton
public class CakeConsumerService {
    private static final Logger LOG = LoggerFactory.getLogger(CakeConsumerService.class);
    private final HttpClient httpClient;
    private final CakeRepository repository;
    private final ObjectMapper objectMapper;
    private final CakeConfig cakeConfig;

    public CakeConsumerService(@Client HttpClient httpClient, CakeRepository repository, ObjectMapper objectMapper, CakeConfig cakeConfig) {
        this.httpClient = httpClient;
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.cakeConfig = cakeConfig;
    }

    public void loadCakes() {
        try {
            loadCakesFromUrl();
        } catch (Exception e) {
            LOG.error("Failed to load cakes", e);
        }
    }

    private void loadCakesFromUrl() throws Exception {
        String urlString = cakeConfig.getUrl();
        if (urlString.isEmpty()) {
            LOG.warn("Cake data URL is not configured.");
            return;
        }

        URI uri = URI.create(urlString);
        LOG.debug("Loading cakes from URL: {}", uri);
        HttpRequest<?> request = HttpRequest.GET(uri).header("Accept", "text/plain");
        // We use toBlocking() for simplicity in this example, but I would consider using reactive streams in production code. This just makes it work like RestTemplate for exmaple.
        // Simlarly we use `retrieve` to get the response directly as a list of CakeDto objects instead of `exchange` which would return a HttpResponse with statis, headers, body etc.
        // The endpoint is sending Content-Type: text/plain, so we need to read it as a String first then parse it as a list of CakeDto objects.
        String rawCake = httpClient.toBlocking().retrieve(request, String.class);
        LOG.debug("Response received: {}", rawCake);
        List<CakeDto> cakes;
        try {
            cakes = objectMapper.readValue(rawCake, Argument.listOf(CakeDto.class));
        } catch (Exception e) {
            LOG.error("Failed to parse cake data from URL: {}", urlString, e);
            // This could be a custom exception type in a real application, instead of a generic Exception.
            throw e;
        }

        if (cakes.isEmpty()) {
            LOG.info("No cakes found at the URL: {}", urlString);
        } else {
            LOG.info("Found {} cakes", cakes.size());
            for (CakeDto cake : cakes) {
                CakeEntity entity = new CakeEntity();
                entity.setTitle(cake.getTitle());
                entity.setDesc(cake.getDesc());
                entity.setImage(cake.getImage());
                LOG.debug("Saving entity: {}", objectMapper.writeValueAsString(entity));
                repository.save(entity);
            }
            LOG.info("Cakes loaded successfully from {}", urlString);
        }
    }
}
