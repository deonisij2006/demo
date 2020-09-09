package com.example.demo.service;

import com.example.demo.dao.CountryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
public class CountryCodeService {
    private final CountryClient countryClient;
    private final CountryRepository countryRepository;
    private final Logger logger = LogManager.getLogger(CountryCodeService.class);

    @Autowired
    public CountryCodeService(CountryClient countryClient, CountryRepository countryRepository) {
        this.countryClient = countryClient;
        this.countryRepository = countryRepository;
    }

    public List<Country> getCountryByNamePrefix(String namePrefix){
        try {
            List<Country> countries = getCountries();
            logger.debug("Clean cache");
            countryRepository.deleteAll();
            logger.debug("Update cache");
            countryRepository.saveAll(countries);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return countryRepository.findAllByCountryStartingWithIgnoreCase(namePrefix);
    }

    public List<Country> getCountries() {
        logger.debug("Request country names");
        Map<String, String> countries = countryClient.getNames();
        logger.debug("Request country phone codes");
        Map<String, String> phones = countryClient.getPhones();
        return countries.entrySet().stream()
                .map(e->new Country(e.getKey(), e.getValue(), phones.get(e.getKey())))
                .collect(toList());
    }
}
