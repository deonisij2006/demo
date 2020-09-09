package com.example.demo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "phone-client", url = "http://country.io")
public interface CountryClient {
    @GetMapping(path = "/names.json")
    Map<String, String> getNames();

    @GetMapping(path = "/phone.json")
    Map<String, String> getPhones();
}
