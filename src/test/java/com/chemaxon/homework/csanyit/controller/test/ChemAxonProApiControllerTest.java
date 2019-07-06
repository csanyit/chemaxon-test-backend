package com.chemaxon.homework.csanyit.controller.test;

import com.chemaxon.homework.csanyit.controller.ChemAxonProApiController;
import com.chemaxon.homework.csanyit.service.ChemAxonProConnectionService;
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

@RunWith(SpringRunner.class)
@WebMvcTest(value = ChemAxonProApiController.class, secure = false)
public class ChemAxonProApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChemAxonProConnectionService chemAxonProConnectionService;

    private static final String GET_DESCRIPTION_SUMMARY_ENDPOINT = "/api/v1//description/summary?name=";

    private static final String GET_DESCRIPTION_SUMMARY_NAME_PARAMETER = "acetozone";

    @Test
    public void testGetDescriptionSummaryWithouthParameterFails() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(GET_DESCRIPTION_SUMMARY_ENDPOINT);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Assert.assertEquals("name query parameter is mandatory", result.getResponse().getContentAsString());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    public void testGetDescriptionSummaryWithEmptyParameterFails() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(GET_DESCRIPTION_SUMMARY_ENDPOINT);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Assert.assertEquals("name query parameter is mandatory", result.getResponse().getContentAsString());
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    public void testGetDescriptionSummaryWithValidParameter() throws Exception {
        Mockito.when(chemAxonProConnectionService.getChemicalDescriptionSummary(GET_DESCRIPTION_SUMMARY_NAME_PARAMETER)).thenReturn(ChemAxonTestHelper.MOCK_DESCRIPTION_SUMMARY);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(GET_DESCRIPTION_SUMMARY_ENDPOINT + GET_DESCRIPTION_SUMMARY_NAME_PARAMETER);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Mockito.verify(chemAxonProConnectionService, Mockito.times(1)).getChemicalDescriptionSummary(Mockito.eq(GET_DESCRIPTION_SUMMARY_NAME_PARAMETER));
        Assert.assertEquals(new ObjectMapper().writeValueAsString(ChemAxonTestHelper.MOCK_DESCRIPTION_SUMMARY), result.getResponse().getContentAsString());
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

}
