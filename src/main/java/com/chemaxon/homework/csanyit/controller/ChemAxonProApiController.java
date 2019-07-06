package com.chemaxon.homework.csanyit.controller;

import com.chemaxon.homework.csanyit.service.ChemAxonProConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Map;

@ControllerAdvice
@RestController
public class ChemAxonProApiController extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChemAxonProApiController.class);

    @Autowired
    private ChemAxonProConnectionService chemAxonProConnectionService;


    @GetMapping(MainController.URL + "/description/summary")
    public List<Map<String, Object>> getChemicalDescriptionSummary(@RequestParam(value = "name", required = false) String chemicalName) throws Exception {
        LOGGER.info("/description/summary called with parameter: " + chemicalName);
        if ( chemicalName == null || chemicalName.isEmpty() ) {
            throw  new IllegalArgumentException("name query parameter is mandatory");
        }
        return chemAxonProConnectionService.getChemicalDescriptionSummary(chemicalName);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<String> handleInvalidArgumentExceotion(IllegalArgumentException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidElementRuntimeException.class)
    public final ResponseEntity<String> handleInvalidElementRuntimeException(InvalidElementRuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<String> handleAllExceptions(Throwable t, WebRequest request) {
        return new ResponseEntity<>(t.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public void setChemAxonProConnectionService(ChemAxonProConnectionService chemAxonProConnectionService) {
        this.chemAxonProConnectionService = chemAxonProConnectionService;
    }

    public class InvalidElementRuntimeException extends RuntimeException {
        public InvalidElementRuntimeException(String message) {
            super(message);
        }
    }


}
