package com.example.demo.controller;

import com.example.demo.service.Country;
import com.example.demo.service.CountryCodeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/rest")
@RestController
public class CountryController {
    private final CountryCodeService countryCodeService;
    private final Logger logger = LogManager.getLogger(CountryCodeService.class);

    @Autowired
    public CountryController(CountryCodeService countryCodeService) {
        this.countryCodeService = countryCodeService;
    }

    @GetMapping(value = "/code")
    List<Country> getCountryCode(@RequestParam(value = "country", required = false)String country) {
        logger.debug("Request with code fore: " + country);
        return countryCodeService.getCountryByNamePrefix(country);
    }
}
