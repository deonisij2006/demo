package com.example.demo.service;

import com.example.demo.dao.CountryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CountryCodeServiceTest {
    @Test
    void getCountryByNamePrefix() {
        CountryRepository repository = Mockito.mock(CountryRepository.class);
        CountryClient countryClient = Mockito.mock(CountryClient.class);
        CountryCodeService service = new CountryCodeService(countryClient, repository);
        service.getCountryByNamePrefix("BS");

        verify(countryClient).getNames();
        verify(countryClient).getPhones();
        verify(repository).deleteAll();
        verify(repository).saveAll(any());
        verify(repository).findAllByCountryStartingWithIgnoreCase("BS");
    }

    @Test
    void getCountryByNamePrefixClientNotAvailable() {
        CountryRepository repository = Mockito.mock(CountryRepository.class);
        CountryClient countryClient = Mockito.mock(CountryClient.class);
        when(countryClient.getPhones()).thenThrow(RuntimeException.class);
        CountryCodeService service = new CountryCodeService(countryClient, repository);
        service.getCountryByNamePrefix("BS");
        verify(repository, never()).deleteAll();
        verify(repository, never()).saveAll(any());
        verify(repository).findAllByCountryStartingWithIgnoreCase("BS");
    }

    @Test
    void getCountries() {
        CountryRepository repository = Mockito.mock(CountryRepository.class);
        CountryClient countryClient = Mockito.mock(CountryClient.class);
        when(countryClient.getNames()).thenReturn(Map.of("name", "country"));
        when(countryClient.getPhones()).thenReturn(Map.of("name", "code"));
        CountryCodeService service = new CountryCodeService(countryClient, repository);
        List<Country> countries = service.getCountries();

        assertEquals(1, countries.size());
        assertEquals("code", countries.get(0).getCode());
    }

}