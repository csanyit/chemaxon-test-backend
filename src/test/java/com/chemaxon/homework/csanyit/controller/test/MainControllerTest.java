package com.chemaxon.homework.csanyit.controller.test;

import com.chemaxon.homework.csanyit.controller.MainController;
import com.chemaxon.homework.csanyit.service.ChemAxonProConnectionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@WebMvcTest(value = MainController.class, secure = false)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChemAxonProConnectionService chemAxonProConnectionService;

    @Test
    public void testPingMethod() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/ping");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Assert.assertEquals("ok", result.getResponse().getContentAsString());
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

}
