package com.dom.service;

import com.dom.config.CakeConfig;
import com.dom.model.CakeEntity;
import com.dom.model.dto.CakeDto;
import com.dom.repository.CakeRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.serde.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CakeConsumerServiceTest {
    @Mock
    private CakeRepository repository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private CakeConfig cakeConfig;
    @Mock
    private HttpClient httpClient;

    private CakeConsumerService cakeConsumerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cakeConsumerService = new CakeConsumerService(httpClient, repository, objectMapper, cakeConfig);
    }

    @Test
    void testLoadCakes_savesCakesSuccessfully() throws Exception {
        String url = "http://test.com/cakes.json";
        when(cakeConfig.getUrl()).thenReturn(url);
        CakeDto cakeDto = new CakeDto();
        cakeDto.setTitle("Chocolate Cake");
        cakeDto.setDesc("Delicious");
        cakeDto.setImage("img.jpg");
        List<CakeDto> cakeList = List.of(cakeDto);
        io.micronaut.http.client.BlockingHttpClient blocking = mock(io.micronaut.http.client.BlockingHttpClient.class);
        when(httpClient.toBlocking()).thenReturn(blocking);
        when(blocking.retrieve(any(HttpRequest.class), eq(io.micronaut.core.type.Argument.listOf(CakeDto.class)))).thenReturn(cakeList);

        cakeConsumerService.loadCakes();

        ArgumentCaptor<CakeEntity> captor = ArgumentCaptor.forClass(CakeEntity.class);
        verify(repository, times(1)).save(captor.capture());
        CakeEntity saved = captor.getValue();
        assertEquals("Chocolate Cake", saved.getTitle());
        assertEquals("Delicious", saved.getDesc());
        assertEquals("img.jpg", saved.getImage());
    }

    @Test
    void testLoadCakes_emptyList() throws Exception {
        String url = "http://test.com/cakes.json";
        when(cakeConfig.getUrl()).thenReturn(url);
        io.micronaut.http.client.BlockingHttpClient blocking = mock(io.micronaut.http.client.BlockingHttpClient.class);
        when(httpClient.toBlocking()).thenReturn(blocking);
        when(blocking.retrieve(any(HttpRequest.class), eq(io.micronaut.core.type.Argument.listOf(CakeDto.class)))).thenReturn(Collections.emptyList());

        cakeConsumerService.loadCakes();
        verify(repository, never()).save(any());
    }

    @Test
    void testLoadCakes_urlNotConfigured() throws Exception {
        when(cakeConfig.getUrl()).thenReturn("");
        cakeConsumerService.loadCakes();
        verifyNoInteractions(httpClient);
        verifyNoInteractions(repository);
    }

    @Test
    void testLoadCakes_exceptionDuringLoad() throws Exception {
        String url = "http://test.com/cakes.json";
        when(cakeConfig.getUrl()).thenReturn(url);
        io.micronaut.http.client.BlockingHttpClient blocking = mock(io.micronaut.http.client.BlockingHttpClient.class);
        when(httpClient.toBlocking()).thenReturn(blocking);
        when(blocking.retrieve(any(HttpRequest.class), eq(io.micronaut.core.type.Argument.listOf(CakeDto.class)))).thenThrow(new RuntimeException("fail"));
        cakeConsumerService.loadCakes();
        verify(repository, never()).save(any());
    }
}