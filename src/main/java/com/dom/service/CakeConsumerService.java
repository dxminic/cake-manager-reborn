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
        if (urlString == null || urlString.isEmpty()) {
            LOG.warn("Cake data URL is not configured.");
            return;
        }

        URI uri = URI.create(urlString);
        LOG.debug("Loading cakes from URL: {}", uri);
        HttpRequest<?> request = HttpRequest.GET(uri).header("Accept", "application/json");
        // We use toBlocking() for simplicity in this example, but I would consider using reactive streams in production code. This just makes it work like RestTemplate for exmaple.
        // Simlarly we use `retrieve` to get the response directly as a list of CakeDto objects instead of `exchange` which would return a HttpResponse with statis, headers, body etc.
        List<CakeDto> cakes = httpClient.toBlocking().retrieve(request, Argument.listOf(CakeDto.class));

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
