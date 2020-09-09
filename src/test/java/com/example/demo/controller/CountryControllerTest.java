package com.example.demo.controller;

import com.example.demo.service.Country;
import com.example.demo.service.CountryCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CountryCodeService countryCodeService;

    @Test
    void getCountryCode() throws Exception {
        List<Country> singleton = Collections.singletonList(new Country("BE", "Belgium", "880"));
        when(countryCodeService.getCountryByNamePrefix("Be")).thenReturn(singleton);
        mockMvc.perform(MockMvcRequestBuilders.get("/rest/code?country=Be").content("application/json") )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("880"))
                .andExpect(jsonPath("$[0].country").value("Belgium"))
                .andExpect(jsonPath("$[0].name").value("BE"))
        ;
    }
}