package com.chemaxon.homework.csanyit.service.test;

import com.chemaxon.homework.csanyit.service.ChemicalizeProConnectionService;
import com.chemaxon.homework.csanyit.test.ChemicalizeTestHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RunWith(SpringRunner.class)
@TestPropertySource(properties = {
        "chemicalize.pro.domain=https://api.chemicalize.com",
        "chemicalize.pro.apikey=TEST_CHEMICALIZE_PRO_API_KEY"
})
public class ChemicalizeProConnectionServiceTest {

    private static final String VALID_GET_CHEMICAL_DESCRIPTION_SUMMARY_PARAMETER = "aspirin";

    @Mock
    private RestTemplate restTemplateMock = Mockito.mock(RestTemplate.class);

    @Autowired
    private ChemicalizeProConnectionService chemicalizeProConnectionService;

    @Before
    public void setUp() {
        chemicalizeProConnectionService.setRestTemplate(restTemplateMock);
    }

    @TestConfiguration
    static class ChemicalizeProConnectionServiceTestContextConfiguration {

        @Bean
        public ChemicalizeProConnectionService chemicalizeProConnectionService() {
            return new ChemicalizeProConnectionService();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetChemicalDescriptionSummaryNullParameters() {
        List resultList = chemicalizeProConnectionService.getChemicalDescriptionSummary(null);
        Assert.assertEquals(Collections.EMPTY_LIST, resultList);
        Mockito.verify(restTemplateMock, Mockito.times(0)).postForObject(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetChemicalDescriptionSummaryEmptyParameter() {
        List resultList = chemicalizeProConnectionService.getChemicalDescriptionSummary(null);
        Assert.assertEquals(Collections.EMPTY_LIST, resultList);
        Mockito.verify(restTemplateMock, Mockito.times(0)).postForObject(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    public void testGetChemicalDescriptionSummaryValidParameter() throws URISyntaxException {
        URI calculateUri = new URI("https://api.chemicalize.com/v1/calculate");
        final Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("structure", VALID_GET_CHEMICAL_DESCRIPTION_SUMMARY_PARAMETER);
        requestBody.put("calculations", Collections.singletonList("BASIC"));
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("X-Api-Key", "TEST_CHEMICALIZE_PRO_API_KEY");
        Mockito.when(restTemplateMock.postForObject(Mockito.eq(calculateUri), Mockito.eq(new HttpEntity<>(requestBody, httpHeaders)), Mockito.eq(List.class))).thenReturn(ChemicalizeTestHelper.MOCK_DESCRIPTION_SUMMARY);
        List resultList = chemicalizeProConnectionService.getChemicalDescriptionSummary(VALID_GET_CHEMICAL_DESCRIPTION_SUMMARY_PARAMETER);
        Assert.assertEquals(ChemicalizeTestHelper.MOCK_DESCRIPTION_SUMMARY, resultList);
        Mockito.verify(restTemplateMock, Mockito.times(1)).postForObject(Mockito.eq(calculateUri), Mockito.eq(new HttpEntity<>(requestBody, httpHeaders)), Mockito.eq(List.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeFailsIfAPIKeyNotSet() throws URISyntaxException {
        new ChemicalizeProConnectionService().initialize();
    }

}
