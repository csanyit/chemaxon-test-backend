package com.chemaxon.homework.csanyit.controller.test;

import com.chemaxon.homework.csanyit.test.ChemicalizeTestHelper;
import com.chemaxon.homework.csanyit.controller.ChemicalizeProApiController;
import com.chemaxon.homework.csanyit.service.ChemicalizeProConnectionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ChemicalizeProApiController.class, secure = false)
public class ChemicalizeProApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChemicalizeProConnectionService chemicalizeProConnectionService;

    private static final String GET_DESCRIPTION_SUMMARY_ENDPOINT = "/api/v1//description/summary?name=";

    private static final String GET_DESCRIPTION_SUMMARY_NAME_PARAMETER = "acetozone";

    @Test
    public void testGetDescriptionSummaryWithouthParameterFails() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(GET_DESCRIPTION_SUMMARY_ENDPOINT);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Assert.assertEquals("{\"message\":\"name query parameter is mandatory\",\"status\":400}", result.getResponse().getContentAsString());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    public void testGetDescriptionSummaryWithEmptyParameterFails() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(GET_DESCRIPTION_SUMMARY_ENDPOINT);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Assert.assertEquals("{\"message\":\"name query parameter is mandatory\",\"status\":400}", result.getResponse().getContentAsString());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    public void testGetDescriptionSummaryServiceFailsElementNotFound() throws Exception {
        Mockito.when(chemicalizeProConnectionService.getChemicalDescriptionSummary(GET_DESCRIPTION_SUMMARY_NAME_PARAMETER)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "null"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(GET_DESCRIPTION_SUMMARY_ENDPOINT + GET_DESCRIPTION_SUMMARY_NAME_PARAMETER);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Mockito.verify(chemicalizeProConnectionService, Mockito.times(1)).getChemicalDescriptionSummary(Mockito.eq(GET_DESCRIPTION_SUMMARY_NAME_PARAMETER));
        Assert.assertEquals("{\"message\":\"404 null\",\"status\":404}", result.getResponse().getContentAsString());
        Assert.assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    public void testGetDescriptionSummaryServiceFailsWithRuntimeException() throws Exception {
        Mockito.when(chemicalizeProConnectionService.getChemicalDescriptionSummary(GET_DESCRIPTION_SUMMARY_NAME_PARAMETER)).thenThrow(new RuntimeException("null"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(GET_DESCRIPTION_SUMMARY_ENDPOINT + GET_DESCRIPTION_SUMMARY_NAME_PARAMETER);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Mockito.verify(chemicalizeProConnectionService, Mockito.times(1)).getChemicalDescriptionSummary(Mockito.eq(GET_DESCRIPTION_SUMMARY_NAME_PARAMETER));
        Assert.assertEquals("{\"message\":\"null\",\"status\":500}", result.getResponse().getContentAsString());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getResponse().getStatus());
    }

    @Test
    public void testGetDescriptionSummaryWithValidParameter() throws Exception {
        Mockito.when(chemicalizeProConnectionService.getChemicalDescriptionSummary(GET_DESCRIPTION_SUMMARY_NAME_PARAMETER)).thenReturn(ChemicalizeTestHelper.MOCK_DESCRIPTION_SUMMARY);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(GET_DESCRIPTION_SUMMARY_ENDPOINT + GET_DESCRIPTION_SUMMARY_NAME_PARAMETER);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Mockito.verify(chemicalizeProConnectionService, Mockito.times(1)).getChemicalDescriptionSummary(Mockito.eq(GET_DESCRIPTION_SUMMARY_NAME_PARAMETER));
        Assert.assertEquals(new ObjectMapper().writeValueAsString(ChemicalizeTestHelper.MOCK_DESCRIPTION_SUMMARY), result.getResponse().getContentAsString());
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

}
