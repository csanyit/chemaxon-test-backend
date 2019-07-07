package com.chemaxon.homework.csanyit.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * This class calls Chemicalize Pro REST API endpoint. To work as expected this class requires correct Chemicalize Pro domain and API key setting.
 *
 * Chemicalize Pro domain should be set in chemicalize.pro.domain configuration property.
 *
 * Chemicalize Pro API key should be set in chemicalize.pro.apikey configuration property.
 */
@Service
public class ChemicalizeProConnectionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChemicalizeProConnectionService.class);

    private static final List<String> CALCULATIONS = Collections.singletonList("BASIC");

    private final HttpHeaders httpHeaders = new HttpHeaders();

    private RestTemplate restTemplate = new RestTemplate();

    private URI calculateUri;

    @Value("${chemicalize.pro.domain}")
    private String chemicalizeProDomain;

    @Value("${chemicalize.pro.apikey}")
    private String chemicalizeProApiKey;

    /**
     * This method initializes endpoint and HTTP headers.
     *
     * <p>Throws  <tt>URISyntaxException</tt>
     * if chemicalize.pro.domain is invalid.
     *
     * <p>Throws  <tt>IllegalStateException</tt>
     * if chemicalize.pro.apikey is empty.
     *
     */
    @PostConstruct
    public void initialize() throws URISyntaxException {
        try {
            calculateUri = new URI(chemicalizeProDomain + "/v1/calculate");
        } catch (URISyntaxException e) {
            LOGGER.error("initialize error during setting up calculateUri", e, e.getMessage());
            throw e;
        }
        if (chemicalizeProApiKey == null || chemicalizeProApiKey.isEmpty()) {
            LOGGER.error("chemicalize.pro.apikey value: " + chemicalizeProApiKey);
            throw new IllegalStateException("Missing configuration: chemicalize.pro.apikey");
        }
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Api-Key", chemicalizeProApiKey);
    }

    /**
     * Return chemical's BASIC calculation result from Chemicalize Pro REST API.
     *
     * <p>Throws  <tt>IllegalArgumentException</tt>
     * if chemicalName parameter is null or empty.
     *
     * <p>Throws  <tt>HttpClientErrorException</tt>
     * if Chemicalize Pro REST API call was invalid (e.g: invalid chemical name or invalid API key).
     *
     * <p>Throws  <tt>RuntimeException</tt>
     * if Chemicalize Pro REST API call failed because of communication error (e.g: invalid hostname or connection error).
    */
    public List<Map<String, Object>> getChemicalDescriptionSummary(String chemicalName) {
        LOGGER.info("getChemicalDescriptionSummary called with parameter: " + chemicalName);
        if (chemicalName == null || chemicalName.isEmpty()) {
            throw new IllegalArgumentException("chemicalName cannot be empty");
        }
        final Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("structure", chemicalName);
        requestBody.put("calculations", CALCULATIONS);
        return doGetChemicalDescriptionSummary(new HttpEntity<>(requestBody, httpHeaders));
    }

    private List<Map<String, Object>> doGetChemicalDescriptionSummary(HttpEntity<Map<String, Object>> httpEntity) {
        try {
            return restTemplate.postForObject(calculateUri, httpEntity, List.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            LOGGER.error("getChemicalDescriptionSummary error during postForObject", httpClientErrorException);
            throw httpClientErrorException;
        } catch (Exception | Error e) {
            LOGGER.error("getChemicalDescriptionSummary error during postForObject", e);
            throw new RuntimeException("Chemicalize Pro communication error");
        }
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

}
