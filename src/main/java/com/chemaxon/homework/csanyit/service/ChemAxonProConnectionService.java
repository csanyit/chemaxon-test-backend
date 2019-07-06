package com.chemaxon.homework.csanyit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class ChemAxonProConnectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChemAxonProConnectionService.class);

    private static final HttpHeaders HTTP_HEADERS = new HttpHeaders();

    private static final List<String> CALCULATIONS = Arrays.asList("BASIC");

    private static URI CALCULATE_URI;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${chemaxon.pro.domain}")
    private String chemAxonProDomain;

    @Value("${chemaxon.pro.apikey}")
    private String chemAxonProApiKey;

    @PostConstruct
    private void initialize() throws URISyntaxException {
        try {
            CALCULATE_URI = new URI(chemAxonProDomain + "/v1/calculate");
        } catch (URISyntaxException e) {
            LOGGER.error("initialize error during setting up CALCULATE_URI", e, e.getMessage());
            throw e;
        }
        HTTP_HEADERS.setContentType(MediaType.APPLICATION_JSON);
        HTTP_HEADERS.set("X-Api-Key", chemAxonProApiKey);
    }

    public List<Map<String, Object>> getChemicalDescriptionSummary(String chemicalName) {
        LOGGER.info("getChemicalDescriptionSummary called with parameter: " + chemicalName);
        if (chemicalName == null || chemicalName.isEmpty()) {
            return new ArrayList();
        }
        final Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("structure", chemicalName);
        requestBody.put("calculations", CALCULATIONS);
        return restTemplate.postForObject(CALCULATE_URI, new HttpEntity<>(requestBody,  HTTP_HEADERS), List.class);
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
