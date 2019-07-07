package com.chemaxon.homework.csanyit.controller;

import com.chemaxon.homework.csanyit.service.ChemicalizeProConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@RestController
public class ChemicalizeProApiController extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChemicalizeProApiController.class);

    @Autowired
    private ChemicalizeProConnectionService chemicalizeProConnectionService;


    @GetMapping(MainController.URL + "/description/summary")
    public List<Map<String, Object>> getChemicalDescriptionSummary(@RequestParam(value = "name", required = false) String chemicalName) throws Exception {
        LOGGER.info("/description/summary called with parameter: " + chemicalName);
        if (chemicalName == null || chemicalName.isEmpty()) {
            throw new IllegalArgumentException("name query parameter is mandatory");
        }
        return chemicalizeProConnectionService.getChemicalDescriptionSummary(chemicalName);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<Map<String, Object>> handleInvalidArgumentExceotion(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(createErrorResponseBody(String.format("{ \"message\" : \"%s\"}", ex.getMessage()), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public final ResponseEntity<Map<String, Object>> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {
        return new ResponseEntity<>(createErrorResponseBody(ex.getResponseBodyAsString(), ex.getStatusCode().value()), ex.getStatusCode());
    }

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<Map<String, Object>> handleAllExceptions(Throwable t, WebRequest request) {
        return new ResponseEntity<>(createErrorResponseBody(String.format("{ \"message\" : \"%s\"}", t.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static final Map<String, Object> createErrorResponseBody(String exceptionMessage, int statusCode) {
        Map<String, Object> responseBodyMap = new HashMap<>();
        responseBodyMap.put("message", exceptionMessage);
        responseBodyMap.put("status", statusCode);
        return responseBodyMap;
    }

    public void setChemicalizeProConnectionService(ChemicalizeProConnectionService chemicalizeProConnectionService) {
        this.chemicalizeProConnectionService = chemicalizeProConnectionService;
    }

}
