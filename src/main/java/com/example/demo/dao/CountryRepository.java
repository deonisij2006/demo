package com.example.demo.dao;

import com.example.demo.service.Country;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CountryRepository extends MongoRepository<Country, String> {
    List<Country> findAllByCountryStartingWithIgnoreCase(String country);
}
