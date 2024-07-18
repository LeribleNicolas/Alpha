package com.alpha.LD.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PocketBaseService {

    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8090/api/collections/";
    private String adminToken;

    @Autowired
    public PocketBaseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void authenticateAdmin(String identity, String password) {
        String authUrl = "http://localhost:8090/api/admins/auth-with-password";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("identity", identity);
        requestBody.put("password", password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(authUrl, HttpMethod.POST, entity, new ParameterizedTypeReference<>() {});
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            this.adminToken = (String) response.getBody().get("token");
        } else {
            throw new RuntimeException("Failed to authenticate admin");
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (adminToken != null) {
            headers.set("Authorization", "Bearer " + adminToken);
        }
        return headers;
    }

    public List<Object> getAllRecords(String collectionName) {
        String url = baseUrl + collectionName + "/records";
        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<List<Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {}
        );
        return response.getBody();
    }

    public Object getRecordById(String collectionName, String id) {
        String url = baseUrl + collectionName + "/records/" + id;
        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());
        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Object.class
        );
        return response.getBody();
    }

    public Object createRecord(String collectionName, Object record) {
        String url = baseUrl + collectionName + "/records";
        HttpEntity<Object> entity = new HttpEntity<>(record, createHeaders());
        return restTemplate.postForObject(url, entity, Object.class);
    }

    public void updateRecord(String collectionName, String id, Object record) {
        String url = baseUrl + collectionName + "/records/" + id;
        HttpEntity<Object> entity = new HttpEntity<>(record, createHeaders());
        restTemplate.put(url, entity);
    }

    public void deleteRecord(String collectionName, String id) {
        String url = baseUrl + collectionName + "/records/" + id;
        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());
        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }
}
